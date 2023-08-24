# 12장 스프링 데이터 JPA

- 데이터 접근 계층(Data Access Layer)은 CRUD로 부르는 코드를 반복해서 개발해야 한다.
- JPA를 사용해도 마찬가지다.

-------------------

## 스프링 데이터 JPA 소개

- 스프링 데이터 JPA는 스프링 프레임워크에서 JPA를 편리하게 사용할 수 있도록 지원하는 프로젝트.
- CRUD를 처리하기 위한 공통 인터페이스 제공.
- 리포지토리를 개발할 때 인터페이스만 작성하면 실행 시점에 스프링 데이터 JPA가 구현 객체를 동적으로 생성해서 주입해준다.
- 데이터 접근 계층을 개발할 때 구현 클래스 없이 인터페이스만 작성해도 개발을 완료할 수 있다.
```java
public interface MemberRepository extends JpaRepository<Member, Long>{
    Member findByUsername(String username);
}
public interface ItemRepository extends JpaRepository<Item, Long>{
}
```
- findByUsername 같이 직접 작성한것은 스프링 데이터 JPA 가 메소드 이름을 분석해서 JPQL을 실행한다.

-----------------

## 공통 인터페이스 기능

- 스프링 데이터 JPA는 간단한 CRUD 기능을 공통으로 처리하는 JpaRepository 인터페이스를 제공한다.
- 제네릭에 엔티티 클래스와 엔티티 클래스가 사용하는 식별자 타입을 지정하면 된다.
- 주요 메소드(T는 엔티티, ID는 엔티티의 식별자 타입, S는 엔티티와 그 자식 타입)
  - savs(S): 새로운 엔티티를 저장하고 이미 있는 엔티티는 수정한다.
  - delete(T): 엔티티 하나를 삭제한다. 내부에서 EntityManager.remove()를 호출한다.
  - findOne(ID): 엔티티 하나를 조회한다. 내부에서 EntityManager.find()를 호출한다.
  - getOne(ID): 엔티티를 프록시로  조회한다. 내부에서 EntityManager.getReference()를 호출한다.
  - findAll(...): 모든 엔티티를 조회한다. 정렬이나 페이징 조건을 파라미터로 제공할 수 있다.
- save(S)에 식별자 값이 없으면 새로운 엔티티로 판단해서 EntityManager.persist()를 호출하고 식별자 값이 있으면 EntityManager.merge()를 호출한다.

--------------------

## 쿼리 메소드 기능

- 메소드 이름으로 쿼리 생성
- 메소드 이름으로 JPA NamedQuery 호출
- @Query 어노테이션을 사용해서 리포지토리 인터페이스에 쿼리 직접 정의

### 메소드 이름으로 쿼리 생성

- 정해진 규칙에 따라서 메소드 이름을 지어야 한다.
- 엔티티의 필드명이 변경되면 인터페이스에 정의한 메소드 이름도 꼭 함께 변경해야 한다.

### JPA NamedQuery

- 스프링 데이터 JPA는 선언한 "도메인 클래스 + .(점) + 메소드 이름" 으로 Named 쿼리를 찾아서 실행한다.
- 실행할 Named 쿼리가 없으면 메소드 이름으로 쿼리 생성 전략을 사용한다.

### @Query, 리포지토리 메소드에 쿼리 정의

- 리포지토리 메소드에 직접 쿼리를 정의하려면 @org.springframework.data.jpa.repository.Query 어노테이션을 사용한다.
- 이름 없는 Named 쿼리라 할 수 있다.
- 어플리케이션 실행 시점에 문법 오류를 발견할 수 있는 장점이 있다.
- 네이티브 SQL 을 사용하려면 @Query 어노테이션에 nativeQuery = true를 설정한다.

### 파라미터 바인딩

