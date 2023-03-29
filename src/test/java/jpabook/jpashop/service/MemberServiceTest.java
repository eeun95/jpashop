package jpabook.jpashop.service;

import jpabook.jpashop.dto.MemberForm;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Fail.fail;

// 메모리 모드로 실제 DB까지 테스트하기 위해 실제 스프링부트를 올려서 테스트함
// 순수 단위 테스트 X
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    //@Rollback(false)
    void 회원가입() throws Exception {
        // given
        Member member = new MemberForm("seo", "경기", "경수대로", "462").toEntity();

        // when
        Long saveId = memberService.join(member);

        // then
        // em.flush();
        Assert.assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
            // (expected = IllegalStateException.class) ?
    void 중복_회원_예외() throws Exception{
        // given
        Member member1 = new MemberForm("seo", "경기", "경수대로", "462").toEntity();
        Member member2 = new MemberForm("seo", "경기", "경수대로", "462").toEntity();

        // when
        memberService.join(member1);

        // then
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> memberService.join(member2));

        //fail("예외 발생해야 한다");
    }
}