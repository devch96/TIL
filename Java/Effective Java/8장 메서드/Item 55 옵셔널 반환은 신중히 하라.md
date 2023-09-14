# 옵셔널 반환은 신중히 하라

- 자바 8 전에는 메서드가 특정 조건에서 값을 반환할 수 없을 때 취할 수 있는 선택지는 예외를 던지거나, 반환 타입이
객체 참조라면 null을 반환하는 것 두가지 밖에 없었다.
- 하지만 예외는 진짜 예외적인 상황에서만 사용해야 하며 예외를 생성할 때 스택 추적 전체를 캡처하므로 비용도 만만치 않다.
- null을 반환하면 이러한 문제는 없지만 별도로 null 처리 코드를 추가해야하는 불편함이 있었다.

### 새로운 선택지

- 자바 8로 올라가며 Optional이란 선택지가 생겼다.
- Optional&lt;T>는 null이 아닌 T 타입 참조를 하나 담거나 혹은 아무것도 담지 않을 수 있다.
- 옵셔널은 원소를 최대 1개를 가질 수 있는 불변 컬렉션이다.(Collection을 구현하진 않았음, 컨테이너가 맞는 표현일 듯)
- 보통은 T를 반환해야 하지만 특정 조건에서는 아무것도 반환하지 않아야 할 때 T 대신 Optioanl&lt;T>를 반환하도록 선언하면 된다.
- 옵셔널을 반환하는 메서드는 예외를 던지는 메서드보다 유연하고 사용하기 쉬우며 null을 반환하는 메서드보다 오류 가능성이 작다.
```java
public static <E extends Comparable<E>> Optional<E> max(Collection<E> c){
    if(c.isEmpty()){
        return Optional.empty();
    }
    E result = ...;
        ...
    return Optional.of(result);    
}
```
- 빈 옵셔널은 Optional.empty()로 만들고 값이 든 옵셔널은 Optional.of(value)로 만든다.
- value에 null을 넣으면 NPE를 던진다.
- null 값도 허용하는 옵셔널을 만들려면 Optional.ofNullable(value)를 사용하자.

### null 혹은 예외를 던지는 대신 옵셔널 반환을 선택해야 하는 기준은?

- 옵셔널은 검사 예외와 취지가 비슷하다.
- 반환 값이 없을 수도 있음을 API 사용자에게 명확히 알려준다.
- 비검사 예외를 던지거나 null을 반환한다면 API 사용자가 인지하지 못해 오류가 커질 수 있지만 검사 예외를 던지면 사용자는 반드시
이에 대처하는 코드를 작성해야 하는 것과 비슷하다.

### 메서드가 옵셔널을 반환할 때 클라이언트가 취할 행동

1. 기본값을 정해둘 수 있다
    ```java
    String lastWordInLexicon = max(words).orElse("단어 없음");
    ```
   
2. 원하는 예외를 던질 수 있다.
    ```java
    Toy myToy = max(toys).orElseThrow(TemperTantrumException::new)
    ```
   - 실제 예외가 아니라 예외 팩터리에 건네니 예외가 실제로 발생하지 않는 한 예외 생성 비용은 들지 않는다.

3. 항상 값이 채워져 있다고 가정한다.
    ```java
    Element lastNobelGas = max(Elements.NOBLE_GASES).get();
    ```
   - 옵셔널이 항상 값이 있다고 확신하면 곧바로 꺼내서 사용할 수 있다.
   - 다만 잘못 판단한 것이라면 NoSuchElementException이 발생한다.

### isPresent

- 옵셔널이 채워져 있으면 true를, 비어 있으면 false를 반환한다.
- 하지만 신중히 사용해야 한다.
- isPresent를 쓴 코드 중 상당수는 앞서 언급한 메서드들로 대체할 수 있으며 그렇게 하면 더 짧고 명확하고 용법에 맞는 코드가 된다.
```java
Optional<ProcessHandle> parentProcess = ph.parent();
System.out.println("부모 PID: " + (parentProcess.isPresent() ? String.valueOf(parentProcess.get().pid()) : "N/A"));

System.out.println("부모 PID:" + ph.parent().map(h -> String.valueOf(h.pid())).orElse("N/A"));
```

### 스트림을 사용하면 Optional을 사용하는 경우가 드물지 않다.
```java
streamOfOptionals
        .filter(Optional::isPresent)
        .map(Optional::get)
        ...
```

### Optional.stream()

- 자바 9에서는 Optional에 stream() 메서드가 추가되었다.
- 이 메서드는 Optional을 Stream으로 변환해주는 어댑터다.
- 옵셔널에 값이 있으면 그 값을 원소로 담은 스트림으로, 없다면 빈 스트림으로 변환한다.

### 옵셔널을 사용한다고 해서 무조건 득이 되는 건 아니다.

- 컬렉션, 스트림, 배열, 옵셔널 같은 컨테이너 타입은 옵셔널로 감싸지 말고 빈 컨테이너를 반환해야 한다.
- 빈 컨테이너를 반환하면 클라이언트에 옵셔널 처리 코드를 넣지 않아도 되기 때문이다.

### 어떤 경우 메서드 반환 타입을 T 대신 Optional&lt;T>로 선언해야 할까?

- 결과가 없을 수 있으며, 클라이언트가 이 상황을 특별하게 처리해야 한다면 Optional을 반환한다.
- Optinal도 새로 할당하고 초기화해야 하는 객체이고, 그 안에서 값을 꺼내려면 메서드를 호출해야 하니 성능이 중요한 상황에서는
옵셔널이 맞지 않을 수 있다.

### 박싱된 기본 타입 옵셔널

- 박싱된 기본 타입을 담는 옵셔널은 기본 타입 자체보다 무겁다.
- 박싱으로 한번 감싸고 옵셔널로 또 한번 감싸기 때문이다.
- 따라서 기본 타입 전용 옵셔널들이 존재한다.
- OptionalInt, OptionalLong, OptionalDouble이다.
- 대체재 까지 있으니 박싱된 기본 타입을 담은 옵셔널을 반환하지 말자.

### 옵셔널을 컬렉션의 키, 값, 원소나 배열의 원소로 사용하는 게 적절한 상황은 거의 없다.

### 옵셔널을 인스턴스 필드에 저장해두는 게 필요할 때는??

- 필수 필드를 갖는 클래스와, 이를 확장해 선택적 필드를 추가한 하위 클래스를 따로 만들어야 함을 암시하는 나쁜냄새다.
- 하지만 가끔은 적절한 상황도 있다.
  - 인스턴스 필드 중 상당수는 필수가 아니고, 필드들이 기본 타입이라 값이 없음을 나타낼 방법이 마땅치 않을 때.
  - 이럴 때는 필드 자체를 옵셔널로 선언하는 것도 좋은 방법

## 핵심 정리

- 값을 반환하지 못할 가능성이 있고, 호출할 때마다 반환값이 없을 가능성을 염두에 둬야 하는 메서드라면 옵셔널을
반환해야 할 상황일 수 있다.
- 옵셔널 반환에는 성능 저하가 뒤따르니 성능에 민감하다면 null 혹은 예외를 던지는 편이 나을 수도 있다.
- 옵셔널을 반환값 이외의 용도로 쓰는 경우는 매우 드물다.