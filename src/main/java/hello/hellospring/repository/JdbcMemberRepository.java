package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;  // 메모리 구현이 아닌, DB로 연결
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {

    private final DataSource dataSource;  // DB에 연결하려면 DataSource 라는것이 필요하다. 스프링을 통해서 데이터소스를 주입받을것이다. 예를들어 dataSource.getConnection() 같은 걸로.
    // control + enter 키 입력

    // @Autowired를 생략하였다.
    // 스프링 부트의 경우 DataSource 같은 DB Connection에 사용하는 기술 지원 로직까지 내부에서 자동으로 빈으로 컨테이너에 등록하기때문에,
    // 생성자가 하나이고 그 생성자의 파라미터가 빈에 등록되어있는 상태라면, @Autowired를 생략 가능하다라는 조건에 충족하여, @Autowired를 생략한것이다.
    public JdbcMemberRepository(DataSource dataSource) {  // 메모리 구현이 아닌, DB로 연결
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {  // 아마 이미 저장된 Member 객체 필드 정보를 DB에 저장하는 과정인것 같다.
        String sql = "insert into member(name) values(?)";

        /*
        Connection conn = dataSource.getConnection();

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, member.getName());

        pstmt.executeUpdate();
        */

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;  // 결과를 받는것.

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, member.getName());

            pstmt.executeUpdate();  // DB에 실제 insert 쿼리가 날아감.
            rs = pstmt.getGeneratedKeys();  // getGeneratedKeys()로 DB가 생성한 key값 반환함.

            if (rs.next()) {  // 값이 더 존재한다면
                member.setId(rs.getLong(1));
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

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();  // executeUpdate()와 다르게, 조회는 executeQuery()이다.

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        } }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>();
            while(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {  // 스프링 프레임워크에서 이걸 굳이 사용한다면, DataSourceUtils를 통해서 getConnection()을 따로 라이브러리에서 가져와서 만들어서 사용해야한다.
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try {
            if (rs != null) {
                rs.close();  // 열렸던 역순인 rs, pstmt, conn 순으로 close
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } try {
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
        } }

    private void close(Connection conn) throws SQLException {  // 스프링 프레임워크에서 이걸 굳이 사용한다면, DataSourceUtils를 통해서 close()를 따로 라이브러리에서 가져와서 만들어서 사용해야한다.
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
