package example.demo.security.auth.api;

import example.demo.security.auth.dto.MemberLoginDto;

public interface AuthService {
    String loginMember(MemberLoginDto loginDto);
}