- 스프링 데이터 JPA는 위치 기반 파라미터 바인딩과 이름 기반 파라미터 바인딩을 모두 지원한다.
- 기본값은 위치 기반인데 파라미터 순서로 바인딩한다.
- 이름 기반 파라미터 바인딩을 사용하려면 @org.springframework.data.repository.query.Param(파라미터 이름) 어노테이션을 사용하면 된다.
- 가독성과 유지보수를 위해 이름 기반 파라미터 바인딩을 사용하는 것이 좋다.

### 벌크성 수정 쿼리

- 스프링 데이터 JPA에서 벌크성 수정, 삭제 쿼리는 @org.springframework.data.jpa.repository.Modifying 어노테이션을 사용하면 된다.
- 벌크성 쿼리르 실행하고 나서 영속성 컨텍스트를 초기화하고 싶으면 @Modifying(clearAutomatically = true)로 하면 된다. 기본값은 false다.

### 반환 타입

- 스프링 데이터 JPA는 유연한 반환 타입을 지원한다.
- 결과가 한 건 이상이면 컬렉션 인터페이스를 사용하고, 단건이면 반환 타입을 지정한다.

### 페이징과 정렬

- 스프링 데이터 JPA는 쿼리 메소드에 페이징과 정렬 기능을 사용할 수 있도록 2가지 특별한 파라미터를 제공한다.
  - org.springframework.data.domain.Sort: 정렬 기능
  - org.springframework.data.domain.Pageable: 페이징 기능(내부에 Sort 포함)
- Pageable을 파라미터에 사용하면 반환 타입으로 List나 org.springframework.data.domain.Page를 사용할 수 있다.
- Page를 사용하면 페이징 기능을 제공하기 위해 전체 데이터 건수를 조회하는 count 쿼리를 추가로 호출한다.
- 예제 코드
  - 검색 조건: 이름이 김으로 시작하는 회원
  - 정렬 조건: 이름으로 내림차순
  - 페이징 조건: 첫 번째 페이지, 페이지당 보여줄 데이터는 10건

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findByNameStartWith(String name, Pageable pageable);
}

PageRequest pageRequest = new PageRequest(0,10,new Sort(Direction.DESC, "name"));
Page<Member> result = memberRepository.findByNameStartWith("김",pageRequest);
List<Member> members = result.getContent(); //조회된 데이터
int totalPages = result.getTotalPages();
boolean hasNextPage = result.hasNextPage();
```

- Pageable은 인터페이스이기 때문에 실제 사용할 때는 org.springframework.data.domain.PageRequest 객체를 사용한다.
- PageRequest 생성자의 첫 번째 파라미터에는 현재 페이지를, 두 번째 파라미터에는 조회할 데이터 수를 입력한다. 추가로 정렬 정보도 파라미터로 사용할 수 있다.
- 페이지는 0부터 시작한다.

### 힌트

- JPA 쿼리 힌트를 사용하려면 @org.springframework.data.jpa.repository.QueryHints 어노테이션을 사용하면 된다.
- SQL 힌트가 아니라 JPA 구현체에게 제공하는 힌트다.

### Lock

- 쿼리 시 락을 걸려면 @org.springframework.data.jpa.repository.Lock 어노테이션을 사용하면 된다.

--------------------

## 명세

- 명세를 이해하기 위한 핵심 단어는 술어인데 이것은 단순히 참이나 거짓으로 평가된다.
- 술어는 AND, OR같은 연산자로 조합할 수 있다.
- 데이터를 검색하기 위한 제약 조건 하나하나를 술어라 할 수 있다.
- 스프링 데이터 JPA는 org.springframework.data.jpa.domian.Specification 클래스로 정의했다.
- Specification은 컴포지트 패턴으로 구성되어 있어서 여러 Specification을 조합할 수 있다.
```java
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order>{
}
```
- Specifications는 명세들을 조립할 수 있도록 도와주는 클레스인데 where(), and(), or(), not() 메소드를 제공한다.
- 명세를 정의하려면 Specification 인터페이스를 구현하면 된다.
- 명세를 정의할 때는 toPredicate(...) 메소드만 구현하면 되는데 JPA Criteria의 Root, CriteriaQuery, CriteriaBuilder 클래스가 모두 파라미터로 주어진다.

-------------------

## 사용자 정의 리포지토리 구현

- 스프링 데이터 JPA는 공통 인터페이스가 제공하는 기능까지 모두 구현하지 않고 우회해서 필요한 메소드만 구현할 수 있는 방법을 제공한다.
```java
public interface MemberRepositoryCustom{
    public List<Member> findMemberCustom();
}

