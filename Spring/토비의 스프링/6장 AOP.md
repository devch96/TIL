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

- 인터페이스의 메서드가 30개로 늘어난다면?
  - 인터페이스가 바뀐다면 클래스로 직접 구현한 프록시는 매번 코드를 추가해야 함
  - 하지만 다이내믹 프록시를 생성해서 사용하는 코드는 전혀 손댈 게 없음
    - 다이내믹 프록시가 만들어질 때 추가된 메서드가 자동으로 포함됨 
    - invoke() 메서드에서 처리되기 때문
- 리플렉션은 매우 유연하고 막강한 기능을 가진 대신 주의 깊게 사용할 필요가 있음
  - 캐스팅 오류가 발생할 수 있음
  - Method를 이용한 타깃 오브젝트의 메서드 호출 후 리턴 타입을 확인해서 캐스팅하는 방법으로 수정

```java
public class UppercaseHandler implements InvocationHandler {
    Object target;
    private UppercaseHandler(Object target) {
        this.target = target;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if(ret instanceof String str) { // Java 17의 새로운 기능 instanceof 패턴 매칭 적용
            return str.toUpperCase();
        }
        return ret;
    }
}
```

- InvocationHandler는 타깃의 종류에 상관없이도 적용이 가능함
  - 리플렉션의 Method 인터페이스를 이용해 타깃의 메서드를 호출하는 것이니 타입의 타깃으로 제한할 필요도 없음
- 메서드의 이름에 대한 조건을 걸 수도 있음

```java
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object ret = method.invoke(target, args);
    if(ret instanceof String str && method.getName().startsWith("say")) {
        return str.toUpperCase();
    }
    return ret;
}
```

### 다이내믹 프록시를 이용한 트랜잭션 부가기능

- 트랜잭션이 필요한 클래스와 메서드가 증가하면 프록시 클래스를 일일이 구현하는 것ㄱ은 큰 부담
- 트랜잭션 부가기능을 제공하는 다이내믹 프록시를 만들어 적용하는 방법이 효율적
  - InvocationHandler는 한 개만 정의해도 충분하기 때문

#### 트랜잭션 InvocationHandler

```java
public class TransactionHandler implements InvocationHandler {
    private Object target; // 부가기능을 제공할 타깃 오브젝트
    private PlatformTransactionManager transactionManager; // 트랜잭션 기능을 제공하는 데 필요한 트랜잭션 매니저
    private String pattern; // 트랜잭션을 적용할 메서드 이름 패턴
    
    public void setTarget(Object target) {
        this.target = target;
    }
    
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
    public Objectg invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().startsWith(pattern)) { // 트랜잭션 적용 대상 메서드를 선별하여 트랜잭션 경계설정 기능을 부여
            return invokeInTransaction(method, args);
        }else {
            return method.invoke(target, args);
        }
    }
    
    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try { // 트랜잭션을 시작하고 타깃 오브젝트의 메서드를 호출. 예외가 발생하지 않으면 커밋
            Object ret = method.invoke(target, args);
            this.transactionManager.commit(status);
            return ret;
        } catch (InvocationTargetException e) { // 예외가 발생하면 롤백
            this.transactionManager.rollback(status);
            throw e.getTargetException();
        }
    }
}
```

### 다이내믹 프록시를 위한 팩토리 빈

- 스프링은 지정된 클래스 이름을 가지고 리플렉션을 이용해서 해당 클래스의 오브젝트를 만든다.
- 하지만 다이내믹 프록시 오브젝트는 이런 식으로 프록시 오브젝트가 생성되지 않고, 클래스 자체도 내부적으로 다이내믹하게 새로 정의해서 사용하기 때문에
다이내믹 프록시 오브젝트의 클래스가 어떤 것인지도 알 수 없다
  - 사전에 프록시 오브젝트의 클래스 정보를 미리 알아내서 빈으로 정의할 방법이 없음
  - 다이내믹 프록시는 Proxy 클래스의 newProxyInstance() 스태틱 메서드를 통해서만 만들 수 있음

#### 팩토리 빈

- 스프링은 클래스 정보를 가지고 디폴트 생성자를 통해 오브젝트를 만드는 방법 외에도 빈을 만들 수 있는 여러 가지 방법을 제공함
  - 팩토리 빈을 아용한 생성 방법
- 팩토리 빈이란 스프링을 대신해서 오브젝트의 생성로직을 담당하도록 만들어진 특별한 빈

