package jpabook.jpashop.api;

import javassist.Loader;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.dto.OrderSimpleQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
* x To 1 (ManyToOne, OneToOne)
* Order
* Order -> Member
* Order -> Delivery
* */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // Order와 Member가 양방향 연관관계이기 때문에 무한 루프 생성 (Member -> Order, Order -> Member ...)
        // 연관관계 한 쪽에 JsonIgnore를 해주면 무한루프는 막을 수 있지만
        // (fetchType 이 LAZY일 경우 프록시 객체를 가져온다 실제 데이터를 사용할 때 프록시 초기화하면서 객체를 꺼내옴)
        // JPA가 프록시 라이브러리를 사용하여 프록시 객체를 가져오기 떄문에 Jackson이 접근할 수 없어서 에러를 냄

        for(Order order : orders) {
            order.getMember().getName();        // Lazy 강제 초기화
            order.getDelivery().getAddress();   // Lazy 강제 초기화
            // 이렇게 해주면 실제 객체로 접근
        }

        // 엔티티 노출은 API 스펙 문제도 있지만 성능상에도 문제가 있음 (불필요한 쿼리 실행)
        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        // Lazy 로딩으로 인한 데이터 베이스 쿼리가 너무 많이 호출되는 문제점이 아직 잔존
        // Order  -> Member -> Delivery

        return orderRepository.findAllByString(new OrderSearch()).stream()
                //.map(o -> new SimpleOrderDto(o))
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        return orderRepository.findAllWithMemberDelivery().stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        return orderRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        // DTO가 엔티티로 파라미터 받는 것은 크게 문제가 되지 않음
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();             // 영속성 컨텍스트가 디비에서 찾아옴
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();     // 영속성 컨텍스트가 디비에서 찾아옴
        }
    }
}
