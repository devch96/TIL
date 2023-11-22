# REST 서비스 생성하기

- 웹 브라우저가 사라지지 않은 것은 분명하나, 인터넷을 사용하는 주요 수단은 아니다.
- 모바일, 태블릿, 스마트 워치, 등등이 많다.
- 웹 브라우저 기반의 애플리케이션조차도 서버 위주로 실행되기보다는 프로세서가 있는 클라이언트에서 자바스크립트 애플리케이션으로
많이 실행된다.

---------

## REST 컨트롤러 작성하기

### 서버에서 데이터 가져오기

```java
@RestController
@RequestMapping(path = "/design", produces="application/json")
@CrossOrigin(origins="*")
public class DesignTacoController {
    private TacoRepository tacoRepo;

    @Autowired
    EntityLinks entityLinks;

    public DesignTacoController(TacoRepository tacoRepo) {
        this.tacoRepo = tacoRepo;
    }

    @GetMapping("/recent")
    public Iterable<Taco> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepo.findAll(page).getContent();
    }
}
```
- @RestController 어노테이션은 다음 두 가지를 지원한다.
  - 스트레오타입 어노테이션이므로 이 어노테이션이 지정된 클래스를 스프링의 컴포넌트 검색으로 찾을 수 있다.
  - 컨트롤러의 모든 HTTP 요청 처리 메서드에서 HTTP 응답 몸체에 직접 쓰는 값을 반환한다는 것을 스프링에게 알려준다.
    - 반환값이 뷰를 통해 HTML로 변환되지 않고 직접 HTTP 응답으로 브라우저에 전달되어 나타난다.
- @RestController = @Controller + @ResponseBody
- @RequestMapping 어노테이션에 produces 속성은 요청의 Accept 헤더에 application/json이 포함된 요청만을 처리한다는 것을 나타낸다.
- @CrossOrigin 어노테이션은 서버 응답에 CORS 헤더를 포함시켜 다른 도메인에서 실행하는 것을 허용한다.

```java
@GetMapping("/{id}")
public Taco tacoById(@PathVariable("id") Long id){
    Optional<Taco> optTaco = tacoRepo.findById(id);
    if(optTaco.isPresent()){
        return optTaco.get();
    }
    return null;
}
```
- 경로의 {id} 부분이 플레이스홀더이며, @PathVariable에 의해 {id} 플레이스홀더와 대응되는 id 매개변수에 해당 요청의 실제 값이 지정된다.
- null을 반환할 경우, 콘텐츠가 없는데도 정상 처리를 나타내는 HTTP 200(OK) 상태 코드를 클라이언트가 받기 때문에 좋은 방법은 아니다.
```java
@GetMapping("/{id}")
public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id){
    Optional<Taco> optTaco = tacoRepo.findById(id);
    if(optTaco.isPresent()){
    return ResponseEntity.ok(optTaco.get());
    }
    return ResponseEntity.notFound().build();
}
```

### 서버에 데이터 전송하기

```java
@PostMapping(consumes="application/json")
@ResponseStatus(HttpStatus.CREATED)
public taco postTaco(@RequestBody Taco taco){
    return tacoRepo.save(taco);
}
```
- consumes 속성은 Content-type이 application/json과 일치하는 요청만 처리한다.
- @RequestBody는 요청 바디의 JSON 데이터가 Taco 객체로 변환되어 taco 매개변수와 바인딩된다는 것을 나타낸다.

### 서버에 데이터 변경하기

- 데이터를 변경하기 위한 HTTP 메서드로는 PUT과 PATCH가 있다.
- PUT은 데이터를 변경하는 데 사용되기는 하지만, 실제로는 GET과 반대의 의미를 갖는다.
  - GET 요청은 서버로부터 클라이언트로 데이터를 전송하는 반면, PUT 요청은 클라이언트로부터 서버로 데이터를 전송한다.
  - PUT은 데이터 전체를 교체하는 것
