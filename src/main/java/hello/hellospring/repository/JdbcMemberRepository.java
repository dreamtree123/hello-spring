package hello.hellospring.repository;
import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {
    private final DataSource dataSource;
    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)";

        //사용한 자원들 릴리즈 (리소스 반환) -> 안하면 DB커넥션 계속 쌓이다가 DB 장애 가능성
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; //'ResultSet'으로 결과를 받음

        try {
            conn = getConnection(); //getConnection()을 통해 커넥션을 가져온 후 'conn'에 담음
            pstmt = conn.prepareStatement(sql, //커넥션에서 sql문을 날리고
                    Statement.RETURN_GENERATED_KEYS); //자동으로 생성된 id값
            pstmt.setString(1, member.getName()); //'parameterindex'를 sql문의 '?'와 매칭시킨 후, 'values()'에 'member.getName()'값을 넣음

            pstmt.executeUpdate(); //DB에 Update sql문을 뿌림

            rs = pstmt.getGeneratedKeys(); //DB가 생성한 key(=Id)를 반환

            if (rs.next()) { //ResultSet rs에 값이 있으면
                member.setId(rs.getLong(1)); //getLong()하여 값을 꺼낸 후 Id를 셋팅
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;

        } catch (Exception e) {
            throw new IllegalStateException(e);

        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";

        //사용한 자원들 릴리즈 (리소스 반환) -> 안하면 DB커넥션 계속 쌓이다가 DB 장애 가능성
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection(); //getConnection()을 통해 커넥션을 가져온 후 'conn'에 담음
            pstmt = conn.prepareStatement(sql); //커넥션에서 sql문을 날리고
            pstmt.setLong(1, id); //'parameterindex'를 sql문의 '?'와 매칭시킨 후, 'id'값을 넣음

            rs = pstmt.executeQuery(); //DB에 Query sql문을 뿌림

            if(rs.next()) { //ResultSet rs에 값이 있으면
                Member member = new Member(); //member객체 생성1
                member.setId(rs.getLong("id")); //member객체 생성2
                member.setName(rs.getString("name")); //member객체 생성3
                return Optional.of(member); //member객체 반환
            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);

        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member"; //모두 조회

        //사용한 자원들 릴리즈 (리소스 반환) -> 안하면 DB커넥션 계속 쌓이다가 DB 장애 가능성
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection(); //getConnection()을 통해 커넥션을 가져온 후 'conn'에 담음
            pstmt = conn.prepareStatement(sql); //커넥션에서 sql문을 날리고

            rs = pstmt.executeQuery(); //DB에 Query sql문을 뿌림

            List<Member> members = new ArrayList<>(); //connection에 list형태로 담음

            while(rs.next()) { //ResultSet rs에 값이 있으면
                Member member = new Member(); //member객체 생성1
                member.setId(rs.getLong("id")); //member객체 생성2
                member.setName(rs.getString("name")); //member객체 생성3
                members.add(member); //connection.add(member)
            }
            return members; //member 반환

        } catch (Exception e) {
            throw new IllegalStateException(e);

        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";

        //사용한 자원들 릴리즈 (리소스 반환) -> 안하면 DB커넥션 계속 쌓이다가 DB 장애 가능성
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection(); //getConnection()을 통해 커넥션을 가져온 후 'conn'에 담음
            pstmt = conn.prepareStatement(sql);  //커넥션에서 sql문을 날리고
            pstmt.setString(1, name); //'parameterindex'를 sql문의 '?'와 매칭시킨 후, 'name'값을 넣음

            rs = pstmt.executeQuery(); //DB에 Query sql문을 뿌림

            if(rs.next()) { //ResultSet rs에 값이 있으면
                Member member = new Member(); //member객체 생성1
                member.setId(rs.getLong("id")); //member객체 생성2
                member.setName(rs.getString("name")); //member객체 생성3
                return Optional.of(member); //member객체 반환
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);

        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource); //Spring을 통해 커넥션을 얻을때는 꼭 필요한 문장
    }

    //커넥션 닫기
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource); //Spring을 통해 커넥션을 닫을때는 꼭 필요한 문장
    }
}