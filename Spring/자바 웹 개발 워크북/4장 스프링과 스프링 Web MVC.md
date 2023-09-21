# 스프링과 스프링 Web MVC

---------------
## 의존성 주입과 스프링

### 스프링의 시작

- 스프링 프레임워크는 원래 웹이라는 제한적인 용도로만 쓰이는 것이 아닌 객체지향의 `의존성 주입(dependency injection)` 기법을
적용할 수 있는 객체지향 프레임워크.
- 스프링이 등장할 때 다른 프레임워크들과 달리 스프링 프레임워크는 개발과 설계 전반에 관련된 문제들을 같이 다루었기 때문에
가장 성공한 프레임워크로 기록되었다.
- 스프링 프레임워크는 가장 중요한 '코어(core)' 역할을 하는 라이브러리와 여러 개의 추가적인 라이브러리를 결합하는 형태로 프로젝트를 구성한다.
  - 웹 MVC 구현을 쉽게 할 수 있는 Spring Web MVC 등등

### 의존성 주입

- 의존성 주입은 어떻게 하면 '객체와 객체 간의 관계를 더 유연하게 유지할 것인가?'에 대한 고민으로
객체의 생성과 관계를 효과적으로 분리할 수 있는 방법에 대한 고민이다.
- 의존성이란 하나의 객체가 자신이 해야 하는 일을 하기 위해서 다른 객체의 도움이 필수적인 관계를 의미한다.
- 과거에는 의존성을 해결하기 위해 직접 객체를 생성하거나 하나의 객체만을 생성해서 활용하는(싱글턴) 등의 다양한 패턴을 설계해서
적용해 왔는데 스프링 프레임워크는 바로 이런 점을 프레임워크 자체에서 지원하고 있다.
- 스프링 프레임워크는 다양한 방식으로 필요한 객체를 찾아서 사용할 수 있도록 XML 설정이나 자바 설정등을 이용할 수 있다.

### build.gradle

```groovy
    // Spring
implementation 'org.springframework:spring-core:5.3.19'
implementation 'org.springframework:spring-context:5.3.19'
implementation 'org.springframework:spring-test:5.3.19'

// Lombok
compileOnly 'org.projectlombok:lombok:1.18.24'
testCompileOnly 'org.projectlombok:lombok:1.18.24'
annotationProcessor 'org.projectlombok:lombok:1.18.24'
testAnnotationProcessor 'org.projectlombok:lombok:1.18.24'

// Log4j2
implementation 'org.apache.logging.log4j:log4-core:2.17.2'
implementation 'org.apache.logging.log4j:log4-api:2.17.2'
implementation 'org.apache.logging.log4j:log4-slf4j-impl:2.17.2'

// jstl
implementation 'jstl:jstl:1.2'
```

### 의존성 주입하기

#### 설정 파일 추가

- 스프링 프레임워크는 자체적으로 객체를 생성하고 관리하면서 필요한 곳으로 객체를 주입하는 역할을 하는데
이를 위해서는 설정 파일이나 어노테이션 등을 이용한다.
- 스프링이 관리하는 객체들은 빈(Bean)이라는 이름으로 불리는데 프로젝트 내에서 어떤 빈들을 어떻게 관리할 것인지를 설정하는
설정 파일을 작성할 수 있다.
- 스프링의 빈 설정은 XML을 이용하거나 별도의 클래스를 이용해서 자바 설정이 가능하다.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean class="org.zerock.springex.sample.SampleDAO"></bean>
    <bean class="org.zerock.springex.sample.SampleService"></bean>
</beans>
```
#### 스프링의 빈 설정 테스트

- 스프링으로 프로젝트를 구성하는 경우 상당히 많은 객체를 설정하기 때문에 개발 단계에서 많은 테스트를 진행하면서
개발하는 것이 좋음.
```java
@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root-context.xml")
public class SampleTests {

    @Autowired
    private SampleService sampleService;

