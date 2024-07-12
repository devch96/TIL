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

- 애노테이션을 부여하고 자동스캔으로 빈을 등록하면 복잡한 XML 문서 생성과 관리에 따른 수고를 덜어주고 개발 속도를 향상시킬 수 있음
  - 하지만 애플리케이션에 등록될 빈이 어떤 것들이 있는지 한눈에 파악하기 어려움
  - 빈의 개수가 많아지면 XML도 어렵긴 마찬가지지만 누군가 책임지고 애플리케이션을 구성하는 빈과 의존관계, 설정 등을 통제하고 관리하는 데는 XML이 유리
- 빈 스캔에 의해 자동등록되는 빈은 XML처럼 상세한 메타정보 항목을 지정할 수 없고, 클래스당 한 개 이상의 빈을 등록할 수 없다는 제한이 있음
  - 동일한 클래스의 여러 인스턴스를 빈으로 등록하려면 수동 설정이 필요함
  - 빈 이름, 스코프, 지연된 생성 밥법과 같은 빈 메타정보 항목은 스테레오타입 애노테이션의 엘리먼트나 기타 애노테이션을 이용해 변경 가능

#### 자바 코드에 의한 빈 등록: @Configuration 클래스의 @Bean 메서드

- 빈 설정 메타정보를 담고 있는 자바 코드는 @Configuration 애노테이션이 달린 클래스를 이용해 작성한다
- 클래스에 @Bean이 붙은 메서드를 정의할 수 있는데, 이 @Bean 메서드를 통해 빈을 정의할 수 있다

```java
@Configuration
public class AnnotatedHelloConfig {
    @Bean
    public AnnotatedHello annotatedHello() { // @Bean이 붙은 메서드 하나가 하나의 빈을 정의. 빈 이름은 메서드 이름
        return new AnnotatedHello(); // 컨테이너는 이 리턴 오브젝트를 빈으로 활용
    }
}
```

- 설정을 담은 자바 코드에 해당하는 AnnotatedHelloConfig 자체도 하나의 빈으로 등록된다
  - @Component를 이용한 자동인식 방식과 같음
- AnnotatedHelloConfig가 빈이면 해당 빈을 가져와서 annoatedHello 메서드를 실행하면 AnnotatedHello 오브젝트가 두개 가 될까?
  - 아니다.
  - annotatedHello도 빈으로 등록되는데 스코프가 싱글톤이기 때문에 DI를 통해 다른 여러 빈에 참조되든 getBean() 메서드에 의해 가져오든
  annotatedHello() 메서드를 직접 실행해서 빈 오브젝트를 가져오든 싱글톤이어야 한다
- 싱글톤 빈의 경우 일반적인 자바 코드와는 조금 다른 방식으로 동작
  - @Configuration과 @Bean을 사용하는 클래스는 순수한 오브젝트 팩토리 클래스라기 보다는 자바 코드로 표현되는 메타정보라고 이해
- 자바 코드에 의한 설정이 XML과 같은 외부 설정파일을 이용하는 것보다 유용한 점
  - 컴파일러나 IDE를 통한 타입 검증이 가능
  - 자동완성과 같은 IDE 지원 기능을 최대한 이용할 수 있음
  - 이해하기 쉬움
  - 복잡한 빈 설정이나 초기화 작업을 손쉽게 적용할 수 있음

#### 자바 코드에 의한 빈 등록: 일반 빈 클래스의 @Bean 메서드

- 기본적으로 @Configuration 애노테이션이 붙은 설정 전용 클래스를 이용하나, 일반 POJO 클래스에도 @Bean을 사용할 수 있음
- @Configuration이 붙지 않은 @Bean 메서드는 @Configuration 클래스의 @Bean과 미묘한 차이가 있음

```java
public class HelloService {
    @Bean
    public Hello hello() {
        Hello hello = new Hello();
        hello.setPrinter(printer());
        return hello;
    }
  
    @Bean
    public Hello hello2() {
      Hello hello = new Hello();
      hello.setPrinter(printer());
      return hello;
    }
    
    @Bean
    public Printer printer() {
        return new StringPrinter();
    }
}
```

