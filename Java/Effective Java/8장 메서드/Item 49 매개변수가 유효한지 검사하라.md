# 매개변수가 유효한지 검사하라

- 메서드와 생성자 대부분은 입력 매개변수의 값이 특정 조건을 만족하기를 바란다.
  - ex) 인덱스는 음수가 아니여야 하며, 객체 참조는 null이어야 하며...
- 이러한 제약은 반드시 문서화해야 하며 메서드 몸체가 시작되기 전에 검사해야 한다.
  - 오류는 가능한 한 빨리 잡아야 한다
- 메서드 몸체가 실행되기 전에 매개변수를 확인한다면 잘못된 값이 넘어왔을 때 즉각적으로 깔끔한 방식으로 예외를 던질 수 있다.

### 매개변수 검사를 제대로 하지 못하면 생기는 문제

1. 메서드가 수행되는 중간에 모호한 예외를 던지며 실패할 수 있다. 
2. 더 나쁜 상황은 메서드가 잘 수행되지만 잘못된 결과를 반환할 때다.
2. 더욱 더 나쁜 상황은 메서드는 문제없이 수행됐지만, 어떤 객체를 이상한 상태로 만들어놓아서 미래의 알 수 없는 시점에 이 메서드와는 관련 없는
오류를 낼 때다.

- 매개변수 검사에 실패하면 실패 원자성을 어기는 결과를 낳을 수 있다.

### public과 protected 메서드는 매개변수 값이 잘못됐을 때 던지는 예외를 문서화해야 한다(@throws 자바독 태그)

- 보통은 IllegalArgumentException, IndexOutOfBoundsException, NullPointerException 중 하나일 것이다.
- 매개변수의 제약을 문서화한다면 그 제약을 어겼을 때 발생하는 예외도 함께 기술해야 한다.
```java
/**
 * (현재 값 mod m) 값을 반환한다. 이 메서드는
 * 항상 음이 아닌 BigInteger를 반환한다는 점에서 remainder 메서드와 다르다.
 * 
 * @param m 계수(양수여야 한다.)
 * @return 현재 값 mod m
 * @throws ArithmeticException m이 0보다 작거나 같으면 발생한다.
 */
public BigInteger mod(BigInteger m){
    if(m.signum() <=0){
        throw new ArithmeticException("계수(m)는 양수여야 한다." +m);
    }
        ...
}
```
- 위 함수는 m이 null일경우 m.signum() 호출 시 NPE를 던지지만 설명 어디에도 없다.
- 그 이유는 이 설명은 BigInteger 클래스 수준에서 기술했기 때문이다.
- 클래스 수준 주석은 그 클래스의 모든 public 메서드에 적용되므로 각 메서드에 일일이 기술하는 것보다 훨씬 깔끔한 방법이다.

### null 검사 자동

- 자바 7에 추가된 java.util.Objects.requireNonNull 메서드는 유연하고 사용하기도 편하니 더이상 null 검사를 수동으로 하지 않아도 된다.
- 원하는 예외 메시지도 지정할 수 있고, 입력을 그대로 반환하므로 값을 사용하는 동시에 null 검사를 수행할 수 있다.
```java
this.strategy = Objects.requireNonNull(strategy,"전략");
```
- 반환값은 그냥 무시하고 필요한 곳 어디서든 순수한 null 검사 목적으로 사용해도 된다.

### 범위 검사

- 자바 9에서는 Objects에 범위 검사 기능도 더해졌다.
  - checkFromIndexSize
  - checkFromToIndex
  - checkIndex
- 예외 메시지를 지정할 수 없고, 리스트와 배열 전용으로 설계됐으며 닫힌 범위(closed range; 양 끝단 값을 포함하는)는 다루지 못한다.
- 이러한 제약이 걸림돌이 되지 않는 상황에서는 아주 유용하고 편리하다.

### private 메서드

- 공개되지 않은 메서드라면 패키지 제작자가 메서드가 호출되는 상황을 통제할 수 있다.
- 따라서 단언문(assert)을 사용해 매개변수 유효성을 검증할 수 있다.
```java
private static void sort(long a[], int offset, int length){
    assert a != null;
    assert offset >= 0 && offset <= a.length;
    assert length >= 0 && length <= a.length - offset;
        ...
}
```

### 단언문과 일반적인 유효성 검사의 다른점

1. 실패하면 AssertionError를 던진다.
2. 런타임에 아무런 효과도, 아무런 성능 저하도 없다.

### 나중에 쓰기 위해 저장하는 매개변수는 특히 더 신경 써서 검사해야 한다.

- List를 받는 생성자의 경우 null 값이 들어올때 검사하면 곧바고 NPE를 던지지만 검사하지 않으면 클라이언트가 돌려받은 List를 사용하려 할 때
비로소 NPE가 발생한다.
- 생성자 매개변수의 유효성 검사는 클래스 불변식을 어기는 객체가 만들어지지 않게 하는 데 꼭 필요하다.

### 메서드 몸체 실행 전에 매개변수 유효성을 검사해야 한다는 규칙에도 예외는 있다.

- 유효성 검사 비용이 지나치게 높거나 실용적이지 않을 때.
- 계산 과정에서 암묵적으로 검사가 수행될 때.
- 예를 들어 Collections.sort(List) 처럼 객체 리스트를 정렬하는 메서드에서 리스트 안의 객체들은 모두 상호 비교될 수 있어야 하며,
정렬 과정에서 비교가 이뤄진다.
- 상호 비교될 수 없는 타입의 객체가 들어있다면 그 객체와 비교할 때 ClassCastException을 던진다.
- 따라서 비교하기 전에 리스트 안에 모든 객체가 상호비교될 수 있는지 검사할 필요는 없다.
- 하지만 암묵적 유효성 검사에 너무 의존했다가는 실패 원자성을 해칠 수 있으니 주의해야 한다.

### 매개변수에 제약을 두는 게 좋다라고 해석하면 안된다.

- 메서드는 최대한 범용적으로 설계해야 한다.
- 메서드가 건네 받은 값으로 무언가 제대로 된 일을 할 수 있다면 매개변수 제약은 적을수록 좋다.

## 핵심 정리

- 메서드나 생성자를 작성할 때면 그 매개변수들에 어떤 제약이 있을지 생각해야 한다.
- 그 제약들을 문서화하고 메서드 코드 시작 부분에서 명시적으로 검사해야 한다.