    @Test
    public void testService1(){
        log.info(sampleService);
        Assertions.assertNotNull(sampleService);
    }
}
```
- @Autowired는 스프링에서 사용하는 의존성 주입 관련 어노테이션으로 '만일 해당 타입의 빈이 존재하면 여기에 주입해주길 원한다'
라는 뜻
  - 필드 주입은 권장되지 않음. 생성자 주입이 좋음
- @ExtendWith(SpringExtension.class)는 JUnit5 버전에서 'spring-test'를 이용하기 위한 설정(JUnit4는 @Runwith)
- @ContextConfiguration 어노테이션은 스프링의 설정 정보를 로딩하기 위해 사용(xml 파일은 location, 자바 설정은 class 옵션 사용)

### ApplicationContext와 빈(Bean)

- 웹에서 서블릿이 존재하는 공간을 서블릿 컨텍스트라고 했던 것처럼 스프링에서는 빈이라고 부르는 객체들을
관리하기 위해서 ApplicationContext라는 존재를 활용.
- root-context.xml을 이용해서 스프링이 실행되고 ApplicationContext 객체가 생성됨.
- xml 파일을 읽어서 bean으로 지정된 클래스의 객체를 생성해서 관리함.

### @Autowired의 의미와 필드 주입

- @Autowired가 처리된 부분에 맞는 타입의 빈이 존재하는지를 확인하고 주입함.
- 멤버변수에 직접 @Autowired를 선언하는 방식을 필드 주입 방식이라고 함.

### contest:component-scan

- 스프링을 이용할 때는 클래스를 작성하거나 객체를 직접 생성하지 않음.
- 이 역할은 스프링의 내부에서 이루어지며 ApplicationContext가 생성된 객체들을 관리함.
- 서블릿을 생성하면 톰캣이 웹 어플리케이션을 실행하고 필요할 때 서블릿 객체를 만드는 것과 비슷한 방식
- 과거에는 서블릿 기술도 web.xml에 servlet이란 태그를 붙였어야 했지만 @WebServlet 어노테이션이 이를 대신함.
- 스프링도 2.5버전 이후에는 xml 파일에 bean 태그 대신 어노테이션 형태로 변화함

### @Service, @Repository

- 스프링 프레임워크는 어플리케이션 전체를 커버하기 때문에 다양한 종류의 어노테이션을 사용하도록 작성됨(2.5 버전 이후)
  - @Controller: MVC의 컨트롤러를 위한 어노테이션
  - @Service: 서비스 계층의 객체를 위한 어노테이션
  - @Repository: DAO와 같은 객체를 위한 어노테이션
  - @Component: 일반 객체나 유틸리티 객체를 위한 어노테이션
- 어노테이션을 이용하게 되면 스프링 설정은 해당 패키지를 조사해서 클래스들의 어노테이션을 이용하는 설정으로 변경됨.
```xml
<context:component-scan base-package="org.zerock.springex.sample"/>
```

### 생성자 주입 방식

- 초기 스프링에는 @Autowired를 멤버 변수에 할당(필드 주입), Setter를 작성하는 방식(수정자 주입)을 많이 이용해
 왔지만, 스프링 3 이후에는 생성자 주입 방식이라고 부르는 방식을 더 많이 활용한다.
- 생성자 주입 방식의 규칙
  - 주입 받아야 하는 객체의 변수는 final로 작성.
  - 생성자를 이용해서 해당 변수를 생성자의 파라미터로 지정
- 생성자 주입 방식은 객체를 생성할 때 문제가 발생하는지를 미리 확인할 수 있기 때문에 필드 주입이나 수정자 주입보다 많이 쓰임.
  - 순환 참조 문제
- Lombok의 @RequiredArgsConstructor를 이용해서 필요한 생성자 함수를 자동으로 생성하면 편리하게 작성할 수 있음.

### 인터페이스를 이용한 느슨한 결합

- 스프링이 의존성 주입을 가능하게 하지만 좀 더 근본적으로 유연한 프로그램을 설계하기 위해서는 인터페이스를 이용해서 나중에 다른 클래스의
객체로 쉽게 변경할 수 있도록 하는 것이 좋음.
- 추상화된 타입을 이용하면 다른 객체로 변경하더라도 원래의 코드를 바꾸지 않고 객체를 변경할 수 있음.
- 인터페이스를 이용하면 실체 객체를 모르고 타입만을 이용해서 코드를 작성하는 일이 가능해짐.
- 객체와 객체의 의존 관계의 실제 객체를 몰라도 가능하게 하는 방식을 느슨한 결합(loose coupling)이라고 함.
- 인터페이스를 구현한 두 클래스가 빈으로 등록될 경우 스프링은 어떤 객체를 주입할 것인지 몰라서 오류를 발생함.

#### @Primary

- 가장 간단한 방법으로 클래스들중 하나를 @Primary 어노테이션으로 지정하면 그 타입의 객체가 주입됨.

#### @Qualifier

- @Qualifier는 이름을 지정해서 특정한 이름의 객체를 주입하는 방식.
- Lombok과 @Qualifier를 같이 이용하기 위해서는 main/java 폴더에 lombok.config 파일을 생성해야 함.
```lombok.config
lombok.copyableannotations += org.springframework.beans.factory.annotation.Qualifier
```

#### 스프링의 빈(Bean)으로 지정되는 객체들

- 모든 클래스의 객체가 스프링의 빈으로 처리되는 것은 아님.
- 스프링의 빈으로 등록되는 객체들은 쉽게 말해서 '핵심 배역'을 하는 객체들.
- 스프링의 빈으로 등록되는 객체들은 주로 오랜 시간 동안 프로그램 내에 상주하면서 중요한 역할을 하는 역할 중심의 객체들.
- DTO나 VO와 같이 역할 보다는 데이터에 중점을 두고 설계된 객체들은 스프링의 빈으로 등록되지 않음.

#### XML이나 어노테이션으로 처리하는 객체

- 빈으로 처리할 때 XML 설정 혹은 어노테이션으로 처리할 수 있지만 이에 대한 기준은 코드를 수정할 수 있는가 로 판단.
- jar 파일로 추가되는 클래스의 객체를 스프링의 빈으로 처리해야 한다면 코드가 존재하지 않기 때문에 xml로 처리.
- 직접 작성되는 클래스는 어노테이션을 이용

### 웹 프로젝트를 위한 스프링 준비

- 스프링의 구조를 보면 ApplicationContext라는 객체가 존재하고, 빈으로 등록된 객체들은 ApplicationContext 내에 생성되어서 관리되는 구조.
- 이렇게 만들어진 ApplicationContext가 웹 어플리케이션에서 동작하려면 웹 어플리케이션이 실행될 때 스프링을 로딩해서
해당 웹 어플리케이션 내부에 스프링의 ApplicationContext를 생성하는 작업이 필요한데 이를 위해선 web.xml에 리스너를 설정.
```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/root-context.xml</param-value>
</context-param>

