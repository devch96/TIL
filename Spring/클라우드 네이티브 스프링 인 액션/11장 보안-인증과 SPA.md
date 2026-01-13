# 보안: 인증과 SPA

- 새로운 프로젝트나 기능을 구현할 때 처음부터 보안을 고려해야 함
- 액세스 제어 시스템은 사용자가 식별되고 필요한 권한이 있는 경우에만 리소스에 접근을 허용
- 식별(identification)
  - 사용자가 자신의 신분을 주장할 때 발생
  - 유저명이나 이메일 주소를 제시
- 인증(authentication)
  - 사용자가 주장하는 신분을 여러 가지 요소(암호, 인증서, 토큰) 을 통해 검증하는 일
  - 신원을 확인하기 위해 여러 가지 요소를 사용하면 다중 요소 인증(multi-factor authentication)
- 권한(authorization)
  - 인증 후에 발생
  - 주어진 상황에서 무엇을 할 수 있는지 확인

--------

## 스프링 보안 기초

- 스프링 보안 프레임워크는 주된 기능을 필터를 통해 제공
- 폴라 북숍 시스템에 인증을 추가하고 싶다면 에지 서비스가 진입점인 만큼 보안과 같은 공통 문제를 여기에서 처리하는 게 합리적

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
}
```

- 스프링 보안에서 보안 정책을 정의하고 설정하기 위한 핵심 장소는 SecurityWebFilterChain 빈인데 어떤 필터가 활성화 돼야 하는지 이 빈을 통해 알 수 있음
  - SeverHttpSecurity에서 제공하는 DSL을 통해 생성 가능

```java
@EnableWebFluxSecurity
public class SecurityConfig {
	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http
			.authorizeExchange(exchange ->
				exchange.anyExchange().authenticated())
			.formLogin(Customizer.withDefaults())
			.build();
	}
}
```

- 스프링에 의해 오토와이어되는 ServerHttpSecurity 객체는 스프링 보안 설정과 SecurityWebFilterChain 빈 생성을 위한 편리한 DSL을 제공함
  - authorizeExchange()를 사용하면 모든 요청에 대한 액세스 정책을 사용할 수 있음
    - 리액티브 스프링에서는 요청을 익스체인지라 부름
- 스프링 보안은 HTTP 기본 인증, 로그인 양식, SML 및 오픈ID 커넥트를 비롯한 여러 인증 전략을 지원함
- 스프링 보안 Customizer 인터페이스를 통해 제공되는 기본 설정 제공
  - 프레임워크에서 제공하는 로그인 페이지와 인증이 되지 않는 경우 해당 페이지로의 자동 리다이렉션이 포함되어 있음
- @EnableWebFluxSecurity 애너테이션을 추가해 스프링 보안 웹플럭스를 지원

--------

## 키클록을 통한 사용자 계정 관리

- 사용자 계정을 영구히 저장할 장소와 새로운 사용자를 등록할 수 있는 방안은 강력한 암호화 알고리즘을 사용해 암호를 저장하고 데이터베이스에 대한
무단 액세스를 방지하는데 특히 중점을 두어야함
  - 이런 기능을 전담해서 수행하는 애플리케이션에 위임하는 것이 좋음
- 키클록은 레드햇 커뮤니티에서 개발 및 유지 관리하는 오픈소스로 아이디 및 액세스 관리 솔루션
  - 단일 로그인, 소셜 로그인, 사용자 연합, 다중 요소 인증, 중앙 집중식 사용자 관리를 포함해 여러 가지 광범위한 기능을 제공함
- 키클록은 로컬에서 독립형 자바 애플리케이션이나 컨테이너로 실행할 수 있고, 프로덕션 환경에서는 쿠버네티스에서 솔루션이 몇 가지 있음
- 키클록은 지속성을 위해 관계형 데이터베이스를 필요로 하며 H2와 함께 제공되지만 외부 저장소가 더 적합함

```yaml
version: "3.8"
services:
  polar-keycloak:
    image: quay.io/keycloak/keycloak:19.0
    container_name: "polar-keycloak"
    environment:
      - KEYCLOAK_ADMIN=user
      - KEYCLOAK_ADMIN_PASSWORD=password
    ports:
      - 8080:8080
