package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // findByName이라고 되어있으면 구현체를 만들어야 되는게 일반적이지만 스프링 데이터 JPA는
    // select m from Mmeber m where m.name = ?
    // name을 보고 알아서 위에 쿼리를 만듬 
    List<Member> findByName(String name);

}