<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

#### root-context.xml에 HikariCP 설정하기

- 스프링 이전에는 HikariCP를 사용하기 위해서 HikariConfig 객체와 HikariDataSource를 초기화해야만 했고, 이를 위해선
ConnectionUtil 클래스에 작성하였다.
- 스프링을 이용한다면 이 설정은 스프링의 빈으로 처리되어야 한다.
```xml
<bean id="hikariConfig" class="com.zaxxer.hikari.HikariConfig">
    <property name="driverClassName" value="org.mariadb.jdbc.Driver"/>
    <property name="jdbcUrl" value="jdbc:mariadb://localhost:3306/webdb"/>
    <property name="username" value="webuser"/>
    <property name="dataSourceProperties">
        <props>
            <prop key="cachePrepStmts">true</prop>
            <prop key="prepStmtCacheSize">250</prop>
            <prop key="prepStmtCacheSqlLimit">2048</prop>
        </props>
    </property>
</bean>

<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource" destroy-method="close">
    <constructor-arg ref="hikariConfig"/>
</bean>
```
- 스프링은 필요한 객체를 스프링에서 주입해 주기 때문에 개별적으로 클래스를 작성해서 빈으로 등록해 두기만 하면
원하는 곳에서 쉽게 다른 객체를 사용할 수 있다.
- 이러한 특징으로 인해 스프링 프레임워크는 웹이나 데이터베이스와 같이 특정한 영역이 아닌 전체 어플리케이션의 구조를
설계할 때 사용된다.

--------------------------

## MyBatis와 스프링 연동

- 스프링 프레임워크의 중요한 특징 중 하나는 다른 프레임워크들을 쉽게 결합해서 사용할 수 있다는 점이다.
- 스프링 프레임워크가 웹이나 데이터베이스와 같이 특정한 영역을 구애받지 않고 시스템의 객체지향 구조를 만드는 데 이용되는 성격 때문이다.
- 데이터베이스과 관련되서 스프링 프레임워크는 자체적으로 spring-jdbc와 같은 라이브러리를 이용해서 구현할 수 있고, MyBatis나 JPA 프레임워크를 이용할 수 있다.

### MyBatis 소개

- MyBatis는 Sql Mapping Framework 라고 표현된다.
- SQL의 실행 결과를 객체지향으로 매핑 해준다는 뜻이다.
- MyBatis를 이용하면 기존의 SQL을 이용하면서 다음과 같은 편리한 점이 생긴다.
  - PreparedStatement/ResultSet의 처리
  - Connection/PreparedStatement/ResultSet의 close()처리
    - MyBatis와 스프링을 연동해서 사용하는 방식으로 이용하면 자동으로 close()
  - SQL의 분리
    - MyBatis를 이요하면 별도의 파일이나 어노테이션 등을 이용해서 SQL 선언

### MyBatis와 스프링의 연동 방식

- MyBatis를 단독으로 개발하고 스프링에서 DAO를 작성해서 처리하는 방식
  - 기존의 DAO에서 SQL의 처리를 MyBatis를 이용하는 구조로써 MyBatis와 스프링 프레임워크를 독립적인 존재로 바라보고 개발하는 방식
- MyBatis와 스프링을 연동하고 Mapper 인터페이스만 이용하는 방식
  - 스프링과 MyBatis 사이에 mybatis-spring이라는 라이브러리를 이용해서 사용
  - 개발 시에는 Mapper 인터페이스라는 방식을 이용해서 인터페이스만으로 모든 개발이 가능한 방식

#### MyBatis를 위한 라이브러리들
```groovy
//Spring
implementation group: 'org.springframework', name: 'spring-jdbc', version: '5.3.19'
implementation group: 'org.springframework', name: 'spring-tx', version: '5.3.19'
//MyBatis
implementation 'org.mybatis:mybatis:3.5.9'
implementation 'org.mybatis:mybatis-spring:2.0.7'
```

### MyBatis를 위한 스프링의 설정 - SqlSessionFactory