- @Configuration이 붙지 않은 클래스에 @Bean을 사용했을 때는 hello.setPrinter(printer())처럼 다른 @Bean 메서드를 호출해서 DI하는 코드에 문제가 발생함
- @Bean이 붙은 메서드는 기본적으로 싱글톤이기 때문에 여러번 호출돼도 하나의 오브젝트만 리턴하는 것을 보장하지만 @Configuration 클래스 안에서 사용된 @Bean만 해당됨

```java
public class HelloService {
    private Printer printer;
    
    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
    
    @Bean
    private Hello hello() {
      Hello hello = new Hello();
      hello.setPrinter(this.printer);
      return hello;
    }

    @Bean
    private Hello hello2() {
      Hello hello = new Hello();
      hello.setPrinter(this.printer);
      return hello;
    }
    
    @Bean
    private Printer printer() {
        return new StringPrinter();
    }
}
```

- 자신의 내부에서 정의되는 빈을 DI받아 이를 각 메서드가 사용하게 하면 싱글턴처럼 된다.
- 일반 클래스에서 @Bean을 사용할 경우 이런 위험성이 있기 떄문에 함부로 남용해서는 안됨
  - 클래스 밖에서 @Bean 메서드를 호출할 수 없도록 private으로 선언해두고, 클래스 내부에서도 DI를 통해 참조해야지 메서드를 직접 호출하지 않도록
  주의를 기울여야 함
- @Configuration 클래스가 아닌 일반 빈 클래스에 @Bean 메서드를 이용한 빈 메타정보 선언 기능을 사용해야 할 때는 언제일까?
  - @Bean 메서드를 통해 정의되는 빈이 클래스로 만들어지는 빈과 매우 밀접한 관계가 있는 경우
    - 종속적인 빈
  - @Bean 메서드는 클래스 내부에 정의되어 있으므로 클래스의 모든 저오에 접근 가능함
    - 설정정보 등을 공유할 수 있으며 외부로 빈의 존재를 감출 수 있음
- 하지만 설정정보가 일반 애플리케이션 코드와 함께 존재하기 때문에 유연성이 떨어짐

#### 빈 등록 메타정보 구성 전략

- 빈 등록 방법은 한 가지만 선택해야 하는 것이 아닌 조합해 사용할 수 있음
  - 애플리케이션의 특성과 개발팀의 문화, 기업의 정책에 맞는 적절한 조합을 찾아내고 일관성 있게 사용하는 것이 중요
- XML 단독 사용
  - 모든 빈을 명시적으로 XML에 등록하는 방법
  - 컨텍스트에서 생성되는 모든 빈을 XML에서 확인할 수 있다는 장점
  - 빈의 개수가 많아지면 XML파일을 관리하기 번거롭다는 단점
  - 모든 설정정보를 자바 코드에서 분리하고 순수한 POJO 코드를 유지하고 싶다면 XML
  - BeanDefinition을 코드에서 직접 만드는 방법을 제외하면 스프링이 제공하는 모든 종류의 빈 설정 메타정보 항목을 저장할 수 있는
  유일한 방법
- XML과 빈 스캐닝의 혼용
  - 애플리케이션 3계층의 핵심 로직을 담고 있는 빈 클래스는 그다지 복잡한 빈 메타정보를 필요로 하지 않음
    - 대부분 싱글톤, 클래스당 하나만 만들어지므로 빈 스캐닝에 의한 자동인식 대상으로 적절
  - 기술 서비스, 기반 서비스, 컨테이너 설정 등의 빈은 XML을 사용하면 됨
    - 스키마에 정의된 전용 태그를 사용해서 AOP나 트랜잭션 속성, 내장형 DB, OXM 마샬러를 위한 빈을 손쉽게 등록할 수 있음
    - 각 방법의 장점을 잘 살려서 적용하면 매우 효과적
      - 기술 서비스나 컨테이너 설정용 빈은 초기 XML에 등록
      - 개발이 진행되면서 생기는 빈들은 스테레오타입 애노테이션 부여 자동스캔
      - 원하는 빈만 선별해서 등록 가능
    - 스캔 대상이 되는 클래스를 위치시킬 패키지를 미리 결정해야함을 주의
      - 웹 기반의 스프링 애플리케이션에는 보통 두 개의 애플리케이션 컨텍스트가 등록돼서 사용됨
      - 빈 스캐닝은 애플리케이션 컨텍스트별로 진행되는 작업
      - XML이라면 알아서 각각 컨텍스트에 속한 빈을 등록하면 되지만 빈 스캐닝은 한 번에 최상위 패키지를 지정해서 하는 것이니
      자칫 하면 양쪽 컨텍스트의 빈 스캐너가 같은 클래스를 중복해서 빈으로 등록해버릴 수 있음
