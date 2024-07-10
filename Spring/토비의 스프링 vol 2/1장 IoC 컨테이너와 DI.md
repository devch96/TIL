# IoC 컨테이너와 DI

- 스프링이 공개되고 자바 개발자에게 DI의 유용성을 알리며 보급된 이후로 IoC/DI는 사실상 자바의 표준 프로그래밍 모델로
발전함
- 스프링의 DI 설정 방식과 IoC 컨테이너의 활용 방법은 매우 다양

## IoC 컨테이너: 빈 팩토리와 애플리케이션 컨텍스트

- 스프링 애플리케이션에는 오브젝트의 생성과 관계설정, 사용, 제거 등의 작업을 애플리케이션 코드 대신 독립된 컨테이너가 담당함
- 컨테이너가 코드 대신 오브젝트에 대한 제어권을 갖고 있다고 해서 IoC(Inversion of Control)이라 부름
- 그래서 스프링 컨테이너를 IoC 컨테이너라고 함
- 스프링에선 IoC를 담당하는 컨테이너를 빈 팩토리 또는 애플리케이션 컨텍스트라고 부르기도 함
  - 오브젝트의 생성과 오브젝트 사이의 런타임 관계를 설정하는 DI 관점으로 볼 때는 컨테이너를 빈 팩토리라 부름
  - DI를 위한 빈 팩토리에 엔터프라이즈 애플리케이션을 개발하는 데 필요한 여러 가지 컨테이너 기능을 추가한 것을 애플리케이션 컨텍스트라고 부름
- 스프링의 IoC 컨테이너는 일반적으로 애플리케이션 컨텍스트를 의미
- 스프링의 빈 팩토리와 애플리케이션 컨텍스트는 각각 기능을 대표하는 BeanFactory와 ApplicationContext라는 두 개의 인터페이스로 정의됨
  - ApplicationContext는 BeanFactory를 상속한 서브인터페이스
- 스프링 컨테이너 또는 IoC 컨테이너라고 말하는 것은 ApplicationContext 인터페이스를 구현한 클래스의 오브젝트
- 스프링 애플리케이션은 하나 이상의 애플리케이션 컨텍스트 오브젝트를 갖고 있음

### IoC 컨테이너를 이용해 애플리케이션 만들기

- 가장 간단하게 IoC 컨테이너를 만드는 방법은 ApplicationContext 구현 클래스의 인스턴스를 만드는 것

```java
StaticApplicationContext ac = new StaticApplicationContext();
```

- 만들어진 컨테이너가 본격적인 IoC 컨테이너로 동작하려면 두 가지가 필요하다
  - POJO 클래스
  - 설정 메타정보

#### POJO 클래스

- 애플리케이션의 핵심 코드를 담고 있는 POJO 클래스
- 각각의 POJO는 특정 기술과 스펙에서 독립적이여야 하고 의존관계에 있는 다른 POJO와 느슨한 결합을 갖도록 만들어야 함
- 지정된 사람에게 인사를 할 수 있는 Hello 클래스, 메시지를 받아서 이를 출력하는 Printer 인터페이스를 구현한 StringPrinter 클래스

```java
public class Hello {
    String name;
    Printer printer;
    
    public String sayHello() {
        return "Hello " + name;
    }
    
    public void print() {
        this.printer.print(sayHello());
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
}

public interface Printer {
    void print(String message);
}

public class StringPrinter implements Printer {
    private StringBuffer buffer = new StringBuffer();
    
    public void print(String message) {
        this.buffer.append(message);
    }
    
    public String toString() {
        return this.buffer.toString();
    }
}
```

#### 설정 메타정보

- POJO 클래스들 중에 애플리케이션에서 사용할 것을 선정하고 이를 IoC 컨테이너가 제어할 수 있도록 적절한 메타정보를 만들어
제공하는 작업
- IoC 컨테이너의 가장 기초적인 역할은 오브젝트를 생성하고 이를 관리하는 것
  - 스프링 컨테이너가 관리하는 오브젝트를 빈(bean)이라 부름
