package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    // 화면에 의존하는 로직(쿼리용)을 별도 패키지에 둠
    // repository는 엔티티만 조회하는 로직만 둠
    // repository는 가급적 순수한 엔티티를 조회할 때만 사용하자

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o " +
                "join o.member m " +
                "join o.delivery d", OrderSimpleQueryDto.class).getResultList();
    }

    /*
    쿼리 방식 선택 권장 순서
    1. 우선 엔티티를 DTO로 변환
    2. 필요하면 페치 조인으로 성능을 최적화 -> 대부분의 성능 이슈 해결
    3. 그래도 안되면 DTO로 직접 조회
    4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용
     */
}
