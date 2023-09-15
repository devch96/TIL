# 공유 중인 가변 데이터는 동기화해 사용하라

- synchronized 키워드는 해당 메서드나 블록을 한번에 한 스레드씩 수행하도록 보장한다.

### 동기화

- 동기화를 베타적 실행, 즉 한 스레드가 변경하는 중이라서 상태가 일관되지 않은 순간의 객체를 다른 스레드가 보지 못하게 막는 용도로만 생각한다.
- 맞는 말이지만 중요한 기능이 하나 더 있다.
- 동기화 없이는 한 스레드가 만든 변화를 다른 스레드에서 확인하지 못할 수 있다.
- 동기화는 일관성이 깨진 상태를 볼 수 없게 하는 것은 물론, 동기화된 메서드나 블록에 들어간 스레드가 같은 락의 보호하에 수행된 모든 이전 수정의 최종 결과를
보게 해준다.
- 언어 명세상 long과 double 외의 변수를 읽고 쓰는 동작은 원자적이다.
- 하지만 자바 언어 명세는 스레드가 필드를 읽을 때 항상 `수정이 완전히 반영된`값을 얻는다고 보장하지만
한 스레드가 저장한 값이 다른 스레드에게 `보이는가`는 보장하지 않는다.

### 동기화는 배타적 실행뿐 아니라 스레드 사이의 안정적인 통신에 꼭 필요하다.

- 한 스레드가 만든 변화가 다른 스레드에게 언제 어떻게 보이는지를 규정한 자바의 메모리 모델 때문이다.
- 공유 중인 가변 데이터를 비록 원자적으로 읽고 쓸 수 있을지라도 동기화에 실패하면 처참한 결과로 이어질 수 있다.

### Thread.stop은 사용하지 말자

- Thread.stop(Throwable obj)메서드는 자바11에서 제거되었다.
- 아무 매개변수도 받지 않는 Thread.stop()은 존재한다.

### 스레드를 멈추는 방법

```java
public class StopThread {
    private static boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested) {
                i++;
            }
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested=true;
    }
}
```

- 위 코드는 1초 후에 종료될 것 같아 보이지만 backgroundThread에서의 stopRequested는 계속 false 값을 갖고 있기 때문에
영원히 수행된다.
- 따라서 stopRequested 필드를 동기화해서 멈춰야 한다.

```java
public class StopThread {
    private static boolean stopRequested;
    
    private static synchronized void requestStop(){
        stopRequested = true;
    }
    
    private static synchronized boolean stopRequested(){
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested()) {
                i++;
            }
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}
```
- 쓰기 메서드(requestStop())과 읽기 메서드(stopRequested()) 모두를 동기화 했다.
- 쓰기와 읽기 모두가 동기화되지 않으면 동작을 보장하지 않는다.

### volatile

- 필드를 volatile으로 선언하면 동기화를 생략해도 된다.
- volatile 한정자는 베타적 수행과는 상관없지만 항상 가장 최근에 기록된 값을 읽게 됨을 보장한다.

```java
public class StopThread {
    private static volatile boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested) {
                i++;
            }
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested=true;
    }
}
```

- 하지만 volatile은 주의해서 사용해야 한다.
```java
private static volatile int nextSerialNumber = 0;

public static int generateSerialNumber(){
    return nextSerialNumber++;
}
```

- 이 경우 증가 연산자(++)는 nextSerialNumber에서 값을 가져오고(첫 번째 접근), 그 다음 1을 더해 저장한다(두 번째 접근)
- 이 상황에서 첫 번째 접근과 두 번째 접근 사이에 다른 스레드가 들어오면 잘못된 결과를 계산해낸다(안전 실패)
- 이런 경우 메서드에 synchronized를 붙이면 된다.
- synchronized를 메서드에 붙였다면 volatile을 제거해야 한다.

### Thread 문제를 피하는 가장 좋은 방법

- 애초에 가변 데이터를 공유하지 않는 것이다.
- 가변 데이터는 단일 스레드에서만 쓰도록 하자.
- 이 정책을 받아들였다면 그 사실을 문서에 남겨 유지보수 과정에서도 정책이 계속 지켜지도록 하는 게 중요하다.

## 핵심 정리

- 여러 스레드가 가변 데이터를 공유한다면 그 데이터를 읽고 쓰는 동작은 반드시 동기화해야 한다.
- 동기화하지 않으면 한 스레드가 수행한 변경을 다른 스레드가 보지 못할 수 있다.
- 공유되는 가변 데이터를 동기화하는 데 실패하면 응답 불가 상태에 빠지거나 안전 실패로 이어질 수 있는데 이는 디버깅 난이도가 가장 높은 문제에 속한다.
- 간헐적이거나 특정 타이밍에만 발생할 수도 있고, VM에 따라 현상이 달라지기도 한다.
- 배타적 실행은 필요없고 스레드끼리의 통신만 필요하다면 volatile 한정자만으로 동기화할 수 있지만 올바로 사용하기는 까다롭다.