```

- 사용자 계정 관리를 시작하기 전에 보안 영역(security realm)을 정의해야 함

### 보안 영역 정의

- 키클록은 애플리케이션이나 시스템에서 보안에 관한 모든 사항을 영역(realm)의 맥락에서 정의함
- 영역은 특정 보안 정책을 적용하는 논리적 도메인
- 키클록은 master라는 영역으로 사전 설정되어 있지만 구축하는 애플리케이션에 대한 영역을 별도로 만드는 것이 좋음


```shell
docker exec -it polar-keyclock bash # 도커 컨테이너 내부 접속

cd /opt/keycloak/bin # 키클록 어드민 CLI 스크립트 위치

./kcadm.sh config credentials --server http://localhost:8080 --realm master --user user --password password # 세션 시작

./kcadm.sh create realms -s realm=PolarBookshop -s enabled=true # 새로운 보안 영역 생성
```

### 사용자 및 역할 관리

- 역할 기반 접근 제어
  - 고객과 직원
- 역할을 기반으로 애플리케이션 엔드포인트를 보호할 수 있음

```shell
./kcadm.sh create roles -r PolarBookshop -s name=employee
./kcadm.sh create roles -r PolarBookshop -s name=customer
```

- 고객 생성

```shell
./kcadm.sh create users -r PolarBookshop -s username=isabelle -s firstName=Isabelle -s lastName=Dahl -s enabled=true
./kcadm.sh add-roles -r PolarBookshop --uusername isabelle --rolename employee --rolename customer

./kcadm.sh create users -r PolarBookshop -s username=bjorn -s firstName=Bjorn -s lastName=Vinterberg -s enabled=true
./kcadm.sh add-roles -r PolarBookshop --uusername bjorn --rolename customer

