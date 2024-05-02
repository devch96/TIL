# AOP

- IoC/DI, 서비스 추상화와 더불어 스프링의 3대 기반기술
- 스프링에 적용된 가장 인깅 있는 AOP의 적용 대상은 선언적 트랜잭션 기능

## 트랜잭션 코드의 분리

### 메서드 분리

- 트랜잭션 경계설정과 비즈니스 로직이 공존하는 메서드

```java
public void upgradeLevels() throws Exception {
    TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
    try{
        ... // 비즈니스 로직
        this.transactionManager.commit(status);
    } catch (Exception e) {
        this.transactionManager.rollback(status);
        throw e;
    }
}
```

## 다이내믹 프록시와 팩토리 빈

### 프록시와 프록시 패턴, 데코레이터 패턴

- 분리된 부가기능을 담은 클래스는 부가기능 외의 나머지 모든 기능은 원래 핵심기능을 가진 클래스로 위임해줘야함
- 문제는 이렇게 구성했더라도 클라이언트가 핵심기능을 가진 클래스를 직접 사용해버리면 부가기능이 적용될 기회가 없음
  - 부가기능은 마치 자신이 핵심기능을 가진 클래스인 것처럼 꾸며 클라이언트가 자신을 거쳐서 핵심기능을 사용하도록 만들어야 함
  - 클라이언트는 인터페이스를 통해서만 핵심기능을 사용하게 하고, 부가기능 자신도 같은 인터페이스를 구현한 뒤에 자신이 그 사이에 끼어들어야 함
- 자신이 클라이언트가 사용하려고 하는 실제 대상인 것처럼 위장해서 클라이언트의 요청을 받아주는 것을 대리자, 대리인과 같은 역할을 한다고 해서
프록시(proxy)라고 부름
  - 프록시를 통해 최종적으로 요청을 위임받아 처리하는 실제 오브젝트를 타깃 또는 실체라고 부름
- 프록시는 사용 목적에 따라 두 가지로 구분
  - 클라이언트가 타깃에 접근하는 방법을 제어하기 위해
  - 타깃에 부가적인 기능을 부여해주기 위해

#### 데코레이터 패턴

- 타깃에 부가적인 기능을 런타임 시 다이내믹하게 부여해주기 위해 프록시를 사용하는 패턴
  - 컴파일 시점에서는 어떤 방법과 순서로 프록시와 타깃이 연결되어 사용되는지 정해져 있지 않음
- 데코레이터 패턴은 인터페이스를 통해 위임하는 방식이기 때문에 어느 데코레이터에서 타깃으로 연결될지 코드 레벨에선 미리 알 수 없음
- 타깃의 코드에 손대지 않고 클라이언트가 호출하는 방법도 변경하지 않은 채로 새로운 기능을 추가할 때 유용한 방법

#### 프록시 패턴

- 일반적으로 사용하는 프록시라는 용어와 프록시 패턴은 구분할 필요가 있음
  - 전자는 클라이언트와 사용 대상 사이에 대리 역할을 맡은 오브젝트를 두는 방법 총칭
  - 후자는 프록시를 사용하는 방법 중에서 타깃에 대한 접근 방법을 제어하려는 목적을 가진 경우
- 프록시 패턴은 타깃의 기능을 확장하거나 추가하지 않고 클라이언트가 타깃에 접근하는 방식을 변경해줌
  - 타깃 오브젝트를 생성가기가 복잡하거나 당장 필요하지 않은 경우에는 꼭 필요한 시점까지 오브젝트를 생성하지 않는 편이 좋음
  - 타깃 오브젝트에 대한 레퍼런스가 미리 필요할 수 있는데 이럴 때 프록시 패턴을 적용하면 됨

### 다이내믹 프록시

#### 프록시의 구성과 프록시 작성의 문제점

- 프록시는 다음의 두 가지 기능으로 구성됨
  - 타깃과 같은 메서드를 구현하고 있다가 메서드가 호출되면 타깃 오브젝트로 위임함
  - 지정된 요청에 대해서는 부가기능을 수행함

```java
public class UserServiceTx implements UserService {
    UserService userService;
    
    public void add(User user) {
        this.userService.add(user);
    }
    
    public void upgradeLevels() {
        ...
    }
}
```