public class MemberRepositoryImpl implements MemberRepositoryCustom{
    @Override
    public List<Member> findMemberCustom(){
      ...
    }
}

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{
}
```
1. 사용자 정의 인터페이스를 작성한다.
2. 사용자 정의 인터페이스를 구현한 클래스를 작성한다. 이때 클래스의 이름은 리포지토리 인터페이스 이름 + Impl로 지어야 한다.
3. 리포지토리 인터페이스에서 사용자 정의 인터페이스를 상속받으면 된다.

---------------------

## Web 확장

- 스프링 데이터 프로젝트는 스프링 MVC에서 사용할 수 있는 편리한 기능을 제공한다.
- 식별자로 도메인 클래스를 바로 바인딩해주는 도메인 클래스 컨버터 기능과 페이징과 정렬 기능이다.

### 설정

- Web 확장 기능을 활성화하려면 org.springframework.data.web.config.SpringDataWebCOnfiguration을 스프링 빈으로 등록한다.
- @org.springframework.data.web.config.EnableSPringDataWebSupport 어노테이션을 사용한다.

### 도메인 클래스 컨버터 기능

- 도메인 클래스 컨버터는 HTTP 파라미터로 넘어온 엔티티의 아이디로 엔티티 객체를 찾아서 바인딩해준다.
```java
@Controller
public class MemberController{
    @RequestMapping("member/memberUpdateForm")
    public String memberUpdateForm(@RequestParam("id") Member member, Model model){
        model.addAttribute("member",member);
        return "member/memberSaveForm";
    }
}
```
- 도메인 클래스 컨버터가 memberRepository.findOne(id) 로 찾은 멤버 객체를 바로 전달해 준다.
- 도메인 클래스 컨버터를 통해 넘어온 회원 엔티티를 컨트롤러에서 직접 수정해도 실제 데이터베이스에 반영되지 않는다.
- 이것은 영속성 컨텍스트의 동작 방식과 OSIV에 관한 내용이다.
  - OSIV를 사용하지 않으면 조회한 엔티티는 준영속 상태이다. 따라서 변경 감지기능이 동작하지 않는다. 수정을 반영하고 싶으면 merge를 사용해야 한다.
  - OSIV를 사용하면 조회한 엔티티는 영속 상태다. 하지만 OSIV의 특성상 컨트롤러와 뷰에서는 영속성 컨텍스트를 플러시 하지 않는다. 수정한 내용을 반영하고 싶으면
  트랜잭션을 시작하는 서비스 계층을 호출해야 한다. 해당 서비스 계층이 종료될 때 플러시와 트랜잭션 커밋이 일어나서 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영해 줄 것이다.

### 페이징과 정렬 기능

- 페이징 기능: PageableHandlerMethodArgumentResolver
- 정렬 기능: SortHandlerMethodArgumentResolver
```java
@RequestMapping(value="/members", method = RequestMethod.GET)
public String list(Pageable pageable, Model model){
    Page<Member> page = memberService.findMembers(pageable);
    model.addAttribute("members", page.getContent());
    return "members/membersList";
}
```

#### 접두사

- 사용해야 할 페이징 정보가 둘 이상이면 접두사를 사용해서 구분한다.
- @Qualifier 어노테이션을 사용한다.
- "{접두사명}_"로 구분한다.
```java
public String list(
        @Qualifier("member") Pageable memberPageable,
        @Qualifier("order") Pageable orderPageable, ...)
