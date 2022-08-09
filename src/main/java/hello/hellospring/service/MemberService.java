package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository = new MemoryMemberRepository(); // 좌항 우항 다른거니까 이름 비슷하다고 헷갈리지말자!

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
