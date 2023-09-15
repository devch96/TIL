# wait와 notify 보다는 동시성 유틸리티를 애용하라

- 자바 5에서 도입된 고수준의 동시성 유틸리티가 이전이라면 wait와 notify로 하드코딩해야 했던 전형적인 일들을
대신 처리해준다.

### java.util.concurrent

- 실행자 프레임워크, 동시성 컬렉션, 동기화 장치 이 세 범주로 나눌 수 있다.
- 동시성 컬렉션은 List, Queue, Map 같은 표준 컬렉션 인터페이스에 동시성을 가미해 구현한 고성능 컬렉션이다.
- 높은 동시성에 도달하기 위해 동기화를 각자의 내부에서 수행한다.
- 따라서 동시성 컬렉션의 동시성을 무력화하는 것은 불가능하며, 외부에서 락을 추가로 사용하면 오히려 속도가 느려진다.

### 동시성 컬렉션은 동기화한 컬렉션을 낡은 유산으로 만들어버렸다.

- 대표적인 예로 Collections.synchronizedMap 보다는 ConcurrentHashMap을 사용하는 게 좋다.

### 컬렉션 인터페이스 중 일부는 작업이 성공적으로 완료될 때까지 기다리도록 확장되었다.

- BlockingQueue에 추가된 메서드 중  take는 큐의 첫 원소를 꺼낸다.
- 이때 만약 큐가 비었다면 새로운 원소가 추가될 때까지 기다린다.
- 이러한 특성 덕에 BlockingQueue는 작업 큐(생산자-소비자 큐)로 쓰기에 적합하다.
- ThreadPoolExecutor를 포함한 대부분의 실행자 서비스 구현체에서 BlockingQueue를 사용한다.

### 동기화 장치는 스레드가 다른 스레드를 기라딜 수 있게 하여 서로 작업을 조율할 수 있게 해준다.

- 동기화 장치는 CountDownLatch와 Semaphore다.
- CycleBarrier와 Exchanger는 그보다 덜 쓰인다.
- 가장 강력한 동기화 장치는 Phaser다.

### CountDownLath

- 카운트다운 래치는 일회성 장벽으로 하나 이상의 스레드가 또 다른 하나 이상의 스레드 작업이 끝날 때까지 기다리게 한다.
- CountDownLatch의 유일한 생성자는 int 값을 받으며, 이 값의 래치의 countDown 메서드를 몇 번 호출해야 대기 중인 스레드들을 깨우는 지 결정한다.

### 동시 실행 시간을 재는 간단한 프레임워크

```java
public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedExcpetion{
    CoundDownLatch ready = new CountDownLatch(concurrency);
    CountDownLatch start = new CountDownLatch(1);
    CoundDownLatch done = new CountDownLatch(concurrency);
        ...
}
```

### 시간 간격을 잴 때는 항상 System.currentTimeMillis가 아닌 System.nanoTime을 사용하자.

- System.nanoTime은 더 정확하고 정밀하며 시스템의 실시간 시계의 시간 보정에 영향을 받지 않는다.

### wait 메서드를 사용할 때는 반드시 대기 반복문(wait loop) 관용구를 사용하라.

```java
synchronized (obj){
    while( ...){
        obj.wait();
    }
        ...
}
```

- 반복문 밖에서는 절대로 호출하지 말자.

### notify와 notifyAll 중 무엇을 선택?

- notify는 스레드 하나만 깨우며, notifyAll은 모든 스레드를 깨운다.
- 일반적으로 언제나 notifyAll을 사용하는게 합리적이고 안전한 조언이다.
- 깨어나야 하는 모든 스레드가 깨어나니 항상 정확한 결과를 얻을 것이다.
- 다른 스레드가 깨어난다고 정확성에 영향을 주지는 않을 것이다.
- 깨어난 스레드들은 기다리전 조건이 충족되었는지 확인하여, 충족되지 않았다면 다시 대기할 것이다.

## 핵심 정리

- wait와 notify를 직접 사용하는 것을 동시성 어셈블리 언어로 프로그래밍하는 것과 같다.
- 코드를 새로 작성한다면 wait와 notify를 쓸 이유가 전혀 없다.
- 레거시 코드를 유지보수해야 한다면 wait는 항상 표준 관용구에 따라 while 문 안에서 호출하자.
- notify보다는 notifyAll을 사용해야 한다.
- notify를 사용한다면 응답 불가 상태에 빠지지 않도록 주의하자