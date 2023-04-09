package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /*
    @PersistenceContext 어노테이션이 있으면 스프링이 자동으로 엔티티 매니저를 생성하여 주입
    @PersistenceContext
    private EntityManager em;
     */

    // 스프링 데이터 JPA가 아니면 @PersistenceContext 사용해야함 (지원을 안해주기 때문)
    private final EntityManager em;


    /*
    엔티티 매니저 팩토리를 직접 주입받고 싶을 땐
    @PersistenceUnit
    private EntityManagerFactory emf;
     */

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name")
                .setParameter("name", name)
                .getResultList();
    }
}
