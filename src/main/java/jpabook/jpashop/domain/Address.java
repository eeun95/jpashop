package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;

    private String street;

    private String zipcode;

    // JPA 구현 라이브러리가 객체를 생성할 때 리플렉션이나 프록시 같은 기술을 사용할 수 있도록 기본 생성자는 필수
    protected Address() {}

    // 값 타입은 immutable하게 설계 되어야 함
    // 생성자에서 값을 모두 초기화하여 변경 불가능한 클래스로 만듦
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
