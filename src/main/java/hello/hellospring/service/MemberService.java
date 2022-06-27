package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

//@Service
public class MemberService  {

    //'회원 서비스'를 만들기전에 우선 '회원 리포지토리'가 있어야 한다!
//    private final MemberRepository memberRepository = new MemoryMemberRepository();

    //memberRepository를 직접 new해서 생성하는 것이 아니라, 외부에서 넣어주도록 변경
    private final MemberRepository memberRepository;

    //@Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /* 회원가입 */
    public Long join(Member member) {
        //'같은 이름이 있는 중복 회원은 가입이 불가하다'는 비즈니스 로직 구현
//        Optional<Member> result = memberRepository.findByName(member.getName());
//        result.ifPresent(m -> { //ifPresent()메소드 (Optional로 감쌌기 때문에 사용 가능) : null이 아닌 값이 존재하면 다음 로직이 동작 (m:member)
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        });
        
        //'같은 이름이 있는 중복 회원은 가입이 불가하다'는 비즈니스 로직 구현
        ValidateDuplicateMember(member);

        memberRepository.save(member);
        return  member.getId(); //회원가입을 하면 Id만 반환되도록.

    }

    //'같은 이름이 있는 중복 회원은 가입이 불가하다'는 비즈니스 로직을 구현한 Extracted Method
    private void ValidateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> { //ifPresent()메소드 (Optional로 감쌌기 때문에 사용 가능) : null이 아닌 값이 존재하면 다음 로직이 동작 (m:member)
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }


    /* 전체 회원 조회 */
    public List<Member> findMembers() {
        return memberRepository.findAll(); //memberRepository클래스에 선언된 findAll() 반환타입이 List이므로, 반환(return)만 해주면 끝남
    }


    /*  */
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
