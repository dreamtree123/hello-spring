package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberServiceTest {

    //회원가입을 하기 위해선 서비스가 있어야하므로, 서비스 생성
    MemberService memberService;

    //clear
    MemoryMemberRepository memberRepository;

    @BeforeEach //검증 메소드들이 각각 실행되기 전에 넣기.
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach //검증 메소드들이 각각 끝날 때마다 동작하는(호출되는) 콜백메소드
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {

        //1. given : 기반이 되는 데이터를
        Member member = new Member(); //회원가입을 하기 위해선 member객체 먼저 생성
        member.setName("hello");

        //2. when : 실행했을 때
        Long saveId = memberService.join(member); //memberService(서비스클래스)의 join()을 검증
        // member객체가 저장되었을 때 member객체의 Id가 반환(return)되도록 설정했으므로, Id값을 'saveId' 변수에 담음

        //3. then : 이러한 결과가 나와야 한다 (검증부)
        //우리가 저장한 데이터가 리포지토리에 있는 데이터와 동일한지 찾고자하는 경우, 리포지토리를 꺼내야함
        Member findMember = memberService.findOne(saveId).get(); //findOne()메소드에서 member객체의 Id를 넘기면(=SaveId) 결과가 나오고, 이 결과를 get한 뒤 'findMember'에 담음
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName()); //member객체의 name이 findMember의 name과 같은지 검증
    }

    @Test
    public void 중복회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);

//        try {
//            memberService.join(member2); //동일한 name('spring')이기 때문에 ValidateDuplicateMember()메소드가 실행된 후 예외처리 문장이 떠야함
//            fail();
//        } catch (IllegalStateException e) {
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
////            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다. 12345");
//        }

//        assertThrows(IllegalStateException.class, () -> memberService.join(member2)); //member2의 join이 이루어지면, IllegalStateException 예외가 터져야함
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        //then
    }

    @Test
    void  findMembers() {
    }

    @Test
    void findOne() {
    }
}