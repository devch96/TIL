# CSRF 보호와 CORS 적용

----------

## 애플리케이션에 CSRF(사이트 간 요청 위조) 보호 적용

- HTTP POST로 직접 엔드포인트를 호출할 수 없는 이유는 스프링 시큐리티에 기본적으로 활성화되는 CSRF 보호 때문이다.
- CSRF는 광범위한 공격 유형이며 CSRF에 취약한 애플리케이션은 인증 후 사용자가 웹 애플리케이션에서 원치 않는
작업을 실행하게 할 수 있다.

### 스프링 시큐리티의 CSRF 보호가 작동하는 방식

- 업무 환경에서 웹 툴을 이용해 파일을 저장하고 관리하는 시나리오
  - 어떤 이유로 페이지 하나를 열어보라는 이메일을 받는다.
  - 페이지를 열어봤지만 빈 페이지였으며 알려진 웹사이트로 리디렉션된다.
  - 업무를 시작하려고 했지만 모든 파일이 없어졌다.
  - 사용자는 파일을 관리할 수 있게 애플리케이션에 로그인한 상태다.
  - 파일을 추가, 변경, 삭제할 때 상호 작용하는 웹 페이지는 이러한 작업을 실행하기 위해 서버의 일부 엔드포인트를 호출한다.
  - 이메일의 알 수 없는 링크를 클릭하여 외부 페이지를 열면 이 페이지가 서버를 호출하고 사용자 대신 작업을 실행한다(파일을 지운다)
- CSRF 공격은 사용자가 웹 애플리케이션에 로그인했다고 가정하며 사용자는 공격자에게 속아서 작업 중인 같은 애플리케이션에서 작업을 실행하는
스크립트가 포함된 페이지를 연다.
- CSRF 보호는 웹 애플리케이션에서 프론트엔드만 변경 작업을 수행할 수 있게 보장한다.
- CSRF 보호의 작동 방식
  - 데이터를 변경하는 작업을 수행하려면 먼저 적어도 한 번은 GET으로 웹 페이지를 요청해야 한다.
  - 이때 애플리케이션은 고유한 토큰을 생성한다.
  - 헤더에 이 고유한 값이 들어 있는 요청에 대해서만 변경 작업을 수행한다.
- CSRF 보호의 시작점은 CsrfFilter라는 필터다.
  - 요청을 가로채고 GET, HEAD, TRACE, OPTIONS를 포함하는 HTTP 방식의 요청을 모두 허용하고 다른 모든 요청에는 토큰이 포함된
  헤더가 있는지 확인한다.
- CsrfFilter는 생성된 CSRF 토큰을 HTTP 요청의 _csrf 특성에 추가한다.

```java
public class CsrfTokenLogger implements Filter{
    private Logger logger = Logger.getLogger(CsrfTokenLogger.class.getName());
    
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException{
        Object o = request.getAttribute("_csrf");
        CsrfToken token = (CsrfToken) o;
        
        logger.info("CSRF token " + token.getToken());
        filterChain.doFilter(request, response);
    }
}
```

### 실제 시나리오에서 CSRF 보호 사용

- CSRF 보호는 브라우저에서 실행되는 웹 앱에 이용되며, 앱의 표시된 콘텐츠를 로드하는 브라우저가 변경 작업을 수행할 수 있다고 예상될 때 필요하다.
- CSRF 토큰은 같은 서버가 프론트엔드와 백엔드 모두를 담당하는 단순한 아키텍처에서 잘 작동한다.
- 클라이언트와 클라이언트가 이용하는 백엔드 솔루션이 독립적일 때는 CSRF 토큰이 잘 작동하지 않는다.
  - 모바일 애플리케이션인 클라이언트가 있거나 독립적으로 개발된 웹 프론트엔드가 있을 때
    - 앵귤러, 리액트, Vue.js
- 변경 작업에 HTTP GET을 이용하는 것은 사소한 실수처럼 보일 수 있지만 CSRF 토큰이 필요하지 않다는 점을 명시해야 한다.

### CSRF 보호 맞춤 구성

- 기본적으로 CSRF 보호는 GET, HEAD, TRACE, OPTIONS 외의 HTTP 방식으로 호출되는 엔드포인트의 모든 경로에 적용된다.
  - 일부 애플리케이션 경로에만 비활성화하려면 어떻게 해야 할까?

