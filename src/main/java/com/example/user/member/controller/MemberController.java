package com.example.user.member.controller;

import com.example.user.member.controller.dto.MemberDto;
import com.example.user.member.domain.Member;
import com.example.user.member.usecase.MemberSaveUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberSaveUseCase memberSaveUseCase;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") MemberDto dto) {
        return "member/addMemberForm";
    }
    
    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("member") MemberDto dto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 유효성 검사 실패 시 폼으로 다시 돌아가기
        if (bindingResult.hasErrors()) {
            return "member/addMemberForm";
        }

        // 중복 검사 추가
        try {
            Member member = dto.memberSave();
            memberSaveUseCase.save(member);  // 중복 검사 후 회원 저장

            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인하세요!");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            // 중복된 로그인 ID나 이메일이 있을 경우 오류 메시지 처리
            if (e.getMessage().contains("로그인 ID")) {
                bindingResult.rejectValue("loginId", "duplicate.loginId", "이미 사용 중인 로그인 ID입니다.");
            }
            if (e.getMessage().contains("이메일")) {
                bindingResult.rejectValue("email", "duplicate.email", "이미 사용 중인 이메일입니다.");
            }
            return "member/addMemberForm";  // 오류 메시지 표시 후 다시 폼으로 돌아감
        }
    }

    @GetMapping("/check-login-id")
    @ResponseBody
    public Map<String, Boolean> checkLoginId(@RequestParam String loginId) {
        boolean exists = memberSaveUseCase.isLoginIdDuplicate(loginId);
        return Collections.singletonMap("exists", exists);
    }

    // 이메일 중복 체크
    @GetMapping("/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        boolean exists = memberSaveUseCase.isEmailDuplicate(email);
        return Collections.singletonMap("exists", exists);
    }
}