- PATCH의 목적은 데이터의 일부분을 변경하는 것이다.
- 특정 주문 데이터의 주소를 변경하는 예시
```java
@PutMapping("/{orderId}")
public Order putOrder(@RequestBody Order order){
    return repo.save(order);
}
```
- PUT은 해당 URL에 이 데이터를 쓰라는 의미이므로 이미 존재하는 해당 데이터 전체를 교체한다.
  - 해당 주문의 속성이 생략되면 null로 변경된다.
- 하지만 스프링 MVC의 어노테이션들은 어떤 종류의 요청을 메서드에서 처리하는지만 나타내며, 해당 요청이 어떻게 처리되는지는 나타내지 않기 때문에 PATCH
가 부분 변경의 의미를 내포한다고 해도 실제로 변경을 수행하는 메서드 코드는 우리가 작성해야 한다.
  - PUT 또한 일부분만 변경할 수 있음.

### 서버에서 데이터 삭제하기

```java
@DeleteMapping("/{orderId}")
@ResponseStatus(code=HttpStatus.NO_CONTENT)
public void deleteOrder(@PathVariable("orderId") Long orderId){
    try{
        repo.deleteById(orderId);
    }catch (EmptyResultDataAccessException e){}
}
```

------------

## 하이퍼미디어 사용하기

- API 클라이언트 코드에서는 흔히 하드코딩된 URL 패턴을 사용하고 문자열로 처리한다.
- 그러나 API의 URL 스킴이 변경되면 어떻게 될까?
- REST API를 구현하는 또 다른 방법으로 HATEOAS(Hypermedia As the Engine Of Application State) 가 있다.
  - API로부터 반환되는 리소스에 해당 리소스와 관련된 하이퍼링크들이 포함된다.
  - 클라이언트가 최소한의 API URL만 알면 반환되는 리소스와 관련하여 처리 가능한 다른 API URL들을 알아내어 사용할 수 있다.
- 스프링 HATEOAS 스타터 의존성을 빌드에 추가해야 한다.
```groovy
implementation 'org.springframework.boot:spring-boot-starter-hateoas'
```
- 도메인 타입 대신 리소스 타입을 반환하도록 컨트롤러를 수정하면 된다.

### 하이퍼링크 추가하기

- 스프링 HATEOAS는 하이퍼링크 리소스를 나타내는 두 개의 기본 타입인 Resource와 Resources를 제공한다.
  - Resource는 단일 리소스
  - Resources는 리소스 컬렉션
- 두 타입이 전달하는 링크는 스프링 MVC 컨트롤러 메서드에서 반환될 때 클라이언트가 받는 JSON에 포함된다.

```java
@GetMapping("/recent")
public Resources<Resource<Taco>> recentTacos() {
    PageRequest page = PageRequest.of(0,12,Sort.by("createdAt").descending());
    List<Taco> tacos = tacoRepo.findAll(page).getContent();
    Resources<Resource<Taco>> recentResources = Resources.wrap(tacos);
    recentResources.add(new Link("http://localhost:8080/design/recent", "recents"));
    return recentResources;
}
```
- 타코 리스트를 반환하지 않고 대신에 Resources.wrap()을 사용해서 타코 리스트를 래핑한다.
- 타코 리스트 전체에 대한 링크만 추가되었고, 타코 리소스 자체나 각 타코의 식자재에 대한 링크는 추가된 것이 없다.
- 일단 URL이 하드코딩 된 것이 좋은 방법이 아니다.
- 스프링 HATEOAS는 링크 빌더를 제공한다.
```java
Resources<Resource<Taco>> recentResources = Resources.wrap(tacos);
recentResources.add(ControllerLinkBuilder.linkTo(DesignTacoController.class)
        .slash("recent")
        .withRel("recents"));
```
- DesignTacoController의 기본 경로가 /design이고, slash 메서드로 설정하였으므로 URL의 경로는 /design/recent가 된다.

### 리소스 어셈블러 생성하기

- 리스트에 포함된 리소스에 대한 링크를 추가해야 하나, 루프를 통해 링크를 추가하는 것은 번거롭다.
- Resources.wrap()에서 리스트의 각 타코를 Resource 객체로 생성하는 대신 새로운 TacoResource 객체로 변환하는 유틸리티 클래스를 정의한다.

