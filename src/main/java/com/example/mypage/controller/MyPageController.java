package com.example.mypage.controller;

import com.example.mypage.service.MypageService;
import com.example.product.domain.Item;
import com.example.product.usecase.ItemSelectAllUseCase;
import com.example.user.login.security.dto.CustomMemberDetails;
import com.example.user.member.controller.dto.MemberInfoDto;
import com.example.user.member.usecase.MemberUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/basic/mypage")
public class MyPageController {

    private final MypageService mypageService;
    private final MemberUpdateUseCase memberUpdateUseCase;
    private final ItemSelectAllUseCase itemSelectAllUseCase;
    
    @GetMapping
    public String MemberInfoDto(@PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal CustomMemberDetails memberDetails, Model model) {
       
        String loginId = memberDetails.getUsername(); // 현재 로그인한 유저ID
        MemberInfoDto info = mypageService.getMemberInfo(loginId); // 현재 로그인한 유저의 정보
        Page<Item> sellingItems  = itemSelectAllUseCase.findByUserIdItem(loginId, pageable); // 현재 로그인한 유저의 판매상품

        Page<Item> buyingItems = itemSelectAllUseCase.findByBuyerIdItem(loginId, pageable);

        model.addAttribute("buyingItems", buyingItems);
        model.addAttribute("memberInfo", info);
        model.addAttribute("sellingItems", sellingItems);
        
        return "basic/mypage"; // 마이페이지 템플릿
    }

    @PostMapping("/update")
    public String updatePassword(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                                 @RequestParam("password") String newPassword,
                                 RedirectAttributes redirectAttributes) {
        
        String loginId = memberDetails.getUsername();
        String password = memberDetails.getPassword();
        
        if (newPassword.isEmpty()) {
            newPassword = password;
            redirectAttributes.addFlashAttribute("message", "수정할 내용이 없습니다!");
        } else {
            memberUpdateUseCase.updatePassword(loginId, newPassword);
            redirectAttributes.addFlashAttribute("message", "수정 완료!");
        }
        
        return "redirect:/basic/mypage";
    }
}