- 프록시의 역할은 위임과 부가작업
- 프록시를 만들기 번거로운 이유
  - 타깃의 인터페이스를 구현하고 위임하는 코드를 작성하기가 번거로움
    - 부가기능이 필요 없는 메서드도 구현해서 타깃으로 위임하는 코드를 일일이 만들어줘야 함
    - 인터페이스의 메서드가 추가되거나 변경될 때마다 함께 수정해주어야 함
  - 부가기능 코드가 중복될 가능성이 많음

#### 리플렉션

- 다이내믹 프록시는 리플렉션 기능을 이용해서 프록시를 만들어줌
- 리플렉션은 자바의 코드 자체를 추상화해서 접근하도록 하는 것
- 자바의 모든 클래스는 그 클래스 자체의 구성정보를 담은 Class 타입의 오브젝트를 가지고 있음
  - '클래스이름.class' 라고 하거나 getClass() 메서드 호출
- 클래스 오브젝트를 이용하면 클래스 코드에 대한 메타정보를 가져오거나 오브젝트를 조작할 수 있음

```java
String name = "hoon";
Method lengthMethod = name.getClass().getMethod("length");
Method lengthMethod = String.class.getMethod("length");
```

- java.lang.reflect.Method 인터페이스는 메서드에 대한 자세한 정보를 담고 있을 뿐만 아니라 이를 이용해 특정 오브젝트의 메서드를 실행시킬수 있다
  - invoke() 메서드 사용
  - invoke 메서드는 메서드를 실행시킬 대상 오브젝트와 파라미터 목록을 받아 메서드를 호출한 뒤 결과를 Object 타입으로 돌려줌

```java
int length = lengthMethod.invoke(name);
int length = name.length();
```

#### 프록시 클래스

```java
interface Hello {
    String sayHello(String name);
    String sayHi(String name);
    String sayThankYou(String name);
}


// 타킷 클래스
public class HelloTarget implements Hello {
    public String sayHello(String name) {
        return "Hello " + name;
    }
    
    public String sayHi(String name) {
        return "Hi " + name;
    }
    
    public String sayThankYou(String name) {
        return "ThankYou " + name;
    }
}

// 프록시 클래스(위임, 부가기능)
public class HelloUppercase implements Hello {
    Hello hello;
    public HelloUppercase(Hello hello) {
        this.hello = hello;
    }
    
    public String sayHello(String name) {
        return hello.sayHello(name).toUpperCase();
    }
    
    public String sayHi(String name) {
      return hello.sayHi(name).toUpperCase();
    }
    
    public String sayThankYou(String name) {
      return hello.sayThankYou(name).toUpperCase();
    }
}
```

- 이 프록시는 프록시 적용의 일반적인 문제점 두 가지를 모두 갖고 있음
  - 인터페이스의 모든 메서드를 구현해 위임하도록 코드를 만들어야 하며
  - 부가기능인 리턴 값을 대문자로 바꾸는 기능이 모든 메서드에 중복돼서 나타남

#### 다이내믹 프록시 적용

1. 클라이언트가 메서드 호출
2. 클라이언트가 프록시 팩토리에 프록시 요청
2. 다이내믹 프록시가 InvocationHandler에게 메서드 처리 요청
3. InvocationHandler는 타깃에 위임하고 결과 리턴

- 다이내믹 프록시는 프록시 팩토리에 의해 런타임 시 다이내믹하게 만들어지는 오브젝트
- 다이내믹 프록시 오브젝트는 타깃의 인터페이스와 같은 타입으로 만들어짐
- InvocationHandler는 리플렉션의 Method 인터페이스를 파라미터로 받음

```java
public class UppercaseHandler implements InvocationHandler {
    Hello target;
    
    public UppdercaseHandler(Hello target) {
        this.target = target;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String ret = (String) method.invoke(target, args); // 타깃으로 위임, 인터페이스의 메서드 호출에 모두 적용
        return ret.toUpperCase(); // 부가기능 제공
    }
}
```

- 다이내믹 프록시의 생성은 Proxy 클래스의 newProxyInstance() 스태틱 팩토리 메서드를 이용하면 됨

```java
Hello proxiedHello = (Hello) Proxy.newProxyInstance(
        getClass().getClassLoader(), // 동적으로 생성되는 다이내믹 프록시 클래스의 로딩에 사용할 클래스 로더
        new Class[] {Hello.class}, // 구현할 인터페이스
        new UppercaseHandler(new HelloTarget())); // 부가기능과 위임 코드를 담은 InvocationHandler
)
```

#### 다이내믹 프록시의 확장

- 