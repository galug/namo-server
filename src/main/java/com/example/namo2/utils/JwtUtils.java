package com.example.namo2.utils;


import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponseStatus;
import com.example.namo2.user.dto.SignUpRes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static com.example.namo2.config.response.BaseResponseStatus.EXPIRATION_REFRESH_TOKEN;

@Slf4j
@NoArgsConstructor
@Component
public class JwtUtils {
    private static final String HEADER = "Authorization";

    @Value("${jwt.secret-key}")
    private String secretKey;


    public SignUpRes generateTokens(Long userId) {
        String accessToken = createAccessToken(userId);
        String refreshToken = createRefreshToken(userId);
        SignUpRes signUpRes = new SignUpRes(accessToken, refreshToken);
        return signUpRes;
    }

    private String createAccessToken(Long userId) {
        return createJwt(userId, 1000 * 60 * 60 * 12L);
    }

    private String createRefreshToken(Long userId) {
        return createJwt(userId, 1000 * 60 * 60 * 24 * 14 * 1L);
    }

    private String createJwt(Long userId, Long tokenValid) {
        byte[] keyBytes = Decoders.BASE64.decode(getSecretKey());
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .signWith(key)
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValid))
                .compact();
    }

    public Long resolveRequest(HttpServletRequest request) throws BaseException {
        try {
            String accessToken = getAccessToken(request);
            return resolveToken(accessToken);
        } catch (ExpiredJwtException e) {
            throw new BaseException(BaseResponseStatus.EXPIRATION_ACCESS_TOKEN);
        }
    }

    private String getAccessToken(HttpServletRequest request) throws BaseException {
        String accessToken = request.getHeader(HEADER);

        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(BaseResponseStatus.EMPTY_ACCESS_KEY);
        }
        return accessToken;
    }

    private Long resolveToken(String accessToken) throws BaseException {
        return Optional.ofNullable(Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody())
                .map((c) -> c.get("userId", Long.class))
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.EMPTY_ACCESS_KEY));
    }

    public boolean validateRequest(HttpServletRequest request) {
        try {
            String jwtToken = getAccessToken(request);
            return validateToken(jwtToken);
        }catch(Exception e) {
            return false;
        }
    }

    public boolean validateToken(String jwtToken) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwtToken);
        return !claims.getBody().getExpiration().before(new Date());
    }

    private String getSecretKey() {
        String secretKeyEncodeBase64 = Encoders.BASE64.encode(secretKey.getBytes());
        return secretKeyEncodeBase64;
    }
}
