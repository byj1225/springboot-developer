package me.byunyoungjae.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.byunyoungjae.springbootdeveloper.config.jwt.TokenProvider;
import me.byunyoungjae.springbootdeveloper.domain.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 리프레시 토큰으로 토큰 유효성 검사를 진행하고
 * 유효한 토큰일때 리프레시 토큰으로 사용자ID를 찾는다.
 */
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        //토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        //리프레시 토큰으로 유저 아이디를 조회
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();

        //유저 아이디로 유저정보 조회
        User user = userService.findById(userId);

        //유저 정보로 액세스 토큰을 만들어 반환함
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }

}
