# 스프링 웹 기술과 스프링 MVC

- 엔터프라이즈 애플리케이션의 가장 앞단에서 사용자, 클라이언트 시스템과 연동하는 책임을 맡고 있는 것이 웹 프레젠테이션 계층
- 자바의 웹 기술은 종류와 기술관련 프레임워크도 다양할 뿐만 아니라 각 기술이 배타적으로 사용되지 않고 조합될 수 있어 선택의 폭이 넓음
- 스프링은 기술의 변화가 잦은 웹 계층과 여타 계층을 깔끔하게 분리해서 개발하는 아키텍처 모델을 지지

------------

## 스프링의 웹 프레젠테이션 계층 기술

### 스프링에서 사용되는 웹 프레임워크의 종류

#### 스프링 웹 프레임워크

- 스프링 서플릿/스프링 MVC
  - 스프링이 직접 제공하는 서블릿 기반의 MVC 프레임워크
  - 프론트 컨트롤러 역할을 하는 DispatcherServlet을 핵심 엔진으로 사용
- 스프링 포틀릿
  - 포틀릿 개발이 필요한 경우에 유용
  - 포틀릿 전용 애플리케이션 컨텍스트를 가짐

#### 스프링 포트폴리오 웹 프레임워크

- Spring Web Flow
  - 스프링 서블릿을 기반으로 상태유지 스타일의 웹 애플리케이션을 작성하게 해주는 프레임워크
  - DSL을 이용해 웹 페이지의 흐름과 규칙을 지정함
  - 무상태 방식으로 개발되는 스프링 MVC와 더불어 스프링 웹 프레임워크의 양 축을 이루고 있는 프레임워크
- Spring JavaScript
  - 자바스크립트 툴킷인 Dojo를 추상화한것으로 스프링 서블릿과 스프링 웹 플로우에 연동해서 손쉽게 Ajax 기능을 구축할 수 있음
- Spring Faces
  - JSF를 스프링 MVC와 스프링 SWF의 뷰로 손쉽게 사용할 수 있게 해주는 프레임워크
- Spring Web Service
  - MVC와 유사한 방식으로 SOAP 기반의 웹 서비스 개발을 가능하게 해주는 프레임워크
    - Simple Object Access Protocol
    - HTTP, HTTPS, SMTP 등을 통해 XML 기반의 메시지를 컴퓨터 네트워크 상에서 교환하는 프로토콜

#### 스프링을 기반으로 두지 않는 웹 프레임워크

- JSP/Servlet
- Struts1
- Struts2
- Tapestry3, 4
- JSF/Seam

### 스프링 MVC와 DispatcherSevlet 전략

- 스프링은 특정 기술이나 방식에 매이지 않으면서 웹 프레젠테이션 계층의 각종 기술을 조합, 확장해서 사용할 수 있는 매우 유연한 웹 애플리케이션 개발의
기본 틀을 제공해줌
- 스프링 MVC 프레임워크를 이미 완성된 고정적인 프레임워크로 보지 말고, 진행하려는 프로젝트의 특성에 맞게 빠르고 편리한 개발이 가능하도록 자신만의
웹 프레임워크를 만드는 데 쓸 수 있는 도구라고 생각해야 함
- 스프링 웹 기술의 핵심이자 기반이 되는 것은 DispatcherServlet

#### DispatcherServlet과 MVC 아키텍처

- MVC는 프레젠테이션 계층의 구성요소를 정보를 담은 모델(M), 화면 출력 로직을 담은 뷰(V), 제어 로직을 담은 컨트롤러(C)로 분리하고
세 가지 요소가 서로 협력해서 하나의 우베 요청을 처리하고 응답을 만들어내는 구조
- MVC 아키텍처는 보통 프론트 컨트롤러 패턴과 함께 사용함
  - 프론트 컨트롤러 패턴은 중앙집중형 컨트롤러를 프레젠테이션 계층의 제일 앞에 둬서 서버로 들어오는 모든 요청을 먼저 받아 처리하게 만듬
  - 프론트 컨트롤러는 클라이언트가 보낸 요청을 받아서 공통적인 작업을 먼저 수행한 후에 적절한 세부 컨트롤러로 작업을 위임해주고, 클라이언트에게 보낼 뷰를 선택해서 최종 결과를
  생성하는 등의 작업을 수행함
  - 예외가 발생했을 때 일관된 방식으로 처리하는 것도 프론트 컨트롤러의 역할

1. DispatcherServlet의 HTTP 요청 접수

- 자바 서버의 서블릿 컨테이너는 HTTP 프로토콜을 통해 들어오는 요청이 스프링의 DispatcherServlert에 할당된 것이라면 HTTP 요청정보를 DispatcherServlet
에 전달해줌
  - web.xml에 DispatcherServlet이 전달받을 URL의 패턴이 정의되어 있음

2. DispatcherServlet에서 컨트롤러로 HTTP 요청 위임

- DispatcherServlet은 URL이나 파라미터 정보, HTTP 명령 등을 참고로해서 어떤 컨트롤러에게 작업을 위임할지 결정
- 컨트롤러를 선정하는 것은 DispatcherServlet의 핸들러 매핑 전략 이용
- DispatcherServlet은 어떤 종류의 오브젝트라도 컨트롤러로 사용할 수 있음
  - 확장성의 비결
- 자바의 오브젝트 사이에 무엇인가 요청이 전달되려면 메서드가 호출돼야 하고, DispatcherServlet이 컨트롤러 오브젝트의 메서드를 호출할 수 있는 방법은?
  - 각기 다른 메서드와 포맷을 가진 오브젝트를 컨트롤러로 만들어놓고 DispatcherServlet이 이를 알아서 호출할 수 있나?
  - 해결책은 어댑터 패턴을 사용
