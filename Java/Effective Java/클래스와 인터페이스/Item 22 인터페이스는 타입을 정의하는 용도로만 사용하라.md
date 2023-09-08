# 인터페이스는 타입을 정의하는 용도로만 사용하라

- 인터페이스는 자신을 구현한 클래스의 인스턴스를 참조할 수 있는 타입 역할을 한다.
- 클래스가 어떤 인터페이스를 구현한다는 것은 자신의 인스턴스로 무엇을 할 수 있는지를 클라이언트에 얘기하는 것이다.
- 인터페이스는 이 용도로만 사용해야 한다.

### 상수 인터페이스 안티패턴은 인터페이스를 잘못 사용한 예다.

```java
public interface PhysicalConstants{
    static final double AVOGADROS_NUMBER = 6.022_140_857e23;
    static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;
    static final double ELECTRON_MASS = 9.109_383_56e-31;
}
```

- 클래스 내부에서 사용하는 상수는 외부 인터페이스가 아니라 내부 구현이다.
- 상수 인터페이스를 구현하는 것은 내부 구현을 클래스의 API로 노출하는 행위다.
- 클래스가 어떤 상수 인터페이스를 쓰는지는 클라이언트에 아무런 의미가 없다.
- 오히려 혼란을 주고 클라이언트 코드가 내부 구현에 해당하는 이 상수들에게 종속되게 한다.

### 상수를 공개할 목적이라면 더 합당한 선택지가 몇 가지 있다.

- 특정 클래스나 인터페이스와 강하게 연관된 상수라면 그 클래스나 인터페이스 자체에 추가해야 한다.
  - Integer와 Double에 선언된 MIN_VALUE, MAX_VALUE
- 열거 타입으로 나타내기 적합한 상수라면 열거 타입으로 만들어 공개하면 된다.
- 인스턴스화할 수 없는 유틸리티 클래스에 담아 공개하는 것도 괜찮다.
```java
public class PhysicalConstants{
    private PhysicalConstants(){} // 생성자를 private으로 하여 인스턴스화 방지
    public static final double AVOGADROS_NUMBER = 6.022_140_857e23;
    public static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;
    public static final double ELECTRON_MASS = 9.109_383_56e-31;
}
```
- 유틸리티 클래스의 상수를 빈번히 사용한다면 정적 임포트(static import)하여 클래스 이름을 생략할 수 있다.

## 정리

- 인터페이스는 타입을 정의하는 용도로만 사용해야 한다.
- 상수 공개용 수단으로는 사용하면 안된다.