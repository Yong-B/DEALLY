package com.example.product.controller;

import com.example.product.controller.dto.ItemCommandDto.ItemSaveRequest;
import com.example.product.domain.Item;
import com.example.product.usecase.ItemSaveUseCase;
import com.example.product.usecase.ItemSelectAllUseCase;
import com.example.product.usecase.ItemSelectOneUseCase;
import com.example.product.usecase.ItemUpdateUseCase;
import com.example.product.usecase.ItemDeleteUseCase;
import com.example.user.login.security.dto.CustomMemberDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/basic/items")
public class ItemController {
    private final ItemSaveUseCase itemSaveUseCase;
    private final ItemSelectAllUseCase itemSelectAllUseCase;
    private final ItemSelectOneUseCase itemSelectOneUseCase;
    private final ItemUpdateUseCase itemUpdateUseCase;
    private final ItemDeleteUseCase itemDeleteUseCase;

    @PostMapping("/add") // 추가
    public String save(@ModelAttribute @Valid ItemSaveRequest dto, @AuthenticationPrincipal CustomMemberDetails memberDetails, RedirectAttributes redirectAttributes) {
        
        String loginId = memberDetails.getUsername();
        
        Item item = Item.builder()
                .itemName(dto.itemName())
                .price(dto.price())
                .quantity(dto.quantity())
                .userId(Long.valueOf(loginId))
                .build();
        
        itemSaveUseCase.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping // 목록 조회
    public String items(Model model) {
        List<Item> items = itemSelectAllUseCase.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}") // 상세 조회
    public String item(@PathVariable Long itemId, @AuthenticationPrincipal CustomMemberDetails memberDetails, Model model) {
        Item item = itemSelectOneUseCase.findById(itemId);
        model.addAttribute("item", item);
        
        String loginId = memberDetails.getUsername();
        
        String ownerId = String.valueOf(item.getUserId());

        return loginId.equals(ownerId) ? "basic/item" : "purchase/item-purchase";

    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemSelectOneUseCase.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit") // 수정
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemUpdateUseCase.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/delete")
    public String delete(@PathVariable Long itemId) {
        itemDeleteUseCase.delete(itemId);
        return "redirect:/basic/items";
    }
}