- DispatcherServlet은 컨트롤러가 어떤 메서드를 가졌고 어떤 인터페이슬르 구현했는지 전혀 알지 못함.
- 컨트롤러의 종류에 따라 적절한 어댑터를 사용함
- 각 어댑터는 자신이 담당하는 컨트롤러에 맞는 호출 방법을 이용해서 컨트롤러에 작업 요청을 보내고 결과를 돌려받아서 DispatcherServlet에게 다시 돌려주는
기능을 갖고 있음
- DispatcherServlet이 핸들러 어댑터에 웹 요청을 전달할 때는 모든 웹 요청 정보가 담긴 HttpServletRequest 타입의 오브젝트를 전달해줌
- 이를 어댑터가 적절히 변환해서 컨트롤러의 메서드가 받을 수 있는 파라미터로 변환해서 전달해주는 것
- HttpServletResponse도 함께 전달해줌
  - 컨트롤러가 결과를 리턴값으로 돌려주는 대신 HttpServletResponse 오브젝트 안에 직접 넣을수도 있기 때문

3. 컨트롤러의 모델 생성과 정보 등록

- 컨트롤러의 작업은 사용자 요청 해석, 비즈니스 로직 수행하도록 서비스 계층 오브젝트에 작업 위임, 결과를 받아 모델 생성, 뷰를 결정 이 네가짇다
- 모델을 생성하고 모델에 정보를 넣어주는 게 컨트롤러가 해야 할 마지막 작업
- 컨트롤러는 어떤 식으로든 다시 DispatcherServlet에 모델과 뷰를 돌려줘야 함
- 모델은 보통 맵에 담긴 정보
  - 이름과 그에 대응하는 값의 쌍으로 정보를 만드는 것

4. 컨트롤러의 결과 리턴: 모델과 뷰

- 컨트롤러가 뷰 오브젝트를 직접 리턴할 수도 있지만, 보통은 뷰의 논리적인 이름을 리턴해주면 DispatcherServlet의 뷰 리졸버가 이를 이용해 뷰 오브젝트를 생성함
- 스프링에는 ModelAndView라는 이름의 오브젝트가 있는데 DispatcherServlet이 최종적으로 어댑터를 통해 컨트롤러로부터 돌려받는 오브젝트다

5. DispatcherServlet의 뷰 호출과 (6) 모델 참조

- DispatcherServlet은 컨트롤러에게 모델과 뷰를 받고 뷰 오브젝트에게 모델을 전달하여 클라이언트에게 돌려줄 최종 결과물을 생성해달라고 요청함
  - 보통은 브라우저를 통해 볼테니 HTML을 생성하는 일이 가장 흔한 뷰의 작업
- 뷰 작업을 통한 최종 결과물은 HttpServletResponse 오브젝트 안에 담김

7. HTTP 응답 돌려주기

- 뷰 생성까지 마쳤으면 DispatcherServlet은 등록된 후처리기가 있는지 확인하고 있다면 후속작업, 없다면 HttpServletResponse를 서블릿 컨테이너에게
돌려줌
- 서블릿 컨테이너는 HttpServletResponse에 담긴 정보를 HTTP 응답으로 만들어 사용자의 브라우저나 클라이언트에게 전송하고 작업을 종료함

#### DispatcherServlet의 DI 가능한 전략

- HandlerMapping
  - URL과 요청 정보를 기준으로 어떤 핸들러 오브젝트(컨트롤러)를 사용할 것인지를 결정하는 로직 담당
  - HandlerMapping 인터페이스를 구현해서 만들 수 있음
  - DispatcherServlet은 하나 이상의 핸들러 매핑을 가질 수 있음
  - 디폴트로는 BeanNameUrlHandlerMapping과 DefaultAnnotationHandlerMapping 두 가지가 있음
- HandlerAdapter
  - 핸들러 매핑으로 선택한 컨트롤러를 DispatcherServlet이 호출할 떄 사용하는 어댑터
  - 컨트롤러의 타입에는 제한이 없기 때문에 호출 방법을 DispatcherServlet이 알 방법이 없으므로 HandlerAdapter가 필요함
  - 컨트롤러 타입에 적합한 어댑터를 가져다가 이를 통해 컨트롤러를 호출함
  - 디폴트로는 HttpRequestAdapter, SimpleControllerHandlerAdapter, AnnotationMethodHandlerAdapter 세 가지
  - @ReqeustMapping과 @Controller 애노테이션을 통해 정의되는 컨트롤러의 경우에는 DefaultAnnotationHandlerMapping에 의해 핸들러가 결정되고
  AnnotationMethodHandlerAdapter에 의해 호출이 일어남
- HandlerExceptionResolver
  - 예외가 발생했을 때 이를 처리하는 로직을 갖고 있음
  - 예외가 발생했을 때 통보 작업은 컨트롤러가 아니라 프론트 컨트롤러인 DispatcherServlet을 통해 처리되어야 함
  - 디폴트로는 AnnotationMethodHandlerExceptionResolver, ResponseStatusExceptionResolver, DefaultHandlerExceptionResolver 세 가지
- ViewResolver
  - 컨트롤러가 리턴한 뷰 이름을 참고해서 적절한 뷰 오브젝트를 찾아주는 로직을 갖음
  - 디폴트로 InternalResourceViewResolver는 JPS나 서블릿 같이 RequestDispatcher에 의해 포워딩될 수 있는 리소스를 뷰로 사용하게 해줌
- LocaleResolver
  - 지역 정보를 결정해주는 전략
  - 디폴트인 AcceptHeaderLocalResolver는 HTTP 헤더의 정보를 보고 지역정보를 설정해줌
  - 지역정보는 애플리케이션에서 활용될 수 있음
- ThemeResolver
  - 테마를 가지고 이를 변경해서 사이트를 구성할 경우 쓸 수 있는 테마 정보 결정 전략
