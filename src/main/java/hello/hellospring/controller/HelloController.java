package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {  // 클래스 이름은 java 파일명과 동일하게.

    @GetMapping("hello")  // 웹어플리케이션(웹앱)에서 '/hello'라고 들어오면 밑의 메소드를 호출해준다.
                          // 참고로 GetMapping의 Get은 method="get"방식 의 뜻이다.
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";  // (ViewName)+.html 이므로 hello.html 파일인 View 파일을 가리키게되고,
                         // 위의 data:hello!! 값을 templates_hello.html 파일로 렌더링함.
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {  // 쿼리파라미터 /hello-mvc?name=shj 이런식으로 적어주면 된다. 참고로 쿼리파라미터는 get방식이다.
        model.addAttribute("name", name);
        return "hello-template";
    }
    //  이러한 방식은
    //  템플릿 엔진을 모델,뷰,컨트롤러인 MVC로 쪼개서
    //  뷰파일을 가지고 렌더링을 하여
    //  템플릿 엔진 코드로 템플릿 뷰 파일의 코드를 변경하여
    //  화면에 출력시켜주는 방식인 '템플릿 엔진' 방식이다.

    @GetMapping("hello-string")
    @ResponseBody  // @ResponseBody 를 사용하면 뷰 리졸버(viewResolver)를 사용하지 않아서 뷰 파일과 관련없어진다.
                   // 대신에 HTTP의 BODY에 문자 내용을 직접 반환하여 그 문자만 출력된다.
    public String helloString(@RequestParam("name") String name) {
        return "<strong>hello " + name + "</strong>";  // 예를들어 /hello-string?name=hihihi 이면, 화면상에 hello hihihi 만 출력된다.
        // return의 문자열 부분에 html 코드를 작성할 순 있긴하지만, 이는 매우 비효율적이다.
    }
    //  이러한 방식은 '템플릿 엔진' 방식처럼 뷰파일을 사용하여 변경하거나 하는 방식이 아닌,
    //  데이터 그대로 화면에 내려주는 방식이다. 이를 'API' 방식이라고 한다.

    @GetMapping("hello-api")
    @ResponseBody  // 이거 사용해서 뷰 리졸버를 사용하지않게되는데, 반환값이 문자가 아닌 객체이므로, JSON 방식으로 변환되어 출력시킴.
    public Hello helloApi(@RequestParam("name") String name) {  // 'Hello클래스로 만든 객체 자료형'을 반환하는 메소드 안에
        Hello hello = new Hello();  // 클래스Hello 새로생성할객체hello = new 클래스생성자Hello()
        hello.setName(name);
        return hello;  // 방금 Hello클래스로 만든 hello객체를 반환함.
    }
    static class Hello {  // static은 정적이란 뜻으로, 선언하게 되는 경우 자바가 컴파일 되는 시점에 정의가 된다. 그래서 위의
        private String name;

        public String getName() {  // private로 지정한 name 필드에 접근하기위해 getter setter 이용함.
            return name;  // 꺼낼때
        }
        public void setName(String name) {  // private로 지정한 name 필드에 접근하기위해 getter setter 이용함.
            this.name = name;  // 넣을때
        }
    }
    // 이거 실행하면 /hello-api?name=hihihi 링크의 화면에 {"name":"hihihi"} 이라고 뜬다.
    // 이는 객체가 반환되어 전달되며, 키-값 쌍으로 이루어진 데이터 오브젝트를 전달하는 JSON 방식으로 변환되어 출력된 것이다.
    // 이러한 방식처럼 데이터를 직접 넘겨주는 방식이 'API' 방식이다. 위는 응용하여 자주 사용되는 'API 방식이자 JSON 방식'의 예시이다.
}
