# 리액티브 API 개발하기

------------

## 스프링 WebFlux 사용하기

- 매 연결마다 하나의 스레드를 사용하는 스프링 MVC같은 전형적인 서블릿 기반의 웹 프레임워크는 스레드 블로킹과
다중 스레드로 수행된다.
  - 요청이 처리될 때 스레드 풀에서 작업 스레드를 가져와서 해당 요청을 처리하며, 작업 스레드가 종료될 때까지 요청 스레드는 블로킹된다.
- 블로킹 웹 프레임워크는 요청량의 증가에 따른 확장이 어렵다.
- 웹 애플리케이션을 사용하는 클라이언트의 수가 증가함에 따라 그 어느 때보다도 확장성이 더욱 중요해졌다.
- 비동기 웹 프레임워크는 더 적은 수의 스레드(일반적으로 CPU 코어당 하나)로 더 높은 확장성을 성취한다.
  - 이벤트 루핑(event looping) 이라는 기법을 적용한 이런 프레임워크는 한 스레드당 많은 요청을 처리할 수 있어서 한 연결당 소요 비용이 더 경제적이다.
- 데이터베이스나 네트워크 작업과 같은 집중적인 작업의 콜백과 요청을 비롯해서, 이벤트 루프에서는 모든 것이 이벤트로 처리된다.
- 비용이 드는 작업이 필요할 때 이벤트 루프는 해당 작업의 콜백을 등록하여 병행으로 수행되게 하고 다른 이벤트 처리로 넘어간다.
- 스프링 5에서는 블로킹이 없는 비동기 웹 프레임워크가 소개되었으며, 이 프레임워크는 프로젝트 리액터를 기반으로 웹 애플리케이션과 API의 더 큰 확장성 요구를 해소한다.

### 스프링 WebFlux 개요

- 리액티브 프로그래밍 모델을 스프링 MVC에 억지로 집어넣는 대신 많은 것을 스프링 MVC로부터 가져와서 별도의 리액티브 웹 프레임워크를 만들었다.
  - 이것이 스프링 WebFlux
- 스프링 MVC와 스프링 WebFlux 간의 가장 중요한 차이는 빌드에 추가하는 의존성이다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-webflux'
```
- 스프링 MVC 대신 WebFlux를 사용할 때는 기본적인 내장 서버가 톰캣 대신 Netty가 된다.

### 리액티브 컨트롤러 작성하기

```java
// 스프링 MVC
@GetMapping("/recent")
public Iterable<Taco> recentTacos(){
    PageRequest page = PageRequest.of(0,12,Sort.by("createdAt").descending());
    return tacoRepo.findAll(page).getContent();
}

// 스프링 WebFlux
@GetMapping("/recent")
public Flux<Taco> recentTacos(){
    return Flux.fromIterable(tacoRepo.findAll()).take(12);     
    // 리포지터리가 Flux 타입을 반환한다면?
    return tacoRepo.findAll().take(12);
}
```

- 이상적으로는 리액티브 컨트롤러가 리액티브 엔드 to 엔드 스택의 제일 끝에 위치하며, 이 스택에는
컨트롤러, 리퍼지터리, 데이터베이스, 그리고 여타 서비스가 포함된다.
- 스프링 MVC 컨트롤러와 어노테이션이 크게 다르지 않다.
  - 반환 타입만 다르다.
- 리퍼지터리로부터 Flux와 같은 리액티브 타입을 받을 때 subscribe()를 호출할 필요가 없다.
  - 프레임워크가 호출해 준다.

#### 단일 값 반환하기

```java
// 스프링 MVC
@GetMapping("/{id}")
public Taco tacoById(@PathVariable("id") Long id){
    Optional<Taco> optTaco = tacoRepo.findById(id);
    if(optTaco.isPresent()){
        return optTaco.get();
    }
    return null;
}

