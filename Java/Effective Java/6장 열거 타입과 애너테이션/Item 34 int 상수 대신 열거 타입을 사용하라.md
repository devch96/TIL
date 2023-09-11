# int 상수 대신 열거 타입을 사용하라

- 열거 타입은 일정 개수의 상수 값을 정의한 다음, 그 외의 값은 허용하지 않는 타입이다.
- 사계절, 태양계의 행성, 카드게임의 카드 종류 등이 좋은 예다.

### 정수 열거 패턴

```java
public static final int APPLE_FUJI = 0;
public static final int APPLE_PIPPIN = 1;
public static final int APPLE_GRANNY_SMITH = 2;

public static final int ORANGE_NAVEL = 0;
public static final int ORANGE_TEMPLE = 1;
public static final int ORANGE_BLOOD = 2;
```

- 정수 열거 패턴은 타입 안전을 보장할 방법이 없고 표현력도 좋지 않다.
- 오렌지를 건네야 할 메서드에 사과를 보내고 동등 연산자로 비교하더라도 컴파일러는 아무런 경고 메세지를 출력하지 않는다.
- 평범한 상수를 나열한 것뿐이라 컴파일하면 그 값이 클라이언트 파일에 그대로 새겨진다. 따라서 상수의 값이 바뀌면 클라이언트도 반드시 다시 컴파일해야 한다.
- 정수 상수는 문자열로 출력해 의미를 찾기가 힘들다.
- 이래서 문자열 열거 패턴이라는 변형 패턴도 있지만 더 안좋다.

### 열거 타입

```java
public enum Apple {FUJI, PIPPIN, GRANNY_SMITH,}
public enum Orange { NAVEL, TEMPLE, BLOOD}
```

- 자바의 열거 타입 자체는 클래스이며, 상수 하나당 자신의 인스턴스를 하나씩 만들어 public static final 필드로 공개한다.
- 클라이언트가 인스턴스를 직접 생성하거나 확장할 수 없으니 열거 타입 선언으로 만들어진 인스턴스들은 딱 하나씩만 존재한다.
- 싱글턴은 원소가 하나뿐인 열거 타입이라 할 수 있고, 거꾸로 열거 타입은 싱글턴의 일반화한 형태라 할 수 있다.
- 열거 타입은 컴파일타임 타입 안전성을 제공한다. Apple 열거 타입을 매개변수로 받는 메서드를 선언했다면, 건네받는 참조는 (null이 아니라면) Apple
의 세 가지 값 중 하나임이 확실하다.
- 열거 타입에는 각자의 이름공간이 있어서 이름이 같은 상수도 평화롭게 공존한다.
- 새로운 상수를 추가하거나 순서를 바꿔도 다시 컴파일하지 않아도 된다.
- 열거 타입의 toString 메서드는 출력하기에 적합한 문자열을 내어준다.
- 열거 타입에는 임의의 메서드나 필드를 추가할 수 있고, 임의의 인터페이스를 구현하게 할 수도 있다.

### 열거 타입에 메서드나 필드를 추가하는 기능

- Apple과 Orange를 예로 들면, 과일의 색을 알려주거나 과일 이미지를 반환하는 메서드를 추가하고 싶을 수 있다.
- 가장 단순하게는 그저 상수 모음일 뿐인 열거 타입이지만 실제로는 클래스이므로 고차원의 추상 개념 하나를 완벽히 표현해낼 수도 있다.

### 거대한 열거 타입

```java
public enum Planet{
    MERCURY(3.302e+23, 2.439e6),
    VENUS  (4.869e+24, 6.052e6),
    ...
    
    private final double mass;
    private final double radius;
    private final double surfaceGravity;
    
    private static final double G = 6.67300E-11;
    
    Planet(double mass, double radius){
        this.mass = mass;
        this.radius = radius;
        surfaceGravity = G * mass / (radius * radius);
    }
    
    ...
}
```

- 열거 타입 상수 각각을 특정 데이터와 연결지으려면 생성자에서 데이터를 받아 인스턴스 필드에 저장하면 된다.
- 열거 타입은 근본적이로 불변이라 모든 필드는 final, private으로 두고 별도의 public 접근자 메서드를 두는 게 낫다.

### 열거 타입을 선언한 클래스 혹은 그 패키지에서만 유용한 기능은 private이나 package-private 메서드로 구현한다.

- 자신을 선언한 클래스 혹은 패키지에서만 사용할 수 있는 기능을 담게 된다.
- 널리 쓰이는 열거 타입은 톱레벨 클래스로 만들고, 특정 톱레벨 클래스에서만 쓰인다면 해당 클래스의 멤버 클래스로 만든다.

### 값에 따라 분기하는 열거 타입

```java
public enum Operation{
    PLUS, MINUS, TIMES, DIVIDE;
    
    public double apply(double x, double y){
        switch(this){
            case PLUS: return x + y;
            case MINUS: return x - y;
            case TIMES: return x * y;
            case DIVIDE: return x / y;
        }
        throw new AssertionError("...");
    }
}
```

- 위 코드는 동작 하나 아름답지 않다.
- 마지막 throw 문은 도달할 일이 없지만 기술적으로는 도달할 수 있기 때문에 생략하면 컴파일 에러가 난다.
- 새로운 상수를 추가하면 case 문도 추가해야 한다.
- 새로운 상수를 추가하고 case 문을 빼먹으면 런타임시에 AssertionError 가 난다.

### 상수별 메서드 구현을 활용한 열거 타입

