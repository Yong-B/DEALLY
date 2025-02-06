package com.example.user.member.controller;

import com.example.user.member.controller.dto.MemberDto.MemberSaveRequest;
import com.example.user.member.domain.Member;
import com.example.user.member.usecase.MemberSaveUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberSaveUseCase memberSaveUseCase;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) {
        return "member/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute MemberSaveRequest dto, RedirectAttributes redirectAttributes) {
        
        Member member = Member.builder()
                .loginId(dto.loginId())
                .password(dto.password())
                .name(dto.name())
                .email(dto.email())
                .build();
        memberSaveUseCase.save(member);
        redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인하세요!");
        return "redirect:/";
    }
}