- XML 없이 빈 스캐닝 단독 사용
  - 애플리케이션 컴포넌트는 물론이고, 각종 기술 서비스와 컨테이너 설정용 빈도 모두 스캔으로 자동등록시키는 것
  - 자바 코드에 의한 빈 설정 방식 사용
  - 모든 빈의 정보가 자바 코드에 담겨 있으므로 빈의 설정정보를 타입에 안전한 방식으로 작성할 수 있다는 것
  - XML을 사용하지 않기 떄문에 스프링이 미리 제공해주는 aop, tx 등을 비롯한 10여 개의 스키마와 그 안에 정의된 전용 태그를 쓸 수 없다는 것이 큰 단점

### 빈 의존관계 설정 방법

- DI 할 대상을 선정하는 방법으로 빈 사이의 의존관계 메타정보를 작성하는 방법을 분류하면 명시적으로 구체적인 빈을 지정하는 방법과
일정한 규칙에 따라 자동으로 선정하는 방법으로 나눔
  - 전자는 DI 할 빈의 아이디를 직접 지정하는 것
  - 후자는 주로 타입 비교를 통해서 호환되는 타입의 빈을 DI 후보로 삼는 방법
    - 자동와이어링(autowiring)이라 부름

#### XML: property, constructor-arg

- 빈 태그를 이용해 빈을 등록했다면 프로퍼티와 생성자 두 가지 방식으로 DI를 지정할 수 있음
  - 프로퍼티는 자바빈 규약을 따르는 수정자 메서드 사용
  - 생성자는 빈 클래스의 생성자를 이용
  - 파라미터로 의존 오브젝트 또는 값을 주입
- property: 수정자 주입
  - ref 애트리뷰트를 사용해 빈 이름을 이용해 주입할 빈을 찾음
  - value 애트리뷰트는 단순 값 또는 빈이 아닌 오브젝트를 주입할 때 사용
```xml
<bean ...>
    <property name="printer" ref="defaultPrinter" />
</bean>
<bean id="defaultPrinter" class="...">

<property name="name" value="Spring"/>
<property name="age" value"30"/>
<proeprty name="myClass" value="java.lang.String"/>
```

- XML의 property에는 해당 프로퍼티의 타입정보가 나타나지 않음
  - 주입 대상 프로퍼티와 주입될 빈 또는 값의 타입이 호환되는지 주의를 기울여서 작성
  - 타입안전성이 떨어짐
- constructor-arg: 생성자 주입
  - 생성자를 통한 빈 또는 값의 주입에 사용됨
  - 수정자 메서드와 다르게 생성자 주입은 생성자의 파라미터를 이용하기 때문에 한 번에 여러 개의 오브젝트를 주입할 수 있음
  - 파라미터의 순서나 타입을 명시하는 방법이 필요함
  - ref, value 애트리뷰트는 property와 동일한 의미
```xml
<bean id="hello" class="springbook.learningtest.spring.ioc.bean.Hello">
  <constructor-arg index="0" value="Spring"/>
  <constructor-arg index="1" ref="printer" />
</bean>
```
- 파라미터에 중복되는 타입이 없으면 타입으로 구분해줄수도 있음

#### XML: 자동와이어링

- 자동와이어링 방식은 XML 문서의 양을 대폭 줄여줄 수 있는 획기적인 방법이지만 위험이 따르기 때문에 사용에 주의를 기울여야 함
- byName: 빈 이름 자동와이어링

```xml
<bean id="hello" class="...Hello" autowire="byName">
  <property name="name" value="Spring"/>
</bean>
```
  
  - printer 프로퍼티는 생략했지만 autowire="byName"에 의해 스프링은 Hello 클래스의 프로퍼티의 이름과 동일한 빈을 찾아서 자동으로 프로퍼티로 등록해줌
    - 프로퍼티와 이름이 같은 빈이 없는 경우는 무시함
    - 아예 루트 태그인 beans의 디폴트 자동와이어링 옵션을 변경해도 됨
    - 자동와이어링이 어려운 프로퍼티 값이나 특별한 이름을 가진 프로퍼티의 경우에는 명시적으로 property를 선언해주면 됨

