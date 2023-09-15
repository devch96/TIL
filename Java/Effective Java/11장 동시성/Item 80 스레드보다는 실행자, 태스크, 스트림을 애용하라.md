# 스레드보다는 실행자, 태스크, 스트림을 애용하라

- java.util.concurrent 패키지는 실행자 프레임워크(Executor Framework)라고 하는 인터페이스 기반의
유연한 태스크 실행 기능을 담고 있다.
```java
ExecutorService exec = Executors.newSingleThreadExecutor(); // 작업 큐 생성
exec.execute(runnable) // 실행자에 실행할 태스크 넘기는 방법
exec.shutdown(); // 실행자 종료 방법
```

- 필요한 실행자 대부분은 Executors의 정적 팩터리들을 이용해 생성할 수 있을 것이다.
- 평범하지 않은 실행자를 원한다면 ThreadPoolExecutor 클래스를 직접 사용해도 된다.

### 작업 큐를 손수 만드는 일은 삼가야 하고, 스레드를 직접 다루는 것도 일반적으로 삼가야 한다.

- 스레드를 직접 다루면 Thread가 작업 단위와 수행 메커니즘 역할을 모두 수행하게 된다.
- 반면 실행자 프레임워크에서는 작업 단위와 실행 메커니즘이 분리된다.

### ForkJoinTask

- 자바 7이 되면서 실행자 프레임워크는 포크-조인(fork-join) 태스크를 지원하도록 확장되었다.
- ForkJoinTast의 인스턴스는 작은 하위 태스크로 나누리 수 있고, ForkJoinPool을 구성하는 스레드들이 이 태스크들을
처리하며, 일을 먼저 끝낸 스레드는 다른 스레드의 남은 태스크를 가져와 대신 처리할 수도 있다.

### 병렬 스트림은 ForkJoinPool로 만들었다.

- 포크-조인에 적합한 형태의 작업이면 병렬 스트림을 이용하면 적은 노력으로 그 이점을 얻을 수 있다.

