package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.BookForm;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/new")
    public String create(BookForm form) {
        itemService.saveItem(form.toEntity());
        return "redirect:/items";
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("items", itemService.findItems());
        return "items/itemList";
    }

    @GetMapping("/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);
        BookForm form = BookForm.builder()
                .id(itemId)
                .name(item.getName())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .author(item.getAuthor())
                .isbn(item.getIsbn())
                .build();
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }
    @PostMapping("/{itemId}/edit/notRecommend")
    public String updateItem(@ModelAttribute("form") BookForm form) {
        Book book = form.toEntity();
        // JPA가 식별할 수 있는 id를 가지고 있기 떄문에 준영속 상태
        // 준영속 엔티티 : 영속성 컨텍스트가 더는 관리하지 않음
        // Book 객체는 이미 DB에 한번 저장되어서 식별자가 존재한다
        // 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티

        // 영속상태 엔티티는 JPA가 관리하기 떄문에 변경 감지(dirty checking)이 일어남
        // 하지만 준영속 상태 객체는 save 없이 수정 불가
        // 1. 변경감지 2. merge 사용(준영속을 영속 상태로 변경)
        itemService.saveItem(book);
        return "redirect:/items";

        /*
        병합 동작 방식
        1. merge() 실행
        2. 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티 조회
            2-1. 1차 캐시에 엔티티가 없으면 DB에서 엔티티를 조회하고 1차 캐시에 저장
        3. 조회한 영속 엔티티에 새로운 엔티티의 값을 채워넣음
        4. 영속 상태인(값이 변경된 영속 엔티티) 엔티티를 반환

        주의!!!
        변경 감지를 사용하면 원하는 속성만 변경할 수 있지만 병합은 모든 필드가 교체됨
        병합할 값이 없으면 null이 되어버림
        -> 변경감지를 사용하자 !!
         */
    }

    @PostMapping("{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
        // 어설프게 엔티티를 파라미터로 사용하지 않고 정확하게 필요한 데이터만 넘김
        // 별도 dto를 만들어 파라미터로 넘김
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }

}
