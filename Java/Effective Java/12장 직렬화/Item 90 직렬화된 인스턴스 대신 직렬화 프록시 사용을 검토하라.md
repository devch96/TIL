# 직렬화된 인스턴스 대신 직렬화 프록시 사용을 검토하라

- Serializable을 구현하기로 결정한 순간 언어의 정상 매커니즘인 생성자 이외의 방법으로 인스턴스를 생성할 수 있게 되며 버그와 보안 문제가 일어날 가능성이 커진다.
- 하지만 이 위험을 크게 줄여줄 기법이 있는데 직렬화 프록시 패턴(serialization proxy pattern)이다.

### 직렬화 프록시 패턴

- 바깥 클래스의 논리적 상태를 정밀하게 표현하는 중첩 클래스를 설계해 private static 으로 선언한다. 이 중첩 클래스가 바로
바깥 클래스의 직렬화 프록시다.
- 중첩 클래스의 생성자는 단 하나여야 하며, 바깥 클래스를 매개변수로 받아야 한다.
- 이 생성자는 단순히 인수로 넘어온 인스턴스의 데이터를 복사한다.
- 바깥 클래스와 직렬화 프록시 모두 Serializable을 구현했다고 선언해야 한다.
```java
private static class SerializationProxy implements Serializable{
    private final Date start;
    private final Date end;
    
    SerilizationProxy(Period p){
        this.start = p.start;
        this.end = p.end;
    }
    private static final long serialVersionUID = 2L;
}
```

- 바깥 클래스에 다음의 writeReplace 메서드를 추가한다.
- 이 메서드는 자바의 직렬화 시스템이 바깥 클래스의 인스턴스 대신 SerializationProxy 인스턴스를 반환하게 하는 역할을 한다.
```java
private Object writeReplace(){
    return new SerializationProxy(this);
}
```

- 바깥 클래스와 논리적으로 동일한 인스턴스를 반환하는 readResolve 메서드를 SerializationProxy 클래스에 추가한다.
- 역직렬화 시에 직렬화 시스템이 직렬화 프록시를 다시 바깥 클래스의 인스턴스로 변환하게 해준다.
```java
private void readResolve(){
    return new Period(start, end);
}
```

### 직렬화 프록시 패턴의 한계

- 클라이언트가 멋대로 확장할 수 있는 클래스에는 적용할 수 없다.
- 객체 그래프에 순환이 있는 클래스에도 적용할 수 없다.

## 핵심 정리

- 제3자가 확장할 수 없는 클래스라면 가능한 한 직렬화 프록시 패턴을 사용하자.
- 이 패턴이 아마도 중요한 불변식을 안정적으로 직렬화해주는 가장 쉬운 방법일 것이다.