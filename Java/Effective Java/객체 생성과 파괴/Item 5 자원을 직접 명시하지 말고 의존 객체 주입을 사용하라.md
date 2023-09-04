# 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

## 정적 유틸리티를 잘못 사용한 예

```java
public class SpellChecker{
    private static final Lexicon dictionary = ...;
    private SpellChecker(){}
    
    public static boolean isValid(String word){
        ...
    }
    public static List<String> suggestions(String typo){
        ...
    }
}
```

## 싱글턴을 잘못 사용한 예

```java
public class SpellChecker{
    private static final Lexicon dictionary = ...;
    private SpellChecker(){}
    public static SpellChecker INSTANCE = new SpellChecker();
    
    public static boolean isValid(String word){
        ...
    }
    public static List<String> suggestions(String typo){
        ...
    }
}
```

- 위의 두 방식 모두 사전을 단 하나만 사용한다고 가정한다는 점에서 훌륭하지 않다.
- 사전이 언어별로 따로 있고, 특수 어휘용 사전을 별도로 두기도 한다. 또한 테스트용 사전도 필요할 수 있다.
- SpellChecker가 여러 사전을 사용하려면 dictionary 필드에서 final 한정자를 제거하고 다른 사전으로 교체하는 메서드를 추가할 수 
있지만, 이 방식은 오류를 내기 쉬우며 멀티스레드 환경에서는 쓸 수 없다.

### 사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.

- 대신 클래스(SpellChecker)가 여러 자원 인스턴스를 지원해야 하며, 클라이언트가 원하는 자원(dictionary)를 사용해야 한다.

### 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식

```java
public class SpellChecker {
    private final Lexicon dictionary;

    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    ...
}
```

- 위를 `의존 객체 주입 패턴`이라고 한다.
- 의존 객체 주입이 유연성과 테스트 용이성을 개선해주긴 하지만, 의존성이 수천 개 나 되는 큰 프로젝트에서는 코드를 어지럽게 만들기도 한다.
- 의존 객체 주입 프레임워크를 사용하면 이런 어질러짐을 해소할 수 있다.

## 정리

- 클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다.
- 필요한 자원을 생성자에 넘겨주자.
- 의존 객체 주입 패턴은 클래스의 유연성, 재사용성, 테스트 용이성을 개선해준다.

