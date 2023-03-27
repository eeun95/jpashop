package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
/*
 class level에 트랜잭션을 쓰면 모든 메서드에 적용됨 (스프링이 제공하는 어노테이션 사용)
 JPA가 조회하는 곳에서 성능을 최적화함 (더티체킹을 안하거나 읽기 전용 트랜잭션일 경우 리소스를 많이 사용하지 않음)
 읽기에는 가급적 readOnly 옵션 넣어주기
 */
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        // 영속성 컨텍스트에 올라오는 순간(persist) id값이 항상 생성되어 있다는게 보장이 됨
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 멀티쓰레드 환경을 고려하면 좋은 로직은 아님 같은 (이름의 회원이 동시에 요청이 들어올 경우)
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 단건 조회
    public Member findById(Long id) {
        return memberRepository.findOne(id);
    }
}
