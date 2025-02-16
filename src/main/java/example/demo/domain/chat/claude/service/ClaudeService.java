package example.demo.domain.chat.claude.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Claude Service Interface
 *
 * @Author : 박재성
 * @Since : 02/16/2025
 */

@Service
public interface ClaudeService {
    ResponseEntity<String> getCompletion(String prompt);

    /*
    ✔ extractCompletion은 interface와 impl로 구분할 필요 없음!
     */
}