./kcadm.sh set-password -r PolarBookshop --username isabelle --new-password password
./kcadm.sh set-password -r PolarBookshop --username bjorn --new-password password
```

----------

## 오픈ID 커넥트, JWT 및 키클록을 통한 인증

- 사용자는 브라우저에서 유저명과 패스워드를 통해 로그인해야함
  - 내부 저장소를 사용하지 않고 키클록을 통해 사용자 확인을 하도록 업데이트할 수 있음
- 하지만 모바일 애플리케이션이나 IoT 기기와 같은 다른 클라이언트를 도입하면?
- 새로운 요구 사항이 있을 때마다 필요한 모든 인증 전략을 지원할수도 있지만 확장 가능한 접근법이 아님
- 지원할 전략에 따라 ID 공급자가 사용자 인증을 전담하는 것
- 서비스는 실제 인증 단계를 수행하는 데 신경 쓰지 않고 이 전담 서비스를 사용해 사용자의 신원을 확인함
- 전용 서비스를 통해 사용자 인증을 하려면 서비스가 사용자 인증을 ID 공급자에게 위임하고 결과를 받는 데 사용할 프로토콜을 수립해야 하며
ID 공급자가 사용자의 신원을 인증한 후 이를 에지 서비스에게 안전하게 알리기 위한 데이터 형식을 정의해야 함

### 오픈ID 커넥트를 통한 사용자 인증

- 오픈ID 커넥트(OpenID Connect, OIDC)는 애플리케이션(클라이언트)이 신뢰할 수 있는 당사자(인증 서버)에게 인증을 맡기고
그 결과를 기반으로 사용자의 신원을 확인하고 사용자 프로필 정볼르 검색할 수 있는 프로토콜
- 인증 서버는 인증 단계의 결과를 ID 토큰을 통해 클라이언트 애플리케이션에 전달함
- OIDC는 OAuth2 위에 구축도니 ID 계층인데 OAuth2는 권한 부여 프레임워크로 인증 토큰을 통해 액세스 위임 문제를 해결하지만 인증 자체를 다루지 않음
- OIDC 프로토콜이 인증을 처리할 때 사용하는 OAuth2 프레임워크의 세 가지 주요 행위자
  - 인증 서버(authorization server)
    - 사용자 인증 및 토큰 발행을 담당하는 개체
  - 사용자(user)
    - 자원 소유자라고도 하며 애플리케이션에 대한 인증된 액세스 권한을 얻기 위해 인증 서버에 로그인하는 사람
  - 클라이언트(client)
    - 사용자를 인증해야 하는 애플리케이션
    - 모바일 애플리케이션, 브라우저 기반 애플리케이션, 서버 측 애플리케이션 등
- 사용자가 로그인하기 위해 키클록과 직접 상호작용하기 때문에 키클록을 제외한 시스템의 어떤 구성 요소에도 사용자의 크리덴셜이 노출되지 않음
- 아직 인증되지 않은 사용자가 인증이 필요한 엔드포인트를 호출하면 발생하는 일
  - 클라이언트는 인증을 위해 브라우저를 키클록으로 리다이렉트함
  - 키클록은 사용자를 인증하고 인증 코드와 함께 브라우저를 서비스로 다시 리다이렉트함
  - 서비스는 키클록을 호출해 인증 코드와 ID 토큰을 교환하는데 ID 토큰은 인증된 사용자에 대한 정보를 포함함
  - 서비스는 인증된 사용자 세션을 브라우저와 함께 시작하는데 세션 쿠키를 기반으로 함

### JWT를 통한 사용자 정보 교환

- JSON 웹 토큰(JWT)는 두 당사자 사이에서 전송되는 클레임(claim)을 표현하기 위한 업계 표준
  - JWT만 따로 사용하지는 않고 데이터의 무결성을 보장하기 위해 JWT 객체를 디지털 서명한 JSON 웹 서명(JSON Web Signature, JWS)을 사용
    - 디지털 서명된 JWT(JWS)는 세 부분으로 나누어져 있는 문자열인데 각 부분은 베이스64로 인코딩되어 있고 점(.)문자로 구분함
      - header.payload.signature
      - 헤더(header)
        - 페이로드에 대해 수행한 암호화 작업에 대한 정보가 들어 있는 JSON 객체
        - JOSE 헤더라고도 함
        - JOSE(JavaScript Object Signing and Encryption) 프레임워크의 표준을 따름
        ```json lines
        {
          "alg": "HS256", // 토큰을 디지털 서명하는 데 사용된 알고리즘
          "typ": "JWT" // 토큰 유형
        }
        ```
      - 페이로드(payload)
        - 클레임을 포함해 토큰이 전달하려 하는 JSON 객체
        - JWT 사양에는 표준적인 클레임 이름을 정의하지만 임의의 이름을 정의할 수 있음
        ```json lines
        {
          "iss": "https://sso.polarbookshop.com", // JWT를 발행한 개체(발급자)
          "sub": "isabelle", // JWT의 주체인 개체(최종 사용자)
          "exp": 1626439022 // JWT 만료 일시(타임 스탬프)
        }
        ```
      - 서명(signature)
        - JWT의 서명으로 클레임이 전송되는 중간에 변경되지 않았음을 보장함
- 폴라 북숍의 경우 에지 서비스는 인증 단계를 키클록에 위임할 수 있음
- 사용자가 성공적으로 인증되고 나면 키클록은 새로 인증된 사용자의 정보(ID 토큰)를 JWT를 통해 에지 서비스에 보냄
- 에지 서비스는 서명을 통해 JWT의 유효성을 검사하고 사용자에 대한 데이터를 뽑아냄
- 세션 쿠키를 기반으로 사용자 브라우저와 인증 세션을 설정하는데 세션 쿠키는 JWT에 매핑됨

### 키클록에서 애플리케이션 등록

- OAuth2 클라이언트는 인증 서버에게 사용자 인증을 요청하고 궁극적으로 인증 서버로부터 토큰을 받을 수 있는 애플리케이션
  - 엣지 서비스
- OIDC/OAuth2를 사용하려면 클라이언트를 미리 인증 서버에 등록해야 함
- 클라이언트는 공개 또는 기밀로 해야함
  - 시크릿을 유지할 수 없다면 공개 클라이언트로 등록
    - 모바일 애플리케이션
  - 백앤드 애플리케이션은 보통 기밀 클라이언트로 등록
- 기밀 클라이언트는 공유 비밀을 사용하는 것과 같은 방식으로 인증 서버에 자신을 인증해야 함

```shell
docker exec -it polar-keycloak bash
cd /opt/keycloak/bin

./kcadm.sh config credentials --server http://localhost:8080 \ 
--realm master --user user --password password

