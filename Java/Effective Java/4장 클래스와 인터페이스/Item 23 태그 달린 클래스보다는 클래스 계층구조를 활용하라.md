# 태그 달린 클래스보다는 클래스 계층구조를 활용하라

- 두 가지 의미를 표현할 수 있으며, 그중 현재 표현하는 의미를 태그 값으로 알려주는 클래스

```java
class Figure {
    enum Shape {RECTANGEL, CIRCLE}

    ;

    final Shape shape;

    double length;
    double width;

    double radius;

    Figure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    Figure(double length, double width) {
        shape = Shape.RECTANGEL;
        this.length = length;
        this.width = width;
    }
    
    double area(){
        switch(shape){
            case RECTANGEL:
                return length*length;
            case CIRCLE:
                return Math.PI * (radius * radius);
            default:
                throw new AssertionError(shape);
        }
    }
    
    ...
}
```

- 태그 달린 클래스는 단점이 많다.
  - 열거 타입 선언, 태그 필드, 조건문 등 쓸데없는 코드가 많다.
  - 구현이 한 클래스에 혼합돼 있어서 가독성이 나쁘다.
  - 다른 의미를 위한 코드도 언제나 함께 하니 메모리도 많이 사용한다.
  - 생성자가 태그 필드를 설정하고 해당 의미에 쓰이는 데이터 필드들을 초기화하는데 컴파일러가 도와주지 않는다. 껏해야 런타임에서 문제가 드러난다.
  - 다른 의미를 추가하려면 코드를 수정해야 한다.(모든 조건문 포함)
  - 인스턴스의 타입만으로는 현재 나타내는 의미를 알 수 없다.

### 태그 달린 클래스는 클래스 계층구조를 어설프게 흉내낸 아류일 뿐이다.

- 계층구조의 루트가 될 추상 클래스를 정의하고, 태그 값에 따라 동작이 달라지는 메서드들을 루트 클래스의 추상 메서드로 선언해야 한다.
```java
abstract class Figure{
    abstract double area();
}

class Circle extends Figure{
    final double radius;
    ...
}

class Rectangle extends Figure{
    ...
}
```
- 각 의미를 독립된 클래스에 담아 관련 없는 데이터 필드를 제거해 메모리 사용률이 올라간다.
- 각 클래스의 생성자가 모든 필드를 남김없이 초기화하고 추상 메서드를 구현했는지 컴파일러가 확인해준다.
- 실수로 빼먹은 case 문 때문에 런타임 오류가 발생할 일도 없다.
- 새로운 타입이 생기면 단순이 구현만 하면 된다.
- 타입이 의미별로 따로 존재하니 변수의 의미를 명시하거나 제한할 수 있고, 특정 의미만 매개변수로 받을 수 있다.

## 핵심 정리

- 태그 달린 클래스를 써야 하는 상황은 거의 없다.
- 새로운 클래스를 작성하는 데 태그 필드가 등장한다면 태그를 없애고 계층구조로 대체하는 방법을 생각해보자.
- 기본 클래스가 태그 필드를 사용하고 있다면 계층구조로 리팩터링하는 걸 고려하자.