# 로 타입은 사용하지 말라

- 클래스와 인터페이스 선언에 타입 매개변수가 쓰이면 이를 제네릭 클래스 혹은 제네릭 인터페이스라 한다.
- List 인터페이스는 원소의 타입을 나타내는 타입 매개변수 E를 받는다. List<E>
- 제네릭 클래스와 제네릭 인터페이스를 통틀어 제네릭 타입이라 한다.

### 제네릭 타입은 일련의 매개변수화 타입을 정의한다.

- List<String>은 원소 타입이 String인 리스트를 뜻하는 매개변수화 타입이다.
- 여기서 String이 정규(formal) 타입 매개변수 E에 해당하는 실제 타입 매개변수이다.
- 제네릭 타입을 하나 정의하면 그에 딸린 로 타입(raw type)도 함께 정의한다.
- 로 타입이란 제네릭 타입에서 타입 매개변수를 전혀 사용하지 않을 때를 말한다.

### 제네릭을 지원하기 전의 컬렉션

```java
// Stamp 인스턴스만 취급
private final Collection stamps = ...;

stamps.add(new Coin(...))
```
- 로 타입 컬렉션에서는 다른 인스턴스를 넣어도 오류가 발생하지 않는다.(경고 메시지만 나옴)
- 꺼낼때(런타임시) 오류를 발생한다.(꺼낼 때 형변환을 하기 때문에 ClassCastException)
- 주석은 컴파일러가 이해하지 못하니 의미가 없다.

### 제네릭을 지원한 후의 컬렉션

```java
private final Collection<Stamp> stamps = ...;
```

- 제네릭을 사용하면 컴파일러가 stamps 컬렉션에 Stamp 인스턴스만 넣어야하는걸 알아차린다.
- 따라서 다른 인스턴스를 넣을 때 incompatible type 에러가 난다.
- 오류는 가능한 한 발생 즉시, 이상적으로는 컴파일할 때 발견하는 것이 좋다.
- 런타임 오류는 런타임에 문제를 겪는 코드와 원인을 제공한 코드가 물리적으로 상당히 떨어져있을 가능성이 크다.

### 로 타입을 쓰면 제네릭이 안겨주는 안전성과 표현력을 모두 잃게 된다.

- 자바는 제네릭이 있지만 로 타입을 쓰는걸 막아두지는 않았다.
- 왜냐하면 자바가 제네릭을 받아들이기까지 거의 10년이 걸린 탓에 제네릭 없이 짠 코드가 많아서 
호환성 때문에 막질 못한 것이다.

### List 같은 로 타입은 안되나 List< Object>처럼 임의 객체를 허용하는 타입은 괜찮다.

- List는 제네릭 타입에서 완전히 발을 뺀 것이고 List&lt;Object>는 모든 타입을 허용한다는 의미를 컴파일러에 전달한 것이기 때문이다.
- 매개변수로 List를 받는 메서드에 List&lt;String>을 넘길 수 있지만, List&lt;Object>를 받는 메서드에는 넘길 수 없다.
- 제네릭의 하위 타입 규칙 때문이다.
- List&lt;String>은 로 타입인 List의 하위 타입이지만, List&lt;Object>의 하위 타입은 아니다.
- List&lt;Object>같은 매개변수화 타입을 사용할 때와 달리 list 같은 로 타입을 사용하면 타입 안전성을 잃게 된다.

```java
public static void main(String[] args){
    List<String> strings = new ArraysList<>();
    unsafeAdd(String, Integer.valueOf(42));
    String s = string.get(0);
}

private static void unsafeAdd(List list, Object o){
    list.add(0);
}
```
- 위 코드의 경우 컴파일에서 오류가 나지 않는다.(경고는 한다)
- get(0)에서 ClassCastException이 일어난다.

### 제네릭 타입을 쓰고 싶지만 실제 타입 매개변수가 무엇인지 신경 쓰고 싶지 않다면

- 물음표(?)를 사용하자.
- 제네릭 타입인 Set< E>의 비한정적 와일드카드 타입은 Set<?> 다.

### 로 타입 써도 되는 곳

- 클래스 리터럴에는 로 타입을 써야 한다.(제네릭이 없다)
  - List.class, String[].class, int.class 는 허용하고 List< String>.class와 List&lt;?>는 허용하지 않는다.
- 런타임에는 제네릭 타입 정보가 지워지므로 instanceof 연산자는 비한정적 와일드카드 타입 이외의 매개변수화 타입에는 적용할 수 없다.

## 핵심 정리

- 로 타입을 사용하면 런타임에 예외가 일어날 수 있으니 사용하면 안된다.