- MyBatis를 이용하기 위해서는 스프링에 설정해둔 HikariDataSource를 이용해서 SqlSessionFactory라는
빈을 설정.
```xml
<bean id="sqlSessionFacotry" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
</bean>
```
- MyBatis와 스프링을 연동하고 매퍼 인터페이스를 활용하는 방식은 개발자가 실제 동작하는 클래스와 객체를 생성하지 않고 스프링에서
자동으로 생성하는 방식을 이용.
- 개발자가 직접 코드를 수정할 수 없다는 단점이 있긴 하지만 인터페이스만으로도 개발을 완료할 수 있다는 장점도있음.
```java
public interface TimeMapper {
    @Select("select now()")
    String getTime();
}
```

### XML로 SQL분리하기

- MyBatis를 이용할 때 SQL은 어노테이션을 이용해서 사용하기도 하지만 대부분은 SQL을 별도의 파일로 분리하는 것을 권장.
- XML을 사용하는 이유는 SQL이 길어지면 이를 어노테이션으로 처리하기가 복잡해지기 때문이기도 하고 어노테이션이 나중에 변경되면
프로젝트 전체를 다시 빌드하는 작업이 필요하기 때문에 단순 파일로 사용하는 것이 편리
- XML과 매퍼 인터페이스를 결합할 때는 다음과 같은 과정으로 작성
  - 매퍼 인터페이스를 정의하고 메소드를 선언
  - 해당 XMl 파일을 작성(파일 이름과 매퍼 인터페이스 이름을 같게)하고 select와 같은 태그를 이용해 SQL을 작성
  - select, insert 등의 태그에 id 속성 값을 매퍼 인터페이스의 메소드 이름과 같게 작성
```java
public interface TimeMapper2 {
    String getNow();
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.zerock.springex.mapper.TimeMapper2">

    <select id="getNow" resultType="string">
        select now()
    </select>
    
</mapper>
```

```xml
<bean id="sqlSessionFacotry" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="mapperLocations" value="classpath:/mappers/**/*.xml"/>
</bean>
```
- mapperLocations는 XML 매퍼 파일들의 위치를 의미.
- resources의 경우 'classpath:' 접두어를 인식되는 경로.

---------------------

## 스프링 Web MVC 기초

- 스프링의 Web MVC는 기본적으로 서블릿 API를 좀 더 추상화된 형태로 작성된 라이브러리지만 기존의 서블릿/JSP를 사용할 때
필요한 많은 기능들을 기본적으로 제공해서 개발의 생산성과 안전성을 획기적으로 높여주었다.

### 스프링 Web MVC의 특징

- 기본적인 흐름은 이전에 다루었던 Web MVC 패턴과 같고 컨트롤러, 뷰, 모델 등의 용어들 역시 그대로 사용.
- 약간의 변화를 주는 부분
  - Front-Controller 패턴을 이용해서 모든 흐름의 사전/사후 처리를 가능하도록 설계된 점
  - 어노테이션을 적극적으로 활용해서 최소한의 코드로 많은 처리가 가능하도록 설계된 점
  - HttpServletReqeust/HttpServeltResponse를 이용하지 않아도 될 만큼 추상화된 방식으로 개발 가능

### DispatcherServlet과 Front Controller

- 스프링 MVC에서 가장 중요한 사실은 모든 요청이 반드시 DispatcherServlet이라는 존재를 통해서 실행된다는 사실.
  - 객체지향에서 이렇게 모든 흐름이 하나의 객체를 통해서 진행되는 패턴을 파사드 패턴이라 하는데 웹 구조에서는 'Front-Controller' 패턴이라 부름.
- Front-Controller 패턴을 이용하면 모든 요청이 반드시 하나의 객체를 지나서 처리되기 때문에 모든 공통적인 처리를 프론트 컨트롤러에서 처리할 수 있음.
- 스프링 MVC에서는 DispatcherServlet이라는 객체가 프론트 컨트롤러 역할을 수행
- 프론트 컨트롤러가 사전/사후에 대한 처리를 하게 되면 중간에 매번 다른 처리를 하는 부분만 별도로 처리하는 구조를 만드는데 이를 컨트롤러라 하고 @Controller를 이용해 처리.

#### servlet-context.xml 설정

```xml
<mvc:annotation-driven></mvc:annotation-driven>
<mvc:resources mapping="/resources/**" location="/resources/"></mvc:resources>

<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/"/>
    <property name="suffix" value=".jsp"/>
</bean>
```
- mvc:annotaion-driven의 설정은 스프링 MVC 설정을 어노테이션 기반으로 처리한다는 의미와 스프링 MVC의 여러 객체들을
자동으로 스프링의 빈으로 등록하게 하는 기능.
- mvc:resources 설정은 이미지나 html 파일과 같이 정적인 파일의 경로를 지정
- InternalResourceViewResolver는 스프링 MVC에서 제공하는 뷰를 어떻게 결정하는지에 대한 설정을 담당.

### web.xml의 DispatcherServlet 설정

```xml
<servlet>
    <servlet-name>appServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/servlet-context.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>appServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```
