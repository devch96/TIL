# 스프링 @MVC

-----------

- 애너테이션을 중심으로 한 새로운 MVC의 확장 기능은 @MVC라는 별칭으로도 불림

----------

## @RequestMapping 핸들러 매핑

- 핸들러 매핑과 핸들러 어댑터의 대상이 오브젝트가 아니라 메서드라는 점
- Controller와 같이 하나의 메서드를 가진 인터페이스로 정의되는 컨트롤러는 특별한 방법을 사용하지 않는 한 URL 당 하나의 컨트롤러 오브젝트가 매핑되고
컨트롤러당 하나의 메서드만 DispatcherServlet의 호출을 받을 수 있음
- 반면 @MVC에서는 모든 것이 메서드 레벨로 세분화됨
- @MVC의 핸들러 매핑을 위해서는 DefaultAnnotationHandlerMapping이 필요함
  - 디폴트라 따로 등록은 안해도 됨
  - 다른 핸들러 매핑 빈을 등록했다면 등록해주어야 함
    - 디폴트가 사라지기 때문

### 클래스/메서드 결합 매핑정보

- @RequestMapping은 타입 레벨이랑 메서드 레벨에 붙일 수 있음
  - 스프링은 두 가지 위치에 붙은 정보를 결합해서 최종 매핑정보를 생성함
  - 기본적인 결합 방법은 타입 레벨(클래스)정보를 기준으로 삼고 메서드 레벨의 정보는 타입 레벨의 매핑을 더 세분화하는데 사용

#### @RequestMapping 애노테이션

- String[] value(): URL 패턴
  - 디폴트 엘리먼트
  - 스트링 배열 타입으로 URL 패턴을 지정함
  - ANT 스타일의 와일드카드를 사용할 수 있음
  - {}를 같이 사용하는 URI 템플릿을 사용할 수도 있음
    - 패스 변수(path variable)이라고 불리며 하나 이상 등록가능함
    - 컨트롤러 메서드에서 파라미터로 전달받을 수 있음
  - 하나 이상의 URL 패턴을 지정할 수 있음
  - 디폴트 접미어 패턴이 적용됨
    - 확장자가 붙지 않고 /로 끝나지도 않는 URL 패턴에는 디폴트 접미어 패턴이 적용되서 /, .*이 붙음
    - 스프링부트 3 에서부터는 /로 끝나지 않는 URL 패턴과 /로 끝나느 패턴이 분리됨
- RequestMethod[] method(): HTTP 요청 메서드
  - RequestMethod는 HTTP 메서드를 정의한 이넘(enum)
  - GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE 7개의 HTTP 메서드 정의
- String[] params(): 요청 파라미터
  - 요청의 파라미터와 그 값을 비교해서 매핑해주는 것
  - 파라미터가 들어오는 것에 따라 매핑이 달라짐
  - 매핑 기준으로 매핑조건이 동시에 만족된다면 좀 더 상세한 쪽이 선택됨
- String[] headers(): HTTP 헤더
  - 헤더이름=값 형식을 사용

#### 타입 레벨 매핑과 메서드 레벨 매핑의 결합

- 타입(클래스와 인터페이스) 레벨에 붙는 @RequestMapping은 타입 내의 모든 매핑용 메서드의 공통 조건을 지정할 때 사용
- 메서드 레벨에서 조건을 세분화함
- 타입 레벨에 /user/*, 메서드 레벨에 /add가 선언되면
  - /user/add
- 타입 레벨에 /user/**, 메서드 레벨에 /add가 선언되면
  - /user/**/add

#### 메서드 레벨 단독 매핑

- 타입 레벨에서는 조건을 주지 않고 메서드 레벨에서 독립적으로 매핑정보를 지정할 수도 있음
- 타입 레벨에 @RequestMapping을 붙이긴 해야함

#### 타입 레벨 단독 매핑

