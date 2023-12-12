# OAuth 2: 권한 부여 서버 구현

- 권한 부여 서버의 역할은 사용자를 인증하고 클라이언트에 토큰을 제공하는 것이다.
- 클라이언트는 리소스 서버가 노출하는 리소스에 사용자를 대신해 접근하는 데 이 토큰을 이용한다.
- 이러한 흐름을 그랜트라고 하며 시나리오에 맞게 여러 그랜트 유형 중 하나를 선택할 수 있다.

------------

## 맞춤형 권한 부여 서버 구현 작성

- 권한 부여 서버가 없으면 OAuth 2 흐름도 없다.
- OAuth 2에서 가장 중요한 것은 액세스 토큰을 얻는 것이며 OAuth 2 아키텍처에서 액세스 토큰을 발행하는 구성 요소가 바로
권한 부여 서버다.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigrerAdaper{
    ...
}
```

- 사용자 관리를 구현하고, 하나 이상의 클라이언트를 등록하고, 어떤 그랜트 유형을 지원할지 결정해야 한다.

-------------

## 사용자 관리 정의

- UserDetails, UserDetailsService, UserDetailsManager 계약을 이용해 자격 증명을 관리하면 된다.

```java
@Configuration
public class WebSecurityConfig{
    @Bean
    public UserDetailsService uds(){
        var uds = new InMemoryUserDetailsManager();
        
        var u = User.withUsername("john")
                .password("12345")
                .authorities("read")
                .build();
        
        usds.createUser(u);
        return uds;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

- UserDetailsService로서 InMemoryUserDetailsManager를 선언하고 NoOpPasswordEncoder를 이용한다.
- 사용자가 준비됐으니 사용자 관리를 권한 부여 서버 구성에 연결하기만 하면 된다.

```java
@Bean
public AuthenticaionManager authenticationManagerBean() throws Exception{
    return super.authenticaionManagerBean();
}
```

- 권한 부여 서버에 AuthenticationManager를 등록하도록 AuthServerConfig를 수정한다.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{
    @Autowired
    private AuthenticaionManager authenticaionManager;
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints){
        endpoints.authenticaionManager(authenticaionManager);
    }
}
```

- OAuth 2 아키텍처에서는 사용자가 클라이언트에 이용 권리를 허가했다고 가정하며 클라이언트가 사용자 대신 리소스를 이용한다.

------------

## 권한 부여 서버에 클라이언트 등록

- OAuth 2 아키텍처에서 클라이언트로 작동하는 앱이 권한 부여 서버를 호출하려면 자체 자격 증명이 필요하다.
- 권한 부여 서버에서 클라이언트를 정의하는 일은 ClientDetails 계약이 담당하고, 해당 ID로 ClientDetails를 검색하는 객체를 정의하는 계약은
ClientDetailsService다.
- 인터페이스는 클라이언트를 나타내는 점이 다르지만 UserDetails 및 UserDetailsService 인터페이스와 비슷하게 작동한다.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{
    
    ...
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
        var service = new InMemoryClientDetailsService();
        var cd = new BaseClientDetails();
        cd.setClientId("client");
        cd.setClientSecret("secret");
        cd.setScope(List.of("read"));
        cd.setAuthorizedGrantTypes(List.of("password"));
        
        service.setClientDetailsStore(Map.of("client",cd));
        clients.withClientDetails(service);
    }
}
```

------------

## 암호 그랜트 유형 이용

- /oauth/token 엔드포인트에서 토큰을 요청할 수 있다.
  - 스프링 시큐리티는 이 엔드포인트를 자동으로 구성해준다.
- 전달해야 하는 매개 변수는 다음과 같다.
  - grant_type
    - password 값을 가진다.
  - username 및 password
    - 사용자 자격 증명
  - scope
    - 허가된 권한

```shell
curl -v -XPOST -u client:secret http://localhost:8080/oauth/token?grant_type=password&username=john&password=12345&scope=read
```

- 스프링 시큐리티의 기본 구성에서 토큰은 간단한 UUID다.
- 클라이언트는 리소스 서버가 노출하는 리소스를 이 토큰을 이용해 호출할 수 있다.

------------

## 승인 코드 그랜트 유형 이용

- 다른 그랜트 유형을 이용하려면 클라이언트 등록에 그랜트 유형을 설정하기만 하면 된다.
  - 승인 코드 그랜트 유형의 경우 리디렉션 URI도 제공해야 한다.
  - 리디렉션 URI는 권한 부여 서버가 인증을 마친 사용자를 리디렉션할 URI다.
  - 권한 부여 서버는 리디렉션 URI를 호출할 때 액세스 코드도 제공한다.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{
    ...
    @Override
    public void configure(ClientDetailsServiceConfigrer clients) throws Exception{
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("authorization_code")
                .scope("read")
                .redirectUris("http://localhost:9090/home");
    }
}
```

- 각기 다른 허가를 이용하는 여러 클라이언트가 있을 수 있고, 한 클라이언트를 위한 여러 그랜트 유형을 설정하는 것도 가능하다.
  - 한 클라이언트가 여러 그랜트 유형을 이용하면 보안 관점에서 아키텍처에 잘못된 관행을 이용한다는 의미일 수 있어 주의해야 한다.
- 승인 코드는 한 번만 이용할 수 있다.
- 다른 유효한 승인 코드를 얻으려면 사용자에게 다시 로그인하도록 요청해야 한다.

------------

## 클라이언트 자격 증명 그랜트 유형 이용

- 클라이언트는 엔드포인트를 호출해 연결을 확인하고 사용자에게 연결 상태나 오류 메시지를 표시할 때, 클라이언트와 리소스 서버 간의 거래를 나타낼 뿐
사용자별 리소스와는 관련이 없으므로 사용자 인증 없이도 클라이언트가 호출할 수 있어야 한다.
  - 이러한 시나리오에 클라이언트 자격 증명 그랜트 유형을 이용할 수 있다.
  - 백엔드-대-백엔드 인증에 이용한다.
  - API 키 인증 방법의 대한으로 이 권한 부여 유형을 쓸 수 있다.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{
    ...
    @Override
    public void configure(ClientDetailsServiceConfigrer clients) throws Exception{
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("client_credentials")
                .scope("info");
    }
}
```
- 클라이언트 자격 증명 그랜트 유형에서 조심할 점은 클라이언트가 자체 자격 증명을 이용하는 것이 이 그랜트 유형의 유일한 조건이라는 점이다.
- 사용자 자격 증명이 필요한 흐름과 같은 범위에 대해 접근을 제공하지 않게 해야 한다.

----------------

## 갱신 토큰 그랜트 유형 이용

- 갱신 토큰은 승인 코드 그랜트 유형 및 암호 그랜트 유형과 함께 이용할 수 있다.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{
    ...
    @Override
    public void configure(ClientDetailsServiceConfigrer clients) throws Exception{
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scope("read");
    }
}
```

- 응답에 갱신 토큰이 포함된다.

----------

## 요약

- ClientRegistration 인터페이스는 스프링 시큐리티에서 OAuth 2 클라이언트 등록을 정의한다. ClientRegistrationRepository 인터페이스는
클라이언트 등록 관리를 담당하는 객체를 기술한다. 이 두 계약으로 권한 부여 서버가 클라이언트 등록을 관리하는 방법을 맞춤 구성할 수 있다.
- 스프링 시큐리티로 구현한 권한 부여 서버에서는 클라이언트 등록으로 그랜트 유형이 정해진다. 같은 권한 부여 서버가 다른 클라이언트에 다른 그랜트 유형을 제공할 수 있다.
- 승인 코드 그랜트 유형에서 권한 부여 서버는 사용자에게 로그인할 방법을 제공해야 한다. 이는 승인 코드 흐름에서 사용자가 권한 부여 서버에 직접 인증하여
클라이언트에 액세스 권한을 부여해야 하기 때문이다.
- 하나의 ClientRegistration이 여러 그랜트 유형을 요청할 수 있다.
- 클라이언트 자격 증명 그랜트 유형은 백엔드 대 백엔드 권한 부여에 이용한다.
- 갱신 토큰은 승인 코드 그랜트 유형 및 암호 그랜트 유형과 함께 이용할 수 있다.