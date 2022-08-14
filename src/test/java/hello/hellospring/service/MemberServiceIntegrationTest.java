package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// test_hellospring_service_MemberServiceTest 클래스 파일을 복사해와서 수정하였음.

@SpringBootTest  // 스프링 컨테이너와 테스트를 함께 실행한다. 즉, 이전에 해보았던 다른 test들과는 다르게, Spring을 사용하므로 @SpringBootTest 를 적어주어야 한다.
@Transactional  // 테스트 케이스에 @Transactional 를 적어주면 테스트 시작 전에 트랜잭션을 시작하고, 테스트 완료 후에 항상 롤백한다.
                // 이렇게 하면 DB에 데이터가 남지 않으므로 다음 테스트에 영향을 주지 않는다.
                // 덕분에 @AfterEach로 DB 초기화를 시켜주는 코드를 작성하지 않아도 된다.
class MemberServiceIntegrationTest {

    // MemberRepository memberRepository = new JdbcMemberRepository(dataSource);
    @Autowired MemberRepository memberRepository;  // MemoryMemberRepository가 아닌, MemberRepository로 코드를 수정한다.
                                                   // 그 이유는 main_hellospring_SpringConfig 의 코드를 jdbc 연결로 변경했기때문이다.
    // MemberService memberService = new MemberService(memberRepository);
    @Autowired MemberService memberService;

    @Test
    void 회원가입() {  // 사실 테스트의 메소드명은 한글로 만들어도 된다.
        // given  // 1. 테스트에서 이러한 것이 주어졌는데
        Member member = new Member();
        member.setName("hello");

        // when  // 2. 이것을 실행했을때
        Long saveId = memberService.join(member);  // 테스트로 memberService의 join 메소드를 검증하겠다

        // then  // 3. 이러한 결과가 나와야한다
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());  // 여기의 Assertions는 assertj 꺼다. option + enter 로 스태틱 임포트 해주면 된다.
    }

    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,  // assertThrows 메소드는 첫번째 인자로 발생할 예외 클래스의 Class 타입을 받고, 두번째 인자에서 예외 발생시 그 예외가 앞의 예외class와 동일한것인지 체크한다.
                () -> memberService.join(member2));// memberService.join(member2) 를 실행할때, 예외가 발생시 예외class타입을 반환함.

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        /*
        // 여기서 try ~ catch 사용 가능하긴함
        try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
        */
    }
}