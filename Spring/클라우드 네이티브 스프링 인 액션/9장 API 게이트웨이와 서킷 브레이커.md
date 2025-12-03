# API 게이트웨이와 서킷 브레이커

- API 게이트웨이는 마이크로서비스와 같은 분산 아키텍처에서 클라이언트가 직접 내부 API를 연결하지 않도록 하기 위해
일반적으로 사용하는 패턴
  - 시스템에 진입점을 설정해놓으면 보안, 모니터링 및 복원력과 같이 공통으로 발생하는 이슈를 다루는 데에도 사용할 수 있음

--------------------

## 에지 서버와 스프링 클라우드 게이트웨이

- 스프링 클라우드 게이트웨이는 스프링 웹플럭스 및 프로젝트 리액터를 기반으로 API 게이트웨이를 제공하기 위해 구축된 프로젝트
  - 보안, 복원력, 모니터링 같은 공통의 관심사와 기능을 한 장소에서 다룸
- API 게이트웨이는 시스템의 진입점
- 마이크로서비스와 같은 분산 시스템에서 클라이언트를 내부 서비스 API로부터 분리하기 위한 편리한 방법인데 이렇게 클라이언트와 분리해놓으면
내부 API는 변경이 자유로움
- 클라이언트에게는 더 안정적이고 클라이언트 친화적인 공개 API를 제공하고 게이트웨이가 그 중간에서 클라이언트의 요청을 내부 API로 변환하기 때문에
시스템을 내부적으로 서비스와 API로 나누고 변경하는 것이 요이해짐
- 호출은 리버스 프록시와 유사하게 주어진 라우팅 규칙에 따라 게이트웨이에서 해당 서비스로 전달됨
- 에지 서버(edge server)는 시스템의 경계에 있는 애플리케이션으로 API 게이트웨이 기능과 공통으로 필요한 기능을 구현함
  - 다른 서비스를 호출할 때 오류가 확산되는 것을 막기 위해 서킷 브레이커를 설정하거나
  내부 서비스에 대한 모든 호출에 대해 재시도 및 타임아웃을 설정할 수 있음
  - 진입하는 트래픽을 제어하고 사용자의 회원 등급과 같은 기준에 따라 사용량을 제한하는 정책을 적용할 수 있음
  - 인증 및 권한 설정을 구현하고 토큰을 다른 서비스에 전달할 수 있음
  - 시스템에 복잡성을 추가한다는 점을 고려해야 함
  - 시스템에 네트워크 홉을 새롭게 추가하기 때문에 응답 시간을 늘림
  - 시스템의 진입 지점이기 때문에 단일 실패 지점이 될 위험이 있음
  - 완화 전략으로 컨피그 서버와 같이 복제본을 2개 이상 배포해야 함

### 스프링 클라우드 게이트웨이를 이용한 에지 서버 부트스트래핑

```yaml
server:
  port: 9000
  netty:
    connection-timeout: 2s
    idle-timeout: 15s
  shutdown: graceful

spring:
  application:
    name: edge-service
  lifecycle:
    timeout-per-shutdown-phase: 15s
```

### 경로와 술어의 정의

- 스프링 클라우드 게이트웨이는 3개의 주된 구성 요소로 이루어져 있음
  - 경로(route): 고유한 ID, 라우트를 따라갈지 여부를 결정하는 술어의 모음, 술어가 허용한 경우 요청을 전달하기 위한 URI, 요청을 다운스트림으로
  전달하기 전이나 후에 적용할 필터의 모음
  - 술어(predicate): 경로, 호스트, 헤더, 쿼리 매개변수, 쿠키, 본문 등 HTTP 요청의 항목
  - 필터(filter): 요청을 다른 서비스로 전달하기 전이나 후에 HTTP 요청 또는 응답을 수정