- @RequestMapping을 타입 레벨에 단독으로 사용해 다른 타입 컨트롤러에 대한 매핑을 위해 사용할 수 있음
- 메서드 레벨에 빈 @RequestMapping 애노테이션을 붙이면 메서드 이름이 URL 대신 적용됨

### 타입 상속과 매핑

- @RequestMapping 정보는 상속됨
- 서브클래스에서 @RequestMapping을 재정의하면 슈퍼클래스의 정보는 무시됨
- 인터페이스의 @RequestMapping은 인터페이스를 구현한 클래스의 매핑정보로 사용됨
- 컨트롤러를 다른 빈이 DI를 통해 참조하는 경우는 거의 없기 때문에 인터페이스를 사용할 이유는 없지만
스프링 MVC를 기반으로 확장 MVC 프레임워크를 설계한다면 사용해야 할 경우도 있음

#### 매핑정보 상속의 종류

- 상위 타입과 메서드의 @RequestMapping 상속
  - 매핑정보는 Super 클래스에만 있고 Sub 클래스에는 없을 경우 @RequestMapping 정보도 그대로 상속받음
- 상위 타입의 @RequestMapping과 하위 타입 메서드의 @RequestMapping 결합
  - 슈퍼클래스 타입 레벨에 @RequestMapping과 서브클래스 메서드 레벨에 @RequestMapping은 결합됨
- 상위 타입 메서드의 @RequestMapping과 하위 타입의 @RequestMapping 결합
  - @RequestMapping 정보가 상속된 후에 결합됨
- 하위 타입과 메서드의 @RequestMapping 재정의
  - 하위 타입에 @RequestMapping을 부여하면 상위 타입에서 지정한 @RequestMapping 매핑정보를 대체해서 적용됨
- 서브클래스 메서드의 URL 패턴 없은 @RequestMapping 재정의
  - URL 조건이 없는 @RequestMapping을 붙열을 경우에는 상위 메서드의 @RequestMapping의 URL 조건이 그대로 상속됨

#### 제네릭스와 매핑정보 상속을 이용한 컨트롤러 작성

- URL의 일부가 중복되는 것도 슈퍼클래스에 미리 정의해둘 수 있음
- CRUD와 검색 기능의 컨트롤러 코드 또한 제네릭스를 사용할 수 있음

```java
public class UserController {
    UserService service;
    
    public void add(User user){
      ...
    }
    public void update(User user) {
      ...
    }
    public User view(Integer id) {
      ...
    }
    public void delete(Integer id) {
      ...
    }
    public List<User> list() {
      ...
    }
}

public abstract class GenericController<T, K, S> {
    S service;
    public void add(T entity){
      ...
    }
    public void update(T entity) {
      ...
    }
  ...
}
```

- 개별 컨트롤러를 상속해서 만듦
- @RequestMapping 또한 제네릭스에 지정해두면 중복 코드를 많이 줄일 수 있음

---------------------

## @Controller

- DefaultAnnotationHandlerMapping은 사용자 요청을 @RequestMapping 정보를 활용해서 컨트롤러 빈의 메서드에 매핑해줌
- AnnotationMethodHandlerAdatper는 매핑된 메서드를 실제로 호출하는 역할을 담당함
- @MVC의 컨트롤러는 특정 인터페이스를 구현하지 않고 메서드의 시그니처또한 정해져있지 않는데 어떻게 AnnotationMethodHandlerAdapter가 컨트롤러 메서드로
사용할까
- 핸들러 어댑터는 DispatcherServlet으로부터 HttpServletRequest와 HttpServletResponse를 받아 이를 컨트롤러가
사용하는 파라미터 타입으로 변환해서 제공함
- 컨트롤러로부터 받은 결과를 ModelAndView 타입의 오브젝트에 담아 DispatcherServlet에 돌려줌
- @Controller에서는 컨트롤러 역할을 담당하는 메서드의 파라미터 개수와 타입, 리턴 타입 등을 자유롭게 결정할 수 있음

### 메서드 파라미터의 종류

