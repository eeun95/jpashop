package jpabook.jpashop.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    // OSIV 옵션을 켜게 되면 너무 오랜시간동안 데이터베이스 커넥션 리소스를 사용하기 때문에 실시간 트래픽이 중요할 때 커넥션 부족 발생
    // OSIV 옵션을 끝 상태로 복잡성을 관리하기 위해 쿼리용 서비스를 만들어 이 트랜잭션 안에서 모두 작동하도록 커맨드/쿼리 분리
}