- IoC 컨테이너가 필요로 하는 설정 메타정보는 이 빈을 어떻게 만들고 어떻게 동작하게 할 것인가에 관한 정보
- 스프링의 설정 메타정보는 XML 파일이 아님
- 스프링의 설정 메타정보는 BeanDefinition 인터페이스로 표현되는 순수한 추상 정보
- BeanDefinition 인터페이스로 정의되는 IoC 컨테이너가 사용하는 빈 메타정보
  - 빈 아이디, 이름, 별칭 : 오브젝트를 구분할 수 있는 식별자
  - 클래스 또는 클래스 이름 : 빈으로 만들 POJO 클래스 또는 서비스 클래스 정보
  - 스코프 : 싱글톤, 프로토타입과 같은 빈의 생성 방식과 존재 범위
  - 프로퍼티 값 또는 참조 : DI에 사용할 프로퍼티 이름과 값 또는 참조하는 빈의 이름
  - 생성자 파라미터 값 또는 참조 : DI에 사용할 생성자 파라미터 이름과 값 또는 참조할 빈의 이름
  - 지연된 로딩 여부, 우선 빈 여부, 자동와이어링 여부, 부모 빈 정보, 빈팩토리 이름 등

```java
StaticApplicationContext ac = new StaticApplicationContext();
ac.registerSingleton("hello1", Hello.class);
Hello hello1 = ac.getBean("hello1", Hello.class);
assertThat(hello1, is(notNullValue()));
```

- IoC 컨테이너가 관리하는 빈은 오브젝트 단위지 클래스 단위가 아님
  - 보통 클래스당 하나의 오브젝트(싱글톤)으로 만들긴 하지만 여러 개 등록하는 경우도 있음
    - DB가 여러 개라면 같은 SimpleDriverDataSource 클래스로 된 빈을 여러 개 등록함

```java
BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
helloDef.getPropertyValues().addPropertyValue("name", "Spring");
ac.registerBeanDefinition("hello2", helloDef);

Hello hello2 = ac.getBean("hello2", Hello.class);
assertThat(hello2.sayHello(), is("Hello Spring"));
assertThat(hello1, is(not(hello2)));
assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
```

- 빈 생성을 위한 설정 메타정보는 RootBeanDefinition을 이용해 정의하고, DI는 다른 빈의 아이디를 프로퍼티에 레퍼런스 타입으로 넣어준다.

```java
@Test
void registerBeanWithDependency() {
    StaticApplicationContext ac = new StaticApplicationContext();

    ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

    BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
    helloDef.getPropertyValues().addPropertyValue("name", "Spring");
    helloDef.getPropertyValues().addPropertyValue("printer",
            new RuntimeBeanReference("printer"));

    ac.registerBeanDefinition("hello", helloDef);

    Hello hello = ac.getBean("hello", Hello.class);
    hello.print();

    assertThat(ac.getBean("printer").toString(), is("Hello Spring"));

}
```

- IoC 컨테이너는 빈 오브젝트가 생성되고 관계가 만들어지면 그 뒤로는 거의 관여하지 않음
  - 기본적으로 싱글톤 빈은 애플리케이션 컨텍스트의 초기화 작업 중에 모두 만들어짐

### IoC 컨테이너의 종류와 사용 방법

- ApplicationContext 인터페이스를 바르게 구현했다면 어떤 클래스든 스프링의 IoC 컨테이너로 사용할 수 있음
  - 직접 개발하는 일은 없을 것
  - 다양한 용도로 쓸 수 있는 십여개의 ApplicationContext 구현 클래스가 존재함

#### StaticApplicationContext

- 코드를 통해 빈 메타정보를 등록하기 위해 사용함
- 스프링의 기능에 대한 학습 테스트를 만들 때를 제외하면 실제로 사용되지 않음
  - 특정 파일 포맷에 종속되지 않고 오브젝트로 표현되는 순수한 메타정보를 사용한다는 걸 보여주기 위해 사용

#### GenericApplicationContext

- 가장 일반적인 애플리케이션 컨텍스트의 구현 클래스
  - 실전에서 사용될 수 있는 모든 기능을 갖추고 있는 애플리케이션 컨텍스트
- 컨테이너의 주요 기능을 DI를 통해 확장할 수 있도록 설계되어 있음
- StaticApplicationContext와 달리 XML 파일과 같은 외부의 리소스에 있는 빈 설정 메타정보를 리더를 통해 읽어들여서 메타정보로 전환해서 사용함
  - 빈 설정 메타정보를 읽어서 BeanDefinition으로 변환하는 기능을 가진 오브젝트는 BeanDefinitionReader 인터페이스를 구현해 만듬
    - XML로 작성된 빈 설정 정보 : XmlBeanDefinitionReader
