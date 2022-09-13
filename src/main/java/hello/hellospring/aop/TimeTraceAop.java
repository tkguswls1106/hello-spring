package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component  // 이처럼 이걸로 컴포넌트 스캔 방법으로 스프링 빈에 등록하여 사용해도 되긴하지만,
            // 이 클래스는 좀 특별하다고 생각되기에 main_hellospring_SpringConfig 에 따로 직접 스프링 빈 등록 방법을 더 선호한다.
            // 일단 여기서는 컴포넌트 스캔 방법을 사용하겠다.
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))")  // 원하는 곳(해당 경로 범위)에 공통 관심 사항 (시간측정로직 TimeTraceAop) 적용
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());  // joinPoint.toString() 이걸로 어떤 메서드를 콜하는지 이름을 알수있게 해준다.
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");  // joinPoint.toString() 이걸로 어떤 메서드를 콜하는지 이름을 알수있게 해준다.
        }
    }
}
