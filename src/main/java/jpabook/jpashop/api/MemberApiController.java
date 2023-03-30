package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        // presentation 계층을 위한 검증 로직이 엔티티에 들어있으면 X
        // DTO로 받아줘야 함
        // 엔티티의 스펙을 바꿀 경우 모든 API 스펙 자체가 변경 (큰 문제) 엔티티와 API 스펙은 독립적이어야 함
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        /*
        [API 정석]
        API는 무조건 엔티티 대신 등록과 응답 DTO(별도의 요청값)를 받는다
        엔티티와 프레젠테이션 계층을 위한 로직을 분리할 수 있다
        엔티티와 API 스펙을 명확하게 분리할 수 있다
        엔티티 외부 노출 X !!!
        */
        Member member = Member.builder().name(request.getName()).build();
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @Data
    static class CreateMemberRequest {
        private String name;
    }
    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    // 멱등하다 : 똑같은 요청을 여러번 호출해도 결과가 똑같다
    @PutMapping("/api/v2/members/{memberId}")
    public UpdateMemberResponse updateMemberV2(@PathVariable Long memberId,
                                               @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(memberId, request.getName());
        // command와 query를 철저히 분리 (유지보수성 증대)
        Member findMember = memberService.findById(memberId);
        return new UpdateMemberResponse(findMember.getId(), request.getName());
    }
    @Data
    static class UpdateMemberRequest {
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
