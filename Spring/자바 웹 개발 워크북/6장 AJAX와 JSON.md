# AJAX와 JSON

---------------------

## REST 방식의 서비스

- 브라우저만을 이용했던 과거의 웹과는 달리 최근의 웹은 모바일 환경에서 중요한 역할을 함.
- 모바일에서 웹으로 데이터를 받기 시작하면서 HTML을 구성하는 기존의 서버 사이드 프로그래밍은
순수한 데이터만 제공하고 나머지는 클라이언트 쪽에서 처리하는 방식으로 변화.

### Ajax와 REST 방식의 이해

- Ajax(Asynchronous JavaScript And XML)의 등장은 웹 개발 방식의 획기적인 변화를 가져왔다.
- Ajax방식은 브라우저에서 서버를 호출하지만 모든 작업이 브라우저 내부에서 이루어지기 때문에 현재
브라우저의 브라우저 화면의 변화 없이 서버와 통신할 수 있다.
- Ajax의 약자에 JavaScript와 XML이라는 단어가 들어가는 것처럼 실제 구현은 자바스크립트를 이용해서 XML
을 주고받는 방식을 이요했지만 최근에는 JSON을 이용하는 방식을 더 선호함.

### 클라이언트 중심의 개발

- 모바일에서도 Ajax 방식으로 데이터를 교환할 수 있다는 점 때문에 환영받음.
- 다양한 라이브러리나 프레임워크가 발전하면서 웹 개발은 클라이언트와 서버의 역할 분배가 이루어짐.

### JSON 문자열

- `서버에서 순수한 데이터를 보내고 클라이언트가 적극적으로 이를 처리한다`라는 개발방식의 핵심은 문자열.
- 문자열은 어떠한 프로그래밍 언어나 기술에 종속되지 않는다는 장점이 있음.
- 문자열을 이용하면 데이터를 주고받는 것에 신경쓸 필요가 없지만, 문자열로 복잡한 구조의 데이터를 표현하는 문제가 발생함.
- 이러한 문제로 고려되는 것이 XML과 JSON이라는 포맷
- JSON은 단순한 문자열이지만 자바스크립트에서 객체를 표현할 때 쓰는 방식으로 표현.

### REST 방식

- REST 방식은 클라이언트 프로그램인 브라우저나 앱이 서버와 데이터를 어떻게 주고받는 것이 좋을지에 대한
가이드.
- 이전의 웹 개발 방식에서는 특정한 URL이 원하는 행위나 작업을 의미하고, GET/POST등은 데이터를 전송하는 위치를 의미.
- Ajax를 이용하면 브라우저의 주소가 이동할 필요 없이 서버와 데이터를 교환할 수 있기 때문에 URL은 행위나 작업이 아닌 원하는 대상 그 자체를 의미하고
GET/POST 방식과 PUT/DELETE 등의 추가적인 전송방식을 활용해서 행위나 작업을 의미하게 되었음.
- REST 방식이라고 하는 것은 GET/POST 등으로 어떤 일을 수행하고 싶은가를 표현 하는 것.
- GET 방식은 조회의 용도, POST는 등록과 같이 전송 방식을 통해 작업을 결정하고 URL은 특정한 자원을 의미.
- ? 를 이용하는 쿼리 스트링으로 햇당 상품의 번호를 전송하는 방식과 달리 최근에는 직접 주소의 일부로 사용하는 방식도
REST 방식의 표현법
- REST 방식에서 URL 하나는 하나의 자원을 식별할 수 있는 고유한 값이고, GET/POST 등은 이에 대한 작업을 의미.

### Controller

- @Controller 어노테이션이 아닌 @RestController 사용.
- @RestController를 이용하게되면 메서드의 모든 리턴 값은 JSP나 Thymeleaf로 전송되는게 아니라
JSON이나 XML등으로 처리 됨.
```java
@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Map<String, Long>> register(@RequestBody ReplyDTO replyDTO){
    Map<String, Long> resultMap = Map.of("rno", 111L);
    
    return ResponseEntity.ok(resultMap);
}
```
- @RequestBody는 JSON 문자열을 DTO로 변환하기 위해서 표시.
- @PostMapping의 consumes 속성은 해당 메서드를 받아서 소비하는 데이터가 어떤 종류인지 명시할 수 있음.
  - JSON 타입의 데이터를 처리하는 메서드임을 명시
- @RestController은 리턴 값 자체가 JSON으로 처리되는데 ResponseEntity 타입을 이용하면 상태 코드를 전송할 수 있음.

