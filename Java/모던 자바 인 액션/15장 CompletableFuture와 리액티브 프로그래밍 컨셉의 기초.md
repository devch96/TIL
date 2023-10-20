# CompletableFuture와 리액티브 프로그래밍 컨셉의 기초

- 최근 소프트웨어 개발 방법을 획기적으로 뒤집는 추세 두 가지
  - 하드웨어의 발전
    - 멀티코어 프로세서가 발전하면서 한 개의 큰 태스크를 병렬로 실행할 수 있는 개별 하위 태스크로 분리하는 것
  - 인터넷 서비스에서 사용하는 애플리케이션이 증가
    - 마이크로서비스 아키텍처
    - 공개 API
    - 다양한 소스의 컨텐츠를 가져와서 합치는 매시업(mashup)형태
- CPU의 코어를 잘 관리해야 하는 입장(하드웨어의 발전)에서 인터넷 서비스를 이용하기 위해 스레드를 블록함으로 연산 자원을
낭비하는 일은 피해야 함

-------------------

## 동시성을 구현하는 자바 지원의 진화

- 처음의 자바는 Runnable과 Thread를 동기화된 클래스와 메서드를 이용해 잠갔다.
- 자바 5는 좀 더 표현력있는 동시성을 지원하는 ExecutorService 인터페이스, Callable and Future를 지원했다.
- 자바 7에서는 분할 정복 알고리즘의 포크/조인 구현을 지원하는 RecursiveTask가 추가되었고 자바 8에서는 스트림과 새로 추가된 람다 지원에 기반한
병렬 프로세싱이 추가되었다.

### 스레드와 높은 수준의 추상화

- 운영체제는 주기적으로 번갈아가며 각 프로세스에 CPU를 할당한다.
- 프로세스는 다시 운영체제에 한 개 이상의 스레드, 프로세스 본인이 가진 같은 주소 공간을 공유하는 프로세스를 요청함으로
태스크를 동시에 또는 협력적으로 실행할 수 있다.

### Executor와 스레드 풀

- 자바 5는 Executor 프레임워크와 스레드 풀을 통해 프로그래머가 태스크 제출과 실행을 분리할 수 있는 기능을 제공했다.

#### 스레드의 문제

- 자바 스레드는 직접 운영체제 스레드에 접근한다.
- 운영체제 스레드를 만들고 종료하려면 비싼 비용을 치러야 하며 더욱이 운영체제 스레드의 숫자는 제한되어 있다.

#### 스레드 풀 그리고 스레드 풀이 더 좋은 이유

- 자바 ExecutorService는 태스크를 제출하고 나중에 결과를 수집할 수 있는 인터페이스를 제공한다.
- 스레드 풀은 하드웨어에 맞는 수의 태스크를 유지함과 동시에 수 천개의 태스크를 스레드 풀에 아무 오버헤드 없이 제출할 수 있다는 점이 좋다.

#### 스레드 풀 그리고 스레드 풀이 나ㅃ즌 이유

- k 스레드를 가진 스레드 풀은 오직 k만큼의 스레드를 동시에 실행할 수 있다.
  - 잠을 자거나 I/O를 기다리거나 네트워크 연결을 기다리는 태스크가 있다면 그 태스크를 할당받은 스레드는 블록 상황에서 워커 스레드에 할당된 상태를
  유지하지만 아무 작업도 하지 않는다.
  - 블록할 수 있는 태스크는 스레드 풀에 제출하지 말아야 하지만 이를 항상 지킬 수 있는 것은 아니다
- 프로그램을 종료하기 전에 모든 스레드 풀을 종료하는 습관을 갖는 것이 중요하다
  - 풀의 워커 스레드가 만들어진 다음 다른 태스크 제출을 기다리면서 종료되지 않은 상태일 수 있으므로

### 스레드의 다른 추상화 : 중첩되지 않은 메서드 호출

- 태스크나 스레드가 메서드 호출 안에서 시작되면 그 메서드 호출은 반환하지 않고 작업이 끝나기를 기다렸다.
- 스레드 생성과 join()이 한 쌍처럼 중첩된 메서드 호출 내에 추가되는 것을 엄격한 포크/조인 이라 부른다.
- 시작된 태스크를 내부 호출이 아니라 외부 호출에서 종료하도록 기다리는 좀 더 여유로운 포크/조인을 사용해도 비교적 안전하다.
- 메서드 호출자에 기능을 제공하도록 메서드가 반환된 후에도 만들어진 태스크 실행이 계속되는 메서드를 비동기 메서드라 그런다.
- 비동기 메서드 주의점
  - 스레드 실행은 메서드를 호출한 다음의 코드와 동시에 실행되므로 데이터 경쟁 문제를 일으키지 않도록 주의해야 한다
  - 스레드가 종료되지 않은 상황에서 자바의 main() 메서드가 반환하면 어떻게될까
    - 애플리케이션을 종료하지 못하고 모든 스레드가 실행을 끝날 때까지 기다림
    - 애플리케이션 종료를 방해하는 스레드를 kill 하고 종료함

