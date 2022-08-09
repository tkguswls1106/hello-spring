package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

// 개발을 진행하기 위해서 초기 개발 단계에서는 구현체로 가벼운 메모리 기반의 데이터 저장소 사용

// 회원 리포지토리 메모리 구현체
public class MemoryMemberRepository implements MemberRepository {  // 인터페이스를 상속받음

    private static Map<Long, Member> store = new HashMap<>();  // save 메소드로 저장을 할때 저장할 메모리 구현  // 내가 보기엔 c언어 자료구조(데이터구조)의 연결리스트나 구조체와 약간 유사하다고 생각함.
    // Map 컬렉션은 키(key)와 값(value) 쌍(pair) 으로 구성된 Entry 객체를 저장하는 구조를 가진 인터페이스이다.
    // id는 Long 자료형이므로 키로 Long을, 값으로 객체를 주기로하면 Member 자료형이므로 값으로 Member을 줘서, Map<Long, Member> 이다.
    // HashMap은 Map 인터페이스를 구현한 대표적인 Map 컬렉션이다.
    // 대부분 HashMap 객체를 생성할 때에는 매개변수가 없는 생성자를 사용한다. 하지만 HashMap에 담을 데이터의 개수가 많은 경우에는 초기 크기를 지정해주는 것을 권장한다.
    // 동시성 문제가 고려되어 있지 않음. 실무에서는 위처럼 공유되는 변수를 사용할때는 ConcurrentHashMap 사용 고려. 여기선 예제니까 단순하게 고려하지않고 그냥 썼다고함.
    private static long sequence = 0L;  // sequence는 0,1,2 이렇게 키값을 생성해주는 역할이라고 보면 된다고 한다.
    // 동시성 문제가 고려되어 있지 않음. 실무에서는 AtomicLong 사용 고려. 여기선 예제니까 단순하게 고려하지않고 그냥 썼다고함.

    @Override
    public Member save(Member member) {
        member.setId(++sequence);  // member의 id로 ++sequence 를 저장하고,  // 메소드 매개변수 안에 Member member가 적혀있으므로, 따로 new로 생성해주지 않고 바로 사용하면 된다.
        store.put(member.getId(), member);  // 방금 저장한 id를 불러와서 그걸 키로 넣고, 값으로 member을 넣어서, 구조체처럼 store라는 이름의 메모리구현체에 키값쌍정보를 DB처럼 저장함.
        return member;  // 저장한 회원정보 반환.
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));  // store 메모리 구현체에서 정보를 꺼낼것이다.
                                                    // null이 반환될 가능성이 있으므로, Optional.ofNullable()로 감싸준다.
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()  // 아마도 데이터가공시에 사용되는것이 stream인듯 하다. -> 인 화살표로 람다 작성.
                .filter(member -> member.getName().equals(name))  // member.getName()이 findByName(String name)의 매개변수로 넘어온 name과 같은지 확인하는 것이다. 같은경우에만 필터링이 실행된다.
                // (member -> member.getName().equals(name))에서, 첫 member은 데이터들을 모두 돌려보는 어떠한 메소드의 매개변수값이고,
                // 그 메소드의 return 반환결과값이 member.getName().equals(name) 인 것이다.
                // 즉, 하나하나 전부 member 데이터를 매개변수로 넣어 돌려 찾아가면서 findByName(String name)의 매개변수로 넘어온 name과 같은지 확인하는 것이다.
                .findAny();  // 필터링된 그중에서 가장 먼저 탐색된 요소를 반환함.
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
        // ArrayList는 List 컬렉션 인터페이스를 구현한 구현클래스이다.
    }

    public void clearStore() {
        store.clear();
    }
}