./kcadm.sh create clients -r PolarBookshop -s clientId=edge-service \
-s enabled=true \
-s publicClient=false \
-s secret=polar-keyclock-secret \
-s 'redirectUris=["http://localhost:9000", "http://localhost:9000/login/oauth2/code/*"]'
```

- 유효한 리다이렉션 URL은 OAuth2 클라이언트 애플리케이션(에지 서비스)이 노출하는 엔드포인트로 키클록이 인증 요청을 리다이렉션할 URL
- 키클록은 리다이렉션 요청 시 중요하고 개인적인 정보를 포함할 수 있기 때문에 어떤 애플리케이션과 엔드포인트가 그런 정보를 받을 수 있을 지 제한해야 함

-----------

## 스프링 보안 및 오픈ID 커넥트로 사용자 인증

### 스프링 보안과 키클록의 통합 설정

- 스프링 보안과 관련한 의존성을 추가한 후 키클록과의 통합을 설정해야 함

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak: # 스프링 보안에서 클라이언트 등록을 식별하는 이름(어떤 문자열이라도 괜찮)
            client-id: edge-service # 키클록에 정의된 OAuth2 클라이언트 식별자
            client-secret: polar-keycloak-secret # 클라이언트가 키클록과 인증하기 위해 사용하는 공유 시크릿
            scope: openid # 클라이언트가 접근 권한을 갖기를 원하는 영역의 목록
        provider:
          keycloak:
            issuer-uri: http://localhost:8080/realms/PolarBookshop
```

- 스프링 보안의 클라이언트 등록 설정에는 식별자가 반드시 있어야 함
- 등록 실별자는 스프링 보안이 키클록에서 인증 코드를 받기 위한 URL에 사용됨
  - 기본 URL 템플릿은 /login/oauth2/code/{registrationId}

### 기초적인 스프링 보안 설정

- 스프링 보안에서 보안 정책을 정의하고 설정하는 장소는 SecurityWebFilterChain 클래스
- ServerHttpSecurity 객체는 스프링 보안에서 OAuth2 클라이언트 설정을 위해 oauth2Login()와 oauth2Client() 두 가지 방법을 제공
- oauth2Login()을 사용하면 애플리케이션이 OAuth2 클라이언트 역할을 할 수 있도록 설정할 수 있으며 오픈ID 커넥트를 통해 사용자를 인증할 수 있음
- oauth2Client()를 사용하면 애플리케이션은 사용자를 인증하지 않고 대신 인증 메커니즘 정의를 사용자에게 맡김

```java
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http
			.authorizeExchange(exchange ->
				exchange.anyExchange().authenticated())
			.oauth2Login(Customizer.withDefaults())
			.build();
	}
}
```

### 인증된 사용자 콘텍스트 검사

- 스프링 보안은 인증 프로세스의 일환으로 사용자에 대한 정보를 유지하기 위한 콘텍스트를 정의하고 이것을 통해 사용자 세션을 ID 토큰에 매핑함

```java
public record User(
	String username,
	String firstName,
	String lastName,
	List<String> roles
) { }
```

- 채택된 인증 전략(사용자 이름/암호, 오픈ID 커넥트/OAuth2, SAML2)이 무엇이든 스프링 보안은 인증된 사용자(principal)에 대한 정보를
Authentication을 구현하는 객체의 한 필드에 갖음
  - OIDC의 경우 프린시플 객체는 OidcUser 인터페이스 유형
  - Authentication은 SeuciryContext 객체의 한 필드에 저장됨
- 로그인한 사용자의 인증 객체에 액세스하는 한 가지 방법은 ReactiveSecurityContextHolder 로 부터 검색해서 가져온 SecurityContext에서 해당 객체를 추출하는 것

```java
@RestController
public class UserController {
	@GetMapping("/user")
	public Mono<User> getUser() {
		return ReactiveSecurityContextHolder.getContext()
			.map(SecurityContext::getAuthentication)
			.map(authentication -> (OidcUser) authentication.getPrincipal())
			.map(oidcUser -> 
				new User(oidcUser.getPreferredUsername(),
					oidcUser.getGivenName(),
					oidcUser.getFamilyName(),
					List.of("employee", "customer")));
	}
}
```

- ReactiveSecurityContextHolder를 직접 사용하는 방법 외에도 스프링 웹 MVC와 웹플럭스에서는 @CurrentSecurityContext와 
@AuthenticationPrincipal 애너테이션을 통해 SecurityContext와 프린시플을 주입할 수도 있음

