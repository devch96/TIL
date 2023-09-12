# 명명 패턴보다 애너테이션을 사용하라

- 전통적으로 도구나 프레임워크가 특별히 다뤄야 할 프로그램 요소에는 딱 구분되는 명명 패턴을 적용해왔다.

### 명명 패턴 이란?

- 변수나 함수의 이름을 일관된 방식으로 작성하는 패턴

<br>

- JUnit 3까지 테스트 메서드 이름을 test로 시작하게끔 했다.
- 명명 패턴의 문제점
  - 오타가 나면 제대로 작동하지 않는다.(tset으로 시작하면 JUnit은 관심없다)
  - 올바른 프로글매 요소에서만 사용되리라 보증할 방법이 없다.
    - 클래스 이름을 TestSafetyMechanisms로 짓고 JUnit에게 던져주면 JUnit은 클래스 이름에 관심이 없기 때문에 의도한 대로 동작하지 않는다.
    - 프로그램 요소를 매개변수로 전달할 마땅한 방법이 없다.

### 애너테이션

- 위 명명 패턴의 모든 문제를 해결해주는 개념으로 JUnit도 버전 4부터 전면 도입하였다.

### 마커(marker) 애너테이션 타입 선언

```java
import java.lang.annotation.*;
/**
 * 테스트 메서드임을 선언하는 애너테이션.
 * 매개변수 없는 정적 메서드 전용.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface  Test{
}
```
- @Test 애너테이션 타입 선언 자체에도 두 가지의 다른 애너테이션이 달려 있다.
- 애터네이션 선언에 다는 애너테이션을 메타애너테이션이라 한다.
- @Retention(RetentionPolicy.RUNTIME)은 @Test가 런타임에도 유지되어야 한다는 표시다.
- @Target(ElementType.METHOD)는 @Test가 반드시 메서드 선언에서만 사용돼야 한다고 알려준다.
- 아무 매개변수 없이 단순히 대상에 마킹(marking)하는 애너테이션을 마크 애너테이션이라 한다.

### 마커 애너테이션을 처리하는 프로그램

```java
public class RunTests {
    public static void main(String[] args) {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName(args[0]);
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                tests++;
                try {
                    m.invoke(null);
                    passed++;
                } catch (InvocationTargetException wrappedExc){
                    ...
                }
            }
        }
    }
}
```

- isAnnotationPresent가 실행할 메서드를 찾아주는 메서드다.

### 매개변수 하나를 받는 애너테이션 타입

```java
/**
 * 명시한 예외를 던져야만 성공하는 테스트 메서드용 애너테이션
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest{
    Class<? extends Throwable> value();
}
```

- Throwable을 확장한 클래스의 Class 객체를 뜻하는 와일드카드 타입을 갖는다.
- 한정적 타입 토큰의 또 하나의 활용 사례다.

### 매개변수 하나짜리 애너테이션을 처리하는 프로그램

```java
if (m.isAnnotationPresent(Test.class)) {
    tests++;
    try {
        m.invoke(null);
        // 실패
    } catch (InvocationTargetException wrappedExc){
        Throwable exc = wrappedEx.getCause();
        Class<? extends Throwable> excType =
            m.getAnnotation(ExceptionTest.class).value();
        if(excType.isInstance(exc)){
            passed++;
        }
    }
}
```

### 배열 매개변수를 받는 애너테이션 타입

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest{
    Class<? extends Throwable>[] value();
}
```

### 배열 매개변수를 애너테이션을 처리하는 프로그램

```java
if (m.isAnnotationPresent(ExceptionTest.class)) {
    tests++;
    try {
        m.invoke(null);
        // 실패
    } catch (InvocationTargetException wrappedExc){
        Throwable exc = wrappedEx.getCause();
        int oldPassed = passed;
        Class<? extends Throwable>[] excTypes =
            m.getAnnotation(ExceptionTest.class).value();
        for(Class<? extends Throwable> excType : excTypes){
            if(excType.isInstance(exc)){
                passed++;
                break;
            }
        }
        if(oldPassed == passed){
            // 실패
        }
    }
}
```

###  @Repeatable

- 자바 8에서는 여러 개의 값을 받는 애너테이션을 배열 매개변수를 사용하는 대신 애너테이션에 @Repeatable 메타애너테이션을 다는 방식이 있다.
- 단 주의할 점은 @Repeatable을 단 애너테이션을 반환하는 컨테이너 애너테이션을 하나 더 정의해야 하고, @Repeatable에 이 컨테이너 애너테이션의
class 객체를 매개변수로 전달해야 한다.
- 또한 컨테이너 애너테이션은 내부 애너테이션 타입의 배열을 반환하는 value 메서드를 정의해야 한다.
- 마지막으로 컨테이너 애너테이션 타입에 적절한 보존 정책(@Retention)과 적용 대상(@Target)을 명시해야 한다.

### 반복 가능한 애너테이션 타입

```java
// 반복 가능한 애너테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class)
public @interface ExceptionTest {
  Class<? extends Throwable> value();
}

// 컨테이너 애너테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTestContainer {
  ExceptionTest[] value();
}

// 사용
@ExceptionTest(IndexOutOfBoundsException.class)
@ExceptionTest(NullPointerException.class)
public static void ...
```

-  반복 가능 애너테이션을 여러 개 달면 하나만 달았을 때와 구분하기 위해 해당 컨테이너 애너테이션 타입이 적용된다.
- isAnnotationPresent로 반복 가능 애너테이션이 달렸는지 검사하면 컨테이너 애너테이션 타입이 적용되서 false 가 나온다.

### 반복 가능한 애너테이션 다루기

```java
if (m.isAnnotationPresent(ExceptionTest.class) || m.isAnnotationPresesnt(ExceptionTestContainer.class)) {
    tests++;
    try {
        m.invoke(null);
        // 실패
    } catch (InvocationTargetException wrappedExc){
        Throwable exc = wrappedEx.getCause();
        int oldPassed = passed;
        ExceptionTest[] excTests = m.getAnnotationByType(ExceptionTest.class);
        for(ExceptionTest excTest : excTests){
            if(excTest.value().isInstance(exc)){
                passed++;
                break;
            }
        }
        if(oldPassed == passed){
            // 실패
        }
    }
}
```

### 애너테이션으로 할 수 있는 일을 명명 패턴으로 처리할 이유는 없다.

## 정리

- 명명 패턴은 개발자간의 약속이므로 쉽게 깨질 위험이 있음.
- 컴파일타임에서 오류를 잡는것이 가장 좋기에 애너테이션으로 대체해야 함.