- 스프링은 파라미터의 타입과 이름, 애너테이션 정보를 참고로 해서 그에 맞는 파라미터 값을 제공해줌
- HttpServletRequest, HttpServletResponse
  - ServletRequest, ServletResponse 타입도 가능
- HttpSession
  - HttpServletRequest를 통해 가져올 수도 있지만 세션만 필요한 경우엔 직접 받는 편이 나음
  - 서버에 따라서 멀티스레드 환경에서 안전성이 보장되지 않음
  - 서버에 상관없이 HttpSession을 안전하게 사용하려면 핸들러 어댑터의 synchronizeOnSession 프로퍼티를 true로 설정해주어야 함
- WebRequest, NativeWebRequest
  - HttpServletRequest의 요청정보를 대부분 그대로 갖고 있는 서블릿 API에 종속적이지 않은 오브젝트 타입
- Locale
  - java.util.Local 타입으로 DispatcherServlet의 지역정보 리졸버가 결정한 Locale 오브젝트를 받을 수 있음
- InputStream, Reader
  - HttpServletRequest의 getInputStream()을 통해 받을 수 있는 컨텐트 스트림
- OutputStream, Writer
  - HttpServletResponse의 getOutputStream()으로 가져올 수 있는 출력용 컨텐트 스트림
- @PathVariable
  - URL에 {}로 들어가는 패스 변수를 받음
  - 요청 파라미터를 URL의 쿼리 스트링으로 보내는 대신 URL 패스로 풀어서 쓰는 방식을 쓰는 경우 매우 유용
  - 패스 변수는 하나의 URI 탬플릿 안에 여러 개를 선언할 수 있음
  - 타입이 일치하지 않는 값이 들어오면 클라이언트에 400 Bad Request 응답 코드가 전달됨
- @RequestParam
  - 단일 HTTP 요청 파라미터를 메서드 파라미터에 넣어주는 애너테이션
  - 해당 파라미터가 반드시 있어야함
    - 없으면 400
  - 필수가 아니라 선택적으로 제공하게 하려면 required 엘리먼트를 false로 설정하면됨
  - String, int와 같은 단순 타입은 생략 가능(메서드 파라미터와 같은 이름의 요청 파라미터 값을 받음) 코드를 이해하는데
  불편할 수 있기 때문에 명시적으로 부여하는 것을 권장
- @CookieValue
  - HTTP 요청과 함께 전달된 쿠키 값을 메서드 파라미터에 넣어줌
  - 기본 값에 쿠키의 이름을 지정하면 됨
  - 지정된 쿠키 값이 반드시 존재해야만 함
    - 없을 경우에도 예외가 발생하지 않게 하려면 required 엘리먼트를 false로 선언해야 함
  - 디폴트 값을 선언해 쿠키 값이 없을 때 디폴트 값으로 대신할 수 있음
- @RequestHeader
  - 요청 헤더 정보를 메서드 파라미터에 넣어주는 애노테이션
  - 기본 값으로 가져올 HTTP 헤더의  이름을 지정함
  - 헤더 값이 반드시 존재해야 함
    - required와 defaultValue 엘리먼트 이용 가능
- @ModelAttribute
  - 메서드 파라미터, 메서드 레벨에 적용 가능함
  - 이름 그대로 모델로 사용되는 오브젝트
  - 하지만 ModelAndView와 조금 의미가 다름
  - 클라이언트로부터 컨트롤러가 받는 요청정보 중에서 하나 이상의 값을 가진 오브젝트 형태로 만들 수 있는
  구조적인 정보를 @ModelAttribute 모델이라고 부름
    - 컨트롤러가 전달받는 오브젝트 형태의 정보
  ```java
  @RequestMapping("/user/add", method=RequestMethod.POST)
  public String add(@ModelAttribute User user) {
    userService.add(user);
  }
  ```
  - @ModelAttribute도 생략이 가능함
    - @RequestParam은 String, int 라면 간주
    - 그 외 오브젝트는 @ModelAttribute로 간주
  - 컨트롤러가 리턴하는 모델에 파라미터로 전달한 오브젝트를 자동으로 추가해줌
    - 모델의 이름은 기본적으로 파라미터 타입의 이름
    - 이름을 바꿀 수 있음
