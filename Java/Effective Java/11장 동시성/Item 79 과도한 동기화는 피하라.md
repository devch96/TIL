# 과도한 동기화는 피하라

- 과도한 동기화는 성능을 떨어뜨리고, 교착상태에 빠드리고, 심지어 예측할 수 없는 동작을 낳기도 한다.

### 응답 불가와 안전 실패를 피하려면 동기화 메서드나 동기화 블록 안에서는 제어를 절대로 클라이언트에 양도하면 안 된다.

- 동기화된 영역 안에서는 재정의할 수 있는 메서드는 호출하면 안 되며, 클라이언트가 넘겨준 함수 객체를 호출해서도 안 된다.
- 외계인 메서드(alien method)가 하는 일에 따라 동기화된 영역은 예외를 일으키거나, 교착상태에 빠지거나, 데이터를 훼손할 수도 있다.
- 이러한 문제는 대부분 외계인 메서드 호출을 동기화 블록 바깥으로 옮기면 된다.

### 외계인 메서드 호출을 동기화 블록 바깥으로 옮기는 더 나은 방법

- 동시성 컬렉션 라이브러리 등 동시성을 위한 라이브러리를 사용하면 된다.

### 기본 규칙은 동기화 영역에서는 가능한 한 일을 적게 하는 것이다.

- 락을 얻고, 공유 데이터를 검사하고, 필요하면 수정하고, 락을 놓는다.
- 오래 걸리는 작업이라면 동기화 영역 바깥으로 옮기는 방법을 생각해야 한다.

### 동기화 성능

- 자바의 동기화 비용은 빠르게 낮아져 왔지만, 과도한 동기화를 피하는 일은 오히려 과거 어느 때보다 중요하다.
- 멀티코어가 일반화된 오늘날 과도한 동기화가 초래하는 진짜 비용은 락을 얻는 데 드는 CPU 시간이 아니라 경쟁하느라 낭비하는 시간이다.
- 병렬로 실행할 기회를 잃고, 모든 코어가 메모리를 일관되게 보기 위한 지연시간이 진짜 비용이다.

### 가변 클래스를 작성하려거든 다음 두 선택지 중 하나를 따르자.

1. 동기화를 전혀 하지 말고, 그 클래스를 동시에 사용해야 하는 클래스가 외부에서 알아서 동기화하게 하자.
2. 동기화를 내부에서 수행해 스레드 안전한 클래스로 만들자.
    - 클라이언트가 외부에서 객체 전체에 락을 거는 것보다 동시성을 월등히 개선할 수 있을 때만
- java.util은 (Vector와 HashTable을 제외하고) 첫 번째 방식을 취했고, java.util.concurrent는 두 번째 방식을 취했다.
- 선택하기 어렵다면 동기화하지 말고 대신 문서에 스레드 안전하지 않다라고 명기하자.

### 여러 스레드가 호출할 가능성이 있는 메서드가 정적 필드를 수정한다면 그 필드를 사용하기 전에 반드시 동기해야 한다.

## 핵심 정리

- 교착상태와 데이터 훼손을 피하려면 동기화 영역 안에서 외계인 메서드를 절대 호출하지 말자.
- 동기화 영역 안에서의 작업은 최소한으로 줄이자.
- 합당한 이유가 있을 때만 내부에서 동기화하고, 동기화했는지 여부를 문서에 명확히 밝히자.