- RequestToViewNameTranslator
  - 컨트롤러에서 뷰 이름이나 뷰 오브젝트를 제공해주지 않았을 경우 URL과 같은 요청정보를 참고해서 뷰 이름을 생성하는 전략

-----------------

## 스프링 웹 애플리케이션 환경 구성

- 서비스 계층과 데이터 엑세스 계층의 코드는 실행환경에서 독립적으로 만들기 쉬움
- 웹 프레젠테이션 계층은 서블릿 또는 포틀릿 컨테이너가 제공되는 서버환경이 있어야 동작
  - 테스트 목적으로 배치하지 않은 채로 코드 실행은 할 수 있긴함

--------------

## 컨트롤러

- MVC의 세 가지 컴포넌트 중에서 가장 많은 책임을 지고 있음
  - 서블릿이 넘겨주는 HTTP 요청은 HttpServletRequest 오브젝트에 담겨 있음
- HttpServletRequest에 담겨 있는 사용자의 요청을 모두 분석한 후에 비로소 서비스 계층의 비즈니스 로직을 담당하는 메서드를 부름
- 컨트롤러는 서비스 계층의 메서드가 돌려준 결과를 보고 뷰를 결정해야 함
  - 때로는 페이지가 바뀌도록 리다이렉트

### 컨트롤러의 종류와 핸들러 어댑터

- 스프링 MVC가 지원하는 컨트롤러의 종류는 네 가지
  - 각 컨트롤러를 DispatcherServlet에 연결해주는 핸들러 어댑터가 하나씩 있어야 하므로 핸드러 어댑터도 네 개
  - SimpleServletHandlerAdapter를 제외한 세 개의 핸들러 어댑터는 DispatcherServlet에 디폴트 전략으로 설정되어 있음

#### Servlet과 SimpleServletHandlerAdapter

- 표준 서블릿
  - javax.servlet.Servlet을 구현한 서블릿 클래스를 스프링 MVC의 컨트롤러로 사용할 수 있음
- 서블릿이라면 web.xml에 등록하고 사용하면 되는데 굳이 스프링 MVC의 컨트롤러로 사용?
  - 기존에 서블릿으로 개발된 코드를 스프링 애플리케이션에 가져와 사용하려면 일단
  서블릿을 web.xml에 별도로 등록하지 말고 MVC 컨트롤러로 등록해서 사용하는 것이 좋음
  - 서블릿 코드를 점진적으로 스프링 애플리케이션에 맞게 포팅할 때 유용
- 서블릿이 컨트롤러 빈으로 등록된 경우에는 자동으로 init(), destory()와 같은 생명주기 메서드가 호출되지 않는다는 점을 유의
  - 서블릿에서 초기화 작업을 하는 코드가 있다면 init-method 애트리뷰트나 @PostConstruct 애노테이션 등을 이용해 빈 생성 후에 초기화 메서드가 실행되게 해야 함

```java
public class ServletControllerTest extends AbstractDispatcherServletTest {
    @Test
    public void helloServletController() throws ServletException, IOException {
        setClasses(SimpleServletHandlerAdapter.class, HelloServlet.class);
        initRequest("/hello").addParameter("name", "Spring");
        
        assertThat(runService().getContentAsString(), is("Hello Spring"));
    }
    
    @Component("/hello")
    static class HelloServlet extends HttpServlet {
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String name = req.getParameter("name");
            resp.getWriter().print("Hello " + name);
        }
    }
}
```

- 서블릿 타입의 컨트롤러를 DispatcherServlet이 호출해줄 때 필요한 핸들러 어댑터를 등록하는 것
  - 서블릿 컨트롤러용 핸들러 어댑터는 SimpleServletHandlerAdapter 클래스
- Servlet 타입의 컨트롤러는 모델과 뷰를 리턴하지 않음
  - 스프링 MVC의 모델과 뷰 라는 개념을 알지 못하는 표준 서블릿을 사용했기 때문
  - HttpServletResponse에 넣어준 정보를 확인하는 방법 사용
  - DispatcherServlet은 컨트롤러가 ModelAndView 타입의 오브젝트 대신 null을 리턴하면 뷰를 호출하는 과정을
  생략하고 작업을 마침
  - 서블릿 컨트롤러처럼 직접 HttpServletResponse에 결과를 넣는 컨트롤러도 있기 때문

#### HttpRequestHandler와 HttpRequestHandlerAdapter

- 인터페이스로 정의된 컨트롤러 타입

```java
public interface HttpRequestHandler {
    void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
```

- 서블릿 인터페이스와 비슷함
- 실제로 HttpRequestHandler는 서블릿처럼 동작하는 컨트롤러를 만들기 위해 사용함
  - 전형적인 서블릿 스펙을 준수할 필요 없이 HTTP 프로토콜을 기반으로 한 전용 서비스를 만드려고 할 때 사용
- 스프링은 HttpRequestHandler를 이용하여 자바의 RMI(Remote Method Invocation)를 대체할 수 있는 HTTP 기반의 가벼운 원격 호출 서비스인
HTTP Invoker를 제공함
- HttpRequestHandler는 모델과 뷰 개념이 없는 HTTP 기반의 RMI와 같은 로우레벨 서비스를 개발할 때 이용함
- 디폴트 전략이므로 빈으로 등록해줄 필요는 없음

####  Controller와 SimpleControllerHandlerAdapter

```java
public interface Controller {
    ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
```

- DispatcherServlet이 컨트롤러와 주고받는 정보를 그대로 메서드의 파라미터와 리턴 값으로 갖고 있음
  - 가장 대표적인 컨트롤러 타입
  - 스프링 3.0의 애노테이션과 관례를 이용한 컨트롤러가 등장하기 전까지 MVC 컨트롤러라고 하면 바로 이 Controller였음
