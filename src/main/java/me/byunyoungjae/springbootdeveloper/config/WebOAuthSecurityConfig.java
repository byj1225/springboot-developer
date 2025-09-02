package me.byunyoungjae.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import me.byunyoungjae.springbootdeveloper.config.jwt.TokenProvider;
import me.byunyoungjae.springbootdeveloper.config.oauth.OAuth2AutorizationRequestBasedOnCookieRepository;
import me.byunyoungjae.springbootdeveloper.config.oauth.OAuth2SuccessHandler;
import me.byunyoungjae.springbootdeveloper.config.oauth.OAuth2UserCustomService;
import me.byunyoungjae.springbootdeveloper.repository.RefreshTokenRepository;
import me.byunyoungjae.springbootdeveloper.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

/**
 *
 */
@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure() { //스프링시큐리티 기능 비활성화
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(
                        new AntPathRequestMatcher("/img/**"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**")
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //토큰방식으로 인증을 하기때문에 기존에 사용하던 폼로그인, 세션 비활성화
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //헤더를 확인할 커스텀 필터 추가
                .addFilterBefore(tokenAuthenticaionFilter(), UsernamePasswordAuthenticationFilter.class)
                //토큰 재발급 URL은 인증없이 접근 가능하도록 설정, 나머지 API URL은 인증필요
                .authorizeRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/api/token")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        //Authorization 요청과 관련된 상태 저장
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.authorizationRequestRepository(oAuth2AutorizationRequestBasedOnCookieRepository()))
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserCustomService))
                        //인증 성공시 실행할 핸들러
                        .successHandler(oAuthSuccessHandler())
                )
                // /api로 시작하는 url인 경우 401 상태 코드를 반환하도록 예외처리
                .exceptionHandling(exceptionhandling -> exceptionhandling
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        ))
                .build();
    }

    public OAuth2SuccessHandler oAuthSuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AutorizationRequestBasedOnCookieRepository(),
                userService);
    }

    @Bean
    public TokenAuthenticaionFilter tokenAuthenticaionFilter() {
        return new TokenAuthenticaionFilter(tokenProvider);
    }

    @Bean
    public OAuth2AutorizationRequestBasedOnCookieRepository oAuth2AutorizationRequestBasedOnCookieRepository() {
        return new OAuth2AutorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
