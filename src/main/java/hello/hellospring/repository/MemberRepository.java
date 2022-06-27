package hello.hellospring.repository;

//import java.lang.reflect.Member;
import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    //Repository에 4가지 기능 (save, findById, findByName, findByAll) 생성

    Member save(Member member); //회원을 저장하면 저장된 회원이 반환됨

    Optional<Member> findById(Long id); //위의 ID로 회원을 찾는 기능을 생성하기 위해 필요
    Optional<Member> findByName(String name);
    //Optional이란? -> 'findBy'로 데이터를 가져올때, 데이터가 null값인 경우 null을 처리하는 방법

    List<Member> findAll(); //저장된 모든 회원 리스트를 반환
}