```java
public interface FactoryBean<T> {
    T getObject() throws Exception; // 빈 오브젝트를 생성해서 돌려준다
    Class<? extends T> getObjectType(); // 생성되는 오브젝트 타입을 알려준다
    boolean isSingleton(); // getObject()가 돌려주는 오브젝트가 항상 같은 싱글톤 오브젝트인지 알려준다
}
```

- FactoryBean 인터페이스를 구현한 클래스를 스프링의 빈으로 등록하면 팩토리 빈으로 동작한다

##### 학습 테스트

```java
public class Message {
    String text;
    
    private Message(String test) {
        this.text = text; // 외부에서 생성자를 통해 오브젝트를 만들 수 없음
    }
    
    public String getText() {
        return text;
    }
    
    public static Message newMessage(String text) {
        return new Message(text); // 생성자 대신 사용할 수 있는 스태틱 팩터리 메서드 제공
    }
}
```

- Message 클래스의 오브젝트를 만들려면 newMessage() 라는 스태틱 메서드를 사용해야 하기 때문에 빈으로 등록해서 사용할 수 없음
  - 사실 스프링은 private 생성자를 가진 클래스도 빈으로 등록해주면 리플렉션을 이용해 오브젝트를 만들어줌
    - 리플렉션은 private으로 선언된 접근 규약을 위반할 수 있음
    - 하지만 강제로 생성하면 위험함

```java
public class MessageFactoryBean implements FactoryBean<Message> {
    String text;

    public void setText(String text) {
        this.text = text; // 오브젝트를 생성할 때 필요한 정보를 팩토리 빈의 프로퍼티로 설정하여 대신 DI 받을 수 있게 함
    }

    public Message getObject() throws Exception {
        return Message.newMessage(text); // 실제 빈으로 사용될 오브젝트를 직접 생성함, 코드를 이용하기 때문에 복잡한 방식의 오브젝트 생성과 초기화 작업도 가능
    }

    public Class<? extends Message> getObjectType() {
        return Message.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
```

#### 다이내믹 프록시를 만들어주는 팩토리 빈

- 스프링 빈에는 팩토리 빈과 UserServiceImpl만 빈으로 등록함
- 팩토리 빈은 다이내믹 프록시가 위임할 타깃 오브젝트인 UserServiceImpl에 대한 레퍼런스를 프로퍼티를 통해 DI 받음

#### 트랜잭션 프록시 팩토리 빈

```java
public class TxProxyFactoryBean implements FactoryBean<Object> {
    Object target;
    PlatformTransactionManager transactionManager;
    String pattern;
    Class<?> serviceInterface;

    // setter

    public Object getObject() throws Exception {
        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(target);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern(pattern);
        return Proxy.newProxyInstance(
                getClass().getClassLoader().new Class[] { serviceInterface },
                txHandler);
    }
    
    public Class<?> getObjectType() {
        return serviceInterface;
    }
    
    public boolean isSingleton() {
        return false;
    }
}
```

### 프록시 팩토리 빈 방식의 장점과 한계

#### 프록시 팩토리 빈의 재사용

- TransactionHandler를 이용하는 다이내믹 프록시를 생성해주는 TxProxyFactoryBean은 코드의 수정 없이도 다양한 클래스에 적용 가능
  - 타깃 오브젝트에 맞는 프로퍼티 정보를 설정해서 빈으로 등록해주기만 하면 됨
- 팩토리 빈이기 때문에 각 빈의 타입은 타깃 인터페이스와 일치하여 하나 이상의 팩토리 빈을 동시에 빈으로 등록해도 상관 없음

#### 프록시 팩토리 빈 방식의 장점

- 데코레이터 패턴이 적용된 프록시를 사용하면 생기는 문제점
  - 프록시를 적용할 대상이 구현하고 있는 인터페이스를 구현하는 프록시 클래스를 일일이 만들어야 하는 번거로움
  - 부가적인 기능이 여러 메서드에 반복적으로 나타나게 돼서 코드 중복의 문제가 발생
- 프록시 팩토리 빈은 이 두 가지 문제를 해결
  - 다이내믹 프록시를 사용하면 타깃 인터페이스를 구현하는 클래스를 일일이 만드는 번거로움을 제거할 수 있음
    - 하나의 핸들러 메서드를 구현하는 것만으로도 수많은 메서드에 부가기능을 부여해줄 수 있으니 부가기능 코드의 중복 문제도 사라짐
  - 다이내믹 프록시에 팩토리 빈을 이용한 DI까지 더해주면 번거로운 다이내믹 프록시 생성 코드도 제거할 수 있음

#### 프록시 팩토리 빈의 한계

