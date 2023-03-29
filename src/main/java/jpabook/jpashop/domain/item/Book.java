package jpabook.jpashop.domain.item;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("B")
public class Book extends Item {

    private String author;
    private String isbn;

    protected Book () {}

    @Builder
    public Book(String name, int price, int stockQuantity, String author, String isbn) {
        super.setName(name);
        super.setPrice(price);
        super.setStockQuantity(stockQuantity);

        this.author = author;
        this.isbn = isbn;
    }
}
