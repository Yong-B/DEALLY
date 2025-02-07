package com.example.user.login.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@ToString
@Builder
public class LoginForm {
    @NotEmpty(message = "아이디는 필수 입력입니다.")
    String loginId;
    @NotEmpty(message = "비밀번호는 필수 입력입니다.")
    String password;
}
