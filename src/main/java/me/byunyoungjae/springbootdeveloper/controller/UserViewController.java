package me.byunyoungjae.springbootdeveloper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 회원가입, 로그인 페이지로 연결하는 컨트롤러 클래스
 */
@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login() {
        return "oauthLogin";
    }

    @GetMapping("signup")
    public String signup() {
        return "signup";
    }

}