- 프록시를 통해 타깃에 부가기능을 제공하는 것은 메서드 단위로 일어나는 일
- 하나의 클래스 안에 존재하는 여러 개의 메서드에 부가기능을 한 번에 제공하는 건 어렵지 않게 가능하나 한 번에 여러 개의 클래스에 공통적인
부가기능을 제공하는 일은 불가능
  - 트랜잭션과 같이 비즈니스 로직을 담은 많은 클래스의 메서드에 적용할 필요가 있다면 거의 비슷한 프록시 팩토리 빈의 설정이 중복됨
- 하나의 타깃에 여러 개의 부가기능을 적용하려 할 때도 문제
  - 같은 타깃 오브젝트에 대해 트랜잭션 뿐 아니라 보안 기능을 제공하는 프록시도 제공하고 기능 검사를 위해 주고받는 메서드 정보를 저장해두는
  부가기능을 추가하고 싶다면 프록시 팩토리 빈 설정의 부가기능이 개수만큼 따라 붙음
- TransactionHandler 오브젝트가 프록시 팩토리 빈 개수만큼 만들어짐
  - TransactionHandler는 타깃 오브젝트를 프로퍼티로 갖고 있음
  - 트랜잭션 부가기능을 제공하는 동일한 코드임에도 불구하고 타깃 오브젝트가 달라지면 새로운 TransactionHandler 오브젝트를 만들어야 함

---------------

## 스프링의 프록시 팩토리 빈

### ProxyFactoryBean

- 스프링은 일관된 방법으로 프록시를 만들 수 있게 도와주는 추상 레이어를 제공함
  - 생성된 프록시는 스프링의 빈으로 등록돼야 함
- TxProxyFactoryBean과 달리 ProxyFactoryBean은 순수하게 프록시를 생성하는 작업만을 담당하고 프록시를 통해 제공해줄 부가기능은 별도의 빈에 둘 수 있음
- ProxyFactoryBean이 생성하는 프록시에서 사용할 부가기능은 MethodInterceptor 인터페이스를 구현해서 만듬
- InvocationHandler는 invoke() 메서드에서 타깃 오브젝트에 대한 정보를 제공하지 않기 때문에 타깃은 InvocationHandler를 구현한 클래스가 직접 알고 있어야 하지만
MethodInterceptor의 invoke() 메서드는 ProxyFactoryBean으로부터 타깃 오브젝트에 대한 정보까지 함께 제공받기에 독리벚ㄱ으로 만들어질 수 있다.
  - 따라서 타깃이 다른 여러 프록시에서 함께 사용할 수 있고, 싱글톤 빈으로 등록이 가능하다

```java
public void proxyFactoryBean() {
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    pfBean.setTarget(new HelloTarget());
    pfBean.addAdvice(new UppercaseAdvice()); // 부가기능을 담은 어드바이스. 여러개 가능
    
    Hello proxiedHello = (Hello) pfBean.getObject();
    assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
}

static class UppercaseAdvice implements MethodInterceptor {
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String ret = (String) invocation.proceed();
        return ret.toUpperCase();
    }
}
```

#### 어드바이스: 타깃이 필요 없는 순수한 부가기능

- MethodInterceptor를 구현한 UppercaseAdvice에는 타깃 오브젝트가 등장하지 않는다
- MethodInterceptor로는 메서드 정보와 함께 타깃 오브젝트가 담긴 MethodInvocation 오브젝트가 전달된다
- ProxyFactoryBean 하나만으로 여러 개의 부가기능을 제공해주는 프록시를 만들 수 있음
  - 새로운 부가기능을 추가해야 할 때마다 프록시와 프록시 팩토리 빈을 추가하지 않아도 됨

### 포인트컷: 부가기능 적용 대상 메서드 선정 방법

- InvocationHandler를 직접 구현했을 때 부가기능 적용 외에 부가기능을 적용할 대상 메서드를 선정하는 것이 있었다.
  - pattern 이라는 메서드 이름 비교용 스트링 값
- MethodInterceptor 오브젝트는 여러 프록시가 공유해서 사용하기에 타깃 정보를 갖고 있지 않다
  - 따라서 싱글톤 빈으로 등록이 가능하다
  - 트랜잭션 적용 대상 메서드 이름 패턴을 넣어주는건 안됨
- 스프링은 부가기능을 제공하는 오브젝트를 어드바이스라고 부르고, 메서드 선정 알고리즘을 담은 오브젝트를 포인트컷이라고 부른다
  - 어드바이스와 포인트컷 모두 프록시에 DI로 주입돼 사용됨
  - 두 가지 모두 여러 프록시에서 공유가 가능하도록 만들어지기 때문에 싱글톤 빈으로 등록이 가능함