- 빈 설정 리더를 만들기만 하면 어떤 형태로도 빈 설정 메타정보를 작성할 수 있음
  - DB의 테이블에 빈 설정정보 저장하고 이를 읽음
  - 원격 서버로부터 정보를 읽음
- 스프링에서는 대표적으로 XML 파일, 자바 소스코드 애노테이션, 자바 클래스 세 가지 방식으로 빈 설정 메타정보를 작성할 수 있음
  - 제한되는것은 아님
- 스프링 컨테이너 자체를 확장해서 새로운 프레임워크를 만들거나, 스프링을 사용하는 독립형 애플리케이션을 만들지 않는 한
GenericApplicationContext를 직접 이용할 필요는 없음
- JUnit 테스트는 테스트 내에서 사용할 수 있도록 애플리케이션 컨텍스트를 자동으로 만들어주는데 이때 사용되는 것이 GenericApplicationContext

#### GenericXmlApplicationContext

- GenericApplicationContext를 사용하는 경우에 번거롭게 XmlBeanDefinitionReader를 직접 만들지 말고
GenericXmlApplicationContext를 사용

#### WebApplicationContext

- 스프링 애플리케이션에서 가장 많이 사용되는 애플리케이션 컨텍스트
- ApplicationContext 인터페이스를 확장한 WebApplicationContext를 구현한 클래스를 사용
- 웹 환경에서 사용할 때 필요한 기능이 추가된 애플리케이션 컨텍스트
- 스프링 IoC 컨테이너는 빈 설정 메타정보를 이용해 빈 오브젝트르르 만들고 DI 작업을 수행함
  - 그것만으로는 애플리케이션이 동작하지 않음
  - main() 메서드처럼 어디에선가 특정 빈 오브젝트의 메서드를 호출해야 함
- 웹 애플리케이션은 동작하는 방식이 근본적으로 달림
  - 독립 자바 프로그램은 자바 VM에게 main() 메서드를 가진 클래스를 시작시켜 달라고 요청 가능함
  - 웹은 main() 메서드를 호출할 방법이 없음
  - 사용자도 여럿이며 동시에 사용함
- 웹 환경에서는 main() 메서드 대신 서블릿 컨테이너가 브라우저로부터 오는 HTTP 요청을 받아서 해당 요청에 매핑되어 있는 서블릿을 실행해주는 방식으로 동작함
  - 서블릿이 일종의 main() 메서드와 같은 역할을 함
- 웹 애플리케이션에서 스프링 애플리케이션을 가동시키는 방법은 main() 메서드 역할을 하는 서블릿을 만들어두고 미리 애플리케이션 컨텍스트를 생성해둔 다음
요청이 서블릿으로 들어올 때마다 getBean() 으로 필요한 빈을 가져와 정해진 메서드를 실행해주면 됨
- 스프링은 웹 환경에서 애플리케이션 컨텍스트를 생성하고 설정 메타정보로 초기화해주고, 클라이언트로부터 들어오는
요청마다 적절한 빈을 찾아서 이를 실행해주는 기능을 가진 DispatcherServlet이라는 이름의 서블릿을 제공함

### IoC 컨테이너 계층구조

- 빈을 담아둘 IoC 컨테이너는 애플리케이션마다 하나씩이면 충분
  - 빈의 개수가 많아져서 설정파일이 커지는 게 문제라면 파일을 여러 개로 쪼개서 만들고 하나의 애플리케이션 컨텍스트가 여러 개의 설정파일을 사용하게 하면 그만
- 한 개 이상의 IoC 컨테이너를 만들어두고 사용해야 할 경우가 있음
  - 트리 모양의 계층구조를 만들 때

#### 부모 컨텍스트를 이용한 계층구조 효과

- 계층구조 안의 모든 컨텍스트는 각자 독립적인 설정정보를 이용해 빈 오브젝트를 만들고 관리
- 각자 독립적으로 자신이 관리하는 빈을 갖고 있긴 하지만 DI를 위해 빈을 찾을 때는 부모 애플리케이션 컨텍스트의 빈까지 모두 검색
  - 자신이 관리하는 빈 중 필요한 빈을 찾고 없으면 부모 컨텍스트, 부모가 없으면 부모의 부모, 루트까지
  - 자식 컨텍스트에게는 요청하지 않음
  - 같은 레벨에 있는 형제 컨텍스트의 빈도 찾을 수 없음