- Controller 타입의 컨트롤러는 Controller 인터페이스를 구현하기만 하면 되기 때문에 특정 클래스를 상속하도록 강제하는 여타 MVC 프레임워크의 컨트롤러보다
유연하게 컨트롤러 클래스를 설계할 수 있음
  - 하지만 실제로는 이 Controller 인터페이스를 직접 구현하는 것은 권장되지 않음
  - 적어도 웹 브라우저를 클라이언트로 갖는 컨트롤러로서의 필수 기능이 구현되어 있는 AbstractController를 상속해서 컨트롤러를 만드는 게 편리하기 때문
- AbstractController는 다음과 같은 웹 개발에 유용하게 쓸 수 있는 프로퍼티를 제공함
  - synchronizeOnSession
    - HTTP 세션에 대한 동기화 여부를 결정하는 프로퍼티
  - supportedMethods
    - HTTP 메서드(GET, POST 등)를 지정할 수 있음
    - 디폴트는 모든 종류의 HTTP 메서드를 다 허용함
  - useExpiresHeader, useCacheControlHeader, useCacheControlNoStore, cacheSeconds
    - HTTP 1.0/ 1.1의 Expires, Cache-Control HTTP 헤더를 이용해 브라우저의 캐시 설정정보를 보내줄 것인지를 결정
- 애플리케이션의 컨트롤러가 특정 클래스를 상속받는 것이 불편하게 느껴진다면 전용 컨트롤러 인터페이스를 정의하고 핸들러 어댑터를 만드는 방법도 가능

#### AnnotationMethodHandlerAdapter

- 다른 핸들러 어댑터와는 다른 특징이 있음
- 컨트롤러의 타입이 정해져 있지 않다는 점
- 클래스와 메서드에 붙은 몇 가지 애너테이션의 정보와 메서드 이름, 파라미터, 리턴 타입에 대한 규칙 등을 종합적으로
분석해서 컨트롤러를 선별하고 호출 방식을 결정함
  - 상당히 유연한 방식으로 컨트롤러를 작성할 수 있음
- 또 다른 특징은 컨트롤러 하나가 하나 이상의 URL에 매핑될 수 있다는 점
  - 다른 컨트롤러는 특별한 확장 기능을 사용하는 경우를 제외하면 URL당 하나의 컨트롤러가 매핑되는 구조
  - 웹 요청의 개수가 늘어나면 컨트롤러도 늘어남

```java
@Controller
public class HelloController {
    @RequestMapping("/hello")
    public String hello(@RequestParam("name") String name, ModelMap map) {
        map.put("message", "Hello " + name);
        return "/WEB-INF/view/hello.jsp";
    }
}
```

### 핸들러 매핑

- 핸들러 매핑은 HTTP 요청정보를 이용해서 이를 처리할 핸들러 오브젝트(컨트롤러)를 찾아주는 기능을 가진 DispatcherServlet의 전략
- 컨트롤러의 타입과는 상관없음
  - 하나의 핸들러 매핑 전략이 여러 가지 타입의 컨트롤러를 선택할 수 있다는 뜻
- 스프링은 기본적으로 다섯 가지 핸들러 매핑 제공
  - 디폴트로 등록된 핸들러 매핑은 BeanNameUrlHandlerMapping과 DefaultAnnotationHandlerMapping

#### BeanNameUrlHandlerMapping

- 디폴트 핸들러 매핑의 하나
- 빈의 이름에 들어 있는 URL을 HTTP 요청의 URL과 비교해서 일치하는 빈을 찾아줌
  - 가장 직관적이고 사용하기 쉬운 핸들러 매핑전략
- URL에는 ANT 패턴이라고 불리는 *나 **, *?와 같은 와일드카드를 사용하는 패턴을 넣을 수 있음
- 패턴에 일치하는 모든 URL을 가진 요청이 해당 컨트롤러 빈으로 매핑됨
- 빠르고 쉽게 URL 매핑정보를 지정할 수 있지만 컨트롤러의 개수가 많아지면 URL 정보가 XML 빈 선언이나 클래스 애노테이션 등에 분산되어 나타나므로
전체적인 매핑구조를 한눈에 파악하고 관리하기 불편함

#### ControllerBeanNameHandlerMapping

- 빈의 아이디나 빈 이름을 이용해 매핑해주는 핸들러 매핑 전략

```java
@Component("hello")
public class MyController implements Controller {
  ...
}
```

- 빈 아이디에 / 를 붙여줌
- /hello URL에 매핑해줌
- 디폴트 핸들러 매핑이 아니므로 사용하려면 전략 빈으로 등록해야 함
- 특정 전략 클래스를 빈으로 등록한 경우에는 디폴트 전략은 모두 무시됨
  - 주의해야 함

#### ControllerClassNameHandlerMapping

- 빈 이름 대신 클래스 이름을 URL에 매핑해주는 핸들러 매핑 클래스
- 클래스 이름을 모두 URL로 사용하지만 Controller로 끝나는 경우에는 Controller를 뺀 나머지 이름을 URL에 매핑해줌

```java
public class HelloController implements Controller {
  ...
}
```

#### SimpleUrlHandlerMapping

- BeanNameUrlHandlerMapping은 빈 이름에 매핑정보를 넣기 때문에 매핑정보를 관리하기 어렵다는 단점이 있음
- URL과 컨트롤러의 매핑정보를 한곳에 모아놓을 수 있는 핸들러 매핑 전략
- 매핑정보는 SImpleUrlHandlerMapping 빈의 프로퍼티에 넣어줌
- 디폴트가 아니기 때문에 빈을 등록해야 사용가능함
- 매핑할 컨트롤러 빈의 이름을 직접 적어줘야 하기 때문에 오타 등의 오류가 발생할 가능성이 있음

