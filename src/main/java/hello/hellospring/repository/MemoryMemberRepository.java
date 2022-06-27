package hello.hellospring.repository;

//import java.lang.reflect.Member;

import hello.hellospring.domain.Member;

import java.util.*;

//@Repository
//생성한 MemberRepository 인터페이스를 implements하는 메모리 구현체
public class MemoryMemberRepository implements MemberRepository {

    //동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
    private static Map<Long, Member> store = new HashMap<>(); //어딘가에 저장을 시키기 위해 Map 사용, Key=Long, Value=Member
    private static long sequence = 0L; //sequence = 0,1,2 등 key값을 생성

    //기능 1) save
    @Override
    public Member save(Member member) { //member 클래스에는 id와 name이 변수로 존재하는 상태
        member.setId(++sequence); //id값을 (일련번호 형식으로) 셋팅한 후
        store.put(member.getId(), member); //시스템에 의해 셋팅된 id, 고객이 입력한 name 정보를 store에 저장 -> map에도 저장됨
        return member; //저장된 결과를 반환
    }


    //기능 2) findById
    @Override
    public Optional<Member> findById(Long id) {
        //return store.get(id);
        return Optional.ofNullable(store.get(id)); //store에서 get한 후, id를 넣음
        //Optional.ofNullable()로 감싸는 이유 : null이 반환될 가능성이 있으므로! 감싸서 반환을 해주면 클라이언트 측에서 처리 가능
    }


    //기능 3) findByName
    @Override
    public Optional<Member> findByName(String name) {
        //루프로 돌리기
        return store.values().stream()
                .filter(member -> member.getName().equals(name)) //람다 사용, member.getName()이 파라미터로 넘어온 name이랑 같은지 확인 (필터링)
                .findAny(); //같은 name을 찾으면 결과가 Optinal로 반환, 같은 name이 없으면 Optional에 null이 포함되어 반환됨
    }

    //기능 4) findAll
    @Override
    public List<Member> findAll() { //List 형태로 되어있음
        return new ArrayList<>(store.values()); //store에 있는 values = member, 결국 member들이 List형태로 반환되는 것.
    }



    //clear 기능
    public void clearStore() {
        store.clear();
    }
}