```java
@RestController
public class UserController {
	@GetMapping("/user")
	public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
		var user = new User(
			oidcUser.getPreferredUsername(),
			oidcUser.getGivenName(),
			oidcUser.getFamilyName(),
			List.of("employee", "customer")
		);
		return Mono.just(user);
	}
}
```

- 사용자의 유저명과 패스워드를 성공적으로 확인하면 키클록은 에지 서비스를 다시 호출하고 새로 인증된 사용자의 ID 토큰을 보냄
- 에지 서비스는 토큰을 저장하고 세션 쿠키와 함께 필요한 엔드포인트로 브라우저를 리다이렉션함
- 브라우저와 에지 서비스 간의 모든 통신에서는 해당 세션 쿠키를 통해 사용자의 인증 컨텍스트를 식별
  - 토큰은 브라우저에 노출되지 않음
- ID 토큰은 OidcUser에 담겨져 있는데 OidcUser는 Authentication 객체의 일부이고 SecurityContext에 포함되어 있음
- 에지 서비스는 스프링 세션 프로젝트를 사용해 세션 데이터를 외부 데이터 서비스(레디스)에 저장했는데, 이로 인해 애플리케이션은 상태를 갖지 않고
확장할 수 있음
  - SecurityContext 객체는 세션 데이터에 포함되기에 자동으로 레디스에 저장됨
- 인증된 사용자(프린시플)를 가져오는 다른 방법은 특정 HTTP 요청과 관련된 콘텍스트(리액티브 용어로 익스체인지)를 사용하는 것
  - 사용률 제한에 대한 설정을 수정할 수 있음
- 스프링 클라우드 게이트웨이와 레디스를 활용해 사용률 제한을 구현했는데 현재 사용률 제한은 초 당 수신된 총 요청 수를 기준으로 계산되지만
각 사용자마다 다르게 적용할 수 있음

```java
@Configuration
public class RateLimiterConfig {
	@Bean
	public KeyResolver keyResolver() {
		return exchange -> exchange.getPrincipal()
			.map(Principal::getName)
			.defaultIfEmpty("anonymous");
	}
}
```

### 스프링 보안 및 키클록에서 사용자 로그아웃 설정

- 사용자가 로그아웃하면 스프링 보안은 사용자와 관련된 모든 세션 데이터를 삭제함
  - 키클록에는 사용자에 대한 세션을 가지고 있음
- 키클록 및 에지 서비스가 인증 프로세스에 관여하고 있는 것과 같은 방식으로 사용자를 완전히 로그아웃하려면 로그아웃 요청 역시
두 구성 요소에 전달되어야 함
- 스프링 보안은 오픈ID 커넥트 RP-주도 로그아웃 사양을 구현하는데 이 사양은 신뢰 당사자인 OAuth2 클라이언트에서 인증 서버로 로그아웃 요청을
전파하는 방법을 정의함
- 스프링 보안은 기본 설정상 프레임워크가 구현하고 노출하는 /logout 엔드포인트에 POST 요청을 보내 로그아웃할 수 있도록 지원
- RP 주도 로그아웃 기능이 활성화되어 있다면 사용자 로그아웃을 스프링 보안이 처리한 후에 에지 서비스는 브라우저를 통해(리다이렉션 사용) 키클록에 로그아웃 요청을
보냄
- 인증 서버는 로그아웃 작업을 수행한 다음 사용자를 다시 애플리케이션으로 리다이렉션할 수 있음
- OidcClientInitiatedServerLogoutSuccessHandler 클래스의 setPOstLogoutRedirectUri() 메서드를 사용하면 로그아웃 후 사용자를 어디로 리다이렉션
하는지 설정할 수 있음
  - 플레이스 홀더를 사용할 수 있음
- 키클록에서 클라이언트를 설정할 때는 정확한 URL이 필요

```java
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
		ReactiveClientRegistrationRepository clientRegistrationRepository) {
		return http
			.authorizeExchange(exchange ->
				exchange.anyExchange().authenticated())
			.oauth2Login(Customizer.withDefaults())
			.logout(logout -> logout.logoutSuccessHandler(
				oidcLogoutSuccessHandler(clientRegistrationRepository)))
			.build();
	}

	private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(
		ReactiveClientRegistrationRepository clientRegistrationRepository
	) {
		var oidcLogoutSuccessHandler =
			new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
		return oidcLogoutSuccessHandler;
	}
}
```

