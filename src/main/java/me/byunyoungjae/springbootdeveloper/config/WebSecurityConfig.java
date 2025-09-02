//package me.byunyoungjae.springbootdeveloper.config;
//
//import lombok.RequiredArgsConstructor;
//import me.byunyoungjae.springbootdeveloper.service.UserDetailService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.FormLoginDsl;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//
///**
// * 실제 인증처리를 수행한다.
// */
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final UserDetailService userService;
//
//    //1 스프링 시큐리티 기능 비활성화
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console()) //h2Console과 정적리소스는 스프링시큐리티 적용안함
//                .requestMatchers(new AntPathRequestMatcher("/static/**"));
//    }
//
//    //2 특정 HTTP 요청에 대한 웹 기반 보안 구성
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeRequests(auth -> auth //3 인증, 인가 설정
//                                .requestMatchers( //누구나 접근할수 있는 페이지
//                                        new AntPathRequestMatcher("/login"),
//                                        new AntPathRequestMatcher("/signup"),
//                                        new AntPathRequestMatcher("/user")
//                                ).permitAll()
//                                .anyRequest().authenticated())
//                .formLogin(formLogin -> formLogin //4 폼 기반 로그인 설정
//                                .loginPage("/login")
//                                .defaultSuccessUrl("/articles")
//                        )
//                .logout(logout -> logout //5 로그아웃 설정
//                        .logoutSuccessUrl("/login") //로그아웃 완료시 이동할 페이지
//                        .invalidateHttpSession(true) //로그아웃 이후 세션을 전체 삭제할지 여부를 설정
//                )
//                .csrf(AbstractHttpConfigurer::disable) //6 csrf 비활성화
//                .build();
//    }
//
//    /**
//     * 7 인증 관리자 관련 설정
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder
//            , UserDetailService userDetailService) throws Exception {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService);    //8 사용자 정보 서비스 설정 - 사용자정보를 가져올 서비스 설정,
//                                                            // 서비스는 반드시 userDetailsService를 상속받은 클래스여야함
//        authProvider.setPasswordEncoder(bCryptPasswordEncoder); //비밀번호를 암호화하기 위한 인코더 설정
//        return new ProviderManager(authProvider);
//    }
//
//    /**
//     * 9 패스워드 인코더로 사용할 빈 등록
//     */
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