- 요청이 술어를 통해 하나의 경로와 일치하면 게이트웨이의 HandlerMapping은 이 요청을 게이트웨이의 WebHandler로 보내고 웹 핸들러는 다시 일련의
필터를 통해 요청을 실행함
- 필터 체인은 요청을 다운스트림 서비스로 보내기 전에 실행할 필터를 가지고 있고, 다른 서비스로부터 받은 응답을 클라이언트에게 전달하기 전에 실행할 필터
두 가지 있음
- 라우트는 최소한 고유한 ID, 요청을 전달할 URI, 하나 이상의 술어로 구성되어야 함

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: catalog-route
          uri: ${CATALOG_SERVICE_URL:http://localhost:9001}/books
          predicates:
            - Path=/books/**
        - id: order-route
          uri: ${ORDER_SERVICE_URL:http://localhost:9002}/orders
          predicates:
            - Path=/orders/**
```

- 경로가 /books 으로 시작하는 요청은 카탈로그 서비스, /orders로 시작하면 주문 서비스로 요청을 보냄
- URI는 환경변수의 값을 사용해 확인하고 정의되지 않은 경우 첫 번째 콜론 기호 뒤에 오는 값을 기본 설정값으로 사용함
- Cookie, Header, Host, Method, Path, Query, RemoteAddr 등 HTTP 요청의 여러 요소와 일치하는지 술어를 통해 확인할 수 있고, AND 조건으로 묶어서 사용할 수 있음
- 내부적으로 에지 서비스의 네티는 HTTP 클라이언트를 통해 요청을 해당 서비스로 전달
  - 외부 서비스를 호출할 때는 서비스 간 통신 장애에 탄력적으로 대처할 수 있도록 타임아웃을 설정하는 것이 필수
- 기본 설정상 스프링 클라우드 게이트웨이에서 사용하는 네티 HTTP 클라이언트는 탄력 연결 풀을 사용해 워크로드가 증가함에 따라 동시 연결 수를 동적으로
늘림


```yaml
spring:
  cloud:
    gateway:
      httpclient: # HTTP 클라이언트에 대한 설정 속성
        connect-timeout: 2000 # 연결을 수립하기까지의 타임아웃(밀리초)
        response-timeout: 5s # 응답을 받을 때까지의 타임아웃(기간)
        pool:
          type: elastic # 연결 풀 유형 (elastic, fixed, disabled 중 하나)
          max-idle-time: 15s # 통신 채널이 닫히기 전 대기하는 시간
          max-life-time: 60s # 통신 채널이 열려 있는 기간
```

### 필터를 통한 요청 및 응답 처리

- 라우트와 술어만으로도 애플리케이션이 프로식 역할을 할 수 있지만 필터가 중요
- 사전 필터는 들어오는 요청을 다운스트림 애플리케이션으로 전달하기 전에 실행할 수 있는 필터
  - 요청의 헤더를 변경
  - 사용률 제한 및 서킷 브레이크 적용
  - 프락시 요청에 대한 재시도 및 타임아웃 정의
  - OAuth2 및 오픈ID 커넥트를 사용한 인증 흐름 시작
- 사후 필터는 다운스트림 애플리케이션에서 응답을 받은 후에 클라이언트로 보내기 전에 응답에 대해 적용
  - 보안 헤더 설정
  - 응답 본문에서 민감한 정보를 변경
- 필터를 번들로 제공해줌

#### 재시도 필터 사용

- 모든 GET 요청에 대해 응답 상태 코드가 5XX 범위(SERVER_ERROR) 최대 3회까지 재시도

```yaml
spring:
  cloud:
    gateway:
      default-filters: # 기본 필터 목록
        - name: Retry # 필터 이름
          args:
            retries: 3 # 최대 3회까지 재시도
            method: GET
            series: SERVER_ERROR # 5XX 오류에 대해서만 재시도
            exception: java.io.IOException, java.util.concurrent.TimeoutException # 지정한 예외가 발생할 때만 재시도
            backoff: # 재시도는 firstBackoff * (factor^n) 공식을 사용해 계산
              firstBackoff: 50ms
              maxBackoff: 500ms
              factor: 2
              basedOnPreviousValue: false
```

- 기본 설정상 지연은 firstBackoff * (factor ^ n) 공식에 의해 계산됨
- basedOnPreviousValue 매개변수를 true로 설정하면 prevBackoff * factor 가 됨

--------------

## 스프링 클라우드 서킷 브레이커와 Resilience4J로 내결함성 개선하기

- 서킷 브레이커는 한 구성 요소의 오류가 그 구성 요소에 의존하는 다른 구성 요소로 전파되는 것을 차단해 나머지 시스템을 보호함
  - 결함이 발생한 구성 요소가 정상적으로 복구될 때까지 일시적으로 통신을 중단함으로써 가능
- 분산 시스템에서는 구성 요소 간의 통합 지점에 서킷 브레이커를 설치할 수 있음
- 재시도와 달리 서킷 브레이커가 작동하면 더 이상 해당 서비스에 대한 호출이 허용되지 않음
  - 임곗값과 타임아웃에 따라 작동이 다름
  - 호출할 폴백 메서드를 정의할 수 있음
- 서킷 브레이커 작동과 같은 최악의 경우라도 우아한 성능 저하를 보장해야 함
  - GET 요청의 경우 기본값 혹은 캐시에 마지막으로 저장된 값을 반환 등
- 스프링 클라우드 게이트웨이는 기본적으로 스프링 클라우드 서킷 브레이커와 통합되어 모든 서비스와의 상호작용을 보호하는 데 사용할 수 있은
CircuitBreaker 게이트웨이 필터를 제공함

### 스프링 클라우드 서킷 브레이커를 통한 서킷 브레이커 소개

```groovy
dependencies {
  implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j'
}
```

- 재시도 필터와 마찬가지로 특정 라우트에 적용하거나 기본 필터로 정의할 수 있음

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: catalog-route
          uri: ${CATALOG_SERVICE_URL:http://localhost:9001}/books
          predicates:
            - Path=/books/**
          filters:
            - name: CircuitBreaker
              args:
                name: catalogCircuitBreaker
                fallbackUri: forward:/catalog-fallback
        - id: order-route
          uri: ${ORDER_SERVICE_URL:http://localhost:9002}/orders
          predicates:
            - Path=/orders/**
          filters:
            - name: CircuitBreaker
              args:
                name: orderCircuitBreaker
```

- 회로가 열린 상태(연결되지 않음) 일 때 요청을 처리하기 위한 폴백 URI를 지정하는 것도 가능

### Resilience4J 서킷 브레이커 설정

- CircuitBreaker 필터를 적용할 라우트를 정의했다면 서킷 브레이커 자체를 설정해야 함
  - Resilience4J가 제공하는 속성 또는 Customizer 빈을 통해 설정할 수 있음

```yaml
resilience4j:
  circuitbreaker:
    configs:
      default: # 모든 서킷 브레이커에 대한 기본 설정
        sliding-window-size: 20 # 회로가 폐쇄된 상태일 때 호출의 결과를 기록하는데 사용하는 슬라이딩 윈도의 크기
        permitted-number-of-calls-in-half-open-state: 5 # 회로가 반개방 상태일 때 허용되는 호출의 수
        failure-rate-threshold: 50 # 실패율이 임계값 이상이면 회로는 개방 상태로 바뀜
        wait-duration-in-open-state: 15000 # 개방 상태에서 반개방 상태로 가기까지 기다릴 시간(밀리초)
  timelimiter:
    configs:
      default: # 모든 시간 제한에 대한 기본 설정
        timeout-duration: 5s # 타임아웃 설정(초)
```

### 스프링 웹플럭스를 이용한 폴백 REST API 정의

- CircuitBreaker 필터를 catalog-route에 추가 시 회로가 개방 상태인 경우 /catalog-fallback 엔드포인트로 요청을 전달하는 폴백 URI 속성 값을 정의함
- Retry 필터가 해당 라우트에도 적용되므로 모든 재시도가 실패하면 폴백 엔드포인트가 호출됨
- 스프링 웹플럭스에서 함수형 엔드포인트는 RouterFunction<?ServerResponse> 빈에서 RouterFunctions가 제공하는 플루언트 API를 통해
라우트로 정의함

```java
@Configuration
public class WebEndpoints {
	@Bean // 함수형 REST 엔드포인트가 빈 내부에서 정의됨
	public RouterFunction<ServerResponse> routerFunction() {
		return RouterFunctions.route() // 라우트를 생성하기 위한 플루언트 API를 제공함
			.GET("/catalog-fallback", request ->  // GET 엔드 포인트에 대한 폴백 응답
				ServerResponse.ok().body(Mono.just(""), String.class))
			.POST("/catalog-fallback", request ->  // POST 엔드 포인트에 대한 폴백 응답
				ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build())
			.build();
	}
}
```
- GET 요청에 대한 폴백은 빈 문자열 반환, POST 요청에 대한 폴백은 HTTP 503 오류 반환

---------

## 스프링 클라우드 게이트웨이와 레디스를통한 요청 사용률 제한

- 클라이언트 측 사용률 제한
  - 주어진 기간 동안 다운스트림 서비스로 전송되는 요청 수를 제한하기 위한 것
  - 클라우드 공급자와 같은 타사 조직이 해당 서비스를 관리하고 제공할 때 채택하면 유용한 패턴
  - 사용 계약에서 허용된 요청보다 더 많은 요청이 발생할 때 추가 비용이 들어가지 않도록 할 수 있음
  - 다운스트림 서비스가 자사 시스템에 속해 있다면 사용률 제한을 통해 DoS를 피할 수 있음
- 서버 측 속도 제한
  - 주어진 기간에 서비스가 수신하는 요청 수를 제한하기 위한 것
  - 전체 시스템을 과부하 또는 DoS 공격에서 보호하기 위해 API 게이트웨이에 구현할 때 유용
  - 사용자가 특정 시간 기간 내에 허용된 요청 수를 초과하면 HTTP 429 - Too Many Requests 상태로 모든 추가 요청이 거부됨

### 레디스 컨테이너 실행

- 사용자가 초댕 최대 10개 요청을 API에 보낼 수 있도록 제한한다 가정
  - 사용자가 매초 수행하는 요청 수를 추적하는 스토리지 매커니즘이 필요
- 애플리케이션 메모리에 저장하면 상태를 갖게 되고 오류를 초래할 수 있음
- 레디스는 일반적으로 캐시, 메시지 브로커, 데이터베이스로 사용하는 메모리 내 저장소

```yaml
polar-redis:
  image: "redis:7.0"
  container_name: "polar-redis"
  ports:
    - 6379:6379
```

### 스프링과 레디스의 통합

```groovy
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive'
}
```

```yaml
spring:
  redis:
    connect-timeout: 2s # 연결이 수립되기까지 기다리는 시간 한도
    host: localhost # 레디스 호스트 기본값
    port: 6379 # 레디스 포트 기본값
    timeout: 1s # 응답을 받기까지 기다리는 시간 한도
```

### 요청 사용률 제한 설정

- RequestRateLimiter 필터를 모든 라우트에 적용하는 기본 필터 혹은 특정 라우트에만 적용하는 필터로 설정할 수 있음
- RequestRateLimiter의 레디스에 대한 구현은 토큰 버킷 알고리즘을 기반으로함
  - 각 사용자에 대해 버킷이 할당되고 시간이 지나면서 토큰이 버킷에 특정한 비율로 떨어짐
  - 각 버킷은 최대 용량이 정해져 있으며 사용자가 요청을 할 때마다 토큰 하나가 버킷에서 삭제됨
  - 더 이상 토큰이 남아 있지 않으면 요청이 허용되지 않으며 사용자는 토큰이 버킷에 떨어지기를 기다려야 함

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - name: RequestRateLimiter
          args:
            redis-rate-limiter:
              replenishRate: 10 # 초당 버킷에 떨어지는 토큰의 수
              burstCapacity: 20 # 최대 20개 요청까지 허용
              requestedTokens: 1 # 하나의 요청 처리에 몇 개의 토큰이 사용되는지 지정
```

- 요청 사용률 제한 설정에서 적합한 값을 찾기 위한 공식은 없음
  - 요구 사항을 먼저 고려해야 하고 시행착오를 거쳐야 함
- RequestRateLimiter 필터는 KeyResolver 빈을 통해 요청에 대해 사용할 버킷을 결정함
  - 스프링 보안의 현재 인증된 사용자를 버킷으로 사용하도록 기본 설정되어 있음


--------

## 레디스를 통한 분산 세션 관리

- 에지 서비스는 저장이 필요한 비즈니스 엔티티를 처리하지는 않지만 RequestRateLimiter 필터와 관련된 상태를 저장하기 위해 상태를 갖는 서비스(레디스)
가 필요함
- 에지 서비스가 복제되면 허용할 수 있는 요청이 얼마나 많이 남아 있는지 추적하는 것이 중요
- 레디스를 사용하면 사용률 제한 기능이 일관성을 갖게 되고 안전해짐

### 스프링 세션 데이터 레디스를 통한 세션 처리

- 스프링은 스프링 세션 프로젝트를 통해 세션 관리 기능을 제공함
- 기본적으로 세션 데이터는 메모리에 저장되지만 클라우드 네이티브 애플리케이션은 메모리를 사용해서는 안 됨
- 분산 세션 저장소를 사용하는 이유는 동일한 애플리케이션에 대해 인스턴스가 여러 개 있기에 사용자에게 원활한 경험을
제공하기 위해 동일한 세션 데이터에 액세스 해야 함

```yaml
spring:
  session:
    store-type: redis
    timeout: 10m
    redis:
      namespace: polar:edge
```

- SaveSession을 기본 필터로 추가하여 스프링 클라우드 게이트웨이가 요청을 서비스로 전달하기 전에 항상 웹 세션을 저장하도록 할 수 있음

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - name: SaveSession
```

```java
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
class EdgeServiceApplicationTests {

	private static final int REDIS_PORT = 6379;
	@Container
	static GenericContainer<?> redis =
		new GenericContainer<>(DockerImageName.parse("redis:7.0"))
			.withExposedPorts(REDIS_PORT);

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", () -> redis.getHost());
		registry.add("spring.redis.port", () -> redis.getMappedPort(REDIS_PORT));
	}

	@Test
	void verifyThatSpringContextLoads() {
	}
}
```

---------

## 쿠버네티스 인그레스를 통한 외부 액세스 관리

- 에지 서비스가 쿠버네티스 클러스터에 배포되면 클러스터 내에서만 액세스할 수 있음
- 인그레스 API를 통해 쿠버네티스 클러스터 내 애플리케이션에 대한 외부 액세스를 관리함

### 인그레스 API와 인그레스 컨트롤러 이해

- 쿠버네티스 클러스터 내부의 애플리케이션을 노출하기 원한다면 ClusterIP 유형의 서비스 객체를 사용할 수 있음
  - 클러스터 안에서 파드가 서로 연결할 수 있도록 사용함
- 서비스 객체는 또한 LoadBalancer 유형일 수 있음
  - 클라우드 공급 업체가 제공하는 외부 로드 밸런서를 사용해 애플리케이션을 인터넷에 노출할 수 있음
  - 인터넷에 노출하기로 결정한 서비스는 모두 다른 IP 주소를 가져야 함
  - 서비스가 직접 노출되기 떄문에 TLS 종료와 같이 네트워크에 관한 추가 설정을 할 기회가 없음
- 인그레스는 '클러스터의 서비스에 대한 외부 액세스를 관리하는' 객체
  - HTTP를 사용한 액세스를 의미
- 인그레스를 통해 부하 분산, SSL 종료, 이름 기반 가상 호스팅을 제공할 수 있음
- 인그레스 객체는 쿠버네티스 클러스터의 진입 지점 역할을 하며 단일한 외부 IP 주소로 들어오는 트래픽을 클러스터 내부에서 실행 중인 여러 서비스로
라우팅할 수 있음
  - 부하 분산이 가능하고 특정 URL로 향하는 외부 트래픽을 허용할 수 있으며 애플리케이션 서비스를 HTTPS 를 통해 노출하기 위해 TLS 종료를 관리할 수 있음
- 인그레스 객체는 스스로 아무것도 할 수 없으며 객체를 사용해 라우팅과 TLS 종료에 관련해서 원하는 상태를 선언해야 함
- 규칙을 적용하고 클러스터 외부의 트래픽을 내부 애플리케이션으로 라우팅하는 실제 구성 요소는 인그레스 컨트롤러
- 인그레스 컨트롤러는 일반적으로 NGINX, HAProxy 또는 엔보이 같은 리버스 프록시를 사용해 구축함

```shell
minikube start --cpus 2 --memory 4g --driver docker --profile polar
minikube addons enable ingress --profile polar
kubectl get all -n ingress-nginx
```

- -n ingress-nginx 는 ingress-nginx라는 네임스페이스내에 존재하는 모든 객체를 가져올 수 있음
  - 네임스페이스는 쿠버네티스에서 한 클러스터 안의 리소스를 여러 개의 그룹으로 분리하기 위해 사용하는 추상화

### 인그레스 객체 사용

- 에지 서비스는 애플리케이션 라우팅을 처리하지만 기본 인프라나 네트워크 설정에는 관여할 필요가 없음
- 클러스터 외부에서 들어오는 모든 HTTP 트래픽을 에지 서비스로 라우팅하는 인그레스를 정의
- 인그레스 라우트 정의와 설정은 HTTP 요청 시 사용하는 DNS 이름을 기반으로 하는 것이 일반적
  - 로컬에서 작업하기에 클러스터 외부에서 액세스할 수 있도록 인그레스에 제공된 외부 IP 주소로 호출할 수 있음

```shell
minikube ip --profile polar
```

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: polar-ingress
spec:
  ingressClassName: nginx
  rules:
    - http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: edge-service
                port:
                  number: 80
```