- 프록시는 클라이언트로부터 요청을 받으면 먼저 포인트컷에게 부가기능을 부여할 메서드인지 확인해달라고 요청함
  - 포인트컷은 Pointcut 인터페이스를 구현해 만듬
- 프록시는 포인트컷으로부터 부가기능을 적용할 대상 메서드인지 확인받으면, MethodInterceptor 타입의 어드바이스를 호출함

```java
public void pointcutAdvisor() {
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    pfBean.setTarget(new HelloTarget());
    
    NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
    pointcut.setMappedName("sayH*");
    pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
    
    Hello proxiedHello = (Hello) pfBean.getObject();
    
    assertThat
            ...
}
```

- 어드바이저 = 포인트컷(메서드 선정 알고리즘) + 어드바이스(부가기능)

### ProxyFactoryBean 적용

#### TransactionAdvice

- 부가기능을 담당하는 어드바이스는 Advice 인터페이스를 구현해서 만듬

```java
public class TransactionAdvice implements MethodInterceptor { 
    PlatformTransactionManager transactionManager;
    
    public void setTransactionManager(PlatformtransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            Object ret = invocation.proceed();
            this.transactionManager.commit(status);
            return ret;
        } catch(RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
```

#### 어드바이스와 포인트컷의 재사용

- ProxyFactoryBean은 스프링의 DI와 템플릿/콜백 패턴, 서비스 추상화 등의 기법이 모두 적용된 것
  - 독립적이며, 여러 프록시가 공유할 수 있는 어드바이스와 포인트컷으로 확장 기능을 분리할 수 있음
- 메서드의 선정을 위한 포인트컷이 필요하면 이름 패턴만 지정해서 ProxyFactoryBean에 등록해주면 됨
  - 트랜잭션을 적용할 메서드의 일므은 일관된 명명규칙을 정해두면 하나의 포인트컷으로 충분할 수 있음

---------------

## 스프링 AOP

### 자동 프록시 생성

- 프록시 팩토리 빈 방식의 접근 방법의 한계라고 생각했던 두 가지 문제가 남아있음
  - 부가기능이 타깃 오브젝트마다 새로 만들어지는 문제
    - 스프링 ProxyFactoryBean의 어드바이스를 통해 해결
  - 부가기능의 적용이 필요한 타깃 오브젝트마다 거의 비슷한 내용의 ProxyFactoryBean 빈 설정정보를 추가해주는 부분

#### 중복 문제의 접근 방법

- JDK의 다이내믹 프록시는 특정 인터페이스를 구현한 오브젝트에 대해서 프록시 역할을 해주는 클래스를 런타임 시 내부적으로 만듬
  - 클래스 소스가 따로 남지 않을 뿐이지 타깃 인터페이스의 모든 메서드를 구현하는 클래스가 만들어짐
  - 개발자가 일일이 인터페이스 메서드를 구현하는 프록시 클래스를 만들어서 위임과 부가기능의 코드를 중복해서 넣어주지 않아도 되게 해줌

#### 빈 후처리기를 이용한 자동 프록시 생성기

- 빈 후처리기는 이름 그대로 스프링 빈 오브젝트로 만들어지고 난 후에 빈 오브젝트를 다시 가공할 수 있게 해줌
- 스프링은 빈 후처리기가 빈으로 등록되어 있으면 빈 오브젝트가 생성될 때마다 빈 후처리기에 보내서 후처리 작업을 요청함
  - 빈 후처리기는 빈 오브젝트의 프로퍼티를 강제로 수정할 수도 있고, 별도의 초기화 작업을 수행할 수도 있음
  - 만들어진 빈 오브젝트 자체를 바꿔치기 할 수도 있음
- 이를 잘 이용하면 스프링이 생성하는 빈 오브젝트의 일부를 프록시로 포장하고 프록시를 빈으로 대신 등록할 수 있음
- DefaultAdvisorAutoProxyCreator는 빈으로 등록된 모든 어드바이저 내의 포인트컷을 이용해 전달받은 빈이 프록시 적용 대상인지 확인함
  - 프록시 적용 대상이면 내장된 프록시 생성기에게 현재 빈에 대한 프록시를 만들게 하고 만들어진 프록시에 어드바이저를 연결함
- 빈 후처리기는 프록시가 생성되면 원래 컨테이너가 전달해준 빈 오브젝트 대신 프록시 오브젝트를 컨테이너에게 돌려줌