#### DefaultAnnotationHandlerMapping

- 컨트롤러 클래스나 메서드에 직접 부여하고 이를 이용해 매핑하는 전략
- @RequestMapping
- URL뿐 아니라 HTTP 메서드, 파라미터와 HTTP 헤더정보까지 매핑에 활용할 수 있다
  - URL이 같지만 메서드를 분리한다거나 파라미터가 지정됐을 때만 따로 분리하는 식의 컨트롤러 매핑이 가능
- 매핑 애노테이션의 사용 정책과 작성 기준을 잘 만들어두지 않으면, 개발자마다 제멋대로 매핑 방식을 적용해서 매핑정보가 지저분해지고 관리하기
힘들어질 수도 있으니 주의해야 함

#### 기타 공통 설정정보

- order
  - 핸들러 매핑은 한 개 이상을 동시에 사용할 수 있음
  - 이미 기본으로 두 개의 핸들러 매핑이 등록되어 있음
  - 두 개 이상의 핸들러 매핑을 적용했을 때는 URL 매핑정보가 중복되는 경우를 주의해야 함
  - 우선순위를 지정할 수 있음
  - Order 인터페이스가 제공하는 order 프로퍼티를 이용해 적용 우선순위를 지정할 수 있음
  - 디폴트 핸들러 매핑 전략에 order 프로퍼티를 설정해주려면 빈으로 등록 해야 함
- defaultHandler
  - 핸들러 매핑 빈의 defaultHandler 프로퍼티를 지정해두면 URL을 매핑할 대상을 찾지 못했을 경우 자동으로 디폴트 핸들러를 선택해줌
  - 핸들러 매핑에서 URL을 매핑할 컨트롤러를 찾지 못하면 404 에러가 발생하는데, 404 에러를 돌려주는 대신 디폴트 핸들러로 넘겨서 안내 메시지를 뿌려주는 것이 좋은 방법
- alwaysUseFullPath
  - 특별한 이유가 있어서 URL 전체를 사용해서 컨트롤러를 매핑하기 원한다면 핸들러 매핑 빈의 alwaysUseFullPath 프로퍼티를 true로 선언해주면 됨
- detectHandlersInAncestorContexts
  - 서블릿 컨텍스트의 부모 컨텍스트는 루트 컨텍스트임
  - 자식 컨텍스트의 빈은 부모 컨텍스트의 빈을 참조할 수 있음
  - 하지만 핸들러 매핑의 경우 현재 컨텍스트, 서블릿 컨텍스트 안에서만 매핑할 컨트롤러를 찾음
  - 웹 환경에 종속적인 컨트롤러 빈은 서블릿 컨텍스트에만 두는 것이 바람직하지만 필요할 경우 true로 바꾸면 됨

### 핸들러 인터셉터

- 핸들러 매핑의 역할은 기본적으로 URL과 요청정보로부터 컨트롤러 빈을 찾아주는 것
- 한 가지 중요한 기능이 더 있음
  - 핸들러 인터셉터를 적용해주는 것
- 핸들러 인터셉터는 DispatcherServlet이 컨트롤러를 호출하기 전과 후에 요청과 응답을 참조하거나 가공할 수 있는 일종의 필터
  - 서블릿 필터와 유사한 개념
- 핸들러 매핑은 DispatcherServlet으로부터 매핑 작업을 요청받으면 그 결과로 핸들러 실행 체인(HandlerExecutionChain)을 돌려줌
- 핸들러 실행 체인은 하나 이상의 핸들러 인터셉터를 거쳐서 컨트롤러가 실행될 수 있도록 구성되어 있음
- 핸들러 인터셉터를 전혀 등록해주지 않았다면 바로 컨트롤러가 실행됨
- 하나 이상의 핸들러 인터셉터를 지정했다면 순서에 따라 인터셉터를 거친 후에 컨트롤러가 호출됨
- HttpServletRequest, HttpServletResponse뿐 아니라 실행될 컨트롤러 빈 오브젝트, 컨트롤러가 돌려주는 ModelAndView, 발생한 예외 등을 제공받을 수
있기 때문에 서블릿 필터보다 더 정교하고 편리하게 인터셉터를 만들 수 있음
- 핸들러 인터셉터는 빈이기 때문에 DI를 통해 다른 빈을 활용할 수 있음

#### HandlerInterceptor

- 핸들러 인터셉터는 HandlerInterceptor 인터페이스를 구현해서 만듬
- boolean preHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Excpetion
  - 컨트롤러가 호출되기 전에 실행됨
  - 리턴 값이 true이면 핸들러 실행 체인의 다음 단계로 진행되지만, false 라면 작업을 중단하고 리턴하므로 컨트롤러와 남은
  인터셉터들은 실행되지 않음
- void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Excpetion
  - 컨트롤러를 실행하고 난 후에 호출됨
    - 일종의 후처리 작업
  - 컨트롤러가 돌려준 ModelAndView 타입의 정보가 제공돼서 컨트롤러 작업 결과를 참조하거나 조작할 수 있음
- void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
  - 모든 뷰에서 최종 결과를 생성하는 일을 포함한 모든 작업이 다 완료된 후에 실행됨
  - 요청 처리 중에 사용한 리소스를 반환해주기에 적당한 메서드

#### 핸들러 인터셉터 적용

- 핸들러 매핑 클래스를 빈으로 등록하고 매핑 빈의 interceptors 프로퍼티를 이용해 핸들러 인터셉터 빈의 레퍼런스를 넣어주면 됨

### 컨트롤러 확장

- Controller를 이용해 만든 기반 컨트롤러에는 개별 컨트롤러가 특정 클래스를 상속하도록 강제한다는 단점이 있음
- 이럴 경우 핸들러 어댑터를 직접 구현해서 아예 새로운 컨트롤러 타입을 도입하는 방법을 고려해봐야 함