### 스레드에 무엇을 바라는가?

- 일반적으로 모든 하드웨어 스레드를 활용해 병렬성의 장점을 극대화하도록 프로그램 구조를 만드는 것,
즉 프로그램을 작은 테스크 단위로 구조화하는 것이 목표다.

----------------------

## 동기 API와 비동기 API

```java
int y = f(x);
int z = g(x);
System.out.println(y+z);
```
- f,g를 실행하는데 오래 걸린다고 가정하면 별도의 스레드로 f와 g를 실행해 구현할 수 있지만 코드가 복잡해진다.
- 비동기 API라는 기능으로 API를 바꿔서 해결할 수 있다.

### Future 형식 API

```java
Future<Integer> y = f(x);
Future<Integer> z = g(x);
System.out.println(y.get() + z.get());
```
- 메서드 호출 즉시 원래 바디를 평가하는 태스크를 포함하는 Future를 반환하고 get() 메서드를 이용해 두 Future가 완료되어 결과가
합쳐지기를 기다린다.

### 리액티브 형식 API

- f,g의 시그니처를 바꿔서 콜백 형식의 프로그래밍을 이용하는 것
```java
void f(int x, IntConsumer dealWithResult);

public class CallbackStyleExample{
    public static void main(String[] args) {
        int x = 1337;
        Result result = new Result();
        f(x, (int y) ->{
            result.left = y;
            System.out.println((result.left + result.right));
        });
        g(x, (int y) ->{
            result.left = z;
            System.out.println((result.left + result.right));
        });
    }
}
```
- f와 g의 호출 합계를 정확하게 출력하지 않고 상황에 따라 먼저 계산된 결과를 출력한다.
- if-then-else를 이용해 적절한 락을 사용해야 한다.

### 잠자기(그리고 기타 블로킹 동작)는 해로운 것으로 간주

- 스레드는 잠들어도 여전히 시스템 자원을 점유한다.
- 몇 개 사용하는 상황에서는 큰 문제가 아니지만 스레드가 많아지고 그 중 대부분이 잠을 잔다면 문제가 심각해진다.
- 스레드 풀에서 잠을 자는 태스크는 다른 태스크가 시작되지 못하게 막으므로 자원을 소비한다.
- 스레드 풀에서 잠자는 스레드만 실행을 막는 것이 아니라 모든 블록 동작도 마찬가지다.
```java
// 코드 A
work1();
Thread.sleep(10000);
work2();

// 코드 B
ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
work1();
scheduledExecutorService.schedule(ScheduledExecutorServiceExample::work2, 10, TimeUnit.SECOND);
scheduledExecutorService.shutdown();
```
- 코드 A가 코드 B 보다 좋다
  - 코드 A는 10초동안 잠을 자면서 워커 스레드를 점유한다
  - 코드 B는 work1을 실행하고 종료한다. 종료하면서 10초 뒤에 work2가 실행되도록 큐에 추가한다.
  - A는 자는 동안 스레드 자원을 점유하는 반면, B는 다른 작업이 실행될 수 있도록 허용한다.
- 태스크가 실행되면 귀중한 자원을 점유하므로 태스크가 끝나서 자우너을 해제하기 전까지 태스크를 계속 실행해야 한다.
- 태스크를 블록하는 것보다는 다음 작업을 태스크로 제출하고 현재 태스크는 종료하는 것이 바람직하다.

### 현실성 확인

- 새로운 시스템을 설계할 때 시스템을 많은 작은 동시 실행되는 태스크로 설계해서 블록할 수 있는 모든 동작을 비동기 호출로 구현한다면
병렬 하드웨어를 최대한 활용할 수 있다.
- 하지만 현실적으로 모든 것은 비동기 라는 설계 원칙을 어겨야 한다

### 비동기 API에서 예외는 어떻게 처리하는가?

- Future나 리액티브 형식의 비동기 API에서 호출된 메서드의 실제 바디는 별도의 스레드에서 호출되며 이때 발생하는 어떤 에러는 이미 호출자의 실행 범위와는
관계가 없는 상황이 된다.
- Future를 구현한 CompletableFuture에서는 런타임 get() 메서드에 예외를 처리할 수 있는 기능을 제공하며 예외에서 회복할 수 있도록
exceptionally() 같은 메서드도 제공한다.
- 리액티브 형식의 비동기 API에서는 return 대신 기존 콜백이 호출되므로 예외가 발생했을 때 실행될 추가 콜백을 만들어 인터페이스를 바꿔야 한다.
```java
void f(int x, Consumer<Integer> dealWithResult, Consumer<Throwable> dealWithException);
```
- 콜백이 여러 개면 이를 따로 제공하는 것보다는 한 객체로 이 메서드를 감싸는 것이 좋다.
```java
void f(int x, Subscriber<Integer> s);
```

