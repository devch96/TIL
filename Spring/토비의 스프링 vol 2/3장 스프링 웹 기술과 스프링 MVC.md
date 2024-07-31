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