```

#### 기본값

- Pageable의 기본값은 page=0,size=20이다.
- 기본값을 변경하고 싶으면 @PageableDefault 어노테이션을 사용하면 된다.
```java
@RequestMapping(value="/members", method = RequestMethod.GET)
public String list(@PageableDefault(size = 12, sort = "name", direction = Sor.Direction.DESC) Pageable pageable){
}
```

----------------------------

## 스프링 데이터 JPA가 사용하는 구현체

- 스프링 데이터 JPA가 제공하는 공통 인터페이스는 org.springframework.data.jpa.repository.support.SimpleJpaRepository 클래스가 구현한다.
```java
@Repository
@Transactional(readOnly = true)
public class SimpleJpaRepository<T, ID extends Serializable> implements JpaRepository<T, ID>
        , JpaRepository<T, ID>, JpaSpecificationExecutor<T>{
    
    @Transactional
    public <S extends T> S save(S entity){
        if(entityInformation.isNew(entity)){
            em.persist(entity);
            return entity;
        }else{
            return em.merge(entity);
        }
    }
  ...
}
```
- @Repository 적용: 데이터 엑세스 계층의 구성 요소임을 나타냄. 
데이터 엑세스 객체를 스프링 컨테이너에 등록하고 JPA 예외를 스프링이 추상화한 예외로 변환함.
- @Transactional 트랜잭션 적용: JPA의 모든 변경은 트랜잭션 안에서 이루어져야 한다. 
스프링 데이터 JPA가 제공하는 공통 인터페이스를 사용하면 데이터를 변경(등록, 수정,삭제) 하는 메소드에 @Transactional로 트랜잭션 처리가 되어 있다.
따라서 서비스 계층에서 트랜잭션을 시작하지 않으면 리포지토리에서 트랜잭션을 시작한다. 물론 서비스 계층에서 트랜잭션을 시작했으면 리포지토리도
해당 트랜잭션을 전파받아서 그대로 사용한다.
- @Transactional(readOnly=true): 데이터를 조회하는 메소드에는 readOnly=true 옵션이 적용되어 있다. 데이터를 변경하지 않는 트랜잭션에서 readOnly=true
옵션을 사용하면 플러시를 생략해서 약간의 성능 향상을 얻을 수 있다.
- save()메소드: 엔티티가 새로운 엔티티면 저장(persist)하고 이미 있는 엔티티면 병합(merge)한다.
------------------------------

## 스프링 데이터 JPA와 QueryDSL 통합

- 스프링 데이터 JPA는 2가지 방법으로 QueryDSL을 지원한다.
  - org.springframework.data.querydsl.QueryDslPredicateExecutor
  - org.springframework.data.querydsl.QueryDslRepositorySupport

###  QueryDslPredicateExecutor 사용

- 리포지토리에서 QueryDslPredicateExecutor를 상속받으면 된다.
- QueryDslPredicateExceutor 인터페이스를 보면 QueryDSL을 검색조건으로 사용하면서 
스프링 데이터 JPA가 제공하는 페이징과 정렬 기능도 함께 사용할 수 있다.
- QueryDslPredicateExecutor은 편리하게 사용할 수 있지만 join, fetch와 같은 기능을 사용하지 못한다.

### QueryDslRepositorySupport 사용

- QueryDSL의 모든 기능을 사용하려면 JPAQuery 객체를 직접 생성해서 사용하면 된다.
- 이때 스프링 데이터 JPA가 제공하는 QueryDslRepositorySupport를 상속 받아 사용하면 조금 더 편리하게 QueryDSL을 사용할 수 있다.
- 생성자에서 QueryDslRepositorySupport에 엔티티 클래스 정보를 넘겨주어야 한다.

---------------------------