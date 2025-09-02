package me.byunyoungjae.springbootdeveloper.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.yml에서 정의한 값을 변수로 사용하는 프로퍼티
 */
@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class jwtProperties {
    private String issuer;
    private String secretKey;
}