- Errors, BindingResult
  - @ModelAttribute가 붙은 파라미터를 처리할 때는 @RequestParam과 달리 검증(validation) 작업이 일어남
  - @ModelAttribute는 Errors나 BindingResult 파라미터를 함께 사용하지 않으면 스프링에서 요청 파라미터의 타입이나 값에 문제가
  없도록 애플리케이션이 보장해준다고 생각한다
  - BindingResult나 Errors를 사용할 때 주의할 점은 파라미터의 위치
    - @ModelAttribute 파라미터 뒤에 나와야 함
    - 자신의 바로 앞에 있는 @ModelAttribute 파라미터의 검증 작업에서 발생한 오류만을 전달해주기 때문
- SessionStatus
  - 현재 세션을 다룰 수 있는 오브젝트
  - 세션 안에 불필요한 오브젝트를 방치하는 것은 일종의 메모리 누수이므로 필요 없어지면 확실하게 제거해야 함
- @RequestBody
  - HTTP 요청 본문(body) 부분이 그대로 전달됨
  - XML이나 JSON 기반의 메시지를 사용하는 요청의 경우에는 이 방법이 매우 유용함
  - AnnotationMethodHandlerAdapter에는 HttpMessageConverter 타입의 메시지 변환기가 여러 개 등록되어 있음
  - @RequestBody가 붙은 파라미터가 있으면 HTTP 요청의 미디어 타입과 파라미터 타입을 처리할 수 있는 것이 있다면 HTTP 요청의 본문 부분을
  통째로 변환해서 지정된 메서드 파라미터로 전달해줌
  - 보통 @ResponseBody와 함께 사용함
- @Value
  - 주로 시스템 프로퍼티나 다른 빈의 프로퍼티 값 또는 좀 더 복잡한 SpEL을 이용해 클래스의 상수를 읽어오거나 특정 메서드를 호출한 결과 값, 조건식 등을 넣을 수 있음
  ```java
  @RequestMapping
  public String hello(@Value("#{systemProperties['os.name']}") String osName)
  ```
- @Valid
  - JSR-303의 빈 검증기를 이용해서 모델 오브젝트를 검증하도록 지시하는 지시자

### 리턴 타입의 종류

- @MVC 컨트롤러 메서드에는 리턴 타입도 다양하게 결정할 수 있음
- 컨트롤러가 DispatcherServlet에 돌려줘야 하는 정보는 모델과 뷰
- @Controller 메서드의 리턴 타입은 기타 정보와 결합해서 ModelAndView로 만들어짐

#### 자동 추가 모델 오브젝트와 자동생성 뷰 이름

- @ModelAttribute 모델 오브젝트 또는 커맨드 오브젝트
  - @ModelAttribute 또는 생략하였지만 기본 타입이 아닌 커맨드 오브젝트라면 컨트롤러가 리턴하는 모델에 추가됨
- Map, Model, ModelMap 파라미터
  - 이런 파라미터에 추가한 오브젝트는 DispatcherServlet을 거쳐 뷰에 전달되는 모델에 자동으로 추가됨
- @ModelAttribute 메서드
  - @ModelAttribute가 붙은 메서드는 컨트롤러 클래스 안에 정의하지만 컨트롤러 기능을 담당하지 않음
  - 클래스 내의 다른 컨트롤러 메서드의 모델에 자동으로 추가됨
- BindingResult
  - 모델 맵에 추가될 때의 키는 'org.springframework.validation.BindingResult.모델이름'
- ModelAndView
  - 컨트롤러가 리턴해야 하는 정보를 담고 있는 가장 대표적인 타입
- String
  - 메서드의 리턴 타입이 스트링이면 리턴 값은 뷰 이름으로 사용됨
  - 모델은 파라미터로 맵을 가져와 넣어주고 리턴 값은 뷰 이름을 스트링 타입으로 선언하는 방식은 흔히 사용되는 방법
