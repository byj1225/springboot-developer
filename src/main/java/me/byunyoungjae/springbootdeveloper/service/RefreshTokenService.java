package me.byunyoungjae.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.byunyoungjae.springbootdeveloper.domain.RefreshToken;
import me.byunyoungjae.springbootdeveloper.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

/**
 * 전달받은 RefreshToken으로 리프레시토큰 객체를 조회
 */
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

}