#### 확장된 포인트컷

- 포인트컷은 클래스 필터와 메서드 매처 두 가지를 돌려주는 메서드를 갖고 있다
- 포인트컷 선정 기능을 모두 적용한다면 먼저 프록시를 적용할 클래스인지 판단하고 나서 적용 대상 클래스인 경우에는 어드바이스를 적용할 메서드인지 확인하는 식으로 동작
- 클래스 자체가 프록시 적용 대상이 아니라면 어드바이스를 통한 부가기능 부여는 물 건너감

### DefaultAdvisorAutoProxyCreator의 적용

#### 클래스 필터를 적용한 포인트컷 작성

```java
public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {
    public void setMappedClassName(String mappedClassName) {
        this.setClassFilter(new SimpleClassFilter(mappedClassName));
    }
    
    static class SimpleClassFilter implements ClassFilter {
        String mappedName;
        
        private SimpleClassFilter(String mappedName) {
            this.mappedName = mappedName;
        }
        
        public boolean matches(Class<?> clazz) {
            return PatternMatchUtils.simpleMatch(mappedName, clazz.getSimpleName());
        }
    }
}
```

### 포인트컷 표현식을 이용한 포인트컷

- 포인트컷은 메서드의 이름과 클래스의 이름 패턴을 각각 클래스 필터와 메서드 매처 오브젝트로 비교해서 선정하는 방식
  - 복잡하고 세밀한 기준을 이용해 클래스나 메서드를 선정하면?
  - 리플렉션 API를 통해서 사용
  - 하지만 작성하기 번거로움
- 스프링은 정규식이나 JSP의 EL과 비슷한 일종의 표현식 언어를 사용해 포인트컷을 작성할 수 있게 제공함

#### 포인트컷 표현식

- AspectJExpressionPointcut 클래스를 사용하면 됨
- 포인트컷 표현식은 자바의 RegEx 클래스가 지우너하는 정규식처럼 간단한 문자열로 복잡한 선정조건을 쉽게 만들어낼 수 있는 강력한 표현식을 지원함
- AspectJ 포인트컷 표현식이라고 함

#### 포인트컷 표현식 문법

- AspectJ 포인트컷 표현식은 포인트컷 지시자를 이용해 작성함
  - 대표적으로 사용되는 것은 execution()
- execution([접근제한자 패턴] 타입패턴 [타입패턴.] 이름패턴 (타입패턴 | *..*, ...))
- 리플렉션으로 Target 클래스의 minus()라는 메서드의 풀 시그니처를 가져와 비교해보면 이해하기 쉬움

```java
System.out.println(Target.class.getMethod("minus", int.class, int.class));

public int springbook.learningtest.spring.pointcut.Target.minus(int,int) throws java.lang.RuntimeException
```

- public
  - 접근제한다
  - public, protected, private
  - 포인트컷 표현식에서는 생략 가능
  - 생략하면 조건을 부여하지 않는 것
- int
  - 리턴 값의 타입을 나타내는 패턴
  - 포인트컷의 표현식에서 리턴 값의 타입 패턴은 필수항목
  - *를 써서 모든 타입을 다 선택하겠다고 해도 됨
  - 생략 불가
- spring~~~.Target
  - 패키지와 타입 이름을 포함한 클래스의 타입 패턴
  - 생략 가능
  - 생략하면 모든 타입 허용
  - 패키지 이름과 클래스 또는 인터페이스 이름에 *를 사용
  - .. 를 사용하면 한 번에 여러 개의 패키지를 선택할 수 있음
- minus
  - 메서드 이름 패턴
  - 필수항목
  - 모든 메서드라면 *
- (int, int)
  - 메서드 파라미터의 타입 패턴

### AOP란 무엇인가?

- 비즈니스 로직을 담은 UserService에 트랜잭션을 적용해온 과정

#### 트랜잭션 서비스 추상화

- 트랜잭션 기술에 종속되는 코드
  - JDBC의 로컬 트랜잭션 방식을 적용한 코드를 JTA를 이용한 트랜잭션 방식으로 바꾸면 모든 트랜잭션 적용 코드를 수정해야 함
- 서비스 추상화 기법 적용
- 비즈니스 로직 코드는 트랜잭션을 어떻게 처리해야 한다는 구체적인 방법을 몰라도 됨
- DI를 활용한 전형적인 접근 방법

#### 프록시와 데코레이터 패턴

- 추상화를 통해 코드를 제거했지만 비즈니스 로직 코드에는 트랜잭션을 적용하고 있다는 사실이 드러남
- 