```java
public interface HandlerAdapter {
    boolean supports(Object handler); // 이 핸들러 어댑터가 지원하는 컨트롤러 타입인지 확인
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
    // DispatcherServlet의 요청을 받아 컨트롤러를 실행해주는 메서드
    long getLastModified(HttpServletRequest request, Object handler);
    // HttpSErvlet의 getLastModified()를 지원해주는 메서드
}
```

-----------------------------

## 뷰

- MVC 아키텍처에서 모델이 가진 정보를 어떻게 표현해야 하는지에 대한 로직을 갖고 있는 컴포넌트
- 컨트롤러가 작업을 마친 뷰 정보를 ModelAndView 타입 오브젝트에 담아서 DispatcherServlet에 돌려주는 방법은 두 가지가 있음
  - View 타입의 오브젝트를 돌려주는 방법
  - 뷰 이름을 돌려주는 방법
- 뷰 이름을 돌려주는 경우는 뷰 이름으로부터 실제 사용할 뷰를 결정해주는 뷰 리졸버가 필요함

### 뷰

```java
public interface View {
    String getContentType();
    
    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
```

- View 인터페이스는 뷰 오브젝트가 생성하는 콘텐트의 타입 정보를 제공해주는 getContentType() 메서드와 모델을 전달받아 클라이언트에
돌려줄 결과물을 만들어주는 render() 메서드 두 가지로 구성됨
- View 인터페이스를 직접 구현해서 뷰를 만들어야 할 필요는 없음
  - 스프링이 웹에서 자주 사용되는 타입의 콘텐트를 생성해주는 다양한 뷰를 이미 구현해놓았기 때문
- 뷰를 사용하는 방법은 두 가지가 있음
  - 스프링이 제공하는 기반 뷰 클래스를 확장해서 코드로 뷰를 만드는 방법
    - 엑셀, PDF, RSS 피드와 같은 뷰는 콘텐트를 생성하는 API를 사용해서 뷰 로직을 작성함
  - 스프링이 제공하는 뷰를 활용하되 뷰 클래스 자체를 상속하거나 코드를 작성하지 않음
    - JSP나 프리마커 같은 템플릿 파일을 사용하거나 모델을 자동으로 뷰로 전환하는 로직을 적용하는 방법

#### InternalResourceView와 JstlView

- forward()나 include()를 이용하는 뷰
  - 다른 서블릿을 실행해서 그 결과를 현재 서블릿의 결과로 사용하거나 추가하는 방식
  - JSP 뷰를 적용할때 사용
- 스프링 MVC 컨트롤러에서 InternalResourceView를 만들어 ModelAndView에 넣어서 DispatcherServlet으로 넘겨주면
JSP로 포워딩하는 기능을 가진 뷰를 사용할 수 있음

```java
public class HelloController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("meesage", message);
        
        View view = new InternalResourceView("/WEB-INF/view/hello.jsp");
        
        return new ModelAndView(view, model);
    }
}
```

- forward() 대신 include()를 사용하려면 alwaysInclude 프로퍼티를 true로 바꿔주면 됨

#### RedirectView

- HttpServletResponse의 sendRedirect()를 호출해주는 기능을 가진 뷰
- 실제 뷰가 생성되는 것이 아닌 URL만 만들어져 다른 페이지로 리다이렉트됨
  - 모델정보가 있다면 URL 뒤에 파라미터로 추가됨
- 컨트롤러가 직접 RedirectView 오브젝트를 만들어서 리턴해도 되지만 뷰 리졸버가 인식할 수 있도록 redirect: 로 시작하는 뷰 이름을 사용하면 편리함
  -  return new ModelAndView("redirect:/main");

#### VelocityView, FreeMakerView

- 벨로시티와 프리마커라는 두 개의 대표적인 자바 템플릿 엔진을 뷰로 사용하게 해줌
  - JSP와 마찬가지로 컨트롤러에서 직접 뷰 오브젝트를 만드는 대신 VelocityViewResolver와 FreeMakerViewResolver를 통해
  자동으로 뷰가 만들어져 사용되게 하는 편이 남

#### MarshallingView

- application/xml 타입의 XML 컨텐츠를 작성하게 해주는 뷰
- 미리 준비해둔 마샬러 빈을 지정하고 모델에서 변환에 사용할 오브젝트를 지정해주면 OXM 마샬러를 통해 모델 오브젝트를 XML로 변환해서 뷰의 결과로
사용할 수 있음
- 마샬링 뷰를 컨트롤러에서 직접 DI 받는 대신 뷰 리졸버를 이용해서 선택할 수 있음
  - 컨트롤러에서 뷰 이름만 리턴하고 뷰 이름과 마샬링 뷰 빈을 연결해주는 정보를 별도의 설정파일이나 XML 문서를 통해 지정해주는 방법을 사용함

#### AbstractExcelView, AbstractJExcelView, AbstractPdfView

- 엑셀과 PDF 문서를 만들어주는 뷰
- Abstract가 붙어 있으니 상속을 해서 코드를 구현해야 하는 뷰
- AbstractExcelView는 아파치 POI 라이브러리를 이용해 엑셀 뷰를 만들고 AbstractJExcelView는 JExcelAPI를 사용해 엑셀 문서를 만들고
AbstractPdfView는 iText 프레임워크 API로 PDF 문서를 생성해줌

#### AbstractAtomFeedView, AbstractRssFeedView

- application/atom+xml, application/rss+xml 타입의 피드 문서를 생성해주는 뷰
- 상속을 통해 피드정보를 생성하는 메서드를 직접 구현해주어야 함

### 뷰 리졸버

