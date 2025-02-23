package example.demo.domain.chat.service;

import example.demo.domain.chat.Chat;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.ChatRoomErrorCode;
import example.demo.domain.chat.dto.ChatDto;
import example.demo.domain.chat.dto.ChatRoomResponseDto;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public ChatRoomResponseDto createChatRoom(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .build();

        chatRoomRepository.save(chatRoom);
        return new ChatRoomResponseDto(chatRoom.getChatRoomId());
    }

    @Override
    public List<ChatDto> getChatListByChatRoomId(Long chatRoomId) {
        if (!chatRoomRepository.existsById(chatRoomId)) {
            throw new RestApiException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND);
        }
        Optional<Chat> optionalChat = chatRepository.findChatListByChatRoomId(chatRoomId);
        Chat chat = optionalChat.get();
        return Collections.singletonList(convertToDto(chat));
    }

    private ChatDto convertToDto(Chat chat) {
        return ChatDto.builder()
                .chatId(chat.getChatId())
                .modelType(chat.getModelType())
                .question(chat.getQuestion())
                .answer(chat.getAnswer())
                .chatRoomId(chat.getChatRoom().getChatRoomId())
                .build();
    }
}
