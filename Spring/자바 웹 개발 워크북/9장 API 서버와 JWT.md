# API 서버와 JWT

-------------------------

## JWT 인증

- 화면 없이 Ajax와 JSON을 이용해서 데이터를 주고받는 구조에서는 HttpSession이나 쿠키를 이용하는
기존의 인증 방식에 제한받게 됨.
- 이를 해결하기 위해서 인증받은 사용자들은 특정한 문자열을 이용하게 되는데 이때 많이 사용하는 것이 JWT(Json Web Token)

### API 서버

- API 서버는 쉽게 말해서 필요한 데이터만 제공하는 서버.
- API 서버는 화면을 제공하는 것이 아니라 필요한 데이터를 호출하고 결과를 변환받는 방식으로 동작.
- API 서버에서 가장 눈에 띄는 특징은 화면을 제공하지 않는것
- 브라우저에 필요한 화면의 모든 코드(HTML)를 서버에서 만들어 전송하는 방식을 '서버 사이드 렌더링(Server Side Rendering SSR)'
이라고 하는데 JSP와 Thymeleaf가 이에 해당한다.
- API 서버는 화면 구성은 별도의 클라이언트 프로그램에서 처리하고 서버에서는 순수한 데이터만을 전송한다. 이러한 구성을
클라이언트 사이드 렌더링(Client Side Rendering CSR)이라 한다.
- CSR 방식은 주로 JSON/XML 포맷으로 데이터를 구성하고 호출 방식은 REST 방식을 이용하는 경우가 많다.
- 전통적인 SSR 방식은 쿠키와 세션을 이용해서 서버에서 사용자 정보를 추적할 수 있지만 API 서버는 쿠키를 이용해서 데이터를 교환하는 방식이 아니다.

### 토큰 기반의 인증

- API 서버가 단순히 데이터만을 주고받을 때 외부에서 누구나 호출하는 URI를 알게 되면 문제가 생긴다.
- 토큰은 일종의 표식과 같은 역할을 하는 데이터이다.
- API 서버에서 토큰을 받아 보관하고 호출할 때 자신이 가지고 있는 토큰을 같이 전달해서 API 서버에서 이를 확인하는 방식이다.

#### Access Token / Refresh Token

- Access Token은 입장권과 같지만 악의적인 사용자에게 탈취당한다면 문제가 발생한다.
- 따라서 Access Token을 이용할 때는 최대한 유효 시간을 짧게 지정하고 Access Token을 새로 발급받을 수 있는
Refresh Token이라는 것을 생성해 주어서 필요할 때 다시 Access Token을 발급 받을 수 있도록 구성한다.
- 토큰을 이용하는 방식은 네트워크로 데이터를 주고받기 때문에 항상 보안 문제가 있다.
- Access Token과 마찬가지로 Refresh Token 역시 탈취당하면 문제가 있다.
- 그나마 현실적인 해결책은 Refresh Token으로 새로운 Access Token이 생성되는 것을 원래의 사용자가 알 수 있도록 하는 것이다.
- 새로운 Access Token을 생성할 때 Refresh Token의 유효 시간이 얼마 남지 않았다면 Refresh Token도 같이 생성해서 전달하는 방식이 많이 사용된다.

#### Ajax 호출 확인

- REST 방식으로 동작하기 때문에 별도의 클라이언트에서 API를 호출해서 사용.
- 하지만 혼자서 하기엔 무리가 있으므로 html 파일 하나 생성
- 하지만 html 파일 경로를 요청하면 에러 발생
  - Thymeleaf가 없는 환경에서 스프링 MVC의 모든 경로를 스프링에서 처리하려고 시도하기 때문.
  - 따라서 /files/ 로 시작하는 경로는 스프링 MVC에서 일반 파일 경로로 처리하도록 지정해야 함
```java
@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")
                .addResourceLocations("classpath:/static/");
    }
}
```

### 토큰 인증을 위한 시큐리티 필터

- 스프링 시큐리티는 수많은 필터로 구성되어 있고, 이를 이용해서 컨트롤러에 도달하기 전에 필요한 인증 처리를 진행할 수 있다.

### 인증과 JWT 발행 처리

- 사용자의 아이디와 패스워드를 이용해서 JWT 문자열을 발행하는 기능은 컨트롤러를 이용할 수도 있지만
스프링 시큐리티의 AbstractAuthenticationProcessingFilter 클래스를 이용하면 좀 더 완전한 분리가 가능하다.

#### AbstractAuthenticationProcessingFilter 설정