- 뷰 리졸버는 핸들러 매핑이 URL로부터 컨트롤러를 찾아주는 것처럼, 뷰 이름으로부터 사용할 뷰 오브젝트를 찾아줌
- 뷰 리졸버는 ViewResolver 인터페이스를 구현해서 만듬
- 뷰 리졸버를 빈으로 등록하지 않으면 DispatcherServlet의 디폴트 뷰 리졸버인 InternalResourceViewResolver가 사용됨
- 핸들러 매핑과 마찬가지로 뷰 리졸버도 하나 이상을 빈으로 등록해서 사용할 수 있음
  - order 프로퍼티를 이용해 뷰 리졸버의 적용 순서를 지정해주는 것이 좋음

#### InternalResourceViewResolver

- 뷰 리졸버를 지정하지 않았을 때 자동등록되는 디폴트 뷰 리졸버
  - 주로 JSP를 뷰로 사용하고자 할 때 쓰임
- 디폴트 상태의 InternalResourceViewResolver를 사용할 경우, /WEB-INF/view/hello.jsp를 뷰로 이용하려면 전체 경로를
다 적어줘야 함
  - prefix, suffix 프로퍼티를 이용해 항상 앞뒤에 붙는 내용을 생략할 수 있음
  - 프로퍼티 설정을 해주려면 결국 InternalResourceViewResolver를 직접 빈으로 등록하는 수 밖에 없음

#### VelocityViewResolver, FreeMarkerViewResolver

- 템플릿 엔진 기반의 뷰인 VelocityView와 FreedMarkerView를 사용하게 해주는 뷰 리졸버
- 사용 방법은 InternalResourceViewResolver와 비슷함

#### ResourceBundleViewResolver, XmlViewResolver, BeanNameViewResolver

- 여러 가지 종류의 뷰를 혼용하거나 뷰의 종류를 코드 밖에서 변경해줘야 하는 경우에는 외부 리소스 파일에 각 뷰 이름에 해당하는
뷰 클래스와 설정을 담아두고 이를 참조하는 ResourceBundleViewResolver와 XmlViewResolver를 사용하면 됨

```properties
hello.(class)=org.springframework.web.servlet.view.JstlView
hello.url=/WEB-INF/view/hello.jsp

main.(class)=org.springframework.web.servlet.view.velocity.VelocityView
main.url=main.vm
```

#### ContentNegotiatingViewResolver

- 직접 뷰 이름으로 뷰 오브젝트를 찾아주지 않는 대신 미디어 타입 정보를 활용해서 다른 뷰 리졸버에게 뷰를 찾도록 위임한 후에 가장
적절한 뷰를 선정해서 돌려줌
  - 뷰 리졸버를 결정해주는 리졸버
- 스프링 3.0에서 새로 추가된 뷰 리졸버

-------------

## 기타 전략

### 핸들러 예외 리졸버

- HandlerExceptionResolver는 컨트롤러의 작업 중에 발생한 예외를 어떻게 처리할지 결정하는 전략
- 컨트롤러나 그 뒤의 계층에서 던져진 예외는 DispatcherServlet이 일단 전달받은 뒤에 다시 서블릿 밖으로 던져서 서블릿 컨테이너가 처리하게 함
  - 다른 설정을 하지 않았다면 브라우저에 HTTP Status 500 Internal Server Error 같은 메시지가 출력됨
  - web.xml에 error-page를 지정해서 예외가 발생했을 때 JSP 안내 페이지 등을 보여줄 수도 있음
- 핸들러 예외 리졸버가 등록되어 있다면 DispatcherServlet은 먼저 핸들러 예외 리졸버에게 해당 예외를 처리할 수 있는지 확인함
  - 예외를 처리해주는 핸들러 예외 리졸버가 있다면 예외는 DispatcherServlet 밖으로 던져지지 않고 해당 핸들러 예외 리졸버가 처리함

```java
public interface HandlerExceptionResolver {
    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);
}
```

- 처리 불가능한 예외라면 null을 리턴함
- 스프링은 총 네 개의 HandlerExceptionResolver 구현 전략을 제공하고, 그 중 세 개는 디폴트로 등록되도록 설정함

#### AnnotationMethodHandlerExceptionResolver

- 예외가 발생한 컨트롤러 내의 메서드 중에서 @ExceptionHandler 애노테이션이 붙은 메서드를 찾아 예외처리를 맡겨주는 핸들러 예외 리졸버
  - 스프링 3.0에 새로 추가됨
  - 디폴트 핸들러 예외 리졸버
- 메서드를 실행하는 중에 @ExceptionHandler에 지정한 타입의 예외가 발생하는 경우에 호출됨
- 예외처리용 메서드는 모델과 뷰를 리턴할 수 있는데, 모델과 뷰가 정상적으로 리턴되면 DispatcherServlet은 마치 컨트롤러에서 ModelAndView가
돌려진 것처럼 뷰를 통해 결과를 만들어줌
- 특정 컨트롤러의 작업 중에 발생하는 예외만 처리하는 예외 핸들러를 만들고 싶다면 이 방법이 가장 편리

#### ResponseStatusExceptionResolver

- 두 번째 디폴트 핸들러 예외 전략
- 예외를 특정 HTTP 응답 상태 코드로 전환해주는 것
  - HTTP 500 에러 대신 의미 있는 HTTP 응답 상태를 돌려주는 방법
- 예외 클래스에 @ResponseStatus를 붙이고 HttpStatus에 정의되어 있는 HTTP 응답 상태 값을 value 엘리먼트에 지정
  - 필요하면 reason에 자세한 설명을 넣을 수도 있음
- ResponseStatusExceptionResolver는 발생한 예외의 클래스에 @ResponseStatus가 있는지 확인하고, 만약 있다면 애노테이션에 지정해둔 HTTP 응답 상태 코드를
클라이언트에 전달함
- 단점은 직접 @ResponseStatus를 붙여줄 수 있는 예외 클래스를 만들어 사용해야 한다는 것
  - 기존에 정의된 예외 클래스에는 바로 적용할 수가 없음
