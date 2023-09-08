# clone 재정의는 주의해서 진행하라

- Cloneable은 복제해도 되는 클래스임을 명시하는 용도인 믹스인 인터페이스(mixin interface)지만,
의도한 목적을 제대로 이루지 못했다.
- 문제는 clone 메서드가 선언된 곳이 Cloneable이 아닌 Object이고, 그마저도 protected라는 데 있다.

## Cloneable 인터페이스

- 메서드 하나 없는 Cloneable 인터페이스는 Object의 protected 메서드인 clone의 동작 방식을 결정한다.
- Cloneable을 구현한 클래스의 인스턴스에서 clone을 호출하면 그 객체의 필드들을 하나하나 복사한 객체를 반환한다.
- 실무에서는 Cloneable을 구현한 클래스는 clone 메서드를 public으로 제공하며, 사용자는 당연히 복제가 제대로 이뤄지리라 기대한다.

## clone 메서드의 일반 규약

- x.clone() != x는 참이다.
- x.clone().getClass() == x.getClass() 도 참이지만 필수는 아니다.
- x.clone().equals(x) 도 일반적으로 참이지만 필수는 아니다.
- 관례상 이 메서드가 반환하는 객체는 super.clone을 호출해 얻어야 한다. Object를 제외한 모든 상위 클래스가 이 관례를 따른다면
x.clone().getClass() == x.getClass()는 참이다.

### 가변 상태를 참조하지 않는 클래스용 clone 메서드

```java
@Override
public PhoneNumber clone(){
    try{
        return (PhoneNumber) super.clone();
    }catch(CloneNotSupportedException e){
        throw new AssertionError();
    }
}
```

- 클래스에 정의도니 모든 필드는 원본 필드와 똑같은 값을 갖는다.
- 모든 필드가 기본 타입이거나 불변 객체를 참조한다면 이 객체는 완벽히 우리가 원하는 상태라 더 손볼 것이 없다.

### 가변 객체를 참조하면?

- 원본이랑 복제본 둘다 같은 객체를 참조하기 때문에 하나를 수정하면 다른 하나도 수정되어 불변식을 해친다.
- clone 메서드는 사실상 생성자와 같은 효과를 낸다.
- clone은 원본 객체에 아무런 해를 끼치지 않는 동시에 복제된 객체의 불변식을 보장해야 한다.

```java
@Override
public Stack clone(){
    try{
        Stack result = (Stack) super.clone();
        result.elements = elements.clone();
        return result;
    }
}
```
- clone을 재귀적으로 호출한다.
- elements 필드가 final이었다면 위의 방식은 작동하지 않는다.
- Cloneable 아키텍처는 `가변 객체를 참조하는 필드는 final로 선언하라`는 일반 용법과 충돌한다.
- 재귀적으로 호출해도 안되는 상황도 있다.
```java
public class HashTable implements Cloneable{
    private Entry[] buckets = ...;
    ...
    
    Entry deepCopy(){
        Entry result = new Entry(key, value, next);
        for(Entry p = result; p.next != null; p = p.next){
            p.next = new Entry(p.next.key, p.next.value, p.next.next);
        }
        return result;
    }
    
    @Override
    public HashTable clone(){
        try{
            HashTable result = (HashTable) super.clone();
            result.buckets = new Entry[buckets.length];
            for (int i = 0; i < buckets.length; i++) {
                if(buckets[i] != null){
                    result.buckets[i] = buckets[i].deepCopy();
                }
            }
            return result;
        }...
    }
}
```

### 하위 클래스에서 Cloneable을 지원하지 못하게 하는 clone 메서드
```java
@Override
protected final Object clone() throws CloneNotSupportedException{
    throw new CloneNotSupportedException();
}
```

- Cloneable을 구현한 스레드 안전 클래스를 작성할 때는 clone 메서드 역시 동기화해줘야 한다.
- Cloneable을 이미 구현한 클래스를 확장한다면 어쩔 수 없이 clone을 잘 작동하도록 구현해야하지만 그렇지 않은 상황에서는 복사 생성자와
복사 팩토리라는 더 나은 객체 복사 방식을 제공할 수 있다.

### 복사 생성자

```java
public Yum(Yum yum){
        ...
}
```

### 복사 팩토리

```java
public static Yum newInstance(){
        ...
}
```

- 복사 팩토리는 복사 생성자를 모방한 정적 팩토리다.

## 핵심 정리

- 새로운 인터페이스를 만들 때는 Cloneable을 확장해서는 안 되며, 새로운 클래스도 이를 구현해서는 안 된다.
- final 클래스라면 Cloneable을 구현해도 위험이 크지 않지만, 성능 최적화 관점에서 검토한 후 별다른 문제가 없을 때만 드물게 허용해야 한다.
- 복제 기능은 생성자와 팩토리를 이용하면 된다.
- 배열은 clone 메서드 방식이 좋다.

