package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpashopApplication.class, args);
    }

    /*
    // 지연로딩은 다 무시
    @Bean
    Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

     */
}