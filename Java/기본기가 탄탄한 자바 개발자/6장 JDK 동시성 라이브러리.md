# JDK 동시성 라이브러리

--------

## 최신 동시 애플리케이션을 위한 빌딩 블록

- 자바 5에서 멀티스레드 코드 작업을 위한 java.util.concurrent 라이브러리가 나옴

------

## 아토믹 클래스

- java.util.concurrent.atomic 패키지에는 Atomic으로 시작하는 여러 클래스가 있음
  - AtomicBoolean
  - AtomicInteger
  - AtomicLong
  - AtomicReference 등
- 동시성 기본 타입(concurrency primitive)의 예
- 아토믹 클래스들은 이름이 비슷한 자료형의 클래스로부터 상속되지 않으므로 AtomicBoolean은 Boolean 대신 사용할 수 없음
- 아토믹의 핵심은 스레드에서 안전한 가변 변수를 제공하는 것
- 아토믹 클래스는 락 없는 액세스를 지원하므로 아토믹 클래스들은 volatile 필드와 유사한 방식으로 동작함
  - voltaile로는 불가능한 기능을 제공하기 위해 더욱 포괄적인 클래스 API로 래핑돼 있음

```java
private static AtomicInteger nextAccountId = new AtomicInteger(1);

private final int accoutId;
private double balance;

public Account(int openingBalance) {
    this.accoutId = nextAccountId.getAndIncrement();
    balance = openingBalance;
}
```

- 각 객체가 생성될 때마다 정적 인스턴스인 AtomicInteger에서 getAndIncrement()를 호출하면 int 값을 반환하고 변경 가능한 변수를
원자 단위로 증가시킴
  - 데이터베이스 시퀀스 번호처럼 유일해짐

```java
public class TaskManager implements Runnable { 
    private final AtomicBoolean shutdown = new AtomicBoolean(false);
    
    public void shutdown() {
        shuwdown.set(true);
    }
    
    @Override
    public void run() {
        while (!shutdown.get()) {
          ...
        }
    }
}
```

--------

## 잠금 클래스

- 동기화에 대한 블록 구조 접근 방식은 잠금에 대한 간단한 개념을 기반으로 하기에 여러 가지 단점이 있음
  - 한 가지 유형의 잠금만 존재
  - 잠금은 잠금된 객체에 대한 모든 동기화된 작업에 동등하게 적용됨
  - 잠금은 동기화된 블록이나 메서드의 시작에서 획득함
  - 잠금은 블록이나 메서드의 끝에서 해제됨
  - 잠금이 획득되거나 스레드가 무기한으로 차단됨
- 여러 가지 개선을 위해 변경할 여지가 있음
  - 다양한 유형의 잠금(reader/writer)을 추가
  - 잠금을 블록에 제한하지 않고(한 메서드에서 잠금 획득 및 다른 메서드에서 잠금 해제) 허용
  - 스레드가 잠금을 획득할 수 없는 경우(이미 선점되어 있다면) 스레드가 작업을 중단 또는 취소하거나 진행하거나 다른 작업을 수행할 수 있도록
  tryLock() 허용
  - 스레드가 잠금을 시도하고 일정 시간이 지난 후 포기할 수 있도록 허용
- 모든 가능성을 실현하기 위한 핵심은 java.util.concurrent.locks 패키지의 Lock 인터페이스
- Lock 인터페이스의 구현과 함께 제공
  - ReentrantLock: 자바 동기화 블록에서 사용되는 잠금과 거의 동일하지만 더 유연함
  - ReentrantReadWriteLock: 많은 리더와 적은 작성자가 있는 경우 성능을 개선할 수 있음
- 