package example.demo.security.util;

import example.demo.domain.member.MemberStatus;
import example.demo.security.auth.dto.CustomMemberInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.expiration_time}") long accessTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    public String createAccessToken(CustomMemberInfoDto memberInfoDto) {
        return createToken(memberInfoDto, accessTokenExpTime);
    }

    //JWT 생성
    private String createToken(CustomMemberInfoDto memberInfoDto, long accessTokenExpTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberInfoDto.getMemberId());
        claims.put("email", memberInfoDto.getEmail());
        claims.put("memberStatus", memberInfoDto.getMemberStatus().toString());
        claims.put("accountLocked", memberInfoDto.isAccountLocked());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenExpTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Token에서 member ID 추출
    public Long getMemberId(String token) {
        token = removeBearerPrefix(token);
        return parseClaims(token).get("memberId", Long.class);
    }

    //Member 권한 추출
    public String getMemberStatus(String token) {
        token = removeBearerPrefix(token);
        return parseClaims(token).get("memberStatus", String.class);
    }

    //JWT검증
    public boolean validateToken(String token) {
        token = removeBearerPrefix(token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported  JWT token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    private Claims parseClaims(String token) {
        token = removeBearerPrefix(token);
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //token 사용자 속성 정보 조회
    public <T> T getClaimFromToken(String token, final Function<Claims, T> claimsResolver) {
        token = removeBearerPrefix(token);
        //token유효성 검증
        if (Boolean.FALSE.equals(validateToken(token))) {
            return null;
        }
        final Claims claims = parseClaims(token);

        return claimsResolver.apply(claims);
    }


    //만료 일자 조회
    public Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //refresh token 생성
    public String generateRefreshToken(final String id) {
        return doGenerateRefreshToken(id);
    }

    public String generateRefreshToken(final long id) {
        return doGenerateRefreshToken(String.valueOf(id));
    }

    public String doGenerateRefreshToken(final String id) {
        return Jwts.builder()
                .setId(id)
                .setExpiration(new Date(System.currentTimeMillis() + (long) 259200000))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .compact();
    }

    //Prefix 제거 메소드
    private String removeBearerPrefix(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

}