- servlet 설정은 DispatcherServlet이 로딩할 때 servlet-context.xml을 이용하도록 설정.
- load-on-startup은 톰캣 로딩 시에 클래스를 미리 로딩해두기 위한 설정
- servlet-mapping 설정은 DispatcherServlet이 모든 경로의 요청에 대한 처리를 담당하기 때문에 / 로 지정

### 스프링 MVC 컨트롤러

- 전통적인 자바의 클래스 구현 방식과 여러모로 상당히 다름.
- 과거의 많은 프레임워크들은 상속이나 인터페이스를 기반으로 구현되는 방식을 선호했다면 스프링 MVC의 컨트롤러들은 다음과 같은 점이 다름.
  - 상속이나 인터페이스를 구현하는 방식으로 사용하지 않고 어노테이션으로 처리.
  - 오버라이드 없이 필요한 메소드들을 정의
  - 메소드의 파라미터를 기본 자료형이나 객체 자료형을 마음대로 지정
  - 메소드의 리턴타입도 void, String, 객체 등 다양한 타입 사용 가능
```java
@Log4j2
@Controller
public class SampleController {

    @GetMapping("/hello")
    public void hello(){
        log.info("hello..........");
    }
}
```
- @Controller는 해당 클래스가 스프링 MVC에서 컨트롤러 역할을 한다는 것을 의미하고 스프링의 빈으로 처리되기 위해 사용
- @GetMapping은 GET 방식으로 들어오는 요청을 처리하기 위해 사용.

### servlet-context.xml의 component-scan

```xml
<context:component-scan base-package="org.zerock.springex.controller"/>
```

### @RequestMapping와 파생 어노테이션들

- 스프링 컨트롤러에서 가장 많이 사용하는 어노테이션은 @RequestMapping
- @RequestMapping은 특정한 경로의 요청을 지정하기 위해 사용.
- 컨트롤러 클래스 선언부에도 사용할 수 있고, 메서드에도 사용할 수 있음.
- 서블릿 중심의 MVC의 경우 Servlet을 상속받아서 doGet()/doPost() 와 같은 제한적인 메서드를 오버라이드해서
사용했지만 스프링 MVC의 경우 하나의 컨트롤러를 이용해서 여러 경로의 호출을 모두 처리할 수 있음.
```java
@Controller
@RequestMapping("/todo")
@Log4j2
public class TodoController {
    
    @RequestMapping("/list")
    public void list(){
        log.info("todo list.........");
    }
    
    @RequestMapping(value = "/register", method= RequestMethod.GET)
    public void register(){
        log.info("todo register.........");
    }
}
```
- @RequestMapping에는 method라는 속성을 이용해 GET/POST 방식을 구분해 처리했지만
스프링 4버전 이후에는 @GetMapping, @PostMapping 어노테이션이 추가됨.
```java
@PostMapping(value = "/register")
public void register(){
    log.info("todo register.........");
}
```

### 파라미터 자동 수집과 변환

- 파라미터 자동 수집은 DTO나 VO 등을 메서드의 파라미터로 설정하면 자동으로 전달되는 HttpServletRequest의
파라미터를 수집해 주는 기능이다.
- 단순히 문자열만이 아니라 숫자도 가능하고, 배열이나 리스트, 첨부 파일도 가능.
- 파라미터 수집은 다음과 같은 기준으로 동작
  - 기본 자료형의 경우 자동으로 형 변환 처리 가능
  - 객체 자료형의 경우는 setter를 통해 처리.
  - 객체 자료형의 경우 생성자가 없거나 파라미터가 없는 생성자가 필요
```java
@GetMapping("/ex1")
public void ex1(String name, int age){
    log.info("ex1............");
    log.info("name: {}", name);
    log.info("age: {}", age);
}
```

#### @RequestParam

- 간혹 파라미터가 전달되지 않으면 문제가 발생할 수 있는데 @RequestParam은 defaultValue라는 속성이 있어서
기본값을 지정할 수 있다.
```java
@GetMapping("/ex2")
public void ex2(@RequestParam(name = "name", defaultValue = "AAA") String name,
                @RequestParam(name = "age", defaultValue = "20") int age){
    log.info("ex2............");
    log.info("name: {}", name);
    log.info("age: {}", age);
}
```

#### Formatter를 이용한 파라미터의 커스텀 처리

- HTTP는 문자열로 데이터를 전달하기 때문에 컨트롤러는 문자열을 기준으로 특정한 클래스의 객체로 처리하는 작업이 진행됨.
- 개발에서 가장 문제가 되는 타입이 바로 날짜 관련 타입
```java
public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(object);
    }
}
```
```xml
<mvc:annotation-driven conversion-service="conversionService"/>
<bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
<property name="formatters">
  <set>
    <bean class="org.zerock.springex.controller.formatter.LocalDateFormatter"/>
  </set>
</property>
</bean>
```

### 객체 자료형의 파라미터 수집

- 기본 자료형과 달리 객체 자료형을 파라미터로 처리하기 위해서는 객체가 생성되고 setter를 이용해 처리.
```java
@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private long tno;
    private String title;
    private LocalDate dueDate;
    private boolean finished;
    private String writer;
}

@PostMapping("/register")
public void registerPost(TodoDTO todoDTO) {
  log.info("Post todo register........");
  log.info(todoDTO);
}
```

