# 복구할 수 있는 상황에는 검사 예외를, 프로그래밍 오류에는 런타임 예외를 사용하라

- 자바는 문제 상황을 알리는 타입(throwable)으로 검사 예외, 런타임 예외, 에러 이렇게 세 가지를 제공한다.

### 호출하는 쪽에서 복구하리라 여겨지는 상황이라면 검사 예외를 사용하라.

- 이것이 검사와 비검사 예외를 구분하는 기본 규칙이다.
- 검사 예외를 던지면 호출자가 그 예외를 catch로 잡아 처리하거나 더 바깥으로 전파하도록 강제하게 된다.
- API 설계자는 API 사용자에게 검사 예외를 던져주어 그 상황에서 회복해내라고 요구한 것이다.
- 사용자는 예외를 잡기만 하고 별다른 조치를 췾하지 않을 수도 있지만 좋지 않은 생각이다.
- 비검사 예외는 런타임 예외와 에러다.
- 이 둘은 프로그램에서 잡을 필요가 없거나 통상적으로는 잡지 말아야 한다.
- 비검사 예외나 에러를 던졌다는 것은 복구가 불가능하거나 더 실행해봐야 득보다는 실이 많다는 뜻이다.

### 프로그래밍 오류를 나타낼 때는 런타임 예외를 사용하자.

- 런타임 예외의 대부분은 전제조건을 만족하지 못했을 때 발생한다.
- 즉 API 명세에 기록된 제약을 지키지 못했다는 뜻이다.
- 이 오류는 복구할 수 있는 상황인지 프로그래밍 오류인지가 명확히 구분되지는 않아서 API 설계자의 판단에 달렸다.
- 복구 가능하다고 믿는다면 검사 예외를, 그렇지 않다면 런타임 예외를 사용하자.
- 확신하기 어렵다면 비검사 예외를 선택하는 편도 괜찮다.

### 에러

- JVM이 자원 부족, 불변식 깨짐 등 더 이상 수행을 계속할 수 없는 상황을 나타낼 때 사용한다.
- 따라서 Error 클래스를 상속해 하위 클래스를 만드는 일은 자제해야 한다.
- 구현하는 비검사 throwable은 모두 RuntimeException의 하위 클래스여야 한다.
- Error는 상속하지 말아야 할 뿐만 아니라 throw 문으로 직접 던지는 일도 없어야 한다(AssertionError 는 예외)

## 핵심 정리

- 복구할 수 있는 상황이라면 검사 예외를, 프로그래밍 오류라면 비검사 예외를 던지자.
- 확실하지 않다면 비검사 예외를 던지자.
- 검사 예외도 아니고 런타임 예외도 아닌 throwable은 정의하지 말자.
- 검사 예외라면 복구에 필요한 정보를 알려주는 메서드도 제공하자.