- AbstractAuthenticationProcessingFilter는 로그인 처리를 담당하기 때문에
다른 필터들과 달리 로그인을 처리하는 경로에 대한 설정과 실제 인증 처리를 담당하는 AuthenticationManager 객체의 설정이
필수로 필요함.
- 이는 SecurityConfig에서 설정.
```java
private final APIUserDetailsService apiUserDetailsService; // UserDetailsService 주입
        
// filterChain        
AuthenticationManagerBuilder authenticationManagerBuilder = 
        http.getSharedObject(AuthenticationManagerBuilder.class);

authenticationManagerBuilder.userDetailsService(apiUserDetailsService)
        .passwordEncoder(passwordEncoder());

AuthenticationManager authenticationManager =
        authenticationManagerBuilder.build();

http.authenticationManager(authenticationManager);

APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
apiLoginFilter.setAuthenticationManager(authenticationManager);

http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);
```
- APILoginFilter는 /generateToken이라는 경로로 지정되었고, 스프링 시큐리티에서 username과 password를 처리하는
UsernamePasswordAuthenticationFilter의 앞쪽으로 동작하도록 설정.

### APILoginFilter의 JSON 처리

#### 인증 정보 JSON 문자열 처리

- JWT 문자열을 얻기 위해서 전송되는 mid, mpw는 JSON 문자열로 전송되므로 HttpServletRequest로 처리하려면
Gson 라이브러리를 활용해서 처리함.
```java
@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
    public APILoginFilter(String defaultFilterProcessUrl){
        super(defaultFilterProcessUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.info("APILoginFilter...................................");
        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("GET METHOD NOT SUPPORT");
            return null;
        }

        Map<String, String> jsonData = parseRequestJSON(request);

        log.info(jsonData);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                jsonData.get("mid"),
                jsonData.get("mpw"));
        
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request){
        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
```

### JWT 문자열의 생성과 검증

- JWT는 인코딩된 문자열이다.
- JWT는 크게 헤더, 페이로드, 서명 부분으로 작성되어 있는데 각 부분은 . 을 이용해서 구분된다.
```java
@Component
@Log4j2
public class JWTUtil {

    @Value("${org.zerock.jwt.secret")
    private String key;

    public String generateToken(Map<String, Object> valueMap, int days){
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        int time = (1) * days;

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;
    }

    public Map<String, Object> validateToken(String token) throws JwtException{
        Map<String, Object> claim = null;

        claim = Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claim;
    }
}
```

### Access Token 발행

- /generateToken 을 POST 방식으로 필요한 정보를 전달하면 APILoginFilter가 동작하고 인증 처리가 된 후에는
APILoginSuccessHandler가 동작하게 됨.
- APILoginSuccessHandler의 내부에는 인증된 사용자에게 토큰을 발행해주어야 함.
```java
@Override
public void onAuthenticationSuccess(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
    log.info("Login Success Handler .................");

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    log.info(authentication);
    log.info(authentication.getName());

    Map<String, Object> claim = Map.of("mid",authentication.getName());
    String accessToken = jwtUtil.generateToken(claim, 1);
    String refreshToken = jwtUtil.generateToken(claim, 30);

    Gson gson = new Gson();

    Map<String, String> keyMap = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    String jsonStr = gson.toJson(keyMap);
    response.getWriter().println(jsonStr);
}
```

### Access Token 검증 필터

- 특정한 경로를 호출할 때 이 토큰들을 검사하고 문제가 없을 때만 접근 가능하도록 구성.
- 스프링 시큐리티가 웹 환경에서 동작할 때는 여러 종류의 필터를 통해서 동작.
- 필터들을 구성하는 일은 기존의 서블릿 기반 필터를 이용할 수도 있지만 스프링 시큐리티는 다른 빈들을 연동해서 동작하는게
가능하다는 장점이 있음.

### TokenCheckFilter의 생성

- TokenCheckFilter는 현재 사용자가 로그인한 사용자인지 체크하는 로그인 체크용 필터와 유사하게 JWT 토큰을 검사하는 역할을 위해서 사용.
- OncePerRequestFilter를 상속해서 구성.
- OncePerRequestFilter는 하나의 요청에 대해서 한번씩 동작하는 필터로 서블릿 API의 필터와 유사.

### TokenCheckFilter 내 토큰 추출

- Access Token이 없는 경우: 토큰이 없다는 메시지 전달 필요
- Access Token이 잘못된 경우: 잘못된 토큰이라는 메시지 전달 필요
- Access Token이 존재하지만 오래된 값인 경우: 토큰을 갱신하라는 메시지 전달 필요