- void
  - 메서드의 리턴 타입이 void면 RequestToViewNameResolver 전략을 통해 자동생성되는 뷰 이름이 사용됨
  - URL과 뷰 이름을 일관되게 통일할 수 있다면 void형의 사용도 고려해볼만 함
- 모델 오브젝트
  - 모델에 추가할 오브젝트가 하나뿐이라면, Model 파라미터를 받아서 저장하는 대신 모델 오브젝트를 바로 리턴해도 됨
  - 스프링은 리턴 타입이 미리 지정된 타입이나 void가 아닌 단순 오브젝트라면 이를 모델 오브젝트로 인식해서 모델에 자동으로 추가함
- View
  - 뷰 이름 대신 뷰 오브젝트를 사용할 수 있음
- @ResponseBody
  - @RequestBody와 비슷한 방식으로 동작함
  - HTTP 응답의 메시지 본문으로 전환됨

### @SessionAttributes와 SessionStatus

- HTTP 는 Stateless 하기 때문에 하나의 요청을 처리한 후에는 사용했던 모든 리소스를 정리함
- 애플리케이션은 기본적으로 상태를 유지할 필요가 있으므로 세션을 사용함

#### 도메인 중심 프로그래밍 모델과 상태 유지를 위한 세션 도입의 필요성

- 수정 기능을 위해서는 최소한 두 번의 요청이 서버로 전달돼야 함
  - 수정 폼을 띄워달라는 요청
  - 정보를 수정하고 수정 완료 요청
- 하지만 수정한 폼의 필드 값에 오류가 있는 경우에 에러 메시지와 함께 수정 화면을 다시 보여줘야함

--------------

## 모델 바인딩과 검증

- @ModelAttribute가 지정된 파라미터를 @Controller 메서드에 추가하면 세 가지 작업이 자동으로 진행됨
  - 파라미터 타입의 오브젝트를 만듬
    - @ModelAttribute User user 라는 파라미터 선언이 있다면 User 타입의 오브젝트를 생성함
      - 디폴트 생성자가 반드시 필요함
    - @SessionAttributes에 의해 세션에 저장된 모델 오브젝트가 있다면 가져옴
  - 준비된 모델 오브젝트의 프로퍼티에 웹 파라미터를 바인딩함
    - 스프링에 준비되어 있는 기본 프로퍼티 에디터를 이용해 HTTP 파라미터 값을 모델의 프로퍼티 타입에 맞게 전환함

### PropertyEditor

- 스프링이 기본적으로 제공하는 바인딩용 타입 변환 API
  - 스프링의 API가 아닌 자바빈 표준에 정의된 인터페이스
- PropertyEditor는 원래 비주얼 환경에서의 프로퍼티 창과 자바빈 컴포넌트를 위해 설계됐으므로 인터페이스를 보면 AWT를 사용하믄 paintValue() 메서드를 갖고있기도 함

#### 디폴트 프로퍼티 에디터

- URL이 /hello?charset==UTF-8 이라면 charset 파라미터는 UTF-9로 설정된 Charset 타입의 오브젝트를 받음

```java
@RequestMapping("/hello")
public void hello(@RequestParam Charset charset, Model model)
```

- UTF-8 이라는 문자열로 들어온 파라미터를 바인딩 과정에서 메서드 파라미터 타입인 Charset으로 전환하기 위해 스프링이 디폴트로 등록해준
CharsetEditor를 적용했기 때문
- 바인딩 과정에서는 변환할 파라미터 또는 모델 프로퍼티의 타입에 맞는 프로퍼티 에디터가 자동으로 선정돼서 사용됨
- 스프링이 지원하지 않는 타입을 파라미터로 사용한다면 직접 프로퍼티 에디터를 만들어서 적용할 수 있음

#### 커스텀 프로퍼티 에디터

