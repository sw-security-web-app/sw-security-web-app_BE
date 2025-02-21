package example.demo.security.service;

import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.error.RestApiException;
import example.demo.security.auth.dto.CustomMemberInfoDto;
import example.demo.security.domain.RefreshToken;
import example.demo.security.domain.RefreshTokenResponseDto;
import example.demo.security.exception.SecurityErrorCode;
import example.demo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private final JwtUtil jwtUtil;
    private final RefreshToken refresh;

    //refresh token를 이용하여 accesToken, refresh Token 재발행
    @Override
    public RefreshTokenResponseDto refreshToken(String refreshToken, CustomMemberInfoDto customMemberInfoDto) {
       //refresh Token 유효성 검증
        checkRefreshToken(refreshToken);

        //refresh token id 조회
        String id = refresh.getRefreshToken(refreshToken);

        //새로운 access Token 생성
        String newAccessToken=jwtUtil.createAccessToken(customMemberInfoDto);

        refresh.removeUserRefreshToken(customMemberInfoDto.getMemberId());

        //새로운 refresh token 생성 후 저장
        String newRefreshToken= jwtUtil.generateRefreshToken(customMemberInfoDto.getMemberId());
        refresh.putRefreshToken(newRefreshToken,customMemberInfoDto.getMemberId());

        return RefreshTokenResponseDto.builder()
                .refreshToken(newRefreshToken)
                .accessToken(newAccessToken)
                .build();
    }

    private void checkRefreshToken(String refreshToken){
        if(Boolean.FALSE.equals(jwtUtil.validateToken(refreshToken))){
            throw new RestApiException(SecurityErrorCode.INVALID_TOKEN);
        }
    }
}