- @ResponseStatus를 직접 부여할 수 없는 기존 예외가 발생했을 때 HTTP 응답 상태를 지정해주려면 @ExceptionHandler 방식의 핸들러 메서드를 사용하면 됨
  - 기존의 예외를 처리하는 @ExceptionHandler 메서드를 만들고 리턴 타입은 void
  - HttpServletResponse를 파라미터로 전달받아서 setStatus() 메서드를 이용해 응답 상태와 에러 메시지 등을 설정

#### DefaultHandlerExceptionResolver

- 디폴트로 등록
- 다른 디폴트 예외 리졸버에서 처리하지 못한 예외를 다루는 마지막 핸들러 예외 리졸버
- 스프링에서 내부적으로 발생하는 주요 예외를 처리해주는 표준 예외처리 로직을 담고있음
- 스프링 MVC 내부에서 발생하는 예외를 다루는 것이므로 크게 신경쓰지 않아도 되나, 다른 핸들러 예외 리졸버를 빈으로 등록한다면 디폴트 예외 리졸버가
자동으로 적용되지 않기에 함께 등록해주어야 함

#### SimpleMappingExceptionResolver

- web.xml의 error-page와 비슷하게 예외를 처리할 뷰를 지정할 수 있게 해줌

### 지역정보 리졸버

- LocalResolver는 애플리케이션에서 사용하는 지역정보(Locale)를 결정하는 전략
- 디폴트로는 AcceptHeaderLocalResolver
  - HTTP 헤더의 지역정보를 그대로 사용함

### 멀티파트 리졸버

- 파일 업로드와 같이 멀티파트 포맷의 요청정보를 처리하는 전략을 설정할 수 있음
- DispatcherServlet은 클라이언트로부터 멀티파트 요청을 받으면 멀티파트 리졸버에게 요청해서 HttpServletRequest의 확장 타입인
MultipartHttpServletRequest 오브젝트로 전환함
  - MultipartHttpServletRequest에는 멀티파트를 디코딩한 내용과 이를 참조하거나 조작할 수 있는 기능이 있음
- 멀티파트 리졸버 전략은 디폴트로 등록된 것이 없기 때문에 빈을 등록해주어야 함

--------------

## 스프링 3.1의 MVC

### 플래시 맵 매니저 전략

#### 플래시 맵

- 플래시 애트리뷰트를 저장하는 맵
  - 플래시 애트리뷰트란 하나의 요청에서 생성되어 다음 요청으로 전달되는 정보
  - 웹 요청 사이에 전달되는 정보라면 세션을 생각할 수 있겠지만 플래시 애트리뷰트는 세션에 저장되는 정보처럼 오래 유지되지 않음
  - 다음 요청에서 한 번 사용되고 바로 제거되는 특징이 있음
  - Post/Redirect/Get 패턴을 적용하는 경우 POST 단계의 작업 결과 메시지를 리다이렉트된 페이지로 전달할 때 주로 사용함
- AJAX를 이용한 서버 폴링처럼 사용자의 액션 없이 수시로 서버로 요청을 보내는 기능을 가진 웹 페이지에선 자칫하면 POST와 REDIRECT 사이에 다른 요청이 끼어들
위험이 있음
  - 플래시 애트리뷰트가 특정 페이지의 요청을 처리할 때만 사용되도록 URL 경로나 파라미터 같은 URL 조건을 지정할 필요가 있음
- 플래시 애트리뷰트가 저장된 뒤 브라우저를 중간에 닫거나 강제로 다른 페이지로 이동해 서버에 저장된 플래시 애트리뷰트가 사용되고 제거되지 않아 메모리 누수 발생 위험이 있음
  - 플래시 애트리뷰트에 제한시간을 두고 시간이 지나면 강제로 ㅈ제거할 필요도 있음

```java
FlashMap fm = new FlashMap();
fm.put("message", "abcd");
fm.setTargetRequestPath("/user/list");
fm.startExpirationPeriod(10); // 초
```

#### 플래시 맵 매니저

- 컨트롤러에서 만들어진 플래시 맵 오브젝트는 요청이 끝나기 전에 서버 어딘가에 저장돼야 함
  - 다음 요청이 올 때까지 정보를 유지했다가 사용할 수 있음
- 플래시 맵을 저장, 유지, 조회, 제거하는 등의 작업을 담당하는 오브젝트를 플래시 맵 매니저라 함
  - FlashMapManager 인터페이스를 구현해서 만듬
- 플래시 맵 매니저는 DispatcherServlet이 미리 준비해둔 것을 사용하면 됨
  - RequestContextUtils.getFlashMapManager()를 사용해서 가져올 수 있음

```java
FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
fm.put("message", "abcd");
fm.setTargetRequestPath("/user/list");
fm.startExpirationPeriod(10);

Map<?, String> fm = RequestContextUtils.getInputFlashMap(request);
String message = fm.get("message");
model.addAttribute("flashMessage", message);
```

#### 플래시 맵 매니저 전략

- 플래시 맵 정보를 저장하고 가져오는 방법이 스프링 3.1에 새로운 DispatcherServlet 전략으로 추가됨

### WebApplicationInitializer를 이용한 컨텍스트 등록

- 스프링 3.1에서는 ServletContainerInitializer를 이용하면 스프링 컨텍스트 설정과 등록 작업에 자바 코드를 이용할 수 있음

```java
public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
```

#### 루트 웹 컨텍스트 등록


```xml
<listner>
  <listner-class>org.springframework.web.context.ContextLoaderListener
  </listner-class>
</listner>
```

```java
ServletContextListener listener = new ContextLoaderListener();
servletContext.addListener(listener);
```