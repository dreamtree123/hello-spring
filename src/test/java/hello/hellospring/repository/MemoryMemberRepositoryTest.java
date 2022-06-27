package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryMemberRepositoryTest { //다른데서 가져다 쓸 것이 아니기 때문에 Public 삭제

    //MemberRepository repository = new MemoryMemberRepository();
    MemoryMemberRepository repository = new MemoryMemberRepository(); //MemoryMemberRepository만 테스트하는것이므로 변경?

    @AfterEach //검증 메소드들이 각각 끝날 때마다 동작하는(호출되는) 콜백메소드
    public void afterEach() {
        repository.clearStore();
    }

    //구현한 save 기능이 동작하는지 확인
    @Test
    public void save() {
        Member member = new Member(); //member 객체 생성
        member.setName("spring"); //member의 이름을 'spring'으로 설정
        repository.save(member); //repository(회원 저장소)에다가 'member'을 save

        //save시 저장된 Id를 DB에서 가지고 오는지 확인 (검증)
        Member result = repository.findById(member.getId()).get(); //optional에서 값을 꺼낼 때는 get() 사용

        //검증 방법 1
        //DB에서 꺼낸것 == new로 선언했던 member -> true
        //System.out.println("result = " + (result == member)); //같으면 콘솔창에 'result = true' 출력

        //검증 방법 2
        //Assertions.assertEquals(result, member);
        //Assertions.assertEquals(member, result); //우리가 기대(expert)하는것은 findById를 거친 후의 'member'이므로 순서 주의!

        //검증 방법3
        assertThat(member).isEqualTo(result);
        }


    //구현한 findByName 기능이 동작하는지 확인
    @Test
    public void findByName() {
        Member member1 = new Member(); //member1 객체 생성
        member1.setName("spring1"); //member1의 이름을 "spring1"으로 설정
        repository.save(member1); //repository(회원 저장소)에다가 'member1'을 save

        Member member2 = new Member(); //member2 객체 생성
        member2.setName("spring2" ); //member2의 이름을 "spring2"으로 설정
        repository.save(member2); //repository(회원 저장소)에다가 'member2'을 save

        //Optional<Member> result = repository.findByName("spring1");
        Member result = repository.findByName("spring1").get(); //get()을 사용하면 Optional 필요 x
        assertThat(result).isEqualTo(member1);
    }


    //구현한 findByAll 기능이 동작하는지 확인
    @Test
    public void findAll() {
        Member member1 = new Member(); //member1 객체 생성
        member1.setName("spring1"); //member1의 이름을 "spring1"으로 설정
        repository.save(member1); //repository(회원 저장소)에다가 'member1'을 save

        Member member2 = new Member(); //member2 객체 생성
        member2.setName("spring2" ); //member2의 이름을 "spring2"으로 설정
        repository.save(member2); //repository(회원 저장소)에다가 'member2'을 save

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }

}