### Model이라는 특별한 파라미터

- 스프링 MVC는 기본적으로 웹 MVC와 동일한 방식이므로 모델이라고 부르는 데이터를 JSP까지 전달할 필요가 있음.
- 순수 서블릿 방식에서는 req.setAttribute()를 이용해 데이터를 담아 JSP까지 전달했지만 스프링 MVC 방식에서는 Model
이라는 객체를 이용해 처리.
- 초기 스프링 MVC에서는 ModelAndView라는 객체를 생성하는 방식으로 사용했지만 3버전 이후엔 Model이라는 파라미터만 추가하면 됨.
- Model에는 addAttribute()라는 메서드를 이용해 뷰에 전달할 이름과 값을 지정할 수 있음.

#### Java Beans와 @ModelAttribute

- 스프링 MVC의 컨트롤러는 특이하게 파라미터로 getter/setter를 이용하는 Java Beans 형식의 사용자 정의 클래스가
파라미터인 경우에는 자동으로 화면까지 객체를 전달.
```java
@GetMapping("/ex4")
public void ex4(TodoDTO todoDTO, Model model){
    log.info(todoDTO);
}
```
- 이럴 경우 JSP에서는 별도의 처리 없이 ${todoDTO}를 사용 가능.
- 자동으로 생성된 변수명 대신 별도의 이름을 사용하고 싶다면 명시적으로 @ModelAttribute를 지정할 수 있음.

```java
@GetMapping("/ex4")
public void ex4(@ModelAttribute("dto") TodoDTO todoDTO, Model model){
    log.info(todoDTO);
}
```
- 이럴 경우 JSP에서 ${dto}로 처리 가능

### RedirectAttributes와 리다이렉션

- PRG(Post-Redirect-Get)패턴을 처리하기 위해서 스프링 MVC에서는 RedirectAttributes 타입 제공.
- RedirectAttributes 역시 Model과 마찬가지로 파라미터로 추가해주기만 하면 자동으로 생성되는 방식.
- RedirectAttributes의 중요 메서드
  - addAttribute(키, 값): 리다이렉트할 때 쿼리 스트링이 되는 값을 지정
  - addFlashAttribute(키, 값): 일회용으로만 데이터를 전달하고 삭제되는 값을 지정
- addAttribute로 데이터를 추가하면 리다이렉트할 URL에 쿼리 스트링이 추가되고, addFlashAttribute를 이용하면 URL에는 보이지 않지만 JSP에서 일회용으로 사용가능.
```java
@GetMapping("/ex5")
public String ex5(RedirectAttributes redirectAttributes) {
    redirectAttributes.addAttribute("name", "ABC");
    redirectAttributes.addFlashAttribute("result", "success)");

    return "redirect:/ex6";
}
```

### 다양한 리턴 타입

- 스프링 MVC는 파라미터도 자유롭게 지정하듯 메서드의 리턴 타입도 다양하게 사용할 수있음.
- 주로 사용하는 리턴 타입
  - void
  - 문자열
  - 객체나 배열, 기본 자료형
  - ResponseEntity
- 일반적으로 화면이 따로 있는 경우에는 주로 void 혹은 문자열 사용
- JSON 타입을 활용할 때에는 객체나 ResponseEntity 타입을 주로 사용
- void는 컨트롤러의 @RequestMapping 값과 @GetMapping 등 메서드에서 선언된 값을 그대로 뷰의 이름으로 사용.
  - 주로 상황에 관계없이 동일한 화면을 보여주는 경우
- 문자열은 상황에 따라서 다른 화면을 보여주는 경우에 사용
- 문자열의 경우 특별한 접두어를 사용할 수 있음.
  - redirect: 리다이렉션을 이용하는 경우
  - forward: 브라우저의 URL은 고정하고 내부적으로 다른 URL로 처리하는 경우
- forward를 이용하는 경우는 거의 없고 주로 redirect만 사용

### 스프링 MVC에서 주로 사용하는 어노테이션들

#### 컨트롤러 선언부에서 사용하는 어노테이션

- @Controller: 스프링 빈이 처리됨을 명시
- @RestController: REST 방식의 처리를 위한 컨트롤러임을 명시
- @RequestMapping: 특정한 URL 패턴에 맞는 컨트롤러인지를 명시

#### 메서드 선언부에 사용하는 어노테이션

- @GetMapping/@PostMapping/@DeleteMapping/@PutMapping..: HTTP 전송 방식에 따라 해당 메서드를 지정하는 경우에 사용
- @RequestMapping: GET/POST 방식 모두를 지원하는 경우에 사용
- @ResponseBody: REST 방식에서 사용

#### 메서드의 파라미터에 사용하는 어노테이션

