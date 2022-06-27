package hello.hellospring;

import hello.hellospring.repository.JdbcTemplateMemberRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    //DB와 연결할 수 있는, 데이터 정보가 있는 'DataSource' 주입(DI) 방법1
    //@Autowired DataSource dataSource;

    //DB와 연결할 수 있는, 데이터 정보가 있는 'DataSource' 주입(DI) 방법2
    private DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Bean //'memberService' 스프링 빈 등록
    public MemberService memberService() {

        return new MemberService(memberRepository());
    }

    @Bean //'memberRepository' 스프링 빈 등록
    public MemberRepository memberRepository() {

//        return new MemoryMemberRepository(); //memory구현체 리포지토리
//        return new JdbcMemberRepository(dataSource); //JDBC 리포지토리

        //JDBC Template
        return new JdbcTemplateMemberRepository(dataSource); //JDBC템플릿 리포지토리
    }

}
