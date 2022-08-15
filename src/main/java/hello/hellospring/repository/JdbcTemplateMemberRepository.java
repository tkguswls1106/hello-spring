package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;  // 메모리 구현이 아닌, DB로 연결
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    // @Autowired를 생략할 수 있다.
    // 스프링 부트의 경우 DataSource 같은 DB Connection에 사용하는 기술 지원 로직까지 내부에서 자동으로 빈으로 컨테이너에 등록하기때문에,
    // 생성자가 하나이고 그 생성자의 파라미터가 빈에 등록되어있는 상태라면, @Autowired를 생략 가능하다라는 조건에 충족하여, @Autowired를 생략할 수 있다.
    public JdbcTemplateMemberRepository(DataSource dataSource) {  // DataSource를 인젝션 받는다.
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // query() 메소드는 sql 파라미터로 전달받은 쿼리를 실행하고 RowMapper를 이용해서 ResultSet의 결과를 자바 객체로 변환한다.
        // jdbcTemplate.query()는 List<Member> 형태를 반환함.
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);  // 첫번째 파라미터는 sql 쿼리의 ?물음표 부분에, memberRowMapper()로 불러온 ResultSet의 member 객체의 id값을 집어넣는다.
        return result.stream().findAny();  // 그러면 완성된 쿼리(반환 자료형은 리스트)로 DB에 적용시켜 DB에서 찾아봄.
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper() {  // RowMapper 는 결과인 ResultSet인 rs 값을 담아와서 그 객체를 반환하는 역할이다.
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
    /*
    // 아래 코드를 option + enter 키를 눌러, 람다로 변경한것이 위의 코드임.
    // 참고로 람다 함수는 함수형 프로그래밍 언어에서 사용되는 개념으로 익명 함수라고도 한다.
    // RowMapper 인터페이스의 mapRow() 메소드는 SQL 실행 결과로 구한 ResultSet에서 한 행의 데이터를 읽어와 자바 객체로 변환하는 매퍼 기능을 구현한다.
    private RowMapper<Member> memberRowMapper() {
        return new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return member;
            }
        };
    }
    */
}
