package example.demo.security.service;

import example.demo.security.auth.dto.CustomMemberInfoDto;
import example.demo.security.domain.RefreshTokenResponseDto;

public interface RefreshTokenService {
    RefreshTokenResponseDto refreshToken(String refreshToken, CustomMemberInfoDto customMemberInfoDto);
}
