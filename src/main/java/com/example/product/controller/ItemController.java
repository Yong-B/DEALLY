package com.example.product.controller;

import com.example.product.controller.dto.ItemCommandDto.ItemSaveRequest;
import com.example.product.domain.Item;
import com.example.product.usecase.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

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
    public String save(@ModelAttribute @Valid ItemSaveRequest dto, RedirectAttributes redirectAttributes) {
        Item item = Item.builder()
                .itemName(dto.itemName())
                .price(dto.price())
                .quantity(dto.quantity())
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
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemSelectOneUseCase.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";

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
