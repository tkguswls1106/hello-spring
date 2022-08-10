package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    // private final MemberService memberService = new MemberService();

    private final MemberService memberService;
    @Autowired  // 생성자에 @Autowired 가 있으면, 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다. 이는 객체 의존관계를 외부에서 넣어주는 것인 DI이다.
    // 스프링 컨테이너가 뜰때 클래스 MemberController를 생성해주는데, 그때 MemberController 생성자 호출을 하게된다.
    // 그런데 그 생성자에 @Autowired 가 붙어있으면, 스프링이 스프링 컨테이너에 저장된 MemberService를 가져와서 의존성 연결을 MemberController에다가 해준다.
    // 하지만 이는 혹여나 MemberService를 스프링 컨테이너에 스프링 빈으로 등록(저장)해두지 않은상태에서 @Autowired로 의존관계 연결해주고 Run을 하게된다면, 에러가 발생하게 된다.
    // 그러므로 MemberService 클래스는 HelloController도 아니고, @Controller 등등의 어노테이션도 안붙어있기때문에, 따로 스프링 빈에 등록해주는 코드를 작성해주어야 정상적인 Run이 가능하다.
    public MemberController(MemberService memberService) {  // MemberService 매개변수를 가진 생성자 MemberController 메소드를 적어주어, MemberController를 MemberService에 연결하여 의존관계를 형성하였다. (DI 방법중 생성자 주입 방법)
        this.memberService = memberService;
    }

    @GetMapping("/members/new")  // method='get' 방식일때
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")  // method='post' 방식일때
    public String create(MemberForm form) {
        Member member = new Member();  // 멤버 객체를 생성하고,
        member.setName(form.getName());  // 매개변수로 가져온 form의 name 정보를 getName으로 가져와서, 그 name 정보를 멤버 객체에 setName으로 저장함.

        memberService.join(member);  // 완성된 멤버 객체를 멤버서비스의 join메소드로 회원가입 기능을 실행함. 즉, 회원 데이터 저장.

        return "redirect:/";  // 홈화면으로 리다이렉트하여 보내버림.
    }
}

/*
<라우팅 과정>
사용자가 /members/new 링크로 접속
->
main_hellospring_controller_MemberController 의 @GetMapping("/members/new") 부분의 코드 실행
->
return "members/createMemberForm"; 로 인하여 main_templates_members_createMemberForm.html 파일로 이동하여, 겉폼양식 출력
->
input name의 입력할데이터 입력하고 summit 버튼으로 post방식으로 데이터 전달함
->
main_hellospring_controller_MemberController 의 @PostMapping("/members/new") 부분의 코드 실행
->
가져온데이터가 main_hellospring_controller_MemberForm 의 객체 형태(MemberForm form)로 저장되어, @PostMapping("/members/new") 부분의 메소드의 매개변수로 할당됨.
->
멤버 객체를 생성하고,
매개변수로 가져온 form의 name 정보를 getName으로 가져와서,
그 name 정보를 멤버 객체에 setName으로 저장함.
그리고 완성된 멤버 객체를 멤버서비스의 join메소드로 회원가입 기능을 실행함. 즉, 회원 데이터 저장.
마지막으로 return "redirect:/";으로, 홈화면으로 리다이렉트하여 보내버림.
*/