// 스프링 WebFlux
@GetMapping("/{id}")
public Mono<Taco> tacoById(@PathVariable("id") Long id){
    return tacoRepo.findById(id);
}
```

#### RxJava 타입 사용하기

- 스프링 WebFlux를 사용할 때 Flux나 Mono와 같은 리액티브 타입이 자연스러운 선택이지만, Observable이나 Single과 같은 RxJava
타입을 사용할 수도 있다.

#### 리액티브하게 입력 처리하기

- 스프링 WebFlux를 사용할 때 요청을 처리하는 핸들러 메서드의 입력으로도 Mono나 Flux를 받을 수 있다.
```java
// 스프링 MVC
@PostMapping(consumes="application/json")
@ResponseStatus(HttpStatus.CREATED)
public Taco postTaco(@RequestBody Taco taco){
    return tacoRepo.save(taco);
}

// 스프링 WebFlux
@PostMapping(consumes="application/json")
@ResponseStatus(HttpStatus.CREATED)
public Mono<Taco> postTaco(@RequestBody Mono<Taco> taco){
    return tacoRepo.saveAll(tacoMono).next();
}
```

- MVC의 postTaco()는 Taco 객체를 반환하는 것은 물론이고, 요청 몸체의 콘텐츠와 결합된 Taco 객체를 입력으로 받는다.
  - 요청 페이로드(실제 데이터)가 완전하게 분석되어 Taco 객체를 생성하는 데 사용될 수 있어야 postTaco()가 호출될 수 있다는 것을 의미한다.
  - 리퍼지터리의 save() 메서드의 블로킹되는 호출이 끝나고 복귀되어야 postTaco()가 끝나고 복귀할 수 있다.
  - postTaco()로 진입할 때와 postTaco()의 내부에서 블로킹이 두 번된다.

--------------

## 함수형 요청 핸들러 정의하기

- 스프링 MVC의 애노테이션 기반 프로그래밍 모델은 널리 사용되고 있지만 몇 가지 단점이 있다.
  - 애노테이션이 무엇을 하는지와 어떻게 해야 하는지를 정의하는 데 괴리가 있다.
    - 애노테이션 자체는 무엇을 정의하며, 어떻게는 프레임워크 코드의 어딘가에 정의되어 있다.
    - 프로그래밍 모델을 커스터마이징하거나 확장할 때 복잡해진다.
    - 애노테이션에 중단점(breakpoint)를 설정할 수 없기 때문에 디버깅도 까다롭다.
  - 해당 환경에 익숙치 않으면 무슨 애노테이션이 어떤 일을 하는 지 알 수 없다.
- 스프링 5에는 리액티브 API를 정의하기 위한 새로운 함수형 프로그래밍 모델이 소개되었다.
  - 라이브러리 형태로 사용되므로 애노테이션을 사용하지 않고 요청을 핸들러 코드에 연관시킨다.
- 스프링의 함수형 프로그래밍 모델을 사용한 API의 작성에는 다음 네 가지 기본 타입이 수반된다
  - RequestPredicate
    - 처리될 요청의 종류를 선언한다
  - RouterFunction
    - 일치하는 요청이 어떻게 핸들러에 전달되어야 하는지를 선언한다.
  - ServerRequest
    - HTTP 요청을 나타내며, 헤더와 몸체 정보를 사용할 수 있다.
  - ServerResponse
    - HTTP 응답을 나타내며, 헤더와 몸체 정보를 포함한다.

```java
@Configuration
public class RouterFunctionConfig{
    @Bean
    public RouterFunction<?> helloRouterFunction(){
        return route(GET("/hello"), 
                request -> ok().body(just("Hello World!"), String.class));
    }
}
```
- 이 코드에서 helloRouterFunction() 메서드는 한 종류의 요청만 처리하는 RouterFunction을 반환 타입으로 선언하짐나
다른 종류의 요청을 처리해야 하더라도 또 다른 @Bean 메서드를 작성할 필요 없이 andRoute()를 호출하면 된다.
```java
@Bean
public RouterFunction<?> helloRouterFunction(){
    return route(GET("/hello"), 
            request -> ok().body(just("Hello World!"), String.class))
            .andRoute(GET("/bye"),
            request -> ok().body(just("See ya!"), String.class));
}
```

- 타코 클라우드의 예

```java
@Configuration
public class RouterFunctionConfig {
  @Autowired
  private TacoRepository tacoRepo;

