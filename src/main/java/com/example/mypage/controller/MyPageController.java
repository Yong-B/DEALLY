package com.example.mypage.controller;

import com.example.mypage.service.MypageService;
import com.example.user.login.security.dto.CustomMemberDetails;
import com.example.user.member.controller.dto.MemberDto;
import com.example.user.member.controller.dto.MemberInfoDto;
import com.example.user.member.usecase.MemberUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/basic/mypage")
public class MyPageController {

    private final MypageService mypageService;
    private final MemberUpdateUseCase memberUpdateUseCase;

    @GetMapping
    public String MemberInfoDto(@AuthenticationPrincipal CustomMemberDetails memberDetails, Model model) {
        String loginId = memberDetails.getUsername();
        MemberInfoDto info = mypageService.getMemberInfo(loginId);

        model.addAttribute("memberInfo", info);
        return "basic/mypage"; // 마이페이지 템플릿
    }

    @PostMapping("/update")
    public String updatePassword(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                                 @RequestParam("password") String newPassword,
                                 RedirectAttributes redirectAttributes) {
        String loginId = memberDetails.getUsername();
        memberUpdateUseCase.updatePassword(loginId, newPassword);
        redirectAttributes.addFlashAttribute("message", "수정 완료!");
        return "redirect:/basic/mypage";
    }
}
