# 상속보다는 컴포지션을 사용하라

- 상속은 코드를 재사용하는 강력한 수단이지만 항상 최선은 아니다.
- 상위 클래스와 하위 클래스를 모두 같은 프로그래머가 통제하는 패키지 안에서라면 상속도 안전한 방법이다.
- 확장할 목적으로 설계되었고 문서화도 잘 된 클래스도 마찬가지로 안전하다.
- 하지만 다른 패키지의 구체 클래스를 상속하는 일은 위험하다.

### 메서드 호출과 달리 상속은 캡슐화를 깨드린다.

- 달리 말하면 상위 클래스가 어떻게 구현되느냐에 따라 하위 클래스의 동작에 이상이 생길 수 있다.
- 상위 클래스 설계자가 확장을 충분히 고려하고 문서화도 제대로 해두지 않으면 하위 클래스는 상위 클래스의
변화에 발맞춰 수정돼야만 한다.

### 상속을 잘못 사용한 예

```java
public class InstrumentedHashSet<E> extends HashSet<E>{
    private int addCount = 0;
    ...
    @Override
    public boolean add(E e){
        addCount++;
        return super.add(e);
    }
    
    @Override
    public boolean addAll(Collections<? extends E> c){
        return super.addAll(c);
    }
    
    public int getAddCount(){
        return addCount;
    }
}
```
- 여기서 addAll메서드로 3개의 인자를 넣을 경우 getAddCount의 예상 값은 3이여야 한다.
- 하지만 HashSet의 addAll 메서드는 HashSet.add를 호출하고, 그 add는 재정의된 add 메서드를 호출하기에 addCount 값이 6이된다.
- 이러한 방식은 상위 클래스의 구현 방식에 따라가야 하는 것이다.

### 기존 클래스를 확장하는 대신, 새로운 클래스를 만들고 private 필드로 기존 클래스의 인스턴스를 참조하게 하자.

- 기존 클래스가 새로운 클래스의 구성요소로 쓰인다는 뜻에서 이러한 설계를 컴포지션이라 한다.
- 새 클래스의 인스턴스 메서드들은 기존 클래스에 대응하는 메서드를 호출해 그 결과를 반환한다.
- 이 방식을 전달이라 하며, 새 클래스이 메서드들은 전달 메서드라 부른다.
- 그 결과 새로운 클래스는 기존 클래스의 내부 구현 방식의 영향에서 벗어난다.
```java
public class ForwardingSet<E> implements Set<E>{
    private final Set<E> s;
    public ForwardingSet(Set<E> s){
        this.s = s;
    }
    ...
    public boolean addAll(Collections<? extends E> c){
        return s.addAll(c);
    }
}

public class InstrumentedSet<E> extends ForwardingSet<e>{
    private int addCount = 0;
            ...
    @Override
    public boolean add(E e){
            addCount++;
            return super.add(e);
            }
    
    @Override
    public boolean addAll(Collections<? extends E> c){
            return super.addAll(c);
            }
    
    public int getAddCount(){
            return addCount;
            }
}
```

- 다른 Set 인스턴스를 감싸고 있다는 뜻에서 InstrumentedSet 같은 클래스를 래퍼 클래스라 하며, 다른 Set에 계측 기능을
덧씌운다는 뜻에서 데코레이터 패턴이라고 한다.

### 래퍼 클래스는 단점이 거의 없다.

- 한 가지, 래퍼 클래스가 콜백(callback) 프레임워크와는 어울리지 않는다는 점만 주의하면 된다.
- 콜백 프레임워크에서는 자기 자신의 참조를 다른 객체에 남겨서 다음 호출 때 사용하도록 한다.
- 내부 객체는 자신을 감싸고 있는 래퍼의 존재를 모르니 대신 자신(this)의 참조를 넘기고 콜백 때는 래퍼가 아닌 내부 객체를 호출하게 된다.
- 이를 SELF 문제라 한다.

### 상속은 반드시 하위 클래스가 상위 클래스의 `진짜` 하위 타입인 상황에서만 쓰여야 한다.

- 클래스 B가 클래스 A와 is-a 관계일 때만 클래스 A를 상속해야 한다.
- 클래스 A를 상속하는 클래스 B를 작성하려 한다면 "B가 정말 A인가?" 라고 자문해보자.
- "그렇다"라는 확답이 안나오면 B는 A를 상속해서는 안된다.
- "아니다"라면 A를 private 인스턴스로 두고 A와는 다른 API를 제공해야 한다. 즉 A는 B의 필수 구성요소가 아니라 구현 방법 중 하나일 뿐이다.

## 핵심 정리

- 상속은 강력하지만 캡슐화를 해친다는 문제가 있다.
- 상속은 상위 클래스와 하위 클래스가 순수한 is-a 관계이고, 같은 패키지이면서 상위 클래스가 확장을 고려해 설계
되었을 때 써야한다.
- 상속의 취약점을 피하려면 상속 대신 컴포지션과 전달을 사용하자.