#### Access Token의 추출과 검증

- 토큰 검증 단계에서 가장 먼저 할 일은 브라우저가 전송하는 Access Token을 추출하는 것.
- 일반적으로 HTTP Header 중에 Authorization을 이용해 전달.
- Authorization 헤더는 type + 인증값으로 작성되는데 type 값들은 Basic, Bearer, Digest, HOBA, Mutual등을 이용.
- OAuth나 JWT는 Bearer라는 타입을 이용.
```java
public class AccessTokenException extends RuntimeException{

    TOKEN_ERROR token_error;

    public enum TOKEN_ERROR{
        UNACCEPT(401,"Token is null or too short"),
        BADTYPE(401,"Token type Bearer"),
        MALFORM(403,"Malformed Token"),
        BADSIGN(403,"BadSignatured Token"),
        EXPIRED(403,"Expired Token");

        private int status;
        private String msg;

        TOKEN_ERROR(int status, String msg){
            this.status = status;
            this.msg = msg;
        }

        public int getStatue(){
            return this.status;
        }

        public String getMsg(){
            return this.msg;
        }
    }

    public AccessTokenException(TOKEN_ERROR error){
        super(error.name());
        this.token_error = error;
    }

    public void sendResponseError(HttpServletResponse response) {
        response.setStatus(token_error.getStatue());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg", token_error.getMsg(), "time", new Date()));

        try{
            response.getWriter().println(responseStr);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}

public class TokenCheckFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    String path = request.getRequestURI();
    if (!path.startsWith("/api/")) {
      filterChain.doFilter(request, response);
      return;
    }

    log.info("Token Check Filter.............");
    log.info("JWTUtil: {}", jwtUtil);

    try {
      validateAccessToken(request);
      filterChain.doFilter(request, response);
    } catch (AccessTokenException accessTokenException) {
      accessTokenException.sendResponseError(response);
    }
  }

  private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {
    String headerStr = request.getHeader("Authorization");

    if (headerStr == null || headerStr.length() < 8) {
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
    }

    String tokenType = headerStr.substring(0, 6);
    String tokenStr = headerStr.substring(7);

    if(tokenType.equalsIgnoreCase("Bearer") == false){
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
    }
    try {
      Map<String, Object> values = jwtUtil.validateToken(tokenStr);
      return values;
    } catch (MalformedJwtException malformedJwtException) {
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
    } catch (SignatureException signatureException) {
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
    } catch (ExpiredJwtException expiredJwtException) {
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
    }
  }
}
```

### Refresh Token 처리

- 만료된 토큰이 전송되는 경우에 사용자는 다시 서버에 Access Token을 갱신해 달라고 요구해야 한다.

### RefreshTokenFilter의 생성