  @Bean
  public RouterFunction<?> routerFunction() {
    return route(GET("/design/taco"), this::recents)
            .andRoute(POST("/design", this::postTaco));
  }

  public Mono<ServerResponse> recents(ServerRequest request) {
    return ServerResponse.ok()
            .body(tacoRepo.findAll().take(12), Taco.class);
  }

  public Mono<ServerResponse> postTaco(ServerRequest request) {
    Mono<Taco> taco = request.bodyToMono(Taco.class);
    Mono<Taco> savedTaco = tacoRepo.save(taco);
    return ServerResponse.created(
            URI.create("http://localhost:8080/design/taco/" + 
                    savedTaco.getId()))
            .body(savedTaco, Taco.class);
  }
}
```

- RouterFunction의 내부 기능이 간단할 때는 람다가 아주 좋다. 그러나 여러 경우에서 해당 기능을 별도의 메서드로 추출하고
메서드 참조로 사용하는 것이 코드 파악에 더 좋다.

----------------

## 리액티브 컨트롤러 테스트하기

- WebTestClient는 스프링 WebFlux를 사용하는 리액티브 컨트롤러의 테스트를 쉽게 작성하게 해주는 새로운 테스트 유틸리티다.

### GET 요청 테스트하기

```java
@Test
public void shouldReturnRecentTacos(){
    // given
    Taco[]tacos={
    testTaco(1L),testTaco(2L),
    testTaco(3L),testTaco(4L),
    testTaco(5L),testTaco(6L),
    testTaco(7L),testTaco(8L),
    testTaco(9L),testTaco(10L),
    testTaco(11L),testTaco(12L),
    testTaco(13L),testTaco(14L),
    testTaco(15L),testTaco(16L),
    }
    Flux<Taco> tacoFlux=Flux.just(tacos);
    
    TacoRepository tacoRepo=Mockito.mock(TacoRepository.class);
    
    given(tacoRepo.findAll()).willReturn(tacoFlux);
    
    // when, then
    WebTestClient testClient=WebTestClient.bindToController(new DesignTacoController(tacoRepo)).build();
    
    testClient.get().uri("/design/recent")
    .exchange()
    .expectStatus().isOk()
    .expectBody()
    ...
}

