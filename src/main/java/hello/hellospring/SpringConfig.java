package hello.hellospring;

import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    // main_hellospring_repository_SpringDataJpaMemberRepository 덕분에 알아서 구현체가 빈에 등록되어있어 @Autowired로 의존관계 형성 가능해져서 인젝션 받음.
    @Autowired  // @Autowired 생략 가능하긴함.
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean  // 스프링 빈을 내가 직접 등록할거야 라는 의미이다.
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

    /*
    @Bean
    public MemberRepository memberRepository() {  // public 메소드반환자료형 메소드명()  // memberRepository()는 생성자 아니니까 헷갈리지말자!
        // return new MemoryMemberRepository();  // MemberRepository는 인터페이스이기때문에 new로 인스턴스 생성이 불가능하므로, 메모리구현체인 new MemoryMemberRepository() 를 반환한다.
        // return new JdbcMemberRepository(dataSource);  // 이로써 MemoryMemberRepository 를 Jdbc 데이터베이스로 교체하였음.
        // return new JdbcTemplateMemberRepository(dataSource);
        // return new JpaMemberRepository(em);
    }
    */
}

/*
직접 스프링 빈 등록 및 연결 과정(컨트롤러는 어쩔수없이 컴포넌트 스캔 방법 사용):

< main_hellospring_controller_MemberController 에서 >
@Controller 이라서 어쩔수없이 @Autowired로 컴포넌트 스캔 방법으로 MemberController을 MemberService에 연결함.

->

< main_hellospring_SpringConfig 에서 >
@Configuration을 봄
-> @Bean을 봄
-> MemberService와 MemberRepository를 모두 스프링 빈으로 등록

->

< main_hellospring_service_MemberService 에서 (main_hellospring_SpringConfig 에서 확인해도 되긴함) >
스프링 빈에 등록되어있는 MemberRepository를, 생성자메소드 MemberService()안의 매개변수로 넣어줌으로써, 어노테이션 자동 의존관계 연결(컴포넌트 스캔 방법)없이 직접 연결해주게 되는 것이다.

결국 의존관계가 'MemberController' -> 'MemberService' -> MemberRepository' 이렇게 연결되게 되었다.
*/