- byType: 타입에 의한 자동와이어링
  - 타입에 의한 자동와이어링은 타입이 같은 빈이 두 개 이상 존재하는 경우에는 적용되지 못함
  - 타입을 비교하면 스트링으로 된 이름을 비교할 때보다 느림
- XML 안에서 자동와이어링을 사용하는 방식의 단점
  - XML만 봐서는 빈 사이의 의존관계를 알기 힘듬
  - 대응되는 빈이 존재해야만 DI가 이뤄짐
  - 오타로 빈 이름을 잘못 적을 수 있음
  - 빈에 대해 한 가지 자동와이어링 방식밖에 지정할 수 없다는 것도 한계

#### 애노테이션: @Resource

- property 선언과 비슷하게 주입할 빈을 아이디로 지정하는 방법
- 수정자뿐만 아니라 필드에도 붙일 수 있음

```java
public class Hello {
    private Printer printer;
    
    @Resource(name="printer")
    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
}
```
- @Resource와 같은 애노테이션으로 된 의존관계 정보를 이용해 DI가 이뤄지게 하려면 세 가지 방법중 하나를 선택해야 함
  - XML의 <context:annotation-config />
  - XML의 <context:component-scan />
  - AnnotationConfigApplicationContext 또는 AnnotationConfigWebApplicationContext

```java
@Component
public class Hello {
    @Resource(name="printer")
    private Printer printer;
}
```

- @Resource가 필드에 붙어 있을 때는 그에 대응되는 수정자가 없어도 상관없음
- 이런 방법을 필드 주입(field Injection)이라고 함

#### 애노테이션: @Autowired/@Inject

- @Autowired는 스프링 2.5부터 적용된 스프링 전용 애노테이션
- @Inject는 JavaEE 6의 표준 스펙으로 여타 프레임워크에서도 동일한 의미로 사용되는 DI를 위한 애노테이션
- 스프링으로 개발한 POJO를 앞으로 다른 환경에서도 사용할 가능성이 있다면 @Inject
- @Autowired는 XML의 타입에 의한 자동와이어링 방식을 생성자, 필드, 수정자 메서드, 일반 메서드의 네 가지로 확장한 것
- 수정자 메서드와 필드
  - @Resource와 사용 방법이 비슷함
  - 필드나 수정자를 만들어주면 스프링이 자동으로 DI 해주도록 만드는 것
  - 필드나 프로퍼티 타입을 이용해 후보 빈을 찾음
- 생성자
  - 생성자의 모든 파라미터에 타입에 의한 자동와이어링이 적용됨
  - 단 하나의 생성자에만 사용할 수 있다는 제한이 있음
  - 생성자가 하나면 생략 가능
- 일반 메서드
  - 생성자 주입을 수정자 주입보다 선호하는 개발자들이 있음
  - 하지만 무엇이 항상 옳지는 않음
  - 모든 프로퍼티를 다 DI 하지 않고 일부는 디폴트 값을 주고 선택적으로 DI할 수 있게 만들어야 할 때도 많음
- 컬렉션과 배역
  - 같은 타입의 빈이 하나 이상 존재할 때 그 빈들을 모두 DI 받도록 할 수있음
  - 컬렉션이나 배열로 선언하면 됨
- @Qualifier
  - 타입 외의 정보를 추가해서 자동와이어링을 세밀하게 제어할 수 있는 보조적 방법

### 프로퍼티 값 설정 방법

- DI를 통해 주입되는 것은 두 가지
  - 다른 빈 오브젝트 레퍼런스
  - 단순 값
- 싱글톤은 동시성 문제 때문에 필드 값을 함부로 수정하지 않음
  - 상태가 없는 방식으로 만들기 때문에 필드에 있는 값은 읽기전용인 경우가 대부분
  - 상태를 가진 빈으로 만든다면 주입되는 값은 일종의 초기값

#### 메타정보 종류에 따른 값 설정 방법

- XML: property와 전용 태그
  - ref 애트리뷰트를 이용해 다른 빈의 아이디를 지정함
  - value 애트리뷰트를 사용하면 런타임 시에 주입할 값으로 인식
  - 프로퍼티 타입이 String이라면 가장 간단
    - value 애트리뷰트 값도 스트링이기 때문
  - int, float, double, boolean 같은 기본 타입이거나 Class나 Resource같은 오브젝트는 변환이 필요함
    - 스프링 컨테이너는 XML의 문자열로 된 값을 프로퍼티 타입으로 변환해주는 변환 서비스를 내장
