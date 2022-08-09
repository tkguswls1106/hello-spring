package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    // MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    MemoryMemberRepository memberRepository;
    // MemberService memberService = new MemberService(memberRepository);
    MemberService memberService;

    @BeforeEach  // BeforeEach는 클래스 테스트 실행시, 각 테스트 메소드들이 실행되기 전에 미리 앞서서 어떠한 동작을 실행할수있게 해주는 역할이다.
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach  // AfterEach는 클래스 테스트 실행시, 각 메소드들이 실행이 끝날때마다 어떠한 동작을 실행할수있게 해주는 역할이다.
    public void afterEach() {
        memberRepository.clearStore();  // 이는 MemoryMemberRepository 클래스 안에 clearStore 메소드를 적어주고 코드를 적은것이다.
    }

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