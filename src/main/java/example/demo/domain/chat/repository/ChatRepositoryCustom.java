package example.demo.domain.chat.repository;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.dto.response.ChatDetailResponseDto;
import example.demo.domain.chat.dto.response.ChatTotalDetailResponseDto;

import java.util.List;

public interface ChatRepositoryCustom {
     ChatTotalDetailResponseDto getSliceOfChatting(Long chatRoomId, Long chatId, Long memberId, int size, AIModelType type);

}
