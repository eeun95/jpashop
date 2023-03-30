package jpabook.jpashop.dto;

import jpabook.jpashop.domain.item.Book;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// Setter 없어서 ArgResolver가 바인딩 못했음 ^^; 기초겠지만 몰랐으니 알아두자 !!!
@Setter
@Builder
public class BookForm {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

    public Book toEntity() {
        return Book.builder()
                .id(id)
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .author(author)
                .isbn(isbn)
                .build();
    }
}
