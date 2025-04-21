package com.example.product.controller;

import com.example.product.controller.dto.ItemCommandDto.ItemSaveRequest;
import com.example.product.domain.Item;
import com.example.product.domain.ItemStatus;
import com.example.product.file.FileStore;
import com.example.product.file.domain.UploadFile;
import com.example.product.usecase.ItemSaveUseCase;
import com.example.product.usecase.ItemSelectAllUseCase;
import com.example.product.usecase.ItemSelectOneUseCase;
import com.example.product.usecase.ItemUpdateUseCase;
import com.example.product.usecase.ItemDeleteUseCase;
import com.example.user.login.security.dto.CustomMemberDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/basic/items")
public class ItemController {
    private final ItemSaveUseCase itemSaveUseCase;
    private final ItemSelectAllUseCase itemSelectAllUseCase;
    private final ItemSelectOneUseCase itemSelectOneUseCase;
    private final ItemUpdateUseCase itemUpdateUseCase;
    private final ItemDeleteUseCase itemDeleteUseCase;
    private final FileStore fileStore;

    @PostMapping("/add") // 추가
    public String save(@ModelAttribute @Valid ItemSaveRequest dto, @AuthenticationPrincipal CustomMemberDetails memberDetails, RedirectAttributes redirectAttributes) {

        String loginId = memberDetails.getUsername();

        Item item = Item.builder()
                .itemName(dto.itemName())
                .price(dto.price())
                .description(dto.description())
                .userId(loginId)
                .build();

        itemSaveUseCase.save(item);

        try {
            // 파일 저장 및 Item과 연결
            List<UploadFile> uploadFiles = fileStore.storeFiles(dto.imageFiles(), item);
            item.addImages(uploadFiles);

            itemSaveUseCase.save(item);  // Item과 이미지 함께 저장
        } catch (IOException e) {
            log.error("파일 저장 실패", e);
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/basic/items/add";  // 파일 저장 실패 시 등록 폼으로 리다이렉트
        }

        redirectAttributes.addAttribute("itemId", item.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping // 목록 조회
    public String items(Model model,
                        @PageableDefault(page = 0, size = 30, sort = "id", direction = Direction.ASC)Pageable pageable,
                        String searchKeyword) {
        Page<Item> items;

        if (searchKeyword == null) {
            items = itemSelectAllUseCase.findAll(pageable);
        } else {
            items = itemSelectAllUseCase.searchByItemName(searchKeyword, pageable);
        }

        model.addAttribute("items", items);
        model.addAttribute("searchKeyword", searchKeyword);

        // 검색 결과 없을 때 메시지 추가
        if (items.isEmpty()) {
            model.addAttribute("noResultsMessage", "게시글이 없어요. 검색어를 수정하시거나, 다른 조건으로 검색해주세요.");
        }
        return "basic/items";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/{itemId}") // 상세 조회
    public String item(@PathVariable Long itemId, @AuthenticationPrincipal CustomMemberDetails memberDetails, Model model) {
        Item item = itemSelectOneUseCase.findById(itemId);
        model.addAttribute("item", item);

        if (memberDetails == null) {
            return "purchase/item-purchase";
        }

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

    @PostMapping("/{itemId}/changeStatus")
    public String changeStatus(@PathVariable Long itemId, @RequestParam("status") ItemStatus status,
                               RedirectAttributes redirectAttributes) {
        itemUpdateUseCase.updateStatus(itemId, status);

        // 상태가 COMPLETED일 경우 /basic/chatroom으로 리다이렉트
        if (status == ItemStatus.COMPLETED) {
            return "redirect:/basic/{itemId}/chatroom-selection";
        }

        // 상태가 COMPLETED가 아니라면 해당 아이템의 상세 페이지로 리다이렉트
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/delete")
    public String delete(@PathVariable Long itemId) {
        itemDeleteUseCase.delete(itemId);
        return "redirect:/basic/items";
    }
}