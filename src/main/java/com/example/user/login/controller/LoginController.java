package com.example.user.login.controller;

import com.example.user.login.controller.dto.LoginForm;
import com.example.user.login.usecase.LoginUseCase;
import com.example.user.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginUseCase loginUseCase;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult) {
        System.out.println("로그인 시도 - ID: " + form.getLoginId() + ", PW: " + form.getPassword());

        if (bindingResult.hasErrors()) {
            System.out.println("유효성 검사 실패: " + bindingResult.getAllErrors());
            return "login/loginForm";
        }

        try {
            Optional<Member> loginMember = loginUseCase.login(form.getLoginId(), form.getPassword());
            if (loginMember.isEmpty()) {
                bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
                return "login/loginForm";
            }
            return "redirect:/basic/items";
        } catch (Exception e) {
            System.out.println("로그인 중 오류 발생: " + e.getMessage());
            bindingResult.reject("loginError", "로그인 처리 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return "login/loginForm";
        }
    }
}
