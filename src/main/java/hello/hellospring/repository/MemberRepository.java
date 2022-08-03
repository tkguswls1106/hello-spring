package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

// 아직 데이터 저장소가 선정되지 않아서, 우선 인터페이스로 구현 클래스를 변경할 수 있도록 설계.
// 데이터 저장소는 RDB, NoSQL 등등 다양한 저장소를 고민중인 상황으로 가정.

// 회원 객체 저장할 리포지토리 '인터페이스' // 헷갈리지 말자. 리포지토리에 넣고 빼는 등등의 객체 관리 기능을 구현하기위해 설계한 인터페이스이지, 이게 리포지토리가 아니다.
public interface MemberRepository {  // 여러 기능들을 인터페이스에 담을것이다.
    Member save(Member member);  // 반환자료형: Member클래스로 만든 객체자료형, 메소드명: save, 메소드 매개변수: Member클래스와 그 객체
                                 // 회원정보를 객체형으로 저장하는 기능임. 회원을 저장하면 저장된 회원 객체가 반환됨. 참고로 메소드 형태임.
    Optional<Member> findById(Long id);  // save메소드로 저장한 회원정보들중 id로 회원 도메인 객체를 찾는 기능임. 참고로 메소드 형태임.
                                         // findById() 메소드는 매개변수에 해당하는 값을 토대로 DB에서 값을 조회해오는 역할을 수행하며, 반환형은 Optional<T> 인 메소드이다.
                                         // findByid() 메소드는 조회하려는 값이 존재할 수도, 존재하지 않을 수도 있어서 null에 의한 오류를 최소화 하기 위해 리턴으로 Optional<T>를 받는다.
                                         // Optional<T> 클래스를 사용하면 반환된 값이 null 값인경우 나타나는 NPE(NullPointerException)를 방지해줄 수 있다.
    Optional<Member> findByName(String name);  // save메소드로 저장한 회원정보들중 name으로 회원 도메인 객체를 찾는 기능임. 참고로 메소드 형태임.
    List<Member> findAll();  // 지금까지 save메소드로 저장된 모든 회원들의 리스트들을 반환해준다. 이게 기능이다.
}