- @RequestParam: Request에 있는 특정한 이름의 데이터를 파라미터로 받아서 처리하는 경우에 사용
- @PathVariable: URl 경로의 일부를 변수로 삼아서 처리하기 위해 사용
- @ModelAttribute: 해당 파라미터는 반드시 Model에 포함되어서 다시 뷰로 전달됨을 명시

### 스프링 MVC의 예외 처리

- 컨트롤러에서 발생하는 예외를 처리하는 가장 일반적인 방식은 @ControllerAdvice를 이용하는 것
- @ControllerAdvice가 선언된 클래스는 스프링 빈으로 처리됨.

### @ExceptionHandler

- @ControllerAdvice의 메서드들에는 @ExceptionHandler 어노테이션을 사용할 수 있음.
- 이를 이용해 전달되는 Exception 객체들을 지정하고 메서드의 파라미터에서 이를 이용할 수 있음.
```java
@ControllerAdvice
@Log4j2
public class CommonExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    public String exceptNumber(NumberFormatException numberFormatException) {
        log.error("-------------------------");
        log.error(numberFormatException.getMessage());

        return "NUMBER FORMAT EXCEPTION";
    }
}
```

### 범용적인 예외처리

- 어디선가 문제가 발생하고 이를 자세한 메시지로 확인하고 싶은 경우가 많음.
- 예외 처리의 상위 타입인 Exception 타입을 처리하도록 구성하면 됨.
```java
@ResponseBody
@ExceptionHandler(Exception.class)
public String exceptCommon(Exception exception){
    log.error("--------------------");
    log.error(exception.getMessage());

    StringBuffer buffer = new StringBuffer("<ul>");
    buffer.append("<li>" + exception.getMessage() + "</li>");
    Arrays.stream(exception.getStackTrace()).forEach(stackTraceElement -> {
        buffer.append("<li>" + stackTraceElement + "</li>");
    });
    buffer.append("</ul>");

    return buffer.toString();
}
```
- 개발시에 디버깅용으로 예외처리를 해두고 배포할 때는 별도의 에러 페이지를 만들어서 사용하는 것이 좋음.

### 404 에러 페이지와 @ResponseStatus

- 존재하지 않는 URL을 호출하면 톰캣이 만들어낸 화면을 보게 됨.
- @ControllerAdvice 메서드에 @ResponseStatus를 이용하면 상태에 맞는 화면을 별도로 작성할 수 있음.
```java
@ExceptionHandler(NoHandlerFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public String notFound(){
    return "custom404";
}
```
- DispatcherServlet의 설정 조정 필요
```xml
<init-param>
    <param-name>throwExceptionIfNoHandlerFound</param-name>
    <param-value>true</param-value>
</init-param>
```

---------------------

## 스프링 Web MVC 구현하기

### 프로젝트의 구현 목표와 준비

- 검색과 필터링을 적용할 수 있는 화면을 구성하고 MyBatis의 동적 쿼리를 이용해서 상황에 맞는 Todo들을 검색
- 새로운 Todo를 등록할 때 문자열, boolean, LocalDate를 자동으로 처리하도록 함.
- 목록에서 조회 화면으로 이동할 때 모든 검색, 필터링, 페이징 조건을 유지하도록 구성
- 조회 화면에서는 모든 조건을 유지한 채로 수정/삭제 화면으로 이동하도록 구성
- 삭제 시에는 다시 목록 화면으로 이동
- 수정 시에는 다시 조회 화면으로 이동하지만 검색, 필터링, 페이징 조건은 초기화

### build.gradle

```groovy
    compileOnly('javax.servlet:javax.servlet-api:4.0.1')

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")

    // Spring
    implementation group: 'org.springframework', name: 'spring-core', version: '5.3.19'
    implementation group: 'org.springframework', name: 'spring-context', version: '5.3.19'
    implementation group: 'org.springframework', name: 'spring-test', version: '5.3.19'
    implementation group: 'org.springframework', name: 'spring-webmvc', version: '5.3.19'
    implementation group: 'org.springframework', name: 'spring-jdbc', version: '5.3.19'
    implementation group: 'org.springframework', name: 'spring-tx', version: '5.3.19'

    // Lombok
    compileOnly('org.projectlombok:lombok:1.18.24')
    annotationProcessor('org.projectlombok:lombok:1.18.24')
    testCompileOnly('org.projectlombok:lombok:1.18.24')
    testAnnotationProcessor('org.projectlombok:lombok:1.18.24')

    // Log4j2
    implementation group: 'org.apache.logging.log4j', name:'log4j-core',version:'2.17.2'
    implementation group: 'org.apache.logging.log4j', name:'log4j-api',version:'2.17.2'
    implementation group: 'org.apache.logging.log4j', name:'log4j-slf4j-impl',version:'2.17.2'

    // jstl
    implementation group: 'jstl', name: 'jstl', version: '1.2'

    // DataSource
    implementation 'org.mariadb.jdbc:mariadb-java-client:3.0.4'
    implementation group: 'com.zaxxer', name: 'HikariCP', version: '5.0.1'

    // MyBatis
    implementation 'org.mybatis:mybatis:3.5.9'
    implementation 'org.mybatis:mybatis-spring:2.0.7'

    //ModelMapper
    implementation group: 'org.modelmapper', name: 'modelmapper', version: '3.0.0'

    //Validate
    implementation group: 'org.hibernate', name: 'hibernate-validator', version:'6.2.1.Final'
```

