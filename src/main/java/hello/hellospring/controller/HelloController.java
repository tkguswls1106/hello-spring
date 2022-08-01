package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")  // 웹어플리케이션(웹앱)에서 '/hello'라고 들어오면 밑의 메소드를 호출해준다.
                          // 참고로 GetMapping의 Get은 method="get"방식 의 뜻이다.
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";  // (ViewName)+.html 이므로 hello.html 파일인 View 파일을 가리키게되고,
                         // 위의 data:hello!! 값을 templates_hello.html 파일로 렌더링함.
    }
}