private Taco testTaco(Long number){
    Taco taco = new Taco();
    taco.setId(UUID.randomUUID());
    taco.setName("Taco " + number);
    List<IngredientUDT> ingredients = new ArrayList<>();
    ingredients.add(new IngredientUDT("IGNA", "Ingredient A", Type.WRAP));
    ingredients.add(new IngredientUDT("INGB","Ingredient B", Type.PROTEIN));
    taco.setIngredients(ingredients);
    return taco;
}
```
- 응답의 JSON 데이터가 많거나 중첩이 심해서 복잡할 경우에는 jsonPath()를 사용하기 번거로울 수 있다.
  - WebTestClient는 json() 메서드를 제공한다.
```java
ClassPathResource recentsResource = new ClassPathResource("/tacos/recent-tacos.json");
String recentsJosn = StreamUtils.copyToString(recentsResource.getInputStream(), Charset.defaultCharset());
testClient.get().uri("/design/recent")
        .accept(MediaTeyp.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .json(recentsJson);
```

### POST 요청 테스트하기

```java
@Test
public void shouldSaveATaco(){
    // given
    Mono<Taco> unsavedTacoMono = Mono.just(testTaco(null));
    Taco savedTaco = testTaco(null);
    savedTaco.setId(1L);
    Mono<Taco> savedTacoMono = Mono.just(savedTaco);
    
    given(tacoRepo.save(any())).willReturn(savedTacoMono);
    
    // when, then
    testClient.post()
        .uri("/design")
        .contentType(MediaType.APPLICATION_JSON)
        .body(unsavedTacoMono, Taco.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Taco.class)
        .isEqualTo(savedTaco);
}
```

-------------

## REST API를 리액티브하게 사용하기

- RestTemplate이 제공하는 모든 메서드는 리액티브가 아닌 도메인 타입이나 컬렉션을 처리하기에 리액티브 방식으로 응답 데이터를
사용하고자 한다면 이것을 Flux나 Mono 타입으로 래핑해야 한다.
- RestTemplate의 대안으로 WebClient를 제공한다.
- WebClient는 외부 API로 요청을 할 때 리액티브 타입의 전송과 수신 모두를 한다.
- WebClient를 사용하는 일반적인 패턴은 다음과 같다
  - WebClient의 인스턴스를 생성한다
  - 요청을 전송할 HTTP 메서드를 지정한다.
  - 요청에 필요한 URI와 헤더를 지정한다.
  - 요청을 제출한다.
  - 응답을 소비한다.

### 리소스 얻기(GET)

```java
Mono<Ingredient> ingredient = WebClient.create()
        .get()
        .uri("http://localhost:8080/ingredients/{id}", ingredientId)
        .retrieve()
        .bodyToMono(Ingredient.class);

ingredient.subscribe(i -> {....})
```
- create() 메서드로 새로운 WebClient 인스턴스를 생성한다.
- get()과 uri()를 사용해서 GET 요청을 정의한다.
- retrieve() 메서드는 해당 요청을 실행한다.
- bodyToMono() 호출에서는 응답 몸체의 페이로드를 Mono로 추출한다.
- bodyToMono()로부터 반환되는 Mono에 추가로 오퍼레이션을 적용하려면 해당 요청이 전송되기 전에 구독을 해야 해서
subscribe() 메서드를 호출한다.
- bodyToFlux()도 있다.

#### 기본 URI로 요청하기

```java
@Bean
public WebClient webClient(){
    return WebClient.create("http://localhost:8080");
}

@Autowired
WebClient webClient;
public Mono<Ingredient> getIngredientById(String ingredientId){
    Mono<Ingredient> ingredient = webClient
        .get()
        .uri("/ingredients/{id}", ingredientId)
        .retrieve()
        .bodyToMono(Ingredient.class);
    ingredient.subscribe(i -> {...})
}
```

#### 오래 실행되는 요청 타임아웃시키기

- 네트워크나 서비스 때문에 클라이언트의 요청이 지체되는 것을 방지하기 위해 Flux나 Mono의 timeout() 메서드를 사요해서 데이터를
기다리는 시간을 제한할 수 있다.

```java
Flux<Ingredient> ingredients = webClient
    .get()
    .uri("/ingredients")
    .retrieve()
    .bodyToFlux(Ingredient.class);
ingredients
        .timeout(Duration.ofSeconds(1))
        .subscribe(i -> {...},
        e->{
            // handle timeout error
        })
```
- 1초보다 오래걸리면 subscribe()의 두 번째 인자로 지정된 에러 핸들러가 호출된다.

### 리소스 전송하기

```java
Mono<Ingredient> ingredientMono = ...;
Mono<Ingredient> result = webClient
        .post()
        .uri("/ingredients")
        .body(ingredientMono, Ingredient.class)
        .retrieve()
        .bodyToMono(Ingredient.class);

result.subscribe(i -> {...})
```

- 만일 전송할 Mono나 Flux가 없는 대신 도메인 객체가 있다면 syncBody()를 사용할 수 있다.

```java
Ingredient ingredient = ...;
Mono<Ingredient> result = webClient
        .post()
        .uri("/ingredients")
        .syncBody(ingredientMono, Ingredient.class)
        .retrieve()
        .bodyToMono(Ingredient.class);

result.subscribe(i -> {...})
```

### 리소스 삭제하기

```java
Mono<Void> result = webClient
        .delete()
        .uri("/ingredients/{id}", ingredientId)
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
```

- 요청을 전송하려면 bodyToMono()에서 Void를 반환하고 subscribe()로 구독해야 한다.

### 에러 처리하기

- 에러를 처리해야 할 때는 onStatus() 메서드를 호출하며, 이때 처리해야 할 HTTP 상태 코드를 지정할 수 있다.
- onStatus()는 두 개의 함수를 인자로 받는다.
  - 처리해야 할 HTTP 상태, 일치시키는 데 사용되는 조건 함수

```java
Mono<Ingredient> ingredientMono = webClient
        .get()
        .uri("/ingredients/{id}", ingredientId)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new UnknownIngredientException()))
        .bodyToMono(Ingredient.class)