----------------------

## CompletableFuture와 콤비네티어를 이용한 동시성

- 자바 8에서는 Future 인터페이스의 구현인 CompletableFuture를 이용해 Future를 조합할 수 있는 기능을 추가했다.
- 일반적으로 Future는 실행해서 get()으로 결과를 얻을 수 있는 Callable로 만들어지는데 CompletableFuture는 실행할 코드 없이
Future를 만들 수 있도록 허용하며 complete() 메서드를 이용해 나중에 어떤 값을 이용해 다른 스레드가 이를 완료할 수 있고 get()으로 값을 얻을 수 있도록
허용한다.

```java
public class CFCombine {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int x = 1337;

        CompletableFuture<Integer> a = new CompletableFuture<>();
        CompletableFuture<Integer> b = new CompletableFuture<>();
        CompletableFuture<Integer> c = a.thenCombine(b, (y,z) -> y+z);
        executorService.submit(() -> a.complete(f(x)));
        executorService.submit(() -> b.complete(g(x)));

        System.out.println(c.get());
        executorService.shutdown();
        
        
    }
}
```
- Future a와 Future b의 결과를 알지 못한 상태에서 thenCombine은 두 연산이 끝났을 때 스레드 풀에서 실행된 연산을 만든다.
- 결과를 추가하는 세 번째 연산 c는 다른 두 작업이 끝날 때까지는 스레드에서 실행되지 않는다(먼저 시작해서 블록되지 않음)

-------------------

## 발행-구독 그리고 리액티브 프로그래밍

- Future와 CompletableFuture은 독립적 실행과 병렬성이라는 정식적 모델에 기반하기 때문에 한 번만 실행해 결과를 제공한다
- 반면 리액티브 프로그래밍은 여러 Future 같은 객체를 통해 여러 결과를 제공한다.
- 한 번의 결과가 아니라 여러 번의 결과가 필요하고, 가장 최근의 결과가 중요하다면 이것을 리액티브라 부른다.
- 왜냐면 특정 결과에 반응(react)하는 부분이 존재하기 때문이다.
- 자바 9에서는 Flow 인터페이스에 발행-구독 모델을 적용해 리액티브 프로그래밍을 제공한다.

### 두 플로를 합치는 예제

```java
private class SimpleCell {
    private int value = 0;
    private String name;

    public SimpleCell(String name) {
        this.name = name;
    }
}

interface Publisher<T> {
    void subscribe(Subscriber<? super T> subscriber);
}

interface Subscriber<T> {
    void onNext(T t);
}

private class SimpleCell implements Publisher<Integer>, Subscriber<Integer> {
    private int value = 0;
    private String name;
    private List<Subscriber> subscribers = new ArrayList<>();
    
    public SimpleCell(String name){
        this.name = name;
    }
    
    @Override
    public void subscribe(Subscriber<? super Integer> subscriber){
        subscribers.add(subscriber);
    }
    
    private void notifyAllSubscribers(){
        subscribers.forEach(subscriber -> subscriber.onNext(this.value));
    }
    
    @Override
    public void onNext(Integer newValue){
        this.value = newValue;
        notifyAllSubscribers();
    }
}
```

### 역압력

- Subscriber에서 Publisher로 정보를 요청해야 할 필요가 있을 때는..?
- Subscriber 인터페이스는 onSubscribe(Subscription subscription) 메서드를 포함한다.
- Publisher와 Subscriber 사이에 채널이 연결되면 첫 이벤트로 이 메서드가 호출된다.
- Publisher는 Subscription 객체를 만들어 Subscriber로 전달하면 Subscriber는 이를 통해 Publisher로 정보를 보낼 수 있다.

------------------

## 리액티브 시스템 vs 리액티브 프로그래밍

- 리액티브 시스템은 런타임 환경이 변화에 대응하도록 전체 아키텍처가 설계된 프로그램을 가리킨다.
  - 반응성, 회복성, 탄력성 세 가지 속성이 있다.

------------------

## 마치며

- 스레드 풀은 보통 유용하지만 블록되는 태스크가 많아지면 문제가 발생한다
- 메서드를 비동기로 만들면 병렬성을 추가할 수 있으며 부수적으로 루프를 최적화한다
- CompletableFuture 클래스는 한 번의 비동기 연산을 표현한다.
- 플로 API 는 발행-구독 프로토콜, 역압력을 이용하면 자바의 리액티브 프로그래밍의 기초를 제공한다.