- 애노테이션: @Value
  - 빈 의존관계는 아니지만 어떤 값을 외부에서 주입해야 하는 용도는 두 가지
    - 환경에 따라 매번 달라질 수 있는 값
      - DataSource 타입의 빈에 제공하는 DriverClass, URL, Username, Password
      - 파일 경로처럼 환경에 의존적인 정보
    - 초기값을 미리 갖고 있다는 점
      - 클래스의 필드에 초기값을 설정해두고 특별한 경우(테스트, 이벤트) 초기값 대신 다른 값을 지정하고 싶을 때
- 자바 코드: @Value
  - @Configuration과 @Bean을 사용하는 경우에도 프로퍼티 값을 외부로 독립시킬 수 있음
  - 클래스 자체가 메타정보이기 때문에 설정을 변경해야 할 때마다 코드를 수정하고 재컴파일하는 게 문제가 되지 않음

#### PropertyEditor와 ConversionService

- XML의 value 애트리뷰트나 @Value의 엘리먼트는 모두 텍스트 문자로 작성됨
  - 스트링이면 아무 문제 없지만 그 외의 타입인 경우엔 타입을 변경하는 과정 필요
- 스프링은 두 가지 종류의 타입 변환 서비스 제공
  - 디폴트로 사용되는 타입 변환기는 PropertyEditor라는 java.beans의 인터페이스를 구현한 것
- 스프링이 기본적으로 지원하는 변환 가능한 타입
  - 기본 타입
  - 배열
  - 기타
    - Charset
    - Class
    - Currency
    - File
    - InputStream
    - Local
    - Pattern
    - Resource
    - Timezone
    - URI,URL

#### 컬렉션

- 스프링은 List, Set, Map, Properties와 같은 컬렉션 타입을 XML로 작성해서 프로퍼티에 주입하는 방법을 제공함
  - value 애트리뷰트를 통해 스트링 값을 넣는 대신 컬렉션 선언용 태그를 사용해야 함
  - value 애트리뷰트가 생략됨
- List, set
  - list, value를 이용해 선언함
```xml
<list>
  <value>Spring</value>
  <value>IoC</value>
  <value>DI</value>
</list>
```
- Map
  - map, entry 태그를 사용함
```xml
<map>
  <entry key="Kim" value="30"/>
  <entry key="Lee" value="20"/>
  <entry key="Ahn" value="10"/>
</map> 
```

- Properties
  - java.util.Properties 타입은 props와 prop을 이용함

```xml
<props>
  <prop key="username">Spring</prop>
  <prop key="password">Book</prop>
</props>
```

- 컬렉션 대신에 다른 빈의 레퍼런스를 넣을 수 있음

### 컨테이너가 자동등록하는 빈

- 스프링 컨테이너는 초기화 과정에서 몇 가지 빈을 기본적으로 등록해줌
  - 자주 사용하지는 않지만 간혹 필요할 때가 있으니 기억해두면 좋음

#### ApplicationContext, BeanFactory

- 스프링에서 컨테이너는 자신을 빈으로 등록해두고 필요하면 일반 빈에서 DI 받아서 사용할 수 있음
- 애플리케이션 컨텍스트를 일반 빈에서 사용하고 싶다면 ApplicationContext 타입의 빈을 DI 받도록 선언하면 됨

```java
public class SystemBean {
    @Autowired
    ApplicationContext context;
    
    public void specialJobWithContext() {
        this.context.getBean(...);
    }
}
```

- 애노테이션을 이용한 의존관계 설정을 사용하지 않는다면 @Autowired를 사용할 수 없음
- 이때는 ApplicationContextAware라는 특별한 인터페이스를 구현해주면 됨
  - ApplicationContextAware 인터페이스에는 setApplicationContext() 메서드가 있어서 스프링이 애플리케이션 컨텍스트 오브젝트를 DI 해줄 수 있음
- 컨텍스트 내부에 만들어진 빈 팩토리 오브젝트를 직접 사용하고 싶다면 BeanFacotyr 타입으로 DI 해줄 필요가 있음
  - BeanFactory 인터페이스의 메서드를 이용하기 보다는 애플리케이션 컨텍스트 내에서 생성한 DefaultListableBeanFactory 오브젝트로 캐스팅해서 제공하는 기능을 사용하기
  위해
