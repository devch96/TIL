# ordinal 메서드 대신 인스턴스 필드를 사용하라

- 열거 타입 상수는 자연스럽게 하나의 정숫값에 대응된다.
- 모든 열거 타입은 해당 상수가 그 열거 타입에서 몇 번째 위치인지를 반환하는 ordinal 메서드를 제공한다.

### ordinal을 잘못 사용한 예

```java
public enum Ensemble{
    SOLO, DUET, TRIO, QUARTET, QUINTET,
    SEXTET, SEPTET, OCTET, NONET, DECTET;
    
    public int numberOfMusicians(){
        return ordinal() + 1;
    }
}
```

- 상수 선언 순서를 바꾸는 순간 오동작하며, 사용 중인 정수와 값이 같은 상수는 추가할 방법이 없다.
- 값을 중간에 비워둘 수도 없다.

### 열거 타입 상수에 연결된 값은 ordinal 메서드로 얻지말고, 인스턴스 필드에 저장하자.

```java
public enum Ensemble{
    SOLO(1), DUET(2), TRIO(3), QUARTET(4) ...
    private final int numberOfMusicians;
    Ensemble(int size){
        this.numberOfMusicians = size;
    }
    public int numberOfMusicians(){
        return numberOfMusicians;
    }
}
```
- Enum API 문서를 보면 "ordinal에 이 메서드는 쓸 일이 거의 없다. EnumSet과 EnumMap같이 열거 타입 기반의 범용
자료구조에 쓸 목적으로 설계되었다" 라고 한다.
