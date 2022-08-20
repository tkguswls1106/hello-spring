package hello.hellospring.domain;

import javax.persistence.*;

// 회원 도메인 객체
@Entity  // 이것은 JPA가 관리하는 엔티티이다 라는 것이다.
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  // sql쿼리로 DB에 데이터를 insert해주면, DB가 pk id값을 자동으로 생성해서 부여해주것을 IDENTITY 전략(strategy) 이라고 부른다.
    private Long id;

    // @Column(name = "name(필드와 매핑할 테이블의 컬럼 이름. 기본값은 객체의 필드 이름)")  // @Column(name = "username")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