### @Valid와 @RestControllerAdvice

- REST 방식의 컨트롤러는 대부분 Ajax와 같이 눈에 보이지 않는 방식으로 서버를 호출하고 결과를 전송하므로
에러가 발생하면 어디에서 났는지 보기 힘듬.
- 따라서 @RestControllerAdvice를 설계하는 것이 좋음.
```java
@RestController
@Log4j2
public class CustomRestAdvice {
    
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleBindException(BindException e){
        log.error(e);

        Map<String, String> errorMap = new HashMap<>();
        
        if(e.hasErrors()){
            BindingResult bindingResult = e.getBindingResult();
            
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }
        return ResponseEntity.badRequest().body(errorMap);
    }
}
```
- @RestControllerAdvice를 이용하면 컨트롤러에서 발생하는 예외에 대해 JSON과 같은
순수한 응답 메시지를 생성해서 보낼 수 가 있음.

----------------

## 다대일 연관관계 실습

### 연관관계를 결정하는 방법

- 데이터베이스에서는 PK와 FK를 이용해서 엔티티 간의 관계를 표현.
- JPA는 연관 관계의 기준은 항상 변화가 많은 쪽을 기준으로 결정, ERD의 FK를 기준으로 결정.

### 변화가 많은 쪽을 기준

- 조금 더 자주 변화가 있는 쪽
  - 회원과 게시글일 경우, 회원들의 활동을 통해서 여러 개의 게시물이 만들어지므로 연관관계의 핵심은 게시물

#### ERD 상의 FK를 기준

- 연관 관계의 주어를 결정하기 애매할 때는 ERD를 그려서 확인하는 것이 확실.

### 단방향과 양방향

- 데이터베이스에는 특정한 PK를 다른 테이블에서 FK로 참조해서 사용하지만, 객체지향에서는 참조의 방향이 나눠짐.
- 한쪽만 참조를 유지하는 방식을 단방향, 양쪽 모두 참조하는 방식을 양방향
  - 양방향
    - 양쪽 객체 모두 서로 참조를 유지하기 때문에 모든 관리는 양쪽 객체에 동일하게 적용해야 하는
    불편함이 있지만 JPA에서 필요한 데이터를 탐색하는 작업에서는 편리함을 제공.
  - 단방향
    - 구현이 단순하고 에러 발생의 여지를 많이 줄일 수 있지만, 데이터베이스 상에서 조인 처리와 같이
    다른 엔티티 객체의 내용을 사용하는 데 더 어려움.
- 연관 관계를 구성할 때 다음과 같은 점을 주의
  - @ToString을 할 때 참조하는 객체를 사용하지 않도록 exclude 속성값 지정
  - @ManyToOne과 같이 연관 관계를 나타낼 때는 fetch 속성을 LAZY로 지정
```java
@ToString(exclude = "board")
public class Reply extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
```

#### 특정 게시물의 댓글 조회와 인덱스

- 댓글이 사용되는 방식은 주로 게시물 번호를 통해서 사용되는 경우가 많음.
- 쿼리 조건으로 자주 사용되는 컬럼에는 인덱스를 생성해 두는 것이 좋은데 @Table 어노테이션에 추가적인 설정을 이용해서
인덱스를 지정할 수 있음.
```java
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
```

### FetchType.LAZY

- LAZY 속성값은 지연 로딩이라고 표현하는데 지연 로딩은 기본적으로 필요한 순간까지 데이터베이스와 연결하지 않는 방식으로 동작.
- EAGER는 성능에 영향을 줄 수 있으므로 기본 전략은 LAZY로 선택하되 필요에 따라서 EAGER를 고려해볼 수 있음.

#### LEFT JOIN 처리

```java
@Override
public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    
    JPQLQuery<Board> query = from(board);
    query.leftJoin(reply).on(reply.board.eq(board));
    
    query.groupBy(board);
    return null;
}
```
#### Projections.bean()

- JPA에는 Projection(프로젝션)이라고 해서 JPQL의 결과를 바로 DTO로 처리하는 기능을 제공.
- Querydsl도 같은 기능 제공.
- 필요한 쿼리의 결과를 Projection.bean()이라는 것을 이용해 한번에 DTO로 처리할 수 있는데
이를 이용하려면 JPQLQuery 객체의 select()를 이용.
```java
JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
        board.bno,
        board.title,
        board.writer,
        board.regDate,
        reply.count().as("replyCount")));
```