- 미리 만들어진 애플리케이션 컨텍스트의 설정을 그대로 가져다가 사용하면서 그중 일부 빈만 설정을 변경하고 싶다면 애플리케이션 컨텍스트를 두 개 만들어서
하위 컨텍스트에서 바꾸고 싶은 빈들을 다시 설정하면 됨
- 기존 설정을 수정하지 않고 사용하지만 일부 빈 구성을 바꾸고 싶을 경우 애플리케이션 컨텍스트 계층구조를 만드는 방법이 편리함
  - 또 다른 경우는 애플리케이션 컨텍스트가 공유하는 설정을 만들기 위해

### 웹 애플리케이션의 IoC 컨테이너 구성

- 서버에서 동작하는 애플리케이션에서 스프링 IoC 컨테이너를 사용하는 방법은 크게 세가지
  - 두 가지 방법은 웹 모듈 안에 컨테이너를 두는것, 하나는 엔터프라이즈 애플리케이션 레벨에 두는 것
- 자바 서버 기술이 등장했던 초기에는 URL 당 하나의 서블릿을 만들어 등록하고 각각 독립적인 기능을 담당하게 했음
- 최근에는 많은 웹 요청을 한 번에 받을 수 있는 대표 서블릿을 등록해두고 공통적인 선행 작업을 수행하게 한 후에, 각 요청의 기능을 담당하는
핸들러라고 불리는 클래스를 호출하는 방식으로 개발
  - 몇 개의 서블릿이 중앙집중식으로 모든 요청을 다 받아서 처리하는 방식
  - 프론트 컨트롤러 패턴
- 웹 애플리케이션 안에서 동작하는 IoC 컨테이너는 두 가지 방법으로 만들어짐
  - 스프링 애플리케이션의 요청을 처리하는 서블릿 안에서 만들어지는 것
  - 웹 애플리케이션 레벨에서 만들어지는 것

#### 웹 애플리케이션의 컨텍스트 계층구조

- 웹 애플리케이션 레벨에 등록되는 컨테이너는 보통 루트 웹 애플리케이션 컨텍스트라고 불림
  - 서블릿 레벨에 등록되는 컨테이너들의 부모 컨테이너가 됨
  - 최상단에 위치
- 보통은 여러 개의 서블릿 컨테이너가 아닌 하나의 컨테이너를 사용함
  - 1:1구조
- 여러 개의 자식 컨텍스트가 아닌데 왜 계층구조?
  - 전체 애플리케이션에서 웹 기술에 의존적인 부분과 그렇지 않은 부분을 구분하기 위해서
  - 스프링을 이용하는 웹 애플리케이션이라고 해서 반드시 웹 기술을 사용해야 하는 건 아님
    - 데이터 액세스 계층이나 서비스 계층은 스프링 기술을 사용하고 스프링 빈으로 만들지만
    웹을 담당하는 프레젠테이션 계층은 스프링 외의 기술을 사용하는 경우도 있음
- JSP나 스트럿츠, AJAX 엔진 같은 곳에서 어떻게 루트 애플리케이션 컨텍스트로 접근?
  - 스프링은 웹 애플리에키션마다 하나씩 존재하는 서블릿 컨텍스트를 통해 루트 애플리케이션 컨텍스트에 접근할 수 있는 방법을 제공

```java
WebApplicationContextUtils.getWebApplicationContext(ServletContext sc);
```

- ServletContext는 웹 애플리케이션마다 하나씩 만들어지는 것으로 서블릿의 런타임 환경정보를 갖고 있음
  - HttpServletRequest나 HttpSession 오브젝트를 갖고있다면 ServletContext를 가져올 수 있음

#### 웹 애플리케이션 컨텍스트 구성 방법

- 서블릿 컨텍스트와 루트 애플리케이션 컨텍스트 계층구조
  - 가장 많이 사용되는 기본적인 구성 방법
  - 스프링 웹 기술을 사용하는 경우 웹 관련 빈들은 서블릿의 컨텍스트에 두고 나머지는 루트 애플리케이션 컨텍스트에 등록
  - 스프링 웹 외에도 기타 웹 프레임워크나 HTTP 요청을 통해 동작하는 각종 서비스를 함께 사용할 수 있음
- 루트 애플리케이션 컨텍스트 단일 구조
  - 스프링 웹 기술을 사용하지 않고 서드파티 웹 프레임워크나 서비스 엔진만을 사용해서 프레젠테이션 계층을 만든다면 스프링 서블릿을 둘 이유가 없음
