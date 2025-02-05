package example.demo.domain.member.sms;

import example.demo.data.RedisCustomService;
import example.demo.data.RedisCustomServiceImpl;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import example.demo.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SmsVerificationServiceImpl implements SmsVerificationService{
    private final RedisCustomService redisCustomService;
    private final String SMS_PREFIX="sms: ";
    @Override
    @Transactional(readOnly = true)
    public void verifySms(SmsCertificationRequestDto smsCertificationRequestDto) {
        if(isVerify(smsCertificationRequestDto)){
            throw new RestApiException(MemberErrorCode.INVALID_CERTIFICATION_CODE);
        }
        //인증번호 검증 완료시
        String VALIDATION_PREFIX = "cer: ";
        redisCustomService.saveRedisData(VALIDATION_PREFIX +smsCertificationRequestDto.getPhoneNumber(),"TRUE", (long) (10*60));
        redisCustomService.deleteRedisData(SMS_PREFIX+smsCertificationRequestDto.getPhoneNumber());
    }

    private boolean isVerify(SmsCertificationRequestDto smsCertificationRequestDto) {
        return !(redisCustomService.hasKey(SMS_PREFIX+smsCertificationRequestDto.getPhoneNumber())&&
                redisCustomService.getRedisData(SMS_PREFIX+smsCertificationRequestDto.getPhoneNumber())
                        .equals(smsCertificationRequestDto.getCertificationCode())
        );
    }


}
