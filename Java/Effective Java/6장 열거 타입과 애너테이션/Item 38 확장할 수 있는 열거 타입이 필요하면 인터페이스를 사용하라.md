# 확장할 수 있는 열거 타입이 필요하면 인터페이스를 사용하라

- 열거 타입은 타입 안전 열거 패턴(typesafe enum pattern)보다 거의 모든 상황에서 우수하나 확장할 수 없다는
단점이 있다.

### 타입 안전 열거 패턴
```java
public class TypeSafeEnumPattern{
    private final String type;
    
    private final TypeSafeEnumPattern(String type){
        this.type = type;
    }

    public static final TypeSafeEnumPattern TypeA = new TypeSafeEnumPattern("TypeA");
    public static final TypeSafeEnumPattern TypeB = new TypeSafeEnumPattern("TypeB");
    public static final TypeSafeEnumPattern TypeC = new TypeSafeEnumPattern("TypeC");
}
```
- 대부분 상황에서 열거 타입을 확장하는 건 좋지 않은 생각이지만 확장하는 열거 타입이 어울리는 쓰임은 연산 코드다.

### 연산 코드
```java
public interface Operation{
    double apply(double x, double y);
}

public enum BasicOperation implements Operation{
    PLUS("+"){
        public double apply(double x, double y){
            return x+y;
        }
    },
    MINUS("-"){
        public double apply(double x, double y){
            return x-y;
        }
    },
    ...
}

public enum ExtendedOperation implements Operation{
    EXP("^"){
        public double apply(double x, double y){
            return Math.pow(x,y);
        }
    },
    REMAINDER("%"){
        public double apply(double x, double y){
            return x % y;
        }
    },
    ...
}
```

- BasicOperation이 아닌 Operation 인터페이스를 사용하도록 작성되어 있기만 하면 새로 확장한 연산은
기존 연산을 쓰던 곳이면 어디든 쓸 수 있다.
- 개별 인스턴스 수준에서뿐 아니라 타입 수준에서도 확장된 열거 타입을 넘겨 확장된 열거 타입의 원소 모두를 사용하게 할 수도 있다.
```java
public static void main(String[] args){
    double x = Double.parseDouble(args[0]);
    double y = Double.parseDouble(args[1]);
    test(ExtendedOperation.class, x, y);
}

private static <T extends Enum<T> & Operation> void test(
        Class<T> opEnumType, double x, double y) {
    for (Operation op : opEnumType.getEnumConstants())
        ...
}
```
- main 메서드는 test 메서드에 ExtendedOperation의 class 리터럴을 넘겨 확장된 연산들이 무엇인지 알려준다.
- class 리터럴은 한정적 타입 토큰 역할을 한다.
- 인터페이스를 이용해 확장 가능한 열거 타입을 흉내 내는 방식도 문제는 구현을 상속할 수 없다는 점이다.
- 아무 상태에도 이ㅢ존하지 않는 경우에는 디폴트 구현을 이용해 인터페이스에 추가하는 방법이 있다.

## 핵심 정리

- 열거 타입 자체는 확장할 수 없지만, 인터페이스와 그 인터페이스를 구현하는 기본 열거 타입을 함께 사용해 같은 효과를 낼 수 있다.
- 클라이언트는 이 인터페이스를 구현해 자신만의 열거 타입을 만들 수 있다.
- API가 인터페이스 기반으로 작성되었다면 기본 열거 타입의 인스턴스가 쓰이는 모든 곳을 새로 확장한
열거 타입의 인스턴스로 대체해 사용할 수 있다.