- 서블릿 컨텍스트 단일 구조
  - 스프링 웹 기술을 사용하면서 스프링 외의 프레임워크나 서비스 엔진에서 스프링의 빈을 이용할 생각이 아니라면 루트 애플리케이션 컨텍스트를 생략할 수 있음

#### 루트 애플리케이션 컨텍스트 등록

- 서블릿의 이벤트 리스너를 이용하는 것
- 스프링은 웹 애플리케이션의 시작과 종료 시 발생하는 이벤트를 처리하는 리스너인 ServletContextListener를 이용
  - ServletContextListener 인터페이스를 구현한 리스너는 웹 애플리케이션 전체에 적용 가능한 DB 연결 기능이나 로깅 같은 서비스를 만드는 데 유용하게 쓰임

-------------

## IoC/DI를 위한 빈 설정 메타정보 작성

- IoC 컨테이너의 가장 기본적인 역할은 코드를 대신해 애플리케이션을 구성하는 오브젝트를 생성하고 관리하는 것
  - POJO로 만들어진 애플리케이션 클래스와 서비스 오브젝트들이 그 대상
- 컨테이너는 자신이 만들 오브젝트를 빈 설정 메타정보를 통해 빈의 크랠스와 이름을 제공받음
  - 파일이나 애노테이션 같은 리소스로부터 전용 리더를 통해 읽혀 BeanDefinition 타입의 오브젝트로 변환됨
  - IoC 컨테이너가 직접 사용하는 BeanDefinition은 순수한 오브젝트로 표현되는 빈 생성 정보

### 빈 설정 메타정보

- BeanDefinition에는 IoC 컨테이너가 빈을 만들 때 필요한 핵심 정보가 담겨 있음
  - 몇 가지 필수항목을 제외하면 디폴트 값이 그대로 적용 됨
- BeanDefinition은 재사용 가능함
  - 설정 메타저보가 같지만 이름이 다른 여러 개의 빈 오브젝트를 만들 수 있기 때문
  - 따라서 빈의 이름이나 아이디를 나타내는 정보는 포함되지 않음
  - IoC 컨테이너에 BeanDefinition 정보가 등록될 때 이름을 부여해줌

#### 빈 설정 메타정보 항목

- beanClassName
  - 빈 오브젝트의 클래스 이름
  - 빈 오브젝트는 이 클래스의 인스턴스
  - 필수항목
- parentName
  - 빈 메타정보를 상속받을 부모 Definition의 이름
- factoryBeanName
  - 팩토리 역할을 하는 빈을 이용해 빈 오브젝트를 생성하는 경우
- factoryMethodName
  - 다른 빈 또는 클래스의 메서드를 통해 빈 오브젝트를 생성하는 경우
- scope
  - 빈 오브젝트의 생명주기를 결정하는 스코프
  - Default: 싱글톤
- lazyInit
  - 빈 오브젝트의 생성을 최대한 지연할 것인지를 지정
  - true이면 컨테이너는 빈 오브젝트의 생성을 꼭 필요한 시점까지 미룸
  - Default: false
- dependsOn
  - 먼저 만들어져야 하는 빈을 지정할 수 있음
  - 빈 오브젝트의 생성 순서가 보장돼야 하는 경우 이용
- autowireCandidate
  - 명시적인 설정이 없어도 미리 정해진 규칙을 가지고 자동으로 DI 후보를 결정하는 자동와이어링의 대상으로 포함시킬지의 여부
  - Default: true
- primary
  - 자동와이어링 작업 중에 DI 대상 후보가 여러 개가 발생하는 경우에 최종 선택의 우선권을 부여할지 여부
  - primary가 지정된 빈이 없이 여러 개의 후보가 존재하면 자동와이어링 예외가 발생함
  - Default: false
- abstract
  - 메타정보 상속에만 사용할 추상 빈으로 만들지 여부
  - 추상 빈이 되면 그 자체는 오브젝트가 생성되지 않고 다른 빈의 부모 빈으로만 사용됨
  - Default: false
- autowireMode
  - 오토와이어링 전략, 이름, 타입, 생성자, 자동인식 등의 방법
- dependencyCheck
  - 프로퍼티 값 또는 레퍼런스가 모두 설정되어 있는지를 검증하는 작업
- initMethod
  - 빈이 생성되고 DI를 마친 뒤에 실행할 초기화 메서드의 이름
- destroyMethod
  - 빈의 생멍주기가 다 돼서 제거하기 전에 호출할 메서드의 이름
