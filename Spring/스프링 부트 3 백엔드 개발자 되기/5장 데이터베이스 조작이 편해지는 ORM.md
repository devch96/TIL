# 데이터베이스 조작이 편해지는 ORM

-------------

## ORM이란?

- Object Relational Mapping은 자바의 객체와 데이터베이스를 연결하는 프로그래밍 기법
- ORM 장점
  - SQL을 직접 작성하지 않고 사용하는 언어로 데이터베이스에 접근할 수 있음
  - 객체지향적으로 코드를 작성할 수 있기 때문에 비즈니스 로직에만 집중할 수 있음
  - 데이터베이스 시스템이 추상화되어 있기 때문에 데이터베이스 이식이 편해짐
  - 매핑하는 정보가 명확하기 때문에 ERD에 대한 의존도를 낮출 수 있고 유지보수할 때 유리함
- ORM 단점
  - 프로젝트의 복잡성이 커질수록 사용 난이도가 올라감
  - 복잡하고 무거운 쿼리는 ORM으로 해결이 불가능한 경우가 있음

------------

## JPA와 하이버네이트

- 자바에서는 JPA(Java Persistence API)를 표준 ORM으로 사용함
  - 자바에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스
  - 인터페이스이므로 실제 사용을 위해는 ORM 프레임워크를 추가로 선택해야함
  - 대표적으로는 하이버네이트
- 하이버네이트는 JPA 인터페이스를 구현한 구현체이자 자바용 ORM 프레임워크
  - 내부적으로 JDBC API를 사용함

### 엔티티 매니저란?

#### 엔티티

- 데이터베이스의 테이블과 매핑되는 객체
- 본질적으로 자바 객체이므로 일반 객체와 다르지 않으나 테이블과 직접 연결된다는 아주 특별한 특징이 있어 구분지어 부름

#### 엔티티 매니저

- 엔티티를 관리해 데이터베이스와 애플리케이션 사이에서 객체를 생성, 수정, 삭제하는 등의 역할을 함
- 엔티티 매니저를 만드는 곳이 엔티티 매니저 팩토리
- 스프링 부트는 내부에서 엔티티 매니저 팩토리를 하나만 생성해서 관리하고 @PersistenceContext 또는 @Autowired 애너테이션을 사용해서
엔티티 매니저를 사용함
- 스프링 부트는 기본적으로 빈은 하나만 생성해서 공유하므로 동시성 문제가 발생할 수 있기 때문에 실제로는 엔티티 매니저가 아닌 실제 엔티티 매니저와 연결하는
프록시(가짜) 엔티티 매니저를 사용함. 필요할 때 데이터베이스 트랜잭션과 관련된 실제 엔티티 매니저를 호출함

### 영속성 컨텍스트란?

- 엔티티 매니저는 엔티티를 영속성 컨텍스트에 저장함
- 영속성 컨텍스트는 엔티티를 관리하는 가상의 공간

#### 1차 캐시

- 영속성 컨텍스트는 내부에 1차 캐시를 가지고 있음
- 캐시의 키는 엔티티의 @Id 애너테이션이 달린 기본키 역할을 하는 식별자이며 값은 엔티티
- 엔티티를 조회하면 1차 캐시에서 데이터를 조회하고 값이 있으면 반환
- 값이 없으면 데이터베이스에 조회해 1차 캐시에 저장한 다음 반환
- 캐시된 데이터를 조회할 때에는 데이터베이스를 거치지 않아도 되므로 매우 빠르게 데이터를 조회할 수 있음

#### 쓰기 지연

- 트랜잭션을 커밋하기 전까지는 데이터베이스에 실제로 질의문(쿼리)을 보내지 않고 쿼리를 모았다가 트랜잭션을 커밋하면 모았던 쿼리를 한번에 실행하는 것을 의미
- 적당한 묶음으로 쿼리를 요청할 수 있어 데이터베이스 시스템에 부담을 줄일 수 있음

#### 변경 감지

- 트랜잭션을 커밋하면 1차 캐시에 저장되어 있는 엔티티의 값과 현재 엔티티의 값을 비교해서 변경된 값이 있다면 변경 사항을 감지해 변경된 값을 데이터베이스에
자동으로 반영함

#### 지연 로딩

- 쿼리로 요청한 데이터를 애플리케이션에 바로 로딩하는 것이 아니라 필요할 때 쿼리를 날려 데이터를 조회하는 것을 의미

### 엔티티의 상태

- 영속성 컨텍스트가 관리하고 있지 않은 분리(detached)상태
- 영속성 컨텍스트가 관리하는 관리(managed)상태
- 영속성 컨텍스트와 전혀 관계가 없는 비영속(transient)상태
- 삭제된(removed) 상태

```java
@PersistenceContext
EntityManager em;

public void ex() {
    // 엔티티 매니저가 엔티티를 관리하지 않는 상태(비영속 상태)
    Member member = new Member(1L,"asd");
   
    // 엔티티가 관리 상태로 변함
    em.persist(member);
    
    // 엔티티가 분리된 상태(영속성 컨텍스트에서 관리 안함)
    em.detach(member);
    
    // 엔티티가 삭제된 상태(영속성 컨텍스트와 데이터베이스에서 삭제)
    em.remove(member);
}
```

------------

## 스프링 데이터와 스프링 데이터 JPA

- JPA만 사용하면 엔티티의 상태를 직접 관리하고, 필요한 시점에 커밋을 해야 하는 등의 개발자가 신경 써야 할 부분이 많음
- 스프링 데이터는 비즈니스 로직에 더 집중할 수 있게 데이터베이스 사용 기능을 클래스 레벨에서 추상화함

### 스프링 데이터 JPA란?

- 스프링 데이터의 인터페이스인 PagingAndSortingRepository를 상속받아 JpaRepository 인터페이스를 만들었으며 JPA를 더 편리하게 사용하는 메서드를 제공함
- JpaRepository 인터페이스를 우리가 만든 인터페이스에 상속받고 제네릭에는 관리할 엔티티 이름, 엔티티 기본키의 타입을 입력하면 됨

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

------------

## 핵심 요약

- ORM은 객체와 데이터베이스를 연결하는 프로그래밍 기법
- JPA는 자바에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스
  - 엔티티는 영속성을 가진 객체를 의미
  - 엔티티 매니저는 엔티티를 관리하면서 조회, 삭제, 수정, 생성하는 역할을 함
  - 엔티티 매니저를 만드는 곳이 엔티티 매니저 팩토리
  - 엔티티 매니저는 엔티티를 영속성 컨텍스트에 저장함
  - 영속성 컨텍스트는 1차 캐시, 쓰기 지연, 변경 감지, 지연 로딩의 특징을 가지고 있음
  - 엔티티의 상태는 분리, 관리, 비영속, 삭제 상태로 나뉨
- 하이버네이트는 JPA의 구현체 중 대표적인 구현체로 자바 언어를 위한 ORM 프레임워크
- 스프링 데이터 JPA는 JPA를 쓰기 편하게 만들어 놓은 모듈