### ModelMapper 설정과 @Configuration

- @Configuration은 해당 클래스가 스프링 빈에 대한 설정을 하는 클래스임을 명시.
```java
@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper getMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
```

```xml
<context:component-scan base-package="org.zerock.springex.config"/>
```
- @Bean 은 해당 메소드의 실행 결과로 반환된 객체를 스프링의 빈으로 등록시키는 역할을 함.

### MyBatis와 스프링을 이용한 영속 처리

1. VO 선언
2. Mapper 인터페이스 개발
3. XML 개발
4. 테스트 코드 개발

- MyBatis를 이용하면 '?' eotlsdp '#{..}'과 같이 파라미터를 처리. '#{...}' 부분은 
PreparedStatement로 다시 변경되면서 ? 로 처리하고 주어진 객체의 get...을 호출한 결과를 적용.
```xml
<insert id="insert">
    insert into tbl_todo (title, dueDate, writer) values( #{title}, #{dueDate}, #{writer})
</insert>
```

### 한글 처리를 위한 필터 설정

- web.xml
```xml
<filter>
    <filter-name>encoding</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>encoding</filter-name>
    <servlet-name>appServlet</servlet-name>
</filter-mapping>
```

### @Valud를 이용한 서버사이드 검증

- 과거의 웹 개발에는 자바스크립트를 이용해서 브라우저에서만 유효성 검사를 진행하는 방식이 많았지만,
모바일과 같이 다양한 환경에서 서버를 이용하는 현재에는 프론트쪽의 검증과 더불어 서버에서도 입력되는 값들을
검증하는 것이 일반적.
- 이러한 검증 작업은 컨트롤러에서 @Valid와 BindingResult를 사용해 처리할 수 있다.
```java
public class TodoDTO {
    private long tno;
    
    @NotEmpty
    private String title;
    
    @Future
    private LocalDate dueDate;
    
    private boolean finished;
    
    @NotEmpty
    private String writer;
}

@PostMapping("/register")
public String registerPost(@Valid TodoDTO todoDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
  log.info("Post todo register........");
  if (bindingResult.hasErrors()) {
    log.info("has errors....");
    redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
    return "redirect:/todo/register";
  }
  log.info(todoDTO);
  return "redirect:/todo/list";
}
```

#### JSP에서의 에러 처리

```html
<script>
  const serverValidResult = {}
  <c:forEach items="${errors}" var="error">
  serverValidResult['${error.getField()}'] = '${error.defaultMessage}'
  </c:forEach>
  console.log(serverValidResult)
</script>
```

- 과거에는 검증한 결과를 처리하기 위해서 스프링에서 지원하는 태그를 이용하는 경우가 많았지만,
최근에는 가능하면 JSON과 같이 자바스크립트에서 처리할 수 있는 방식을 선호

### JS로 버튼 눌렀을 때 이동 처리

```html
<script>
  document.querySelector(".btn-primary").addEventListener("click", function(e){
    self.location = "/todo/modify?tno="+${dto.tno}
  },false)

  document.querySelector(".btn-secondary").addEventListener("click", function(e){
    self.location = "/todo/list";
  },false)
</script>
```

### 페이징 처리를 위한 Mapper

- 많은 데이터를 보여주는 작업은 페이징 처리를 해서 최소한의 데이터들을 보여주는 방식을 선호.
- 데이터베이스에서 필요한 만큼의 최소한의 데이터를 가져오고 출력하기 때문에 성능 개선에도 도움이 많이 됨.
- MySQL/MariaDB에서는 limit 이라는 기능을 사용함.
- select * from table order by key desc limit 10;(가져오는 수 fetch)
- select * from table order by key desc limit 10(건너뛰는 수 skip), 10(가져오는 수 fetch);

#### limit 의 단점

- limit이 편리한 페이징 기능을 제공하지만 limit 뒤에 식(expression)은 사용이 불가하고 오직 값 만 주어야 함.

### MyBatis에는 실행 시에 쿼리를 만들수 있는 여러 태그들을 제공

- if
- trim(where, set)
- choose(when, otherwise)
- foreach
```xml
<where>
    <if test="types != null and types.length > 0">
        <foreach collection="types" item="type" open="(" close=") " separator=" OR ">
            <if test="type == 't'.toString()">
                title like concat('%', #{keyword}, '%')
            </if>
            <if test="type == 'w'.toString()">
                writer like concat('%', #{keyword}, '%')
            </if>
        </foreach>
    </if>

    <if test="finished">
        <trim prefix="and">
            finished = 1
        </trim>
    </if>

    <if test="from != null and to != null">
        <trim prefix="and">
            dueDate between  #{from} and #{to}
        </trim>
    </if>
</where>
```

