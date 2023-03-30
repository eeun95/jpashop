package jpabook.jpashop.domain.item;


import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter
@DiscriminatorValue("B")
public class Book extends Item {

    private String author;
    private String isbn;

    public Book () {}

    @Builder
    public Book(Long id, String name, int price, int stockQuantity, String author, String isbn) {
        super.setId(id);
        super.setName(name);
        super.setPrice(price);
        super.setStockQuantity(stockQuantity);

        this.author = author;
        this.isbn = isbn;
    }
}
