# equals는 일반 규약을 지켜 재정의하라

- equals는 재정의하기 쉬워 보이지만 자칫하면 끔직한 결과를 초래한다.
- 문제를 회피하는 가장 쉬운 길은 아예 재정의하지 않는 것이다.
- 그냥 두면 그 클래스의 인스턴스는 오직 자기 자신과만 같게 된다.
- 다음에서 열거한 상황 중 하나에 해당한다면 재정의하지 않는 것이 최선이다.

## equals를 재정의 하지말아야하는 상황

1. 각 인스턴스가 본질적으로 고유하다.
    - 값을 표현하는 게 아니라 동작하는 개체를 표현하는 클래스가 여기 해당한다.
    - Thread가 좋은 예. Object의 equals 메서드는 이러한 클래스에 맞게 구현되었다.
2. 인스턴스의 '논리적 동치성(logical equality)'을 검사할 일이 없다.
    - Pattern은 equals를 재정의해서 두 Pattern의 인스턴스가 같은 정규표현식을 나타내는지 검사하는 방법이 있다.
3. 상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다.
    - Set 구현체는 AbstractSet이 구현한 equals를 상속받아 쓰고, List구현체들은 AbstractList, 
   Map 구현체들은 AbstractMap으로부터 상속받아 그래도 쓴다.
4. 클래스가 private이거나 package-private이고 equals 메서드를 호출할 일이 없다.
    - equals가 실수라도 호출되는 걸 막고 싶다면 다음과 같이 구현하자.
    ```java
    @Override
    public boolean equals(Object o){
        throw new AssertionError();
    }
    ```

- equals를 재정의해야 할 때는 객체 식별성이 아니라 논리적 동치성을 확인해야 하는데, 상위 클래스의 equals가
논리적 동치성을 비교하도록 재정의되지 않았을 때다.
- 주로 값 클래스들이다.
- 두 값 객체를 equals로 비교하는 프로그래머는 객체가 같은지가 아니라 값이 같은지를 알고 싶어 할 것이다.
- 값 클래스라 해도 값이 같은 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스라면 equals를 재정의하지 않아도 된다.
- Enum도 마찬가지이다.
- 논리적으로 같은 인스턴스가 2개 이상 만들어지지 않으니 논리적 동치성과 객체 식별성이 같은 의미다.

## equals 메서드를 재정의할 때의 규약

1. 반사성(reflexivity): null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true다.
2. 대칭성(symmertry): null이 아닌 모든 참조 값 x,y에 대해 x.equals(y)가 true면 y.equals(x)도 true다.
3. 추이성(transivtivity): null이 아닌 모든 참조 값 x,y,z에 대해 x.equals(y)가 true이고 y.equals(z)도 true이면 x.equals(z)도 true다.
4. 일관성(consistency): null이 아닌 모든 참조 값 x,y에 대해 x.equals(y)를 반복해서 호출하면 항상 값이 같아야한다.
5. null-아님: null이 아닌 모든 참조 값 x에 대해, x.equals(null)은 false다.

## 규약의 이해

- 반사성
  - 객체는 자기 자신과 같아야 한다는 뜻이다.
  - 이 요건을 어긴 클래스의 인스턴스를 컬렉션에 넣은 다음 contains 메서드를 호출하면 방금 넣은 인스턴스가 없다고 답할 것이다.
- 대칭성
  - 두 객체는 서로에 대한 동치 여부에 똑같이 답해야 한다는 뜻이다.
    ```java
    public final class CaseInsensitiveString {
        private final String s;
    
        public CaseInsensitiveString(String s) {
            this.s = Objects.requireNonNull(s);
        }
        
        @Override //대칭성 위반
        public boolean equals(Object o){
            if(o instanceof CaseInsensitiveString){
                return s.equalsIgnoreCase((CaseInsensitiveString) o).s);
            }
            if(o instanceof String){
                return s.equalsIgnoreCase((String) o);
            }
            return false;
        }
    }
    
    CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
    String s = "polish";
    
    cis.equals(s) == true;
    s.equals(cis) == false;
    ```
    - CaseInsensitiveString의 equals는 일반 String을 알고있지만 String의 equals는 CaseInsensitiveString의 존재를 모른다.
    - equals 규약을 어기면 그 객체를 사용하는 다른 객체들이 어떻게 반응할지 알 수 없다.
    - CaseInsensitiveString의 equals를 String과도 연동하겠다는 허황된 꿈을 버려야한다.
