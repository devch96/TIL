# OAuth 2: JWT와 암호화 서명 사용

- 암호화 서명으로 토큰을 검증하면 권한 부여 서버를 호출하거나 공유된 데이터베이스를 이용하지 않아도
리소스 서버가 토큰을 검증할 수 있다는 이점이 있다.

-------------

## JWT의 대칭 키로 서명된 토큰 이용

- 토큰에 서명하는 가장 직관적인 방법은 대칭 키를 이용하는 것이다.
- 대칭 키로 토큰에 서명하는 방식은 다른 방식보다 간단하며 속도도 더 빠르나 인증 프로세스에 관여하는 모든 애플리케이션에
항상 키를 공유할 수 있는 것은 아닌 단점도 있다.

### JWT 이용

- JWT는 하나의 토큰 구현이다.
- 토큰은 헤더, 본문, 혀명의 세 부분으로 구성된다.
- 헤더와 본문의 세부 정보는 JSON으로 표기되며 Base64로 인코딩된다.
- 서명은 헤더와 본문을 입력으로 이용하는 암호화 알고리즘으로 생성된다.
- 서명된 JWT는 JWS(JSON Web Token Signed)라고 한다.
- 보통 암호화 알고리즘으로 토큰에 서명하기만해도 충분하지만 종종 토큰을 암호화하기도 한다.
  - 토큰이 서명되면 키나 암호가 없이도 그 내용을 볼 수 있다.
  - 해커는 토큰의 내용을 볼 수 있어도 변경할 수는 없다.
  - 내용을 변경하면 서명이 무효가 되기 때문이다.
- 암호화된 토큰을 JWE(JSON WEb Token Encrypted)라고 한다.

### JWT를 발행하는 권한 부여 서버 구현

- JWT로 토큰 검증을 구현하는 방법은 두 가지다.
  - 토큰에 서명하는 일과 서명을 검증하는 일에 같은 키를 이용하면 키가 대칭이라고 말한다.
  - 토큰에 서명하는 일과 서명을 검증하는 일에 각기 다른 키를 이용하면 비대칭 키 쌍을 이용하는 것이다.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Value("${jwt.key}")
    private String jwtKey;
    
    @Autowird
    private AuthenticaionManager authenticaionManager;
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read");
    }
    
    @Override
    public void configre(AuthorizationServerEndpointsConfigurer endpoints){
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter());
    }
    
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        var converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtKey);
        return converter;
    }
}
```