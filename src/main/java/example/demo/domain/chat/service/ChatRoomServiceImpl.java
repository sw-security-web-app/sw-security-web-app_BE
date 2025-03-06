package example.demo.domain.chat.service;


import example.demo.domain.chat.dto.response.ChatRoomRecentResponseDto;

import example.demo.domain.chat.dto.request.ChatRoomRequestDto;
import example.demo.domain.chat.dto.response.ChatRoomGetResponseDto;
import example.demo.domain.chat.dto.response.ChatRoomRecentResponseDto;

import example.demo.domain.chat.dto.response.ChatRoomResponseDto;
import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.ChatRoom;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public ChatRoomResponseDto createChatRoom(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .build();

        chatRoom=chatRoomRepository.save(chatRoom);
        return new ChatRoomResponseDto(chatRoom.getChatRoomId());
    }

    @Override
    public List<ChatRoomGetResponseDto> getChatRoomList(Long memberId, AIModelType aiModelType) {
        List<ChatRoomGetResponseDto> chatRoomList = chatRoomRepository.findByMemberIdAndAiModelType(memberId, aiModelType);
        return chatRoomList;
    }

    @Override
    public List<ChatRoomRecentResponseDto> getLatestChatRoom(Long memberId, AIModelType aiModelType) {
        List<ChatRoomRecentResponseDto> latestChatRoom = chatRoomRepository.findLatestChatRoomWithLatestAnswer(memberId, aiModelType);
        if (latestChatRoom.isEmpty()) {
            return Collections.emptyList();
        }
        return latestChatRoom;
    }

}
