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