```

### 요청 교환하기

- retrieve() 메서드를 사용해서 요청의 전송을 나타냈다.
  - ResponseSpec 타입의 객체를 반환하며, onStatus(), bodyToFlux(), bodyToMono() 같은 메서드를 호출한다.
- 간단한 상황에서는 ResponseSpec을 사용하는 것이 좋으나, 응답의 헤더나 쿠키 값을 사용할 필요가 있을 때는 처리할 수 없다.
  - 대신 exchange()를 호출한다.
- exchange() 메서드는 ClientResponse 타입의 Mono를 반환한다.

```java
Mono<Ingredient> ingredientMono = webClient
        .get()
        .uri("/ingredients/{id}", ingredientId)
        .exchange()
        .flatMap(cr -> {
            if(cr.headers().header("X_UNAVAILABLE").contains("true")){
                return Mono.empty();
            }
            return Mono.just(cr);
        })
        .flatMap(cr -> cr.bodyToMono(Ingredient.class));
```

---------------

## 리액티브 웹 API 보안

- 스프링 시큐리티의 웹 보안 모델은 서블릿 필터를 중심으로 만들어졌다.
  - 요청자가 올바른 권한을 갖고 있는지 확인하기 위해 서블릿 기반 웹 프레임워크의 요청 바운드(클라이언트의 요청을 서블릿이 받기 전에) 가로채야 한다면
  서블릿 필터가 확실한 선택이다.
- 스프링 WebFlux로 웹 애플리케이션을 작성할 때는 서블릿이 개입된다는 보장이 없다.
- 스프링 WebFlux 애플리케이션의 보안에 서블릿 필터를 사용할 수 없는 것은 사실이나 5.0.0 버전부터 스프링 시큐리티는
서블릿 기반의 스프링 MVC와 리액티브 스프링 WebFlux 애플리케이션 모두의 보안에 사용될 수 있다.
  - 스프링 WebFilter가 이 일을 해준다
  - WebFilter는 서블릿 API에 의존하지 않는 스프링 특유의 서블릿 필터 같은 것이다.

### 리액티브 웹 보안 구성하기

```java
// 스프링 MVC
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/design","/orders").hasAuthority("USER")
                .antMatchers("/**").permitAll();
    }
}

// 스프링 WebFlux
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig{
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http.authorizeExchange()
                .pathMatchers("/design","/orders").hasAuthority("USER")
                .anyExchange().permitAll()
                .and()
                .build();
    }
}
```

### 리액티브 사용자 명세 서비스 구성하기

```java
// 스프링 MVC
@Autowired
UserRepository userRepo;

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    auth.userDetailService(new UserDetailService(){
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFountExcpetion{
            User user = userRepo.findByUsername(username)
            if(user == null){
                throw new UsernameNotFoundException(username + " not found");
            }
            return user.toUserDetails();
        }
    })
}

// 스프링 WebFlux
@Service
public ReactiveUserDetailsService userDetailsService(UserRepository userRepo){
    return new ReactiveUserDetailsService(){
        @Override
        public Mono<UserDetails> findByUsername(String username){
            return userRepo.findByUsername(username)
                .map(user -> {
                    return user.toUserDetails();
                });
        }
    }
}
```

-------------

## 요약

- 스프링 WebFlux는 리액티브 웹 프레임워크를 제공한다. 이 프레임워크의 프로그래밍 모델은 스프링 MVC가 많이 반영되었다. 애노테이션도 많이 공유한다.
- 스프링 5는 또한 스프링 WebFlux의 대안으로 함수형 프로그래밍 모델을 제공한다.
- 리액티브 컨트롤러는 WebTestClient를 사용해서 테스트할 수 있다.
- 클라이언트 측에는 스프링 5가 스프링 RestTemplate의 리액티브 버전인 WebClient를 제공한다.
- 스프링 시큐리티 5는 리액티브 보안을 지원하며, 이것의 프로그래밍 모델은 리액티브가 아닌 스프링 MVC 애플리케이션의 것과 크게 다르지 않다.
