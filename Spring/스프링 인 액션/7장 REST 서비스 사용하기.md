# REST 서비스 사용하기

- 스프링 애플리케이션에서 API를 제공하면서 다른 애플리케이션의 API를 요청하는 것은 흔한 일이다.
- 스프링 애플리케이션은 다음과 같은 방법을 사용해서 REST API를 사용할 수 있다.
  - RestTemplate
    - 스프링 프레임워크에서 제공하는 간단하고 동기화된 REST 클라이언트
  - Traverson
    - 스프링 HATEOAS에서 제공하는 하이퍼링크를 인식하는 동기화 REST 클라이언트
  - WebClient
    - 스프링 5에서 소개된 반응형 비동기 REST 클라이언트

---------

## RestTemplate으로 REST 엔드포인트 사용하기

- 저수준의 HTTP 라이브러리로 작업하면서 클라이언트는 클라이언트 인스턴스와 요청 객체를 생성하고 해당 요청을 실행하고
응답을 분석하여 관련 도메인 객체와 연관시켜 처리하는 것은 복잡하다.
- 장황한 코드를 피하기 위해 스프링은 RestTemplate을 제공한다.
- RestTemplate의 메서드는 다음의 세 가지 형태로 오버로딩되어 있다.
  - 가변 인자 리스트에 지정된 URL 매개변수에 URL 문자열을 인자로 받는다
  - Map(String, String)에 지정된 uRL 매개변수에 URL 문자열을 인자로 받는다.
  - java.net.URI를 URL에 대한 인자로 받으며, 매개변수화된 URL은 지원하지 않는다.
- RestTemplate을 사용하려면 우리가 필요한 시점에 RestTemplate 인스턴스를 생성해야 한다.

```java
RestTemplate rest = new RestTemplate();

@Bean
public RestTemplate restTemplate(){
    return new RestTemplate();
}
```

### 리소스 가져오기(GET)

- 만일 해당 API에 HATEOAS가 활성화되지 않았다면 getForObject()를 사용해서 가져올 수 있다.

```java
public Ingredient getIngredientById(String ingredientId){
    return rest.getForObject("http://localhost:8080/ingredients/{id}"),
        Ingredient.class, ingredientId);
}
```

- 변수 매개변수들은 주어진 순서대로 플레이스홀더에 지정된다.
- getForObject()의 두 번째 매개변수는 응답이 바인딩되는 타입으로, 여기서는 JSON 형식인 응답 데이터가 객체로 역직렬화되어 반환된다.
- Map을 사용해서 URL 변수들을 지정할 수 있다.

```java
public Ingredient getIngredientById(String ingredientId){
    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("id",ingredientId);
    return rest.getForObject("http://localhost:8080/ingredients/{id}"),
        Ingredient.class, urlVariables);
}
```

- URI 객체를 구성하여 호출할 수도 있다.

```java
public Ingredient getIngredientById(String ingredientId){
    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("id",ingredientId);
    URI url = UriComponenetsBuilder
        .fromHttpUrl("http://localhost:8080/ingredients/{id}")
        .build(urlVariables);
    return rest.getForObject(url, Ingredient.class);
}

```
- getForObject() 메서드는 리소스로 도메인 객체만 가져와서 응답 결과로 반환한다.
- 클라이언트가 이외에 추가로 필요한 것이 있다면 getForEntity()를 사용할 수 있다.
- getForEntity는 getForObject()와 동일하게 동작하지만, ResponseEntity객체를 반환한다.
- ResponseEntity에는 응답 헤더와 같은 더 상세한 응답 컨텐츠가 포함될 수 있다.

```java
public Ingredient getIngredientById(String ingredientId){
    ResponseEntity<Ingredient> responseEntity = rest.getForEntity("http://localhost:8080/ingredients/{id}",
        Ingredient.class, ingredientId);
    log.info("Fetched time: {}", responseEntity.getHeaders().getDate());
    return responseEntity.getBody();
}
```

### 리소스 쓰기(PUT)

- RestTemplate은 put() 메서드를 제공한다.
- 직렬화된 후 지정된 URL조 전송되는 Object 타입을 인자로 받는다.
  - URL 자체는 URI 객체 혹은 문자열
  - URL 변수들은 가변 인자 리스트나 Map으로 제공될수있음.

```java
public void updateIngredient(Ingredient ingredient){
    rest.put("http://localhost:8080/ingredients/{id}"),
        ingredient,
        ingredient.getId());
}
```

### 리소스 삭제하기(DELETE)

```java
public void deleteIngredient(Ingredient ingredient){
    rest.delete("http://localhost:8080.ingredients/{id}",
        ingredient.getId());
}
```

### 리소스 데이터 추가하기(POST)

- post 요청이 수행된 후 새로 생성된 Ingredient 리소스를 반환받고 싶다면 postForObject()를 사용한다.

```java
public Ingredient createIngredient(Ingredient ingredient){
        return rest.postForObject("http://localhost:8080/ingredients"
        ,ingredient
        ,Ingredient.class);
}
```

------------

## Traverson으로 REST API 사용하기

- Traverson은 스프링 데이터 HATEOAS에 같이 제공되며, 스프링 애플리케이션에서 하이퍼 미디어 API를 사용할 수 있는 솔루션이다.
- Traverson을 사용할 때는 우선 해당 API의 기본 URI를 갖는 객체를 생성해야 한다.

```java
Traverson traverson = new Traverson(
        URI.create("http://localhost:8080/api",MediaTypes.HAL_JSON));
```

- 모든 식자재 리스트를 가져오는 예시

```java
ParameterizedTypeReference<Resources<Ingredient>> ingredientType = 
    new ParameterizedTypeReference<Resources<Ingredient>>() {};

Resources<Ingredient> ingredientRes = traverson.follow("ingredients")
                                            .toObject(ingredientType);

Collection<Ingredient> ingredients = ingredientRes.getContent();
```

- follow() 메서드를 호출하면 리소스 링크의 관계 이름이 ingredients인 리소스로 이동할 수 있다.
- toObject() 메서드의 인자에는 데이터를 읽어 들이는 객체의 타입을 지정해야 한다.
  - Resources 타입의 객체로 읽어 들여야 하는데, 자바에서는 런타임 시에 제네릭 타입의 타입 정보가 소거되어
  리소스 타입을 지정하기 어렵다.
  - 그러나 ParameterizedTypeReference를 생성하면 리소스 타입을 지정할 수 있다.
- Traverson을 사용하면 HATEOAS가 활성화된 API를 이동하면서 해당 API의 리소스를 쉽게 가져올 수 있지만, API에 리소스를 쓰거나
삭제하는 메서드는 제공하지 않는다.
  - RestTemplate은 리소스를 쓰거나 삭제할 수 있지만, API를 이동하는 것은 쉽지 않다.
- API의 이동과 리소스의 변경이나 삭제 모두를 해야 한다면 RestTemplate과 Traverson을 함께 사용해야 한다.

```java
private Ingredient addIngredient(Ingredient ingredient){
    String ingredientsUrl = traverson.follow("ingredients")
        .asLink().getHref();
    
    return rest.postForObject(ingredientsUrl,
        ingredient,
        Ingredient.class);
}
```

-----------------

## 요약

- 클라이언트는 RestTemplate을 사용해서 REST API에 대한 HTTP 요청을 할 수 있다.
- Traverson을 사용하면 클라이언트가 응답에 포함된 하이퍼링크를 사용해서 원하는 API로 이동할 수 있다.