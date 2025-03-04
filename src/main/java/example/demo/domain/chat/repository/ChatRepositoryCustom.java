package example.demo.domain.chat.repository;

import example.demo.domain.chat.Chat;
import example.demo.domain.chat.dto.ChatDetailDto;

import java.util.List;
import java.util.Optional;

public interface ChatRepositoryCustom {
    public List<ChatDetailDto> getSliceOfChatting(Long chatRoomId,Long chatId,Long memberId);
}
