package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    // private final MemberService memberService = new MemberService();

    private final MemberService memberService;
    @Autowired  // 생성자에 @Autowired 가 있으면, 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다. 이는 객체 의존관계를 외부에서 넣어주는 것인 DI이다.
    // 스프링 컨테이너가 뜰때 클래스 MemberController를 생성해주는데, 그때 MemberController 생성자 호출을 하게된다.
    // 그런데 그 생성자에 @Autowired 가 붙어있으면, 스프링이 스프링 컨테이너에 저장된 MemberService를 가져와서 의존성 연결을 MemberController에다가 해준다.
    // 하지만 이는 혹여나 MemberService를 스프링 컨테이너에 스프링 빈으로 등록(저장)해두지 않은상태에서 @Autowired로 의존관계 연결해주고 Run을 하게된다면, 에러가 발생하게 된다.
    // 그러므로 MemberService 클래스는 HelloController도 아니고, @Controller 도 안붙어있기때문에, 따로 스프링 빈에 등록해주는 코드를 작성해주어야 정상적인 Run이 가능하다.
    public MemberController(MemberService memberService) {  // MemberService 매개변수를 가진 생성자 MemberController 메소드를 적어주어, MemberController를 MemberService에 연결하여 의존관계를 형성하였다. (DI)
        this.memberService = memberService;
    }
}
