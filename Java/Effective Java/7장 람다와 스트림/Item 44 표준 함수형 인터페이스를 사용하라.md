# 표준 함수형 인터페이스를 사용하라

- 자바가 람다를 지원하면서 API를 작성하는 모범 사례도 크게 바뀌었다.
- 상위 클래스의 기본 메서드를 재정의해 원하는 동작을 구현하는 템플릿 메서드 패턴의 매력이 크게 줄었다.
- 이는 같은 효과의 함수 객체를 받는 정적 팩터리나 생성자를 제공하는 것으로 대체되었다.

### LinkedhashMap의 removeEldestEntry 메서드

```java
protected boolean removeEldestEntry(Map.Entry<K,V> eldest){
    return size() > 100;
}
```
- removeEldestEntry를 재정의하면 캐시로 사용할 수 있다.
- 오늘날 다시 구현한다면 함수 객체를 받는 정적 팩터리나 생성자를 제공했을 것이다.

### 필요한 용도에 맞는 게 있다면, 직접 구현하지 말고 표준 함수형 인터페이스를 활용하라.

| 인터페이스                | 함수 시그니처             | 예                   |
|----------------------|---------------------|---------------------|
| UnarryOperator&lt;T> | T apply(T t)        | String::toLowerCase |
| BinaryOperator&lt;T> | T apply(T t1, T t2) | BigInteger::add     |
| Predicate&lt;T>      | bollean test(T t)   | Collection::isEmpty |
| Function&lt;T,R>     | R apply(T t)        | Arrays::asList      |
| Supplier&lt;T>       | T get()             | Instant::now        |
| Consumer&lt;T>       | void accept(T t)    | System.out::println |

- 기본 인터페이스는 참조 타입용이다.
- Operator는 인수가 1개인 UnaryOperator와 2개인 BinaryOperator로 나뉘며, 반환값과 인수의 타입이 같은 함수를 뜻한다.
- Predicate는 인수 하나를 받아 boolean을 반환하는 함수를 뜻한다.
- Function는 인수와 반환 타입이 다른 함수를 뜻한다.
- Supplier는 인수를 받지 않고 값을 반환(제공)하는 함수를 뜻한다.
- Consumer는 인수를 하나 받고 반환값은 없는(인수를 소비하는)함수를 뜻한다.
- 여러 변형 인터페이스들이 있다.(표준 함수형 인터페이스 총 43개)

### 표준 함수형 인터페이스 대부분은 기본 타입만 지원한다.

- 기본 함수형 인터페이스에 박싱된 기본 타입을 넣어 사용하지 말자.
- 오토박싱이 생겨서 계산량이 많을 때는 성능이 처참히 느려질 수 있다.

### 전용 함수형 인터페이스를 구현해야 할 때

- 자주 쓰이며, 이름 자체가 용도를 명확히 설명해준다.
- 반드시 따라야 하는 규약이 있다.
- 유용한 디폴트 메서드를 제공할 수 있다.

### @FunctionalInterface

- @Override를 사용하는 이유와 비슷하다.
- 해당 클래스의 코드나 설명 문서를 읽을 이에게 그 인터페이스가 람다용으로 설계된 것임을 알려준다.
- 인터페이스가 추상 메서드를 오직 하나만 가지고 있어야 컴파일되게 해준다.
- 유지보수 과정에서 누군가 실수로 메서드를 추가하지 못하게 막아준다.
- 직접 만든 함수형 인터페이스에는 항상 @FunctionalInterface 애너테이션을 사용하자.