- 추이성
  - 첫 번째 객체와 두번째 객체가 같고, 두 번째 객체와 세 번째 객체가 같다면, 첫 번째 객체와 세 번째 객체도 같아야 한다는 뜻이다.
      ```java
      public class Point{
          private final int x;
          private final int y;
        
          public Point(int x, int y){
              this.x = x;
              this.y = y;
          }
        
          @Override
          public boolean equals(Object o){
              if(!(o instanceof Point)){
                  return false;
              }
              Point p = (Point) o;
              return p.x == x && p.y == y;
          }
      }
    
      public class ColorPoint extends Point{
          private final Color color;
        
          public ColorPoint(int x, int y, Color color){
              super(x,y);
              this.color=color;
          }
        
          @Override // 대칭성 위배
          public boolean equals(Object o){
              if(!(o instanceof ColorPoint)){
                  return false;
              }
              return super.equals(o) && ((ColorPoint) o).color == color;
          }
        
          @Override // 추이성 위배
          public boolean equals(Object o){
              if(!(o instanceof Point)){
                  return false;
              }
              if(!(o instanceof colorPoint)){
                  return o.equals(this);
              }
              return super.equals(o) && ((ColorPoint) o).color == color;
          }
        
          @Override // 리스코프 치환 원칙 위배
          public boolean equals(Object o){
              if (o==null || o.getClass() != getClass()){
                  return false;
              }
              Point p = (Point) o;
              return p.x == x && p.y == y;
          }
      }
      ```
      - 구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 객체 지향적 추상화의 이점을 포기하지 않는 한
      존재하지 않는다.
      - 상속 대신 컴포지션을 사용하면 Point를 상속하는 대신 Point를 ColorPoint의 private 필드로 두고, ColorPoint와 같은 위치의 일반 Point
      를 반환하는 뷰 메서드를 public으로 추가하면 된다.
      - 상위 클래스를 직접 인스턴스로 만드는 게 불가능(추상 클래스)라면 지금까지 이야기한 문제들은 일어나지 않는다.
- 일관성
  - 두 객체가 같다면 수정되지 않는 한 영원히 같아야 한다는 뜻이다.
  - 클래스가 불변이라면 equals가 한번 같다고 한 객체와는 영원히 같다고 답하고, 다르다고 한 객체와는 영원히 다르다고 답해야한다.
  - equals의 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안된다.
    - java.net.URL의 equals는 주어진 URL과 매핑된 호스트의 IP주소를 이용해 비교하는데 호스트 이름을 IP 주소로 바꾸려면 네트워크를 통해야 하는데
    그 결과가 항상 같다고 보장할 수 없다.
    - 잘못된 구현이다.
- null-아님
  - 모든 객체가 null과 같지 않아야 한다는 뜻이다.
  - 동치성을 검사하려면 equals는 건네받은 객체를 적절히 형변환 후 필수 필드들의 값을 알아내야 한다.
    ```java
    @Override // 명시적 null 검사
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
            ...
    }
    
    @Override // 묵시적 null 검사
    public boolean equals(Object o){
        if(!(o instanceof MyType)){
            return false;
        }
        MyType mt = (MyType) o;
            ...
    }
    ```
  - 묵시적 null 검사가 낫다.

## equals 메서드 구현 방법

1. == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.
    - 단순한 성능 최적화용으로, 비교 작업이 복잡한 상황일 때 값어치를 한다.
2. instanceof 연산자로 입력이 올바른 타입인지 확인한다.
    - 올바른 타입은 equals가 정의된 클래스인 것이 보통이지만 가끔은 그 클래스가 구현한 특정 인터페이스가 될 수도 있다.
    - Set, List, Map, Map.Entry 등의 컬렉션 인터페이스들이 여기 해당한다.
3. 입력을 올바른 타입으로 형변환한다.
   - 2번에서 instanceof 검사를 했기 때문에 100% 성공한다.
4. 입력 객체와 자기 자신의 대응되는 핵심 필드들이 모두 일치하는지 하나씩 검사한다.
   - 모든 필드가 일치하면 true를, 하나라도 다르면 false를 반환한다.
   - 2단계에서 인터페이스를 사용했다면 입력의 필드 값을 가져올 때도 그 인터페이스의 메서드를 사용해야 한다.

- 기본 타입 필드는 == 연산자로 비교하고, 참조 타입 필드는 각각의 equals 메서드로, float와 double 필드는 Float.compare, Double.compare로 비교한다.
- null도 정상 값으로 취급하는 참조 타입 필드도 있는데 이런 필드는 정적 메서드인 Objects.equals(Object, Object)로 비교해 NPE 발생을 예방하자.
- 아주 복잡한 필드를 가진 클래스는 필드의 표준형을 저장해둔 후 표준형끼리 비교하면 훨씬 경제적이다.
- 최상의 성능을 바란다면 다를 가능성이 더 크거나 비교하는 비용이 싼 필드를 먼저 비교하자.
- 동기화용 락(lock) 필드 같이 객체의 논리적 상태와 관련 없는 필드는 비교하면 안 된다.
- 핵심 필드로부터 계산해낼 수 있는 파생 필드도 비교할 필요는 없지만, 파생 필드가 객체 전체의 상태를 대표하는 상황에선
파생 필드를 비교하는 쪽이 더 빠를 때도 있다.

## equals를 구현헀다면 세 가지만 자문해보자.

- 대칭적인가?
- 추이성이 있는가?
- 일관적인가?
- 자문에서 끝내지 말고 단위 테스트를 작성하자.

## 주의사항

- equals를 재정의할 땐 hashCode도 반드시 재정의하자.
- 너무 복잡하게 해결하려 들지 말자.
  - 필드들의 동치성만 검사해도 equals 규약을 어렵지 않게 지킬 수 있다.
- Object 외의 타입을 매개변수로 받는 equals 메서드는 선언하지 말자.
  - Object외의 타입을 매개변수로 받으면 Override 가 아닌 Overload(다중 정의)가 된다.

## 핵심 정리

- 꼭 필요한 경우가 아니면 equals를 재정의하지 말자.
- 재정의해야 할 때는 그 클래스의 핵심 필드 모두를 빠짐없이 규약을 지켜가며 비교해야 한다.