-----------

## 스프링 보안과 SPA 통합

- 마이크로서비스 아키텍처와 기타 분산 시스템에서 웹 프런트엔드는 앵귤러, 리액트, 뷰 같은 프레임워크를 사용해 단일 페이지 애플리케이션으로 구축됨
- SPA를 지원하려면 교차 출처 리소스 공유 (CORS)와 사이트 간 요청 위조(CSRF) 문제를 다뤄야 하기 때문에 스프링 보안에서 몇 가지 설정을 추가해야 함

### 앵귤러 애플리케이션 실행

- SPA에 자체 개발, 빌드 및 릴리스 도구가 있으므로 전용 폴더를 사용하면 유지 보수를 더 간결하고 쉽게 할 수 있음
- SPA의 정적 리소스를 빌드 시간에 처리하고 최종 릴리스에 포함하도록 스프링 부트를 설정할 수 있음
- 아니면 전용 서비스가 앵귤러의 정적 리소스를 처리하도록 할 수 있음

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: spa-route
          uri: ${SPA_URL:http://localhost:9004}
          predicates:
            - Path=/,/*.css,/*.js,/favicon.ico
```

- 인증되지 않은 요청을 받으면 스프링 보안에 의해 인증 흐름이 시작되지만 CORS 문제로 인해 AJAX 요청은 작동하지 않음
- 스프링 보안에 의해 활성화된 CSRF 보호로 인해 POST 요청(로그아웃)이 실패함

### 인증 흐름 제어

- 프런트앤드가 서버 렌더링된 페이지로 이루어져 있으면 잘 작동하고 추가 설정도 필요 없음
- 단일 페이지 애플리케이션은 다르게 작동
- 브라우저가 수행하는 표준 HTTP GET 요청을 통해 루트 엔드포인트에 엑세스할 때 앵귤러 애플리케이션이 백엔드에 의해 반환됨
- 첫 번째 단계가 끝난 후 SPA는 AJAX 요청을 통해 백엔드와 상호작용함
- SPA가 인증되지 않은 AJAX 요청을 인증이 필요한 엔드포인트로 보낼 때 스프링 보안이 HTTP 302 코드로 응답하지 하고 키클록으로 리다이렉션하도록 해선 안됨
  - 401 오류 상태의 응답을 반환해야 함
- SPA에서 리다이렉션을 사용하지 않는 주된 이유는 교차 출처 리소스 공유(CORS) 문제 때문
- 두 개의 URL이 동일한 출처(같은 프로토콜, 도메인, 포트)를 가지고 있지 않기에 통신이 차단되는 건 모든 웹 브라우저가 실행하는 표준적인 출처 정책
- CORS는 서버가 SPA와 같은 브라우저 기반 클라이언트에 대해 그 둘의 출처가 다르더라도 AJAX를 통해 HTTP 호출을 허용하는 매커니즘

```java
@Bean
SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
    ReactiveClientRegistrationRepository clientRegistrationRepository) {
    return http
        .authorizeExchange(exchange ->
            exchange.anyExchange().authenticated())
        .exceptionHandling(exceptionHandling -> 
            exceptionHandling.authenticationEntryPoint(
                new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout.logoutSuccessHandler(
            oidcLogoutSuccessHandler(clientRegistrationRepository)))
        .build();
}
```

- SPA가 인증 흐름을 시작하는 역할을 하기 때문에 정적 리소스에 대해서는 인증되지 않은 액세스를 허용해야 함
- 카탈로그에서 책을 검색하는 것 역시 인증 없이도 가능하도록함

```java
@Bean
SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
    ReactiveClientRegistrationRepository clientRegistrationRepository) {
    return http
        .authorizeExchange(exchange ->
            exchange.pathMatchers("/", "/*.css", "/*.js", "/favicon.ico").permitAll()
                .pathMatchers(HttpMethod.GET, "/books/**").permitAll()
                .anyExchange().authenticated())
        .exceptionHandling(exceptionHandling ->
            exceptionHandling.authenticationEntryPoint(
                new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout.logoutSuccessHandler(
            oidcLogoutSuccessHandler(clientRegistrationRepository)))
        .build();
}
```

### 사이트 간 요청 위조 방지

- 세션 쿠키는 사이트 간 요청 위조(CSRF) 공격에 취약하기 때문에 요청을 검증하기에는 충분하지 않음
- 스프링 보안은 CSRF 토큰에 기반해 보호 방안을 제공
- 세션이 시작될 때 프레임워크는 CSRF 토큰을 생성하고 이것을 클라이언트에 제공하면서 모든 상태 변경 요청에 이 토큰을 보낼 것을 요구함
- 스프링 보안이 ServerHttpSecurity와 CookieServerCsrfTokenRepository 클래스의 csrf() DSL을 통해 쿠키로 CSRF 토큰을
제공하도록 설정할 수 있으나 리액티브 애플리케이션의 경우 CsrfToken 값이 실제로 확실하게 제공되도록 하려면 추가적인 단계가 필요함

```java
@Bean
SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                 ReactiveClientRegistrationRepository clientRegistrationRepository) {
  return http
          .authorizeExchange(exchange ->
                  exchange.pathMatchers("/", "/*.css", "/*.js", "/favicon.ico").permitAll()
                          .pathMatchers(HttpMethod.GET, "/books/**").permitAll()
                          .anyExchange().authenticated())
          .exceptionHandling(exceptionHandling ->
                  exceptionHandling.authenticationEntryPoint(
                          new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
          .oauth2Login(Customizer.withDefaults())
          .logout(logout -> logout.logoutSuccessHandler(
                  oidcLogoutSuccessHandler(clientRegistrationRepository)))
          .csrf(csrf -> csrf.csrfTokenRepository(
                  CookieServerCsrfTokenRepository.withHttpOnlyFalse()))
          .build();
}

@Bean
WebFilter csrfWebFilter() {
    return (exchange, chain) -> {
        exchange.getResponse().beforeCommit(() -> Mono.defer(() -> {
            Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());
            return csrfToken != null ? csrfToken.then() : Mono.empty();
        }));
        return chain.filter(exchange);
    };
}
```

-------

## 스프링 보안 및 오픈ID 커넥트 테스트

### OIDC 인증 테스트

```java
@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTests {
	@Autowired
	WebTestClient webClient;

	@MockBean
	ReactiveClientRegistrationRepository clientRegistrationRepository;

	@Test
	void whenNotAuthenticatedThen401() {
		webClient.get()
			.uri("/user")
			.exchange()
			.expectStatus().isUnauthorized();
	}

	@Test
	void whenAuthenticatedThenReturnUser() {
		var expectedUser = new User("jon.snow", "Jon", "Snow", List.of("employee", "customer"));

		webClient.mutateWith(configureMockOidcLogin(expectedUser))
			.get()
			.uri("/user")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(User.class)
			.value(user -> assertThat(user).isEqualTo(expectedUser));
	}

	private SecurityMockServerConfigurers.OidcLoginMutator configureMockOidcLogin(User expectedUser) {
		return SecurityMockServerConfigurers.mockOidcLogin().idToken(
			builder -> {
				builder.claim(StandardClaimNames.PREFERRED_USERNAME, expectedUser.username());
				builder.claim(StandardClaimNames.GIVEN_NAME, expectedUser.firstName());
				builder.claim(StandardClaimNames.FAMILY_NAME, expectedUser.lastName());
			}
		);
	}
}
```

### CSRF 테스트

```java
@WebFluxTest
@Import(SecurityConfig.class)
class SecurityConfigTests {
  @Autowired
  WebTestClient webClient;

  @MockBean
  ReactiveClientRegistrationRepository clientRegistrationRepository;

  @Test
  void whenLogoutAuthenticatedAndWithCsrfTokenThen302() {
    when(clientRegistrationRepository.findByRegistrationId("test"))
            .thenReturn(Mono.just(testClientRegistration()));

    webClient
            .mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
            .mutateWith(SecurityMockServerConfigurers.csrf())
            .post()
            .uri("/logout")
            .exchange()
            .expectStatus().isFound();
  }

  private ClientRegistration testClientRegistration() {
    return ClientRegistration.withRegistrationId("test")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .clientId("test")
            .authorizationUri("https://sso.polarbookshop.com/auth")
            .tokenUri("https://sso.polarbookshop.com/token")
            .redirectUri("https://polarbookshop.com").build();
  }
}
```