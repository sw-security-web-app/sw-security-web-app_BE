package example.demo.domain.member.sms;

import example.demo.data.RedisCustomServiceImpl;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class SmsServiceImplTest {
    @Mock
    private DefaultMessageService messageService;
    @Mock
    private SmsUtil smsUtil;

    @Mock
    private RedisCustomServiceImpl redisCustomService;
    @InjectMocks
    private SmsServiceImpl smsService;

    private final String TEST_PHONE_NUMBER="01012345678";
    private final String TEST_VERIFICATION_NUMBER="654321";

    @DisplayName("단문 메시지를 전송합니다.")
    @Test
    void sendOne() {
        //given
        //when

        //then
    }

    @Test
    void sendSms() {
    }
}