```java
protected void configure(HttpSecurity http) throws Exception{
    http.csrf(c -> {
        c.ignoringAntMatchers("/cioa");
    });
}
```
- 기본적으로 애플리케이션은 서버 쪽의 HTTP 세션에 CSRF 토큰을 저장한다.
  - 소규모 애플리케이션에 적합하지만 많은 요청을 처리하고 수평적 확장이 필요한 애플리케이션에는 적합하지 않다.
  - HTTP 세션은 상태 저장형이며 애플리케이션의 확장성을 떨어뜨린다.
- HTTP 세션이 아닌 데이터베이스에 저장하도록 토큰을 관리
  - CsrfToken
    - CSRF 토큰 자체를 기술한다
  - CsrfTokenRepository
    - CSRF 토큰을 생성, 저장, 로드하는 객체를 기술한다.
- CsrfToken 계약의 정의
  - 요청에서 CSRF 토큰의 값을 포함하는 헤더의 이름(기본 이름은 X-CSRF-TOKEN)
  - 토큰의 값을 저장하는 요청의 특성 이름(기본 이름은 _csrf)
  - 토큰의 값

```java
public interface CsrfToken extends Serializable{
    String getHeaderName();
    String getParameterName();
    String getToken();
}
```

```java
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String identifier;
    private String token;
}

public interface JpaTokenRepository extends JpaRepository<Toekn, Integer> {
    Optional<Token> findTokenByIdentifier(String identifier);
}

public class CustomCsrfTokenRepository implements CsrfTokenRepository {
    @Autowired
    private JpaTokenRepository jpaTokenRepository;

    @Override
    public CsrfToken generateToken(HttpServletRequest httpServletRequest) {
        String uuid = UUID.randomUUID().toString();
        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", uuid);
    }

    @Override
    public void saveToken(CsrfToken csrfToken,
                          HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) {
        String identifier = httpServletRequest.getHeader("X-IDENTIFIER");
        Optional<Token> existingToken = jpaTokenRepository.findTokenByIdentifier(identifier);
        if(existingToken.isPresent()){ // ID가 존재하면 새로 생성된 값으로 토큰의 값을 업데이트
            Token token = existingToken.get();
            token.setToken(csrfToken.getToken());
        } else{ // ID가 존재하지 않으면 생성된 CSRF 토큰의 값과 ID로 새 레코드 생성
            Token token = new Token();
            token.setToken(csrfToken.getToken());
            token.setIdentifier(identifier);
            jpaTokenRepository.save(token);
        }
    }
    
    @Override
    public CsrfToken loadToken(HttpServletRequest httpServletRequest){
        String identifier = httpServletRequest.getHeader("X-IDENTIFIER");
        
        Optional<Token> existingToken = jpaTokenRepository.findTokenByIdentifier(identifier);
        
        if(existingToken.isPresent()){
            Token token = existingToken.get();
            return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token.getToken());
        }
        return null;
    }
}

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter{
    @Bean
    public CsrfTokenRepository customTokenRepository(){
        return new CustomCsrfTokenRepository();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf(c -> {
            c.csrfTokenRepository(customTokenRepository());
            c.ignoriginAntMatchers("/ciao");
        });
    }
}
```

- CSRF 보호 매커니즘은 애플리케이션이 새 토큰을 생성해야 할 때 generateToken() 메서드를 호출한다.
- saveToken() 메서드는 특정 클라이언트를 위해 생성된 토큰을 저장한다.
- loadToken() 메서드 구현은 토큰 세부 정보가 있으면 이를 로드하고 없으면 null을 반환한다.

-----------------

## CORS(교차 출처 리소스 공유) 이용

- CORS의 필요성은 웹 애플리케이션에서 나온다.
- 기본적으로 브라우저는 사이트가 로드된 도메인 이외의 도메인에 대한 요청을 허용하지 않는다.
  - example.com 에서 사이트를 열었다면 브라우저는 이 사이트에서 api.example.com에 요청하는 것을 허용하지 않는다.
- 브라우저는 CORS 메커니즘으로 엄격한 정책을 완화하고 일부 조건에서 서로 다른 출처 간의 요청을 허용한다.
  - 프론트엔드와 백엔드가 별도의 애플리케이션인 요즘에는 이를 애플리케이션에 적용해야 할 가능성이 크기 때문에 알아야 한다.
    - 앵귤러, 리액트, Vue.js 등의 프레임워크로 개발하고 example.com 도메인에서 호스팅하는 프론트엔드 애플리케이션이 api.example.com 등의
    다른 도메인에서 호스팅하는 백엔드의 엔드포인트를 호출하는 것이 일반적

