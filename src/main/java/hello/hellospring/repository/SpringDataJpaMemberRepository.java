package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 아래의 SpringDataJpaMemberRepository 인터페이스의 경우처럼 인터페이스 JpaRepository를 상속받는 인터페이스의 경우,
// 스프링 데이터 JPA가 인터페이스 SpringDataJpaMemberRepository의 메소드 구현체를 알아서 자동으로 만들어서 스프링 빈으로 자동 등록해준다.
// 뿐만아니라 MemberRepository라는 빈을 따로 등록할 필요가 없다. 스프링 jpa가 구현체를 자동으로 생성하여 bean으로 자동 등록해주었기 때문에 그냥 호출해서 사용하면된다.
// 즉, 인터페이스 스프링 제이피에이가 구현체를 자동으로 만들어줌으로써 그걸 가져다쓰기만하면되고, 이걸 SpringConfig에서 따로 Bean등록없이 MemberRepository를 @Autowired 할수있게 해준다.
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {  // 인터페이스가 인터페이스를 상속받을때에는 implements가 아닌, extends를 사용한다. 그리고 콤마(,)로 구분하여 다중 상속이 가능하다.
                                                                                                        // <Member, Long>는 <@Entity가 적혀있는 Member 클래스의 DB 엔티티, Member 엔티티의 pk id의 자료형인 Long> 이다.  // 자료형 <class T,ID 식별자 pk 자료형>
    @Override
    Optional<Member> findByName(String name);
    // findAll , save, findById는 구현이 필요없는데, 이는 JpaRepository안에 매우 기본적이고 공통적인 CRUD등이 전부 구현되어 있기 때문이다.
    // 하지만, findByName처럼 JpaRepository에 없는 특별한 경우에 대해서는 구현되어 있기 어렵다. (모든 시스템이 다르기 때문에)
    // 그래서 findByName 은 직접 구현해주어야한다.
}