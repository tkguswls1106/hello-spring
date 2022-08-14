package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// 여기 부분은 Spring 사용없이 순수한 자바 코드만으로 이루어져있으므로, @SpringBootTest를 적지않아도 된다.
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    // 테스트로 클래스 실행을 하면, 안의 메소드들이 무작위 순서로 실행된다.
    // 그러면서 겹치는 중복 객체라든가가 생기면 에러가 날 수 있기때문에, 각 메소드들 테스트 실행할때마다 다시 리포지토리를 초기화를 시켜주는 clear를 작성해주어야한다.
    @AfterEach  // AfterEach는 클래스 테스트 실행시, 각 메소드들이 실행이 끝날때마다 어떠한 동작을 실행할수있게 해주는 역할이다.
    public void afterEach() {
        repository.clearStore();  // 이는 MemoryMemberRepository 클래스 안에 clearStore 메소드를 적어주고 코드를 적은것이다.
    }

    @Test
    public void save() {  // 실행시켜서 녹색이 뜨면 정상실행 검사 성공. 참고로 이처럼 메소드별 검사도 가능하고, 클래스나 전체 등등 여러 범위로 검사 실행이 가능하다.
        Member member = new Member();  // 메소드 매개변수 안에 Member member가 적혀있지않으므로, 따로 new로 생성해주고 사용해야만 한다.
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();  // findById의 반환타입이 Optional이므로,
                                                                    // 반환된 Optional<Member> 에서 정보를 꺼낼땐 .get()를 써준다.
        assertThat(result).isEqualTo(member);
        // 참고로 위와 같은 코드로 Assertions.assertEquals(member, result); 가 있다.
        // 저장하고 난후 DB에서 불러온 result값이, 초반에 new로 저장한 member와, 값이 서로 같은지 잘 저장되고 잘 불러와졌는지 검증(확인)해보는 것임.
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);  // 반환타입이 Optional이 아니므로, .get()을 사용하지 않는다.
    }
}
