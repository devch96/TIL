# @Override 애너테이션을 일관되게 사용하라

- 자바가 기본으로 제공하는 애너테이션 중 가장 중요한 것은 @Override 일 것이다.
- 이 애너테이션은 상위 타입의 메서드를 재정의했음을 뜻한다.

### 다음 코드에서 버그는?

```java
public class Bigram {
    ...

    public boolean equals(Bigram b) {
        return ...
    }

    public int hashCode() {
        return ...
    }

    public static void main(String[] args) {
        Set<Bigram> s = new HashSet<>();
        ...
        System.out.println(s.size());
    }
}
```
- Bigram은 equals를 재정의하고 hashCode도 함께 재정의했다.
- 하지만 이것은 재정의가 아니라 다중정의(overloading)이다.
- 왜냐하면 Objects의 equals를 재정의하려면 매개변수 탙입이 Object여야 하는데 그렇지 않은 것이다.
- 컴파일시에는 문제가 없지만 런타임때 문제가 발생하므로 오류를 찾기 힘들다.
- 컴파일시에 오류를 잡으려면 @Override 애너테이션을 달면 된다.

### 상위 클래스의 메서드를 재정의하려는 모든 메서드에 @Override 애너테이션을 달자.
- 예외는 구체 클래스에서 상위 클래스의 추상 메서드를 재정의할 때는 굳이 달지 않아도 된다.
- 대부분의 IDE는 재정의할 메서드를 선택하면 @Override를 자동으로 붙여준다.
- 인터페이스가 디폴트 메서드를 지우너하기 시작하면서, 인터페이스 메서드를 구현한 메서드에도 @Override를 다는 습관을 들이면
시그니처가 올바른지 재차 확신 할 수 있다.(이것또한 IDE가 해준다)

## 핵심 정리

- 재정의한 모든 메서드에 @Override 애너테이션을 의식적으로 달면 실수했을 때 컴파일러가 바로 알려줄 것이다.
- 구체 클래스에서 상위 클래스의 추상 매서드를 재정의한 경우엔 이 애너테이션을 달지 않아도 되지만 단다고 해서 해로울 것도 없다.
