# IO 병목, 어떻게 해결하지

## 네트워크 IO와 자원 효율

- 서버 프로그램은 기본적으로 네트워크 프로그램
- 많은 서버는 HTTP 프로토콜을 이용해서 클라이언트와 데이터를 주고받음
- 데이터 처리를 위해 DB를 사용하는데 DB는 TCP에 기반한 프로토콜을 사용해서 데이터를 주고받음
- 레디스를 메모리 캐시로 사용할 때도 네트워크를 통해 데이터를 주고 받음
- 데이터 입출력이 완료될 때까지 스레드는 아무 작업도 하지 않고 입출력이 끝나기를 기다림
  - 스레드가 블로킹(blocking)됨
- 입출력에 소요되는 시간은 코드를 실행하는 시간보다 훨씬 김

-----

## 가상 스레드로 자원 효율 높이기

- 코드를 블로킹 IO로 작성했는데 입출력 동안 스레드가 대기하지 않고 다른 일을 할 수 있다면?
  - CPU의 유휴 시간 감소
  - 더 많은 작업 처리 가능
- 자바의 가상 스레드나 Go 언어의 고루틴 사용
  - 둘 다 경량 스레드
- 경량 스레드는 OS가 관리하는 스레드가 아니라 JVM 같은 언어의 런타임이 관리하는 스레드
- OS가 CPU로 실행할 스레드를 스케줄링하듯 언어 런타임이 OS 스레드로 실행할 경량 스레드를 스케줄링함
- JVM은 플랫폼 스레드(OS 스레드에 1:1로 대응하는 래퍼)로 구성된 풀을 유지함
- CPU가 OS 스케줄러에 의해 여러 스레드를 번갈아 실행하는 것처럼 플랫폼 스레드도 JVM 스케줄러에 의해 여러 가상 스레브를 번갈아 실행함
- JVM은 기본적으로 풀에 CPU 코어 개수만큼 플랫폼 스레드를 생성하고 필요에 따라 플랫폼 스레드를 증가시킴
- 톰캣처럼 요청별 스레드를 생성하는 서버에서 가상 스레드를 사용하면 더 적은 메모리로 더 많은 요청을 처리할 수 있음

### 네트워크 IO와 가상 스레드

- 가상 스레드는 실행하는 과정에서 블로킹되면 플랫폼 스레드와 언마운트되고 실행이 멈춤
  - 언마운트된 플랫폼 스레드는 실행 대기 중인 다른 가상 스레드와 연결된 뒤 실행을 재개함

### 가상 스레드와 성능

- 가상 스레드는 네트워크 프로그래밍처럼 입출력이 주를 이루는 IO 중심 작업일 때 효과가 있음
- CPU 중심 작업은 블로킹 연산이 거의 없으므로 가상 스레드를 많이 생성하더라도 동시 실행 효과를 얻을 수 없음
- IO 중심 작업이어도 스케줄링에 사용되는 플랫폼 스레드 개수보다 가상 스레드의 개수가 많아야 효과를 기대할 수 있음
- 가상 스레드의 이점을 얻으려면 CPU 코어 수를 줄이거나(플랫폼 스레드의 갯수) 트래픽이 많아야 함
- 가상 스레드를 사용해서 높일 수 있는 것은 처리량
  - 실행 속도가 플랫폼 스레드보다 빨라지는 것은 아님
  - 결국 실행하는 것은 같은 CPU

### 가상 스레드의 중요한 장점

- 기존 코드를 크게 수정할 필요가 없음

--------

## 논블로킹 IO로 성능 더 높이기

- 논블로킹 IO는 새로운 것이 아닌 오래 전부터 네트워크 서버의 성능을 높이기 위해 사용한 방식

### 논블로킹 IO 동작 개요

- 입출력이 끝날 때까지 스레드가 대기하지 않음
- 데이터를 조회했는지 여부에 상관없이 대기하지 않고 바로 다음 코드를 실행하므로 블로킹 IO처럼 데이터를 조회했다는 가정하에 코드를 작성할 수 없음
- 루프 안에서 조회를 반복해서 호출한 뒤 데이터를 읽었을 때만 처리하는 방식으로 구현할 수 있음
  - 하지만 이렇게하면 CPU 낭비가 심함
- 실제로 논블로킹 IO를 사용할 때는 데이터 읽기를 바로 시도하기보다는 어떤 연산을 수행할 수 있는지를 확인하고 해당 연산을 실행하는 방식으로 구현함
  - 실행 가능한 IO 연산 목록을 구함(실행 가능한 연산을 구할 때까지 대기)
  - 위에서 구한 IO 연산 목록을 차례대로 순회함
    - 각 IO 연산 처리
  - 위 과정 반복

