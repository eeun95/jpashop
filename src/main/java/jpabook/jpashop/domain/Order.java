package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    // 외래키가 있는 Order가 연관관계 주인
    // 이 필드에 값을 세팅해야 FK 값이 변경됨
    private Member member;

    @OneToMany
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    // access를 많이 하는 order 테이블에 FK를 세팅 (어디에 둬도 무관)
    private Delivery delivery;

    // java8 은 하이버네이트가 알아서 지원해줌
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