- propertyValues
  - 프로퍼티의 이름과 설정 값 또는 레퍼런스, 수정자 메서드를 통한 DI 작업에서 사용
- constructorArgumentValues
  - 생성자의 이름과 설정 값 또는 레퍼런스
- annotationMetadata
  - 빈 클래스에 담긴 애노테이션과 그 애트리뷰트 값, 애노테이션을 이용하는 설정에서 활용

### 빈 등록 방법

- 빈 등록은 빈 메타정보를 작성해서 컨테이너에게 건네주면 됨
- 가장 직접적이고 원시적 방법은 BeanDefinition 구현 오브젝트를 직접 생성하는 것
  - 스프링을 확장해서 프레임워크를 만들거나 스프링의 내부 동작원리를 학습하려는게 목적이 아니라면 별로
- 보통 XML 문서, 프로퍼티 파일, 소스코드 애노테이션과 같은 외부 리소스로 빈 메타정보를 작성하고 적절한 리더나 변환기를 통해 애플리케이션 컨텍스트가
사용할 수 있는 정보로 변환해주는 방법을 사용

#### XML: bean 태그

- bean 태그를 이용하면 스프링 빈 메타정보의 거의 모든 항목을 지정할 수 있으므로 세밀한 제어가 가능
- beans라는 루트 엘리먼트를 갖는 XML 문서에 포함됨
- 기본적으로 id와 class 두 개의 애트리뷰트가 필요

```xml
<bean id="hello" class="springbook.learningtest.spring.ioc.bean.Hello">
  <property name="printer">
    <bean class="springbook.learningtest.spring.ioc.bean.StringPrinter"/>
  </property>
</bean>
```

#### XML: 네임스페이스와 전용 태그

- 스프링 빈을 분류하자면 크게 애플리케이션의 핵심 코드를 담은 컴포넌트와 서비스 또는 커넽이너 설정을 위한 빈으로 구분할 수 있음
- hello 빈은 개발자가 작성한 애플리케이션의 코드로 만든 빈
  - 직접 코드를 작성해서 만든 클래스를 사용하고 애플리케이션의 핵심 로직을 담음
- AOP를 사용한 빈은 애플리케이션의 핵심 로직을 담은 컴포넌트가 아닌 컨텍스트의 빈 생성 작업중에 사용하는 AOP를 위한 포인트컷 오브젝트 정의
  - 기술적인 설정정보를 담고 있음

```java
<aop:pointcut id="mypointcut" expression="execution(* *..*SErviceImpl.upgrade*(..))"/>
<jdbc:embedded-database id="embeddedDatabase" type"HSQL">
    <jdbc:script location="classpath:schema.sql"/>
</jdbc:embedded-database>
```

#### 자동인식을 이용한 빈 등록: 스테레오타입 애노테이션과 빈 스캐너

- 모든 빈을 XML에 일일이 선언하는 것이 귀찮게 느껴질 수도 있음
  - 규모가 커지고 빈의 개수가 많아지면 XML 파일 관리가 번거로울 수 있음
- 특정 애노테이션이 붙은 클래스를 자동으로 찾아서 빈으로 등록해주는 방식을 빈 스캐닝을 통한 자동인식 빈 등록 기능이라 하고, 스캐닝 작업을
담당하는 오브젝트를 빈 스캐너라고 함
- 스프링의 빈 스캐너는 지정된 클래스패스 아래에 있는 모든 패키지의 클래스를 대상으로 필터를 적용해서 빈 등록을 위한 클래스들을 선별해냄
- @Component를 포함해 디폴트 필터에 적용되는 애노테이션을 스프링에서는 스테레오타입 애노테이션이라 부름

```java
@Component
public class AnnotatedHello {
  ...
}
```

- 하나의 빈이 등록되려면 최소한 아이디와 클래스 이름이 메타정보로 제고오대야 함
  - 클래스 이름은 빈 스캐너가 클래스를 감지하는 것이니 쉬움
  - 빈의 아이디는 기본적으로 클래스 이름을 빈의 아이디로 사용함
    - 클래스 이름의 첫 글자만 소문자로 바꾼 것을 사용
    - annotatedHello
- 빈의 이름을 클래스 이름과 다르게 지정해야 할 때는 @Component의 디폴트 값을 이용해 빈 이름을 지정

```java
@Component("myAnnotatedHello")
public class AnnotatedHello{
  ...
}
```

- 