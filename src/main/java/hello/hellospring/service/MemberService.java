package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    // private final MemberRepository memberRepository = new MemoryMemberRepository(); // 좌항 우항 다른거니까 이름 비슷하다고 헷갈리지말자!
    private final MemberRepository memberRepository;  // private final로 선언한다면 직접적으로 값을 참조할 수는 없지만 생성자를 통해 매개변수로 넣어 값을 참조할 수 있다. 각각 생성자 메소드 매개변수 별로 호출할 때마다 새로이 값이 할당(인스턴스화)된다.
                                                      // 반면에 private static final을 선언한 변수를 사용하면 재할당하지 못하며, 메모리에 한 번 올라가면 같은 값을 클래스 내부의 전체 필드, 메서드에서 공유한다.
                                                      // 쉽게 설명하자면, 예를들어 조금씩 다르게 생긴 여러 생성자 메소드들을 생성하였을때, private final 은 각각 메소드별로 별개의 값으로 사용이 가능하지만, private static final 은 통합적인 같은값으로 관리된다는 것이다.
    public MemberService(MemberRepository memberRepository) {  // MemberRepository 매개변수를 가진 생성자 MemberService 메소드를 적어주어, MemberService를 MemberRepository에 연결하여 의존관계를 형성하였다. (DI)
                                                               // 이처럼 객체 의존관계를 외부에서 넣어주는 것을 DI (Dependency Injection), 의존성 주입이라 한다.
        this.memberRepository = memberRepository;
    }  // 이 코드 대신 위의 주석처리된 코드로 쓰면 각 테스트마다 DB저장소가 개별의 것으로 점점 늘어나니까, 하나의 DB 저장소 사용으로 변경해주기위해 이 코드로 대신 작성해준다.
    // 이처럼 외부에서 저장소를 넣어주는것도 DI 이다.

    public Long join(Member member) {  // 회원가입 기능중, 저장기능
        // Optional<Member> result = memberRepository.findByName(member.getName());
        // result.ifPresent(m -> {  // isPresent() 메소드를 사용하여 Optional 객체에 저장된 값이 null인지 아닌지를 먼저 확인한다. 즉, 값이 있다면 해당 로직을 실행한다.
        // throw new IllegalStateException("이미 존재하는 회원입니다.");
        // });
        // 위의 코드 4줄을 줄여서 압축하면 밑의 메소드가 나온다.
        validateDuplicateMember(member);  // 같은 이름의 중복 회원 검증  // 참고로 동일패키지 동일클래스라서 private인 메소드도 사용 가능하다.

        memberRepository.save(member);
        return member.getId();  // 회원가입을 하면, id값을 반환해준다.
    }
    private void validateDuplicateMember(Member member) {  // 회원가입 기능중, 같은 이름의 중복 회원 검증 기능
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {  // isPresent() 메소드를 사용하여 Optional 객체에 저장된 값이 null인지 아닌지를 먼저 확인한다. 즉, 값이 있다면 해당 로직을 실행한다.
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<Member> findMembers() {  // 전체 회원 조회 기능
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