```java
public enum Level {
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);
    
    public int intValue() {return value;}
  
    public static Level valueOf(int value) {
        switch(value) {
          case 1: return BASIC;
          case 2: return SILVER;
          case 3: return GOLD;
          default: throw new AssertionError("Unknown value: " + value);
        }
    } 
}
```

- URL에 level=1이라고 파라미터를 줘서 실행시키면 String 타입을 Level 타입으로 변환할 수 없어 HTTP 500 에러를 만남
  - ConversionNotSupportedException
- PropertyEditor에서 변한을 위해 사용되는 메서드는 총 네가지가 있음
- HTTP 요청 파라미터와 같은 문자열은 스트링 타입으로 서블릿에서 가져옴
- 스프링이 스트링 타입의 문자열을 변경할 타입의 오브젝트로 만들 때는 두 개의 메서드가 사용됨
  - setAsText() 메서드를 이용해 스트링 타입의 문자열을 넣고 getValue()로 변환된 오브젝트를 가져옴
  - 반대로 오브젝트를 다시 문자열로 바꿀때는 setValue()로 오브젝트를 넣고 getAsText()로 문자열을 가져옴
- getValue()와 setValeu()는 오브젝트를 저장하고 가져올 때 사용하는 것으로 손댈 것 없고 setAsText()와 getAsText() 두 가지를 구현해야 함

```java
public class levelPropertyEditor extends PropertyEditorSupport {
    public String getAsText() {
        return String.valueOf(((Level) this.getValue()).intValue());
    }
    
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
    }
}
```

#### @InitBinder

- @MVC에는 스프링 컨테이너에 정의된 디폴트 프로퍼티 에디터만 등록되어 있음
  - LevelPropertyEditor를 추가해야 함
- @Controller 메서드를 호출해줄 책임이 있는 AnnotationMethodHandlerAdapter는 @RequestParam이나 @ModelAttribute, @PathVariable 등처럼
HTTP 요청을 파라미터 변수에 바인딩해주는 작업이 필요한 애노테이션을 발견하면 먼저 WebDataBinder를 만듬
  - WebDataBinder는 HTTP 요청으로부터 가져온 문자열을 파라미터 타입의 오브젝트로 변환하는 기능이 포함되어 있음
  - 커스텀 프로퍼티 에디터를 @RequestParam 과 같은 메서드 파라미터 바인딩에 적용하려면 WebDatabinder에 프로퍼티 에디터를 직접 등록해주어야 함
- 문제는 WebDataBinder는 외부로 직접 노출되지 않음
  - 스프링이 제공하는 WebDataBinder 초기화 메서드를 이용해야 함

```java
@InitBinder
public void initBinder(WebDataBinder dataBinder) {
    dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
}
```

- @InitBinder가 붙은 initBinder() 메서드는 메서드 파라미터를 바인딩하기 전에 호출됨
- @InitBinder에 의해 등록된 커스텀 에디터는 같은 컨트롤러의 메서드에서 HTTP 요청을 바인딩하는 모든 작업에 적용됨
  - WebDataBinder의 바인딩 적용 대상은 RequestParam, CookieValue, RequestHeader, PathVariable, ModelAttribute
- WebDataBinder에 커스텀 프로퍼티 에디터를 등록하는 방법 두 가지
  - 특정 타입에 무조건 적용되는 프로퍼티 에디터 등록
    - 적용 타입과 프로퍼티 에디터 두 개를 파라미터로 받는 registerCustomEditor()를 사용해 등록했다면
    해당 타입을 가진 바인딩 대상이 나오면 항상 프로퍼티 에디터가 적용됨
    - 디폴트 프로퍼티 에디터에서는 지원하지 않는 타입이라면 이 방식을 사용하는 것이 적절함
  - 특정 이름의 프로퍼티에만 적용되는 프로퍼티 에디터 등록
    - 같은 타입이지만 프로퍼티의 이름이 일치하지 않는 경우에는 등록한 커스텀 프로퍼티 에디터가 적용되지 않음
    - 이름이 포함된 프로퍼티 에디터의 등록은 이미 프로퍼티 에디터가 존재하는 경우에 사용하기 적합함