### CORS 작동 방식

- 애플리케이션이 두 개의 서로 다른 도메인 간에 호출하는 것은 모두 금지된다.
- CORS를 이용하면 애플리케이션이 요청을 허용할 도메인, 그리고 공유할 수 있는 세부 정보를 지정할 수 있다.
- CORS 메커니즘은 HTTP 헤더를 기반으로 작동하며 가장 중요한 헤더는 다음과 같다.
  - Access-Control-Allow-Origin
    - 도메인의 리소스에 접근할 수 있는 외부 도메인(원본)을 지정한다
  - Access-Control-Allow-Methods
    - 다른 도메인에 대해 접근을 허용하지만 특정 HTTP 방식만 허용하고 싶을 때 지정한다.
  - Access-Control-Allow-Headers
    - 특정 요청에 이용할 수 있는 헤더에 제한을 추가한다
- 기본적으로 스프링 부트는 CORS 관련 헤더를 설정하지 않는다.
- CORS는 제한을 가하기보다 교차 도메인 호출의 엄격한 제약 조건을 완화하도록 도와주는 기능이다.
- 제한이 적용돼도 일부 상황에서는 엔드포인트를 호출할 수 있다.
  - 종종 브라우저는 요청을 허용해야 하는지 테스트하기 위해 HTTP OPTIONS 방식으로 호출하는 경우가 있다
    - 사전 요청이라고 한다.
    - 이 요청이 실패하면 브라우저는 원래 요청을 수락하지 않는다.
  - 특정 도메인에 CORS 정책을 지정하지 않았는데도 백엔드에 대한 교차 출처 호출을 볼 때 놀라지 않으려면 사전 요청의 개념을 알아두는 것이 중요하다.
- CORS 메커니즘이 유일하게 보장하는 것은 허용하는 출처 도메인만 브라우저의 특정 페이지에서 요청을 수행할 수 있다는 것이다.

### @CrossOrigin 어노테이션으로 CORS 정책 적용

- @CrossOrigin 어노테이션의 장점은 각 엔드포인트에 맞게 손쉽게 CORS를 구성할 수 있다는 것이다.
  - 컨트롤러 클래스에서 메서드 위에 @CrossOrigin 어노테이션만 추가하면 된다.
- @CrossOrigin의 값 매개 변수는 여러 출처를 정의하는 배열을 받는다.
- @CrossOrigin으로 엔드포인트가 정의되는 위치에서 직접 규칙을 지정하면 규칙이 투명해지는 장점이 있지만 코드가 장황해지고 많은 코드를 반복해야 할 수 있다는 단점과
개발자가 새로 구현한 엔드포인트에 어노테이션을 추가하는 것을 잊어버릴 위험도 있다.

### CorsConfigurer로 CORS 적용

```java
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.cors( c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("example.com","example.org"));
                config.setAllowedMethod(List.of("GET","POST","PUT","DELETE"));
                return config;
            };
            c.configurationSource(source);
        });
    }
}
```
- 실제 애플리케이션에서는 이 코드를 다른 클래스로 나누는 것이 좋다.

-------------

## 요약

- CSRF(사이트 간 요청 위조)는 사용자를 속여 위조 스크립트가 포함된 페이지에 접근하도록 하는 공격 유형이다. 이 스크립트는 애플리케이션에
로그인한 사용자를 가장해 사용자 대신 작업을 실행할 수 있다.
- CSRF 보호는 스프링 시큐리티에서 기본적으로 활성화된다.
- 스프링 시큐리티 아키텍처에서 CSRF 보호 논리의 진입점은 HTTP 필터다.
- CORS(교차 출처 리소스 공유)는 특정 도메인에서 호스팅되는 웹 애플리케이션이 다른 도메인의 콘텐츠에 접근하려고 할 때 발생하며 기본적으로 브라우저는
이러한 접근을 허용하지 않는다. CORS 구성을 이용하면 리소스의 일부를 브라우저에서 실행되는 웹 애플리케이션의 다른 도메인에서 호출할 수 있다.
- CORS를 구성하는 방법에는 어노테이션으로 엔드포인트별로 구성하는 방법과 HttpSecurity 객체의 cors() 메서드로 중앙화된 구성 클래스에서
구성하는 방법이 있다.