```java
Selector selector = Selector.open();

ServerSocketChannel serverSocket = ServerSocketChannel.open();
serverSocket.bind(new InetSocketAddress(7031));
serverSocket.configureBlocking(false); // 서버 소켓 비동기 설정

serverSocket.register(selector, SelectionKey.OP_ACCEPT); // 연결 연산 등록

while (true) {
    selector.select(); // 가능한 IO 연산이 있을 때까지 대기
    Set<SelectionKey> selectedKeys = selector.selectedKeys();
    Iterator<SelectionKey> iterator = selectedKeys.iterator();
    while (iterator.hasNext()) { // IO 연산 순회
        SelectionKey key = iterator.next();
        iterator.remove();
        if (key.isAcceptable()) { // 클라이언트 연결 처리 가능하면
            SocketChannel client = serverSocket.accept(); // 클라이언트 연결 처리
            client.configureBlocking(false); // 비동기 설정
            client.register(selector, SelectionKey.OP_READ); // 읽기 연산 등록
        } else if (key.isReadable()) { // 읽기 연산 가능하면
            SocketChannel channel = (SocketChannel) key.channel(); // 채널 구함
            int readBytes = channel.read(inBuffer); // 채널에 읽기 연산 실행
            if (readBytes == -1) {
                channel.close();
            }else {
                inBuffer.flip();
                outBuffer.put(inBuffer); // 출력 버퍼에 복사
                inBuffer.clear();
                outBuffer.flip();
                channel.write(outBuffer); // 채널에 쓰기 연산 실행
                outBuffer.clear();
            } 
        }
    }
}
```

- 일반적으로 블로킹 IO로 구현한 서버는 커넥션별(또는 요청별) 스레드를 할당함
  - 동시 연결 클라이언트가 1000개라면 클라이언트를 처리할 스레드를 1000개 생성
- 논블로킹 IO는 클라이언트 수에 상관없이 소수의 스레드를 사용함
- 논블로킹 IO에서 동시성을 높이기 위해서 사용하는 방법은 채널들을 N개 그룹으로 나누고 각 그룹마다 스레드를 생성하는 것
  - 보통 CPU 개수만큼 그룹을 나누고 각 그룹마다 입출력을 처리할 스레드를 할당함

### 리액터 패턴

- 논블로킹 IO를 이용해서 구현할 때 사용하는 패턴 중 하나
- 동시에 들어오는 여러 이벤트를 처리하기 위한 이벤트 처리 방법
- 리액터 패턴은 리액터와 핸들러 두 요소로 구성됨
- 리액터는 이벤트가 발생할 때까지 대기하다가 이벤트가 발생하면 알맞은 핸들러에 이벤트를 전달함
- 이벤트를 받은 핸들러는 필요한 로직을 수행함

```java
while (isRunning) {
    List<Event> events = getEvents(); // 이벤트가 발생할 때까지 대기
    for (Event event : events) {
        Handler handler = getHandler(event); // 이벤트를 처리할 핸들러 구함
        handler.handle(event); // 이벤트를 처리함
    }
}
```

- 리액터는 이벤트를 대기하고 핸들러에 전달하는 과정을 반복하는데 그래서 리액터를 이벤트 루프라고 함

### 프레임워크 사용하기

- 줄 단위로 데이터를 수신하는 서버를 구현한다고 가정
  - 블로킹 IO일 경우 BufferedReader를 사용해 줄 단위로 데이터를 쉽게 읽을 수 있음

```java
BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

String line;
while ((line = br.readLine()) != null) {
        ...
}
```

- 논블로킹 IO를 사용하면 처리가 복잡해짐
  - 데이터를 읽은 뒤 \n 문자가 있는지 확인해야 함
  - \n 문자가 없는 경우 읽은 데이터를 버퍼에 계속 누적하는 처리도 해야 함
  - \n 문자가 여러 개 존재하는 경우도 처리해야 함
  - 채널마다 누적 처리를 위한 버퍼도 관리해야 함
- 데이터 형식이 조금만 바뀌어도 저수준의 IO 처리 코드를 변경해야 함
  - 리액터 네티 등 프레임워크 사용하면 좋음

```java
DisposableServer server = TcpServer.create()
        .port(7031)
        .doOnConnection(conn -> conn.addHandlerFirst(new LineBasedFrameDecoder(1024)) // 줄 단위 읽기 처리
        ).handle((in, out) -> {
            return in.receive()
                    .asString()
                    .doOnNext(line -> {
                        log.info("received: {} ", line);
                    })
                    .flatMap(line -> out.sendString(Mono.just(line + "\n"))
                  )
        }
```

------

## 언제 어떤 방법을 택할까

- 논블로킹 IO나 가상 스레드를 적용할 때는 다음을 검토해야 함
  - 문제가 있는가?
    - 문제가 없다면 구현을 변경하는 것은 시간을 낭비하는 것에 불과
    - 논블로킹/비동기 IO 방식으로 구현하면 코드가 복잡해지고 유지보수 난이도도 올라감
  - 문제가 있다면 네트워크 IO 관련 성능 문제인가?
    - 트래픽은 그대로인데 DB 쿼리 시간이 느려지면서 서버 응답 시간이 길어지는 문제가 발생했다면 가상 스레드나 논블로킹 IO를 적용해도
    응답 시간을 줄일 수는 없음
    - CPU 중심 작업도 마찬가지
  - 구현 변경이 가능한가?
    - 우선 순위에 밀려 구현 변경이 불가능한 상황도 있음
    - 기술에 대한 익숙함도 구현 변경 여부에 영향을 줌
