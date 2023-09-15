# 지연 초기화는 신중히 사용하라

- 지연 초기화(lazy initialization)는 필드의 초기화 시점을 그 값이 처음 필요할때까지 늦추는 기법이다.
- 값이 전혀 쓰이지 않으면 초기화도 결코 일어나지 않는다.
- 지연 초기화는 주로 최적화 용도로 쓰이지만, 클래스와 인스턴스 초기화 때 발생하는 위험한 순환 문제를 해결하는 효과도 있다.

### 필요할 때까지는 하지 말라

- 지연 초기화는 양날의 검이다.
- 클래스 혹은 인스턴스 생성 시의 초기화 비용은 줄지만 그 대신 지연 초기화하는 필드에 접근하는 비용은 커진다.
- 실제 초기화에 드는 비용에 따라 초기화된 각 필드를 얼마나 빈번히 호출하느냐에 따라 지연 초기화가 실제로는 성능을 느려지게 할 수도 있다.
- 멀티스레드 환경에서는 지연 초기화를 하기가 까다롭다.

### 지연 초기화가 필요한 경우

- 해당 클래스의 인스턴스 중 그 필드를 사용하는 인스턴스의 비율이 낮은 반면, 그 필드를 초기화하는 비용이 크다면 지연 초기화가 제 역할을 해 줄 것이다.
- 하지만 지연 초기화 적용 전후의 성능을 측정해봐야 한다.(최적화 기법이기 때문)

### 대부분의 상황에서 일반적인 초기화가 지연 초기화보다 낫다.

### 지연 초기화가 초기화 순환성을 깨뜨릴 것 같으면 synchronized를 단 접근자를 사용하자.

```java
private FiledType filed;

private synchronized FieldType getFiled(){
    if(filed == null){
        field = computeFeieldValue();
    }
    return field;
}
```
- 정적 필드에도 똑같지 적용된다(필드와 접근자 메서드 선언에 static 한정자를 추가해야 한다.)

### 성능 때문에 정적 필드를 지연 초기화해야 한다면 지연 초기화 홀더 클래스 관용구를 사용하자.

```java
private static class FieldHolder{
    static final FieldType field = computeFieldValue();
}
private static FieldType getField(){
    return FieldHolder.field;
}
```
- 클래스는 클래스가 처음 쓰일 때 비로소 초기화된다는 특성을 이용한 관용구다.
- getField 메서드가 필드에 접근하면서 동기화를 전혀 하지 않으니 성능이 느려질 거리가 전혀 없다.

### 성능 때문에 인스턴스 필드를 지연 초기화해야 한다면 이중검사 관용구를 사용하라.

```java
private volatile FieldType field;

private FieldType getField(){
    FieldType result = field;
    if(result != null){ // 첫 번째 검사 (락 사용 안함)
        return result;
    }
    synchronized(this){
        if(field==null){
            field = computeFieldValue(); // 두 번째 검사(락 사용)
        }
        return field;
    }
}
```

- 필드의 값을 두 번 검사하는 방식으로, 한 번은 동기화 없이 검사하고, 두 번째는 동기화하여 검사한다.
- 필드가 초기화된 후로는 동기화하지 않으므로 해당 필드는 반드시 volatile로 선언해야 한다.

## 핵심 정리

- 대부분의 필드는 지연시키지 말고 곧바로 초기화해야 한다.
- 성능 때문에 혹은 위험한 초기화 순한을 막기 위해 꼭 지연 초기화를 써야 한다면 올바른 지연 초기화 기법을 사용하자.
- 인스턴스 필드에는 이중검사 관용구를, 정적 필드에는 지연 초기화 홀더 클래스 관용구를 사용하자.