#### WebBindingInitializer

- @InitBinder 메서드에서 WebDataBinder에 추가한 커스텀 프로퍼티 에디터는 메서드가 있는 컨트롤러 클래스 안에서만 동작함
- 애플리케이션 전반에 걸쳐 폭넓게 필요한 경우라면 WebBindingInitializer를 이용하면 됨

```java
public class MyWebBindingInitializer implements WebBindingInitializer {
    public void initBinder(WebDataBinder binder ,WebRequest request) {
        binder.registerCustomEditor(Level.class, new LevelPorpertyEditor());
    }
}
```

- WebBindingInitialzer를 구현해서 만든 클래스를 빈으로 등록하고 @Controller를 담당하는 어댑터 핸들러인 AnnotationMethodHandlerAdapter의 webBindingInitializer 프로펕티에
DI 해줌
  - AnnotationMethodHandlerAdapter도 직접 빈으로 등록해야 함

#### 프로토타입 빈 프로퍼티 에디터

- 지금까지는 프로퍼티 바인딩을 할 때마다 매번 새로운 프로퍼티 에디터 오브젝트를 만들어 사용함
  - new 키워드
- 프로퍼티 에디터를 싱글톤 빈으로 등록해두고 사용하면 안되나?
  - 안됨
  - 프로퍼티 에디터에 의해 타입이 변경되는 오브젝트는 한 번은 프로퍼티 에디터 오브젝트 내부에 저장이 됨
    - 짧은 시간이지만 상태가 있음
- 프로토타입 빈을 등록하고 가져옴

### Converter와 Formatter

- 여러 가지 장점에도 불구하고 PropertyEditor는 매번 바인딩을 할 때마다 새로운 오브젝트를 만들어야 한다는 약점이 있음
- 스프링 3.0에는 PropertyEditor를 대신할 수 있는 새로운 타입 변환 API가 도입됨
  - Converter 인터페이스
- PropertyEditor와 달리 Converter는 변환 과정에서 메서드가 한 번만 호출됨
  - 변환 작업 중에 상태를 인스턴스 변수로 저장하지 않음
  - 멀티스레드 환경에서 안전

#### Converter

- 문자열과 오브젝트 사이의 양방향 변환 기능을 제공하는 PropertyEditor와 다르게 Converter 메서드는 소스 타입에서 타깃 타입으로의 단방향 변환만 지원함
  - 컨버터를 하나 더 만들면 양방향 변환이 가능
- PropertyEditor 처럼 한 쪽이 스트링으로 고정되어 있지 않고 소스와 타깃의 타입을 임의로 지정할 수 있음

```java
public class LevelToStringConverter implements Converter<Level, String> {
    public String convert(Level level) {
        return String.valueOf(level.intValue());
    }
}

public class StringToLevelConverter implements Converter<String, Level> {
    public Level convert(String text) {
        return Level.valueOf(Integer.parseInt(text));
    }
}
```

#### ConversionService

- 컨트롤러의 바인딩 작업에도 이렇게 만든 컨버터를 적용할 수 있음
- PropertyEditor 처럼 Converter 타입의 컨버터를 개별적으로 추가하는 대신 ConversionService 타입의 오브젝트를 통해 WebDataBinder에 설정해주어야 함
- ConversionService는 여러 종류의 컨버터를 이용해서 하나 이상의 타입 변환 서비스를 제공해주는 오브젝트를 만들 때 사용하는 인터페이스
  - 보통 GenericConversionService 클래스를 빈으로 등록하여 사용함
- 스프링 3.0에 추가된 Converter 외에도 GenericConverter와 ConverterFactory를 이용해서 만들 수 있음
  - GenericConverter를 이용하면 하나 이상의 소스-타깃 타입 변환을 한 번에 처리할 수 있는 컨버터를 만들 수 있음
  - 필드 컨텍스트를 제공받을 수 있음
    - 필드 컨텍스트는 모델의 프로퍼티에 대한 바인딩 작업을 할 때 제공받을 수 있는 메타정보

