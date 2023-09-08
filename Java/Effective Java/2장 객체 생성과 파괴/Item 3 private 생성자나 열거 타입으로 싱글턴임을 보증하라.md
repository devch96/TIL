# private 생성자나 열거 타입으로 싱글턴임을 보증하라

- 싱글턴이란 인스턴스를 오직 하나만 생성할 수 있는 클래스.
- 싱글턴은 함수와 같은 무상태 객체나 설계상 유일해야 하는 시스템 컴포넌트에 쓸 수 있다.
- 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다.

## public static final 필드 방식의 싱글턴

```java
public class Elvis{
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
    public void leaveTheBuilding() {}
}
```

- private 생성자는 static final 필드를 초기화할 때 딱 한 번만 호출된다.
- 예외적으로 권한이 있는 클라이언트는 리플렉션 API인 AccessibleObject.setAccessible을 사용해 private 생성자를 호출할 수 있다.

## 정적 팩토리 방식의 싱글턴

```java
public class Elvis{
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
    public static Elvis getInstance() {return INSTANCE;}
    public void leaveTheBuilding() {}
}
```

- public 필드 방식의 장점
  - 해당 클래스가 싱글턴임이 API에 명백히 드러난다는 것.
- 정적 팩토리 방식의 장점
  - API를 바꾸지 않아도 싱글턴이 아니게 변경할 수 있다는 것.
  - 정적 팩토리를 제네릭 싱글턴 팩토리로 만들 수 있다는 것.
  - 정적 팩토리의 메서드 참조를 공급자로 사용할 수 있다는 것.

- 정적 팩토리 방식의 장점이 필요하지 않다면 public 필드 방식이 좋다.
- 싱글턴 클래스를 직렬화하려면 단순히 Serializable을 구현하는 것이 아닌 모든 인스턴스 필드를 일시적(transient)
라고 선언하고 readResolve 메서드를 제공해야 한다.

## 열거 타입 방식의 싱글턴(바람직한 방법)

```java
public enum Elvis{
    INSTANCE;
    
    public void leaveTheBuilding(){}
}
```

- public 필드 방식과 비슷하지만 더 간결하고 추가 노력 없이 직렬화할 수 있고, 아주 복잡한 직렬화 상황이나 리플렉션 공격에도 
제2의 인스턴스가 생기는 일을 완벽히 막아준다.
- 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.
- 하지만 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다.