- 애플리케이션 코드에서 애플리케이션 컨텍스트를 직접 사용할 일은 많지 않지만 스프링을 기반으로 해서 애플리케이션 프레임워크를
개발한다면 많이 사용함

#### ResourceLoader, ApplicationEventPublisher

- 스프링 컨테이너는 ResourceLoader이기도 하기 때문에 서버환경에서 다양한 Resource를 로딩할 수 있는 기능을 제공함
- 코드를 통해 서블릿 컨텍스트의 리소스를 읽어오고 싶다면 컨테이너를 ResourceLoader 타입으로 Di 받아서 활용하면 됨

```java
@Autowired
ResourceLoader resourceLoader;

public void loadDataFile() {
    Resource resource = this.resourceLoader.getResource("WEB-INF/info.dat");
}
```

- ApplicationContext는 ResourceLoader를 상속하고 있으므로 ApplicationContext를 DI 받아서 사용해도 되지만 단지 리소스를 읽어오려는 목적이라면
용도에 맞게 적절한 인터페이스 타입으로 DI 받아 사용하는 것이 바람직함

#### systemProperties, systemEnvironment

- 스프링 컨테이너가 직접 등록하는 빈 중에서 타입이 아니라 이름을 통해 접근할 수 있는 두 가지 빈
- systemProperties 빈은 System.getProperties() 메서드가 돌려주는 Properties 타입의 오브젝트를 읽기전용으로 접근할 수 있게
만든 빈 오브젝트
  - JVM이 생성해주는 시스템 프로퍼티 값을 읽을 수 있게 해줌
  - 코드에서 시스템 프로퍼티를 사용한다면 System.getProperty("os.name")처럼 직접 코드로 만드는 편이 남

--------------------

## 프로토타입과 스코프

- 기본적으로 스프링의 빈은 싱글톤으로 만들어짐
  - 애플리케이션 컨텍스트마다 빈의 오브젝트는 단 한개만 만들어진다는 뜻
  - 매번 애플리케이션 로직을 담은 오브젝트를 새로 만드는 건 비효율적이기 때문
- 하나의 빈 오브젝트에 여러 스레드가 접근하기 때문에 상태 값을 인스턴스 변수에 저장해두고 사용하면 안됨
  - DTO를 리턴값이나 파라미터로 쓰자
- 빈을 싱글톤이 아닌 다른 방법으로 만들어 사용해야 할 때가 있음
  - 빈 당 단 하나의 오브젝트만을 만드는 싱글톤 대신 하나의 빈 설정으로 여러 개의 오브젝트를 만들어서 사용하는 경우

### 프로토타입 스코프

- 싱글톤 스코프는 DI 설정으로 자동 주입하는 것 말고 컨테이너에 getBean() 메서드를 사용해 의존객체를 조회하더라도 매번 같은 오브젝트가 리턴됨이 보장됨
- 프로토타입 스코프는 컨테이너에게 빈을 요청할 때마다 매번 새로운 오브젝트를 생성해줌

#### 프로토타입 빈의 생명주기와 종속성

- 스프링이 관리하는 오브젝트인 빈은 그 생성과 다른 빈에 대한 의존관계 주입, 초기화, DI와 DL을 통한 사용, 제거에 이르기까지 모든 오브젝트의
생명주기를 컨테이너가 관리함
- 프로토타입 빈은 독특하게 이 IoC 기본 원칙을 따르지 않음
- 일단 빈을 제공하고나면 컨테이너는 더 이상 빈 오브젝트를 관리하지 않음
  - DL,DI를 통해 컨테이너 밖으로 전달된 후에는 이 오브젝트는 스프링이 관리하는 빈이 아님
- 프로토타입 빈 오브젝트는 다시 컨테이너를 통해 가져올 방법이 없고 빈이 제거되기 전에 빈이 사용한 리소스를 정리하기 위해 호출하는 메서드도 이용할 수 없음
- 빈 오브젝트는 전적으로 DI 받은 오브젝트에 달려 있음

#### 프로토타입 빈의 용도

- 대부분의 애플리케이션 로직은 싱글톤 빈으로 충분
- 