```java
public enum Operation{
    PLUS {public double apply(double x, double y){return x+y;}},
    MINUS {public double apply(double x, double y){return x-y;}},
    TIMES {public double apply(double x, double y){return x*y;}},
    DIVIDE {public double apply(double x, double y){return x/y;}};
    
    public abstract double apply(double x, double y);
}
```

- apply 메서드가 상수 선언 바로 옆에 붙어 있어 새로운 상수를 추가할 때 까먹을 일이 없다.
- apply가 추상 메서드이므로 재정의하지 않았다면 컴파일 오류로 알려준다.

### 상수별 클래스 몸체와 데이터를 사용한 열거 타입

```java
public enum Operation{
    PLUS("+") {
        public double apply(double x, double y){return x+y;}
    },
    MINUS("-") {
        public double apply(double x, double y){return x-y;}
    },
    TIMES("*") {
        public double apply(double x, double y){return x*y;}
    },
    DIVIDE("/") {
        public double apply(double x, double y){return x/y;}
    };
    private final String symbol;
    
    Operation(String symbol){
        this.symbol = symbol;
    }
    
    @Override
    public String toString(){
        return symbol;
    }

    public abstract double apply(double x, double y);
}

public static void main(String[] args) {
    double x = 2.0;
    double y = 4.0;
    for(Operation op : Operation.values()){
        System.out.printf("%f %s %f = %f%n",
                x,op,y.op.apply(x,y));
    }
}
```

- 열거 타입에는 상수 이름을 입력받아 그 이름에 해당하는 상수를 반환해주는 valueOf(String) 메서드가 자동 생성된다.
- 열거 타입의 toString메서드를 재정의하려거든, toString이 반환하는 문자열을 해당 열거 타입 상수로 변환해주는 fromString
메서드도 함께 제공하는 걸 고려하자.

### 상수별 메서드 구현에는 열거 타입 상수끼리 코드를 공유하기 어렵다는 단점이 있다.

```java
enum PayrollDay{
    MON, TUE, WED, THU, FRI, SAT, SUN;
    
    private static final int MINS_PER_SHIFT = 8 * 60;
    int pay(int minutesWorked, int payRate){
        int basePay = minutesWorked * payRate;
        
        int overtimePay;
        switch (this) {
            case SAT:
            case SUN:
                overtimePay = basePay / 2;
                break;
            default:
                overtimePay = minutesWorked <= MINS_PER_SHIFT ? 0 : (minutesWorked - MINS_PER_SHIFT) * payRate / 2;
        }
        return basePay + overtimePay;    
    }
}
```

- 간결하지만 관리 관점에서 위험한 코드다.
- 휴가와 같은 새로운 값을 추가하려면 case 문을 잊지 말고 넣어줘야 한다.
- 잔업수당을 계산하는 코드를 모든 상수에 중복해서 넣거나 코드를 평일용과 주말용으로 나눠 각각을 도우미 메서드로 작성한 다음
각 상수가 자신에게 필요한 메서드를 적절히 호출하도록 변경하면 상수별 메서드 구현으로 급여를 정확히 계산할 수 있지만,
코드가 장황해져 가독성이 크게 떨어지고 오류 발생 가능성이 높아진다.
- 평일용 overtimePay 메서드를 구현하고 주말에 overtimePay를 재정의해서 사용하더라도 switch 문을 썼을 때와 똑같은 단점이 나타난다.
- 가장 깔끔한 방법은 새로운 상수를 추가할 때 잔업수당 `전략`을 선택하도록 하는 것이다.

```java
enum PayrollDay{
    MON, TUE, WED, THU, FRI, SAT(PayType.WEEKEND), SUN(PayType.WEEKEND);
    
    private final PayType payType;
    
    PayrollDay(PayType payType){
        this.payType = payType;
    }
    
    int pay(int minutesWorked, int payRate){
        return payType.pay(minutesWorked, payRate);
    }
    
    enum PayType{
        WEEKDAY{
            int overtimePay(int minsWorked, int payRate){
                return minutesWorked <= MINS_PER_SHIFT ? 0 : (minutesWorked - MINS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND{
            int overtimePay(int minsWorked, int payRate){
                return minsWorked * payRate / 2;
            }
        };
        ...
    }
}
```
- PayrollDay 열거 타입은 잔업수당 계산을 그 전략 열거 타입에 위임한다.
- switch 문보다 복잡하지만 더 안전하고 유연하다.

### 열거 타입을 써야할 때?

- 필요한 원소를 컴파일타임에 다 알 수 있는 상수 집합이라면 항상 열거 타입을 사용하자.
- 태양계 행성, 한 주의 요일, 체스 말처럼 본질적으로 열거 타입인 타입은 당연히 포함된다.
- 메뉴 아이템, 연산 코드, 명령줄 플래그 등 허용하는 값 모두를 컴파일타임에 이미 알고 있을 때도 쓸 수 있다.

## 핵심 정리

- 열거 타임은 확실히 정수 상수보다 뛰어나다.
- 대다수 열거 타입이 명시적 생성자나 메서드 없이 쓰이지만, 각 상수를 특정 데이터와 연결짓거나 상수마다 다르게 동작하게 할 때는 필요하다.
- 하나의 메서드가 상수별로 다르게 동작해야 할 때는 switch 문 대신 상수별 메서드 구현을 사용하자.
- 열거 타입 상수 일부가 같은 동작을 공유한다면 전략 열거 타입 패턴을 사용하자.