# public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라

- 데이터 필드에 직접 접근할 수 있는건 캡슐화의 이점을 제공하지 못하는 것이다.
- API를 수정하지 않고는 내부 표현을 바꿀 수 없고, 불변식을 보장할 수 없으며, 외부에서 필드에 접근할 때
부수 작업을 수행할 수도 없다.
- 필드들을 모두 private으로 바꾸고 public 접근자(getter)를 추가한다.

### 접근자와 변경자(mutator) 메서드를 활용해 데이터를 캡슐화한다.
```java
class Point{
    private double x;
    private double y;
    
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
}
```
- 패키지 바깥에서 접근할 수 있는 클래스(public class)라면 접근자를 제공함으로써 클래스 내부 표현 방식을
언제든 바꿀 수 있는 유연성을 얻을 수 있다.
- 하지만 package-private 클래스 혹은 private 중첩 클래스라면 데이터 필드를 노출한다 해도 문제 없다.
- 클래스가 표현하려는 추상 개념만 올바르게 표현해주면 된다.
- 오히려 클래스 선언 면이나 클라이언트 코드 면에서 접근자 방식보다 깔끔하다.
- 클라이언트 코드가 클래스 내부 표현에 묶이기는 하나 어차피 이 클래스를 포함하는 패키지 안에서만 동작하는 코드일 뿐이다.

### 불변 필드를 노출한 public 클래스

```java
public final class Time{
    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;
    
    public final int hour;
    public final int minute;
    ...
}
```
- public 클래스의 필드가 불변이라면 직접 노출할 때의 단점이 조금 줄어들지만 여전히 지양해야 한다.
- API를 변경하지 않고는 표현 방식을 바꿀 수 없고, 필드를 읽을 때 부수 작업을 수행할 수 없다는 단점은 여전하다.
```java
public class Client{
    Time.hour 
            ...
    Time.minute
            ...
}
```
- 이런 상황에서 Time의 hour이나 minute을 int가 아닌 다른 방식(표현 방식)으로 바꾸지 못함.
- Client가 사용하고 있는 API를 변경해야 함.

## 핵심 정리

- public 클래스는 절대 가변 필드를 직접 노출해서는 안 된다.
- 불변 필드라면 노출해도 덜 위험하지만 지양해야 한다.
- package-private 클래스나 private 중첩 클래스에서는 종종 필드를 노출하는 편이 나을 때도 있다.