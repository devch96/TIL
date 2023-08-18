# 2장 JPA 시작

------------

## JPA 매핑 어노테이션

- @Entity: 이 클래스를 테이블과 매핑한다고 JPA에게 알려준다. @Entity가 사용된 클래스를 엔티티 클래스라 한다.
- @Table: 엔티티 클래스에 매핑할 테이블 정보를 알려준다. 이 어노테이션을 생략하면 클래스 이름을 엔티티 이름으로 매핑한다.
- @Id: 엔티티 클래스의 필드를 테이블의 기본 키(Primary key)에 매핑한다. @Id가 사용된 필드를 식별자 필드라 한다.
- @Column: 필드를 컬럼에 매핑한다.
- 매핑 정보가 없는 필드: 매핑 어노테이션을 생략하면 필드명을 사용해서 컬럼명으로 매핑한다.
데이터베이스가 대소문자를 구문하면 @Column(name="~")처럼 명시적으로 매핑해야 한다.

## JPA 표준 속성

- javax.persistence.jdbc.driver: jdbc 드라이버
- javax.persistence.jdbc.user: 데이터베이스 접속 아이디
- javax.persistence.jdbc.password: 데이터베이스 접속 비밀번호
- javax.persistence.jdbc.url: 데이터베이스 접속 URL

## 하이버네이트 속성

- hibernate.dialect: 데이터베이스 방언 설정

## 데이터베이스 방언

- JPA는 특정 데이터베이스에 종속적이지 않은 기술.
- SQL 표준을 지키지 않거나 특정 데이터베이스만의 고유한 기능을 JPA에서는 방언(Dialect)라 한다.
- H2: org.hibernate.dialect.H2Dialect
- 오라클 10g: org.hibernate.dialect.Oracle10gDialect
- MySQL: org.hibernate.dialect.MySQL5InnoDBDialect

## 애플리케이션 개발

```java
pulbic class JpaMain {
    
    public static void main(String[] args) {
    // [엔티티 매니저 팩토리] - 생성
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    // [엔티티 매니저] - 생성
    EntityManger em = emf.createEntityManager();
    // [트랜잭션] - 획득
    EntityTransaction tx = em.getTransaction();

    try {
        tx.begin(); // [트랜잭션] - 시작
        logic(em) // 비즈니스 로직 실행
        tx.commit(); // [트랜잭션] - 커밋
    } catch (Exception e) {
        tx.rollback(); // [트랜잭션] - 롤백
    } finally {
        em.close(); // [엔티티 매니저 종료]
    }
    emf.close(); // [엔티티 매니저 팩토리 종료]-
    }
    
    //비지스 로직
    private static void logic (EntityManager em) {...}
}

```
* 엔티티 매니저 설정
* 트랜잭션 관리
* 비즈니스 로직

## 엔티티 매니저 설정
```java
Persistence      2. 생성 ->        EnityManagerFactory

1. 설정 정보                            3. 생성  
    
META-INF/                           EntityManager...
persistence.xml                     EntityManager...
```


### 엔티티 매니저 팩토리 생성
```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
```
* JPA를 시작하려면 우선 persistence.xml의 설정 정보를 사용해서 엔티티 매니저 팩토리를 생성 해야한다.
* 이때 persistence.xml의 설정정보를 읽어서 JPA를 동작시키기 위한 기반 객체를 만들고 JPA 구현체에 따라서 데이터베이스 커넥션 풀도 생성하므로 엔티티 매니저 팩토리르 생성하는 비용은 아주 크다.
* **엔티티 매니저 팩토리는 애플리케이션 전체에서 딱 한번만 생성하고 공유해서 사용 해야한다.**

### 엔티티 메니저 생성
```java
EntityManger em = emf.createEntityManager();
```
* 엔티티 매니저 팩토리에서 엔티티 매니저를 생성한다. JPA의 기능 대부분은 이 엔티티 매니저가 제공한다. 대표적으로 **엔티티 매니저를 사용해서 엔티티를 데이터베이스에 등록/수정/삭제/조회 할 수 있다.**
* 엔티티 매니저는 내부에 데이터소스를(데이터베이스 커넥션)를 유지하면서 데이터베이스와 통신한다.
* 따라서 애플리케이션 개발자는 엔티티 매니저를 가상의 데이터베이스로 생각할 수 있다.
* **엔티티 매니저는 데이터베이스 커넥션과 밀접한 관계가 있음으로 스레드 간에 공유하거나 재사용하면 안된다.**

### 종료
* 마지막으로 사용이 끝난 엔티티 매니저는 다음처럼 반드시 종료 되어야 한다.
```java
em.close(); // [엔티티 매니저 종료]
```
* 애플리케이션 종료할 때 엔티티 매니저 팩토리도 다음처럼 종료 해야한다.

```java
emf.close(); // [엔티티 매니저 팩토리 종료]-
```

## 트랜잭션 관리
```java
EntityTransaction tx = em.getTransaction();
try {
    tx.begin(); // 트랜잭션 시작
    logic(em); // 비즈니스 로직 실행
    tx.commit(); // 트랜잭션 커밋
}catch (Exception e) {
    tx.rollback(); // 예외발생시 트랜잭션 롤백
}
```
* **JPA를 사용하면 항상 트랜잭션 안에서 데이터를 변경해야 한다.**
* 트랜잭션 없이 데이터 베이스를 변경하면 예외가 발생한다. **트랜잭션을 시작하려면 엔티티 메니저 에서 트랜잭션 API를 받아와야 한다.**
* 트랜잭션 API를 사용해서 비즈니스 로직이 정상 동작하면 트랜잭셔을 커밋하고 예외가 발생하면 롤백 한다.

## JPQL

- JPA는 앤티티 객체를 중심으로 개발하므로 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색해야 한다.
- 그러나 엔티티 객체를 대상으로 검색하려면 데이터베이스의 모든 데이터를 어플리케이션으로 불러와 엔티티 객체로 변경한 다음
검색해야 하는데, 이는 사실상 불가능하다.
- 결국 검색 조건이 포함된 SQL을 사용해야 하는데 JPA는 JPQL(Java Persistence Query Language)라는 쿼리 언어로 문제를 해결한다.
  - JPQL은 엔티티 객체로 대상을 쿼리. 클래스와 필드를 대상으로 쿼리한다.
  - SQL은 데이터베이스 테이블을 대상으로 쿼리한다.
- select m from Member m이 바로 JPQL. Member은 Member 객체이지 MEMBER 테이블이 아니다. JPQL은 테이블을 모른다.