```java
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final String refreshPath;

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!path.equals(refreshPath)) {
            log.info("skip refresh token filter.........");
            filterChain.doFilter(request,response);
        }
        log.info("refresh token filter run...........");

        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

        try {
            checkAccessToken(accessToken);
        } catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
            return;
        }

        Map<String, Object> refreshClaims = null;

        try {

            refreshClaims = checkRefreshToken(refreshToken);
            log.info(refreshClaims);

        }catch(RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;
        }

        //Refresh Token의 유효시간이 얼마 남지 않은 경우
        Integer exp = (Integer)refreshClaims.get("exp");

        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);

        Date current = new Date(System.currentTimeMillis());

        //만료 시간과 현재 시간의 간격 계산
        //만일 3일 미만인 경우에는 Refresh Token도 다시 생성
        long gapTime = (expTime.getTime() - current.getTime());

        log.info("-----------------------------------------");
        log.info("current: " + current);
        log.info("expTime: " + expTime);
        log.info("gap: " + gapTime );

        String mid = (String)refreshClaims.get("mid");

        //이상태까지 오면 무조건 AccessToken은 새로 생성
        String accessTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 1);

        String refreshTokenValue = tokens.get("refreshToken");

        //RefrshToken이 3일도 안남았다면..
        if(gapTime < (1000 * 60  * 3  ) ){
            //if(gapTime < (1000 * 60 * 60 * 24 * 3  ) ){
            log.info("new Refresh Token required...  ");
            refreshTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 30);
        }

        log.info("Refresh Token result....................");
        log.info("accessToken: " + accessTokenValue);
        log.info("refreshToken: " + refreshTokenValue);

        sendTokens(accessTokenValue, refreshTokenValue, response);
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {
        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try {
            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException expiredJwtException) {
            log.info("Access Token has expired");
        } catch (Exception e) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String, Object> checkRefreshToken(String refreshToken)throws RefreshTokenException{

        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);

            return values;

        }catch(ExpiredJwtException expiredJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        }catch(Exception exception){
            exception.printStackTrace();
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
        return null;
    }

    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue,
                "refreshToken", refreshTokenValue));

        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### JWT의 한계

- JWT를 이용해서 자원을 보호하는 방식은 태생적으로 문자열이라는 한계가 존재함.
- Refresh Token을 탈취한 상황이라면 얼마든지 새로운 Access Token을 생성할 수 있기 때문에 안전하지 않음.
- Access Token과 Refresh Token을 데이터베이스에 보관하고 토큰을 갱신할 때 데이터베이스의 값과 비교하는 방법을 이용할 수 있음.
- Access Token과 Refresh Token을 탈취한 경우 최소한 1회 이상은 작업이 가능하다는 점에서
모든 보완책이 완벽하진 않음.

### Ajax와 CORS 설정

- API 서버에서는 JSON 데이터만 주고받는 방식이기 때문에 홤녀이 존재하지 않음.
- 리액트, Vue.js 등을 이용하는 SPA(Single Page Application)방식으로 구현해서 물리적으로 분리되어 있는 서버나 프로그램에서
Ajax로 호출함.
- 이처럼 다른 서버에서 Ajax를 호출하면 동일 출처 정책(same-origin policy)를 위반하게되면서 Ajax 호출이 정상적으로 이뤄지지 않음.
- 동일 출처 정책은 웹 브라우저 보안을 위해 프로토콜, 호스트, 포트가 같은 서버로만 ajax 요청을 주고 받을수 있도록 한 정책.
- CORS(Cross Origin Resource Sharing)처리가 필요.

### CORS 처리가 필요한 상태 확인

- 현재의 프로젝트가 실행되는 환경(API 서버)에서 다른 포트로 별도의 서버(웹 서버)를 구성하고
문제가 생기는지 확인.
- Preflight 문제 발생
  - Ajax는 GET/POST/HEAD 방식의 요청을 Simple Request라고 하고, 여기에 서버로 전송하는 Content-Type이
  application/x-www-form-urlencoded, multipart/form-data, text/plain'인 경우 ajax 호출을 허용.
  - 반면 커스텀 헤더를 이용하거나 Content-Type이 다른 경우 Preflight Request라는 것을 실행함.

### CORS 문제 해결

- 브라우저에서 직접 서버를 호출하는 대신에 현재 서버 내 다른 프로그램을 이용해서 API 서버를 호출하는
프록시 패턴을 이용하거나 JSONP와 같이 JSON이 아니라 순수한 JS 파일을 요청하는 방식 등이 있음.
- 가장 권장되는 해결책은 서버에서 CORS 관련 설정으로 해결하는 것.
- CORS 설정은 주로 필터를 이용해서 브라우저의 응답 메시지에 해당 호출이 문제 없었다는 헤더 정보들을 같이 전송하는 방식.
- 스프링 부트는 이러한 상황을 처리하기 위해서 웹 관련 설정을 약간 조정하는 방식을 이용하거나 컨트롤러는 @CrossOrigin 어노테이션을 이용해 처리할 수 있음.

#### CustomSecurityConfig 수정

```java
@Bean
public CorsConfigurationSource corsConfigurationSource(){
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("HEAD","GET","POST","PUT","DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    
    source.registerCorsConfiguration("/**", configuration);
    return source;
}

    http.cors(httpSecurityCorsConfigurer -> {
    httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
    });
```

### JWT와 @PreAuthorize

- JWT 기반의 인증 작업은 일반적인 세션 기반의 인증과 다르기 때문에 스크링 시큐리티에서 사용하는
@PreAuthorize를 이용할 수 없다는 단점이 있다.
- 스프링 시큐리티에서는 SecurityContextHolder라는 객체로 인증과 관련된 정보를 저장해서 컨트롤러 등에서 이를 활용할 수 있는데
이를 이용하면 @PreAuthorize를 이용할 수 있다.

### TokenCheckFilter 수정

```java
Map<String, Object> payload = validateAccessToken(request);
String mid = (String) payload.get("mid");
log.info("mid: {}",mid);
UserDetails userDetails = apiUserDetailsService.loadUserByUsername(mid);

UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

SecurityContextHolder.getContext().setAuthentication(authentication);
filterChain.doFilter(request, response);
```