```java
public class TacoResource extends ResourceSupport{
    @Getter
    private final String name;
    
    @Getter
    private final Date createdAt;
    
    @Getter
    private final List<Ingredient> ingredients;
    
    public TacoResource(Taco taco){
        this.name = taco.getName();
        this.createdAt = taco.getCreatedAt();
        this.ingredients = taco.getIngredients();
    }
}
```
- 도메인 클래스와 그리 다르지 않으나 id 속성이 없다.
- API 클라이언트 관점에서는 해당 리소스의 self 링크가 리소스 식별자 역할을 한다.
```java
public class TacoResourceAssembler extends ResourceAssemblerSupper<Taco, TacoResource>{
    public TacoResourceAssembler(){
        super(DesignTacoController.class, TacoResource.class);
    }
    
    @Override
    protected TacoResource instantiateResource(Taco taco){
        return new TacoResource(taco);
    }
    
    @Override
    public TacoResource toResource(Taco taco){
        return createResourceWithId(taco.getId(), taco);
    }
}
```
```java
List<TacoResource> tacoResources = new TacoResourceAssembler().toResources(tacos);
Resources<TacoResource> recentResources = new Resources<TacoResource>(tacoResources);
recentResources.add(
        linkTo(methodOn(DesignTacoController.class).recentTacos())
        .withRel("recents"));
```

### embedded 관계 이름 짓기

- TacoResource 클래스의 이름을 다른 것으로 변경한다면 결과 JSON 필드 이름이 그에 맞춰 바뀔 것이고, 변경 전의 이름을 사용하는 클라이언트
코드가 제대로 실행되지 않는다.
- @Relation 어노테이션을 사용하면 자바로 정의된 리소스 타입 클래스 이름과 JSON 필드 이름 간의 결합도를 낮출 수 있다.
```java
@Relation(value="taco", collectionRelation="tacos")
public class TacoResource extends ResourceSupport{
  ...
}
```

-------------

## 데이터 기반 서비스 활성화하기

- 스프링 데이터에는 애플리케이션의 API를 정의하는 데 도움을 줄 수 있는 기능도 있다.
- 스프링 데이터 REST는 스프링 데이터가 생성하는 리퍼지터리의 REST API를 자동 생성한다.
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-rest'
```
- 스프링 데이터 REST가 자동 생성한 API의 기본 경로는 spring.data.rest.base-path 속성에 설정한다.
```yaml
spring:
  data:
    rest:
      base-path: /api
```

### 커스텀 엔드포인트 추가하기

- @RestController 어노테이션이 지정된 빈을 구현하여 엔드포인트에 보충할 수 있지만 다음 두가지를 고려하여 컨트롤러를 작성해야 한다.
  - 스프링 데이터 REST의 기본 경로로 매핑 되지 않는다.
  - 리소스의 하이퍼링크에 자동으로 포함되지 않는다.
- 기본 경로 매핑은 @RepositoryRestController 어노테이션을 사용하면 된다.
- @RepositoryRestController는 반환값을 요청 응답 바디에 자동으로 수록하지 않기에 메서드에 @ResponseBody 어노테이션을 지정해야 한다.

--------

## 요약

- REST 엔드포인트는 스프링 MVC< 그리고 브라우저 지향의 컨트롤러와 동일한 프로그래밍 모델을 따르는 컨트롤러로 생성할 수 있다.
- 모델과 뷰를 거치지 않고 요청 응답 바디에 직접 데이터를 쓰기 위해 컨트롤러의 핸들러 메서드에는 @ResponseBody 어노테이션을 지정할 수 있으며,
ResponseEntity 객체를 반환할 수 있다.
- @RestController 어노테이션을 컨트롤러에 지정하면 해당 컨트롤러의 각 핸들러 메서드에 @ResponseBody를 지정하지 않아도 된다.
- 스프링 HATEOAS는 스프링 MVC에서 반환되는 리소스의 하이퍼링크를 추가할 수 있게 한다.
- 스프링 데이터 리퍼지터리는 스프링 데이터 REST를 사용하는 REST API로 자동 노출될 수 있다.