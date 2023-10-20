# CompletableFuture : 안정적 비동기 프로그래밍

-----------------

## Future의 단순 활용

- 자바 5부터는 미래의 어느 시점에 결과를 얻는 모델에 활용할 수 있도록 Future 인터페이스를 제공하고 있다.
- 비동기 계산을 모델링하는 데 Future를 이용할 수 있으며 Future는 계산이 끝났을 때 결과에 접근할 수 있는 참조를 제공한다.
- Future를 이용하려면 시간이 오래 걸리는 작업을 Callable 객체 내부로 감싼 다음에 ExecutorService에 제출해야 한다.
- 하지만 Executor 스레드에서 진행하고 있는 태스크가 끝나지 않은 상태에서 현재 스레드에서 get을 요청하면 태스크가 끝날때까지
기다린다. 태스크가 끝나지 않는 문제가 있을 수 있으므로 get 메서드를 오버로드해서 대기할 최대 타임아웃 시간을 설정하는 것이 좋다.

### Future 제한

- Future로는 간결한 동시 실행 코드를 구현하기에 충분하지 않다.
  - 두 개의 비동기 계산 결과를 합친다. 두 가지 계산 결과는 독립적일수도 의존적일 수도 있다.
  - Future 집합이 실행하는 모든 태스크의 완료를 기다린다
  - Future 집합에서 가장 빨리 완료되는 태스크를 기다렸다가 결과를 얻는다
  - 프로그램적으로 Future를 완료시킨다(비동기 동작에 수동으로 결과 제공)
  - Future 완료 동작에 반응한다(결과를 기다리면서 블록되지 않고 결과가 준비되었다는 알림을 받은 다음에 Future의 결과로 원하는 추가 동작 수행)
- 이러한 기능은 자바 8에서 제공하는 CompletableFuture 클래스로 사용할 수 있다.

### CompletableFuture로 비동기 애플리케이션 만들기

- 여러 온라인상점 중 가장 저렴한 가격을 제시하는 상점을 찾는 애플리케이션

----------------------------

## 비동기 API 구현

### 동기 메서드를 비동기 메서드로 변환

```java
public Future<Double> getPriceAsync(String product){
    CompletableFuture<Double> futurePrice = new CompletableFuture<>();
    new Thread( () -> {
        double price = calculatePrice(product);
        futurePrice.complete(price);
    }).start();
    return futurePrice;
}
```

### 에러 처리 방법

- 클라이언트는 타임아웃값을 받는 get 메서드의 오버로드 버전을 만들어 해결할 수 있다.
- 하지만 왜 에러가 발생했는지 알 수 없기 때문에 내부에서 에러를 주는것또한 좋다.
```java
public Future<Double> getPriceAsync(String product){
    CompletableFuture<Double> futurePrice = new CompletableFuture<>();
    new Thread( () -> {
        try{
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }catch(Exception e){
            futurePrice.completeExceptionally(e);
        }
    }).start();
    return futurePrice;
}
```

#### 팩터리 메서드 supplyAsync로 CompletableFuture 만들기

- supplyAsync 메서드는 Supplier를 인수로 받아서 CompletableFuture를 반환한다.
```java
public Future<Double> getPriceAsync(String product){
    return CompletableFuture.supplyAsync(() -> calculatePrice(product));
}
```
----------------------

## 비블록 코드 만들기

```java
List<CompletableFuture<String>> priceFutures= shops.stream()
        .map(shop -> CompletableFuture.supplyAsync(
        () -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))))
        .collect(toList());
```

### 커스텀 Executor 사용하기

- 스레드 풀의 최적값을 찾는 방법
  - 스레드 풀이 너무 크면 CPU와 메모리 자원을 서로 경쟁하느라 시간을 낭비
  - 너무 적으면 CPU의 일부 코어는 활용되지 않을 수 있음
  - N(threads) = N(cpu) * U(cpu) * (1 + W/C)
  - N(cpu) 는 Runtime.getRuntime().availableProcessors()가 반환하는 코어 수
  - U(cpu)는 0과 1 사이의 값을 갖는 CPU 활용 비율
  - W/C는 대기시간과 계산시간의 비율
```java
private final Executor executor = Exeutors.newFixedThreadPool(Math.min(shops.size(), 100),
        new ThreadFactory() {
            public Thread newThread(Runnable r){
                Thread t = new Thread(r);
                t.setDaemon(true);
                return tl
            }
        });
```