#### Formatter와 FormattingConversionService

- 포맷터는 스트링 타입의 폼 필드 정보와 컨트롤러 메서드의 파라미터 사이에 양방향으로 적용할 수 있도록 두 개의 변환 메서드를 갖고 있음
- Formatter는 그 자체로 Converter와 같이 스프링이 기본적으로 지원하는 범용적인 타입 변환 API가 아님
  - GenericConversionService에 등록할 수 없음
  - 대신 Formatter 구현 오브젝트를 GenericConverter 타입으로 포장해서 등록해주는 기능을 가진 FormattingConversionService
  를 통해서만 적용될 수 있음
- Formatter 인터페이스는 오브젝트를 문자열로 변환해주는 print() 메서드와 문자열을 오브젝트로 변환해주는 parse() 메서드 두 개로 구성되어 있음
- Formatter를 직접 구현해서 사용하기보다는 FormattingConversionServiceFactoryBean을 통해 기본적으로 등록되는 몇 가지 유용한 애노테이션 기반 포맷터를
활용하는 방법만 익혀도 됨
- @NumberFormat
  - 숫자 변환을 지원하는 포맷터
  - 문자열로 표현된 숫자를 java.lang.Number 타입의 오브젝트로 상호 변환해줌
  - 엘리먼트로 style과 pattern을 지정할 수 있음
    - style은 Style 이넘의 NUMBER, CURRENCY, PERCENT 세 가지를 설정할 수 있음
    - 현재 지역정보를 기준으로 해서 그에 맞는 숫자, 통화, 퍼센트 표시를 지원함
    - 다중 지역 서비스를 제공하는 애플리케이션에 유용한 기능
    - Locale에 정의된 표준 포맷에는 없는 숫자패턴을 사용하고 싶다면 pattern 엘리먼트를 사용
  
  ```java
  class Product {
      @NumberFormat("$###,##0,00")
      BigDecimal price;
  }
  ```
  - @NumberFormat 애노테이션을 이용한 바인딩 기능을 사용하려면 FormattingConversionService에 NumberFormatAnnotationFormatterFactory를
  등록하도록 코드를 작성해야 함

#### 바인딩 기술의 적용 우선순위와 활용 전략

- 사용자 정의 타입의 바인딩을 위한 일괄 적용: Converter
  - 애플리케이션에서 정의한 타입이면서 모델에서 자주 활용되는 타입이라면 컨버터로 만들고 컨버전 서비스로 묶어서 일괄 적용하는 것이 편리함
  - 프로퍼티 에디터로 만들면 매번 오브젝트가 새로 만들어지므로, 싱글턴으로 사용할 수 있는 컨버터가 유리
- 필드와 메서드 파라미터, 애너테이션 등의 메타정보를 활용하는 조건부 변환 기능: ConditionalGenericConverter
  - 조건을 변환 로직에 참고할 필요가 있다면 ConditionalGenericConverter를 이용해야 함
  - GenericConversionService에 등록하여 일괄 적용할 수 있음
- 애너테이션 정보를 활용한 HTTP 요청과 모델 필드 바인딩: AnnotationFormatterFactory와 Formatter
  - @NumberFormat이나 @DateTimeFormat처럼 필드에 부여하는 애너테이션 정보를 이용해 변환 기능을 지원하려면 AnnotationFormatterFacotry를
  이용해 애노테이션에 따른 포맷터를 생성해주는 팩토리를 구현해야 함
- 특정 필드에만 적용되는 변환 기능: PropertyEditor
  - 특정 모델의 필드에 제한해서 변환 기능을 적용해야 할 경우
  - 커스텀 프로퍼티 에디터를 만들어서 컨트롤러의 @InitBinder에서 필드 조건을 추가해서 등록할 수 있음
  - 
