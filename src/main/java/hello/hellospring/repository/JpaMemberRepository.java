package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {  // JPA는 EntityManager 라는것으로 모든걸 동작한다.
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);  // persist는 영속하다. 영구 저장하다. 라는 뜻이다.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {  // findById 에서 pk id 처럼 단건으로 조회하는기능은 createQuery jpql을 사용할필요 없지만, 다른 findByName나 findAll 기능은 jpql을 작성하여 사용해야한다.
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)  // 엔티티 m의 name 필드값에, m.name = ?로 ?값을 조회하고싶은데, 이 ?값을 name이라는 내맘대로 이름으로 지정해두고
                .setParameter("name", name)  // 왼쪽 "name"은 createQuery의 :name 부분이고, 오른쪽 name은 찾고자하는 이름인 findByName 메소드의 매개변수인 name 값이다.
                // setParameter로 데이터를 동적으로 바인딩시켜, 위의 :name부분을 찾고자하는 이름의 name값으로 치환시켜, 조회하는 기능의 jpql 쿼리를 날리겠다는 뜻이다.
                .getResultList();
        return result.stream().findAny();  // 필터링된 그중에서 가장 먼저 탐색된 요소를 반환함.
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();  // createQuery는 JPQL이라는 객체지향 쿼리언어 이다.
                                                                                                // 보통 DB테이블을 대상으로 sql쿼리를 날리지만, jpql은 엔티티가 된 객체를 대상으로 쿼리를 날린다.
                                                                                                // 그러면 나중에 알아서 sql로 번역이 된다.
                                                                                                // 해당 코드를 해석해보자면, Member 엔티티를 조회하는데 그건 m으로 정히겠다는 뜻이다.
    }
}
