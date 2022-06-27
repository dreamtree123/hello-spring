package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository{

    //JdbcTemplate 자체를 injection 받을수는 없고, DataSource를 injection 받아야함
    private final JdbcTemplate jdbcTemplate;

    @Autowired //스프링 빈 등록할 때 생성자가 1개인 경우, @Autowired 어노테이션 생략 가능
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate); //jdbcTemplate을 넘겨서 SimpleJdbcInsert 객체 생성

        //테이블명과 key명을 넣어줌으로써 쿼리를 직접 짤 필요가 x
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id); //sql문을 날린 후 memberRowMapper()로 맵핑
        return result.stream().findAny(); //Optional로 반환
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name); //sql문을 날린 후 memberRowMapper()로 맵핑
        return result.stream().findAny(); //Optional로 반환
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper()); //sql문을 날린 후 memberRowMapper()로 맵핑
    }

//    람다식 변환 전
//    private RowMapper<Member> memberRowMapper() {
//        return new RowMapper<Member>() {
//            @Override
//            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Member member = new Member(); //member객체 생성1
//                member.setId(rs.getLong("id")); //member객체 생성2
//                member.setName("name"); //member객체 생성3
//                return member; //member객체 반환
//            };
//        }
//    }

//    람다식 변환 후 (윈도우 단축키 : alt + enter)
    //rs 결과를 member객체로 맵핑해준 후 반환
    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member(); //member객체 생성1
            member.setId(rs.getLong("id")); //member객체 생성2
            member.setName(rs.getString("name")); //member객체 생성3
            return member; //member객체 반환
        };
    }

}
