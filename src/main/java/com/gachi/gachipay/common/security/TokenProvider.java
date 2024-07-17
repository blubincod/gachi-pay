package com.gachi.gachipay.common.security;

import com.gachi.gachipay.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

// private static final long TOKEN_EXPIRE_TIME = 1000 * 60; //  1분
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2; //  2시간

    private final MemberService memberService;

    @Value("{spring.jwt.secret}")
    private String secretKey;

    /**
     * 토큰 생성 및 발급
     *
     * @param username
     * @return
     */
    public String generateToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
                .compact();
    }

    /**
     * 인증 정보 가져오기
     *
     * @param jwt
     * @return
     */
    public Authentication getAuthentication(String jwt) {

        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUsername(jwt));

        return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    /**
     * token 유효성 검사
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;
        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date()); // 만료시간이 현재시간 보다 전인지 검사
    }

    /**
     * Claim 정보 가져오기
     *
     * @param token
     * @return
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
