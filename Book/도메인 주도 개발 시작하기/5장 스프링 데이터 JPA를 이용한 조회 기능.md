# 스프링 데이터 JPA를 이용한 조회 기능

-----------------------

## 시작에 앞서

- CQRS
- CQRS는 명령(Command) 모델과 조회(Query) 모델을 분리하는 패턴이다.
- 명령 모델은 상태를 변경하는 기능을 구현할 때 사용하고 조회 모델은 데이터를 조회하는 기능을 구현할 때 사용한다.
- 엔티티, 애그리거트, 리포지터리 등의 모델은 상태를 변경할 때 사용된다. 즉 도메인 모델은 명령 모델로 주로 사용된다.

---------------------

## 검색을 위한 스펙

- 검색 조건이 고정되어 있고 단순하면 다음과 같이 특정 조건으로 조회하는 기능을 만들면 된다.
```java
public interface OrderDataDao{
    Optional<OrderData> findById(OrderNo id);
    List<OrderData> findByOrderer(String orderId, Date fromDate, Date toDate);
}
```
- 목록 조회와 같은 기능은 다양한 검색 조건을 조합해야 할 때가 있다.
- 필요한 조합마다 find 메서드를 정의할 수도 있지만 좋은 방법은 아니다.
- 이렇게 검색 조건을 다양하게 조합해야 할 때 사용할 수 있는 것이 스펙(Specification)이다.
- 스펙은 애그리거트가 특정 조건을 충족하는지를 검사할 때 사용하는 인터페이스다.
```java
public interface Specification<T> {
    public boolean isSatisfiedBy(T agg);
}
```
- 스펙을 리포지터리에 사용하면 agg는 애그리거트 루트가 되고, 스펙을 DAO에 사용하면 agg는 검색 결과로 리턴할 데이터 객체가 된다.
- 하지만 실제 스펙은 이렇게 구현하지 않는다.

-------------------

## 스프링 데이터 JPA를 이용한 스펙 구현

- 스프링 데이터 JPA는 검색 조건을 표현하기 위한 인터페이스인 Specification을 제공한다.
- 스펙 인터페이스는 함수형 인터페이스이므로 람다식을 이용해서 객체를 생성할 수 있다.

------------------

## 리포지터리/DAO에서 스펙 사용하기

- 스펙을 충족하는 엔티티를 검색하고 싶다면 findAll() 메서드를 사용하면 된다.
- findAll() 메서드는 스펙 인터페이스를 파라미터로 갖는다.

--------------------

## 스펙 조합

- 스프링 데이터 JPA가 제공하는 스펙 인터페이스는 스펙을 조합할 수 있는 두 메서드를 제공한다
  - and
  - or
- 스펙 인터페이스는 not() 메서드도 제공한다. not()은 정적 메서드로 조건을 반대로 적용할 때 사용한다.
- null 가능성이 있는 스펙 객체와 다른 스펙을 조합할 때는 where을 사용해 야 한다.
- where() 메서드는 스펙 인터페이스의 정적 메서드로 null을 전달하면 아무 조건도 생성하지 않는 스펙 객체를 리턴하고 null이 아니면
인자로 받은 스펙 객체를 그대로 리턴한다.

------------------

## 정렬 지정하기

- 스프링 데이터 JPA는 두 가지 방법을 사용해서 정렬을 지정할 수 있다.
  - 메서드 이름에 OrderBy를 사용해서 정렬 기준 지정
  - Sort를 인자로 전달
```java
public interface OrderSummaryDao extends Repository<OrderSummary, String>{
    List<OrderSummary> findByOrdererIdOrderByNumberDesc(String ordererId);
    List<OrderSummary> findByOrdererIdByOrderDateDescNumberAsc(String ordererId);
}
```
- 정렬 기준 프로퍼티가 두 개 이상이면 메서드 이름이 길어지는 단점이 있다.
- 메서드 이름으로 정렬 순서가 정해지기 때문에 상황에 따라 정렬 순서를 변경할 수도 없다. 이럴 때는 Sort 타입을 사용하면 된다.
```java
List<OrderSummary> findByOrdererId(String ordererId, Sort sort);
        
        
Sort sort1 = Sort.by("number").ascending();
Sort sort2 = Sort.by(orderDate).descending();
Sort sort = sort1.and(sort2);

Sort sort = Sort.by("number").ascending().and(Sort.by(orderDate).descending());
```

---------------------

## 페이징 처리하기

- 스프링 데이터 JPA는 페이징 처리를 위해 Pageable 타입을 이용한다.
```java
List<MemberData> findByNameLike(String name, Pageable pageable);
```
- Pageable 타입은 인터페이스로 실제 Pageable 타입 객체는 PageRequest 클래스를 이용해서 생성한다.
```java
PageRequest pageReq = PageRequest.of(1,10);
```
- of() 메서드의 첫 번째 인자는 페이지 번호를, 두 번째 인자는 한 페이지의 개수를 의미한다.
- PageRequest와 Sort를 사용하면 정렬 순서를 지정할 수 있다.
```java
Sort sort = Sort.by("name").descending();
PageRequest pageReq = PageRequest.of(1,2,sort);
```
- Page 타입을 사용하면 데이터 목록뿐만 아니라 조건에 해당하는 전체 개수도 구할 수 있다.
```java
Page<MemberData> findByBlocked(boolean blocked, Pageable pageable);
```
- 리턴 타입이 Page일 경우 목록 조회 쿼리와 함께 COUNT 쿼리도 실행해서 조건에 해당하는 데이터 개수를 구한다.


