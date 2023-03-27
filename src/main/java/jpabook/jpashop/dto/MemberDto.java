package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;

public class MemberDto {

    private String name;

    private Address address;

    public MemberDto(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .address(address)
                .build();
    }
}
