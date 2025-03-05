package example.demo.domain.chat.service;

import example.demo.domain.chat.dto.ChatRoomGetResponseDto;
import example.demo.domain.chat.dto.response.ChatRoomRecentResponseDto;
import example.demo.domain.chat.dto.request.ChatRoomRequestDto;
import example.demo.domain.chat.dto.response.ChatRoomResponseDto;
import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.Chat;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.repository.ChatRepository;
import example.demo.domain.chat.repository.ChatRoomRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private static final int MAX_CHAT_ROOM_COUNT = 7;

    @Transactional
    @Override
    public ChatRoomResponseDto createChatRoom(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .build();

        chatRoom=chatRoomRepository.save(chatRoom);
        limitChatRoomCount(memberId);

        return new ChatRoomResponseDto(chatRoom.getChatRoomId());
    }

    @Override
    public List<ChatRoomGetResponseDto> getChatRoomList(Long memberId, AIModelType aiModelType) {
        List<ChatRoomRequestDto> chatRoomList = chatRoomRepository.findByMemberIdAndAiModelType(memberId, aiModelType);
        if (chatRoomList.isEmpty()) {
            return Collections.emptyList();
        }
        return chatRoomList.stream()
                .map(chatRoom -> new ChatRoomGetResponseDto(chatRoom.getChatRoomId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatRoomRecentResponseDto> getLatestChatRoom(Long memberId, AIModelType aiModelType) {
        List<ChatRoomRecentResponseDto> latestChatRoom = chatRoomRepository.findLatestChatRoomWithLatestAnswer(memberId, aiModelType);
        if (latestChatRoom.isEmpty()) {
            return Collections.emptyList();
        }
        return latestChatRoom;
    }

    private void limitChatRoomCount(Long memberId) {
        List<ChatRoomRequestDto> chatRoomList = chatRoomRepository.findByMemberOrderByCreatedAtAsc(memberId);
        if (chatRoomList.size() > MAX_CHAT_ROOM_COUNT) {
            ChatRoomRequestDto result = chatRoomList.get(0);
            deleteChatRoomAndChatList(result.getChatRoomId());
        }
    }

    private void deleteChatRoomAndChatList(Long chatRoomId) {
        ChatRoom result = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        chatRoomRepository.delete(result);
    }
}
