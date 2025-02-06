package com.example.user.member.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public  final class MemberDto {
    private MemberDto() {
        
    }

    @Builder
    public record MemberSaveRequest(
            @NotBlank(message = "아이디를 입력하십시오.") // null, empty "", blank " "
            @Size(min = 3, message = "아이디는 세 글자 이상 입력하세요.")
            @Size(max = 50, message = "아이디는 최대 15글자입니다.")
            String loginId,

            @NotNull(message = "비밀번호를 입력하십시오.")
            @Size(min = 3, message = "비밀번호는 세 글자 이상 입력하세요.")
            String password, 
            
            @NotNull(message = "이름을 입력하십시오.")
            String name,
            
            @NotNull(message = "이메일을 입력하십시오.")
            String email) {
    }
}
