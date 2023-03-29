package jpabook.jpashop.dto;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;

@Getter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
