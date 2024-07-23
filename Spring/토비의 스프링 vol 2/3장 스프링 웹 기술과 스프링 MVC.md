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

