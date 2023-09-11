# 비트 필드 대신 EnumSet을 사용하라

- 서로 다른 2의 거듭제곱 값을 할당한 정수 열거 패턴을 사용해 비트별 OR를 사용하여 여러 상수를 하나의
집합으로 모을 수 있으며, 이렇게 만들어진 집합을 비트 필드라 한다.
- 비트 필드를 사용하면 비트별 연산을 사용해 합집합과 교집합 같은 집합 연산을 효율적으로 수행할 수 있다.
- 하지만 비트 필드 값이 그대로 출력되면 단순한 정수 열거 상수를 출력할 때보다 해석하기 어렵고, 최대 몇 비트가 필요한지를
API 작성 시 미리 예측해ㅏ여 적절한 타입을 선택해야 한다.

### EnumSet

- java.util 패키지의 EnumSet 클래스는 열거 타입 상수의 값으로 구성된 집합을 효과적으로 표현해준다.
- Set 인터페이스를 완벽히 구현하며, 타입 안전하고, 다른 어떤 Set 구현체와도 함께 사용할 수 있다.
- 하지만 EnumSet의 내부는 비트 벡터로 구현되었다.
- 원소가 총 64개 이하라면, EnumSet전체를 long 변수 하나로 표현하여 비트 필드에 비견되는 성능을 보여준다.

```java
public class Text{
    public enum Style{BOLD, ITALIC, UNDERLINE, STRIKETHROUGH}
    public void applyStyles(Set<Style> styles){...}
}

text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));
```
- applyStyles 메서드가 EnumSet<Style>이 아닌 Set<Style>을 받은 이유는 모든 클라이언트가 EnumSet을 건네리라 짐작되는 상황이라도
이왕이면 인터페이스로 받는 게 일반적으로 좋은 습관이다.
- 이렇게 하면 좀 특이한 클라이언트가 다른 Set 구현체를 넘기더라도 처리할 수 있기 때문이다.

## 핵심 정리

- 열거할 수 있는 타입을 한데 모아 집합 형태로 사용한다고 해도 비트 필드를 사용할 이유는 없다.
- EnumSet 클래스가 비트 필드 수준의 명료함과 성능을 제공하고 열거 타입의 장점까지 선사하기 때문이다.
