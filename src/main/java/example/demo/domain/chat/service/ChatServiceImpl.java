package example.demo.domain.chat.service;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.Chat;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.ChatRoomErrorCode;
import example.demo.domain.chat.dto.request.ChatDto;
import example.demo.domain.chat.dto.response.ChatTotalDetailResponseDto;
import example.demo.domain.chat.repository.ChatRepository;
import example.demo.domain.chat.repository.ChatRoomRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final JwtUtil jwtUtil;

    /**
     * 대화내용 저장
     */
    @Transactional
    @Override
    public void saveChat(Long memberId, ChatDto chatDto, Long chatRoomId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RestApiException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND));

        if (
                chatDto == null ||
                        chatDto.getModelType() == null ||
                        chatDto.getQuestion() == null ||
                        chatDto.getAnswer() == null
        ) {
            throw new RestApiException(ChatRoomErrorCode.INVALID_CHAT_DATA);
        }

        Chat chat = Chat.builder()
                .modelType(chatDto.getModelType())
                .question(chatDto.getQuestion())
                .answer(chatDto.getAnswer())
                .member(member)
                .chatRoom(chatRoom)
                .build();

        chatRepository.save(chat);
    }

    @Override
    public ChatTotalDetailResponseDto getDetailChattingContent(Long chatRoomId, String token, Long chatId, int size, AIModelType type) {
        //memberId 추출
        Long memberId=jwtUtil.getMemberId(token);

        //채팅방이 없는 경우
        if(chatRoomRepository.findById(chatRoomId).isEmpty()){
            throw new RestApiException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND);
        }

        return chatRepository.getSliceOfChatting(chatRoomId, chatId , memberId, size,type);
    }
}
