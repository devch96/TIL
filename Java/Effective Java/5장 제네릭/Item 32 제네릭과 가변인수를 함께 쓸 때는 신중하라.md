# 제네릭과 가변인수를 함께 쓸 때는 신중하라

- 가변인수(varargs)메서드와 제네릭은 잘 어우러지지 않는다.
- 가변인수는 메서드에 넘기는 인수의 개수를 클라이언트가 조절할 수 있게 해주는데, 구현 방식에 허점이 있다.
- 가변인수 메서드를 호출하면 가변인수를 담기 위한 배열이 자동으로 하나 만들어진다.
- 그런데 내부로 감췄어야 할 이 배열을 노출하는 문제가 생겨 varargs 매개변수에 제네릭이나 매개변수화 타입이 포함되면
컴파일 경고가 발생한다.

### 제네릭과 varargs를 혼용하면 타입 안정성이 깨진다

```java
static void dangerous(List<String>... stringLists){
    List<Integer> intList = List.of(42);
    Object[] objects = stringLists;
    objects[0] = intList; // 힙 오염 발생
    String s = stringLists[0].get(0); // ClassCastException
}
```

- 타입 안정성이 깨지니 제네렉 varargs 배열 매개변수에 값을 저장하는 것은 안전하지 않다.

### 제네릭 배열은 허용하지 않으면서 제네릭 varargs 매개변수를 받는 메서드 선언이 가능한 이유는?

- 제네릭이나 매개변수화 타입의 varargs 매개변수를 받는 메서드가 실무에서 매우 유용하기 때문.
- Arrays.asList(T... a), Collections.addAll(Collections<? super T> c,T... elements) ..
- 자바 7 전에는 제네릭 가변인수 메서드의 작성자가 호출자 쪽에서 발생하는 경고에 대해서 해줄 수 있는 일이 없었다.
- 경고를 그냥 두거나 @SuppressWarning("unchecked")어노테이션을 달아 경고를 숨겨야 했다.
- 자바 7에서는 @SafeVarargs 어노테이션이 추가되어 제네릭 가변인수 메서드 작성자가 클라이언트 측에서 발생하는 경고를
숨길 수 있게 되었다.
- @SafeVarargs는 메서드 작성자가 그 메서드가 타입 안전함을 보장하는 장치이다.

### 메서드가 안전한지는 어떻게 확신할까?

- 가변인수 메서드를 호출할 때 varargs 매개변수를 담는 제네릭 배열이 만들어진다.
- 메서드가 이 배열에 아무것도 저장하지 않고, 그 배열의 참조가 밖으로 노출되지 않는다면 타입 안전하다.
- varargs 매개변수 배열이 호출자로부터 그 메서드로 순수하게 인수들을 전달하는 일만 한다면 안전하다.

### 제네릭 varargs 매개변수 배열에 다른 메서드가 접근하도록 허용하면 안전하지 않다.

```java
static <T> T[] toArray(T... args){
    return args;
}

static <T> T[] pickTwo(T a, T b, T c){
    switch(ThreawdLocalRandom.current().nextInt(3)){
        case 0: return toArray(a,b);
        case 1: return toArray(a,c);
        case 2: return toArray(b,c);
    }
    throw new AssertionError();
}

public static void main(String[]args){
        String[] attributes = pickTwo("좋은","빠른","저렴한");
}
```

- 위 코드는 별다른 경고 없이 컴파일되지만, 실행하려 들면 ClassCastException을 던진다.
- toArray에 넘길 T 인스턴스 2개를 담은 varargs 매개변수 배열을 만드는데 이 코드가 만드는 배열의 타입은 Object[]이다.
- toArray 메서드가 돌려준 배열을 그대로 pickTwo를 호출한 클라이언트까지 전달되기에 pickTwo는 항상 Object[] 타입 배열을 반환한다.
- 하지만 main 메서드에서 String[]으로 형변환하는 코드를 컴파일러가 자동 생성하는데 Object[]는 String[]의 하위 타입이 아니므로
형변환에 실패한다.
- varargs 매개변수 배열을 이미 안전하다고 선언(@SafeVarargs)되있는 메서드에 넘기거나, 호출만 하는 일반 메서드에 넘기는 것은 안전하다.

### 제네릭이나 매개변수화 타입의 varargs 매개변수를 받는 모든 메서드에 @SafeVarargs를 달라.

- 안전하지 않은 varargs 메서드는 절대 작성해서는 안 된다.
- 힙 오염 경고가 뜨는 메서드가 있다면 그 메서드가 진짜 안전한지 점검하고 어노테이션을 달자.
- varargs 매개변수 배열에 아무것도 저장하지 않는다.
- 그 배열(혹은 복제본)을 신뢰할 수 없는 코드에 노출하지 않는다.
- 위 둘 중 하나라도 어겼다면 수정해야 한다.

### 어노테이션이 유일한 정답은 아니다.

- varargs 매개변수를 List 매개변수로 바꿀 수도 있다.
```java
static <T> List<T> pickTwo(T a, T b, T c){
    switch(ThreawdLocalRandom.current().nextInt(3)){
        case 0: return List.of(a,b);
        case 1: return List.of(b,c);
        case 2: return List.of(a,c);
    }
    throw new AssertionError();
}

public static void main(String[]args){
    List<String> attributes = pickTwo("좋은","빠른","저렴한");
}
```


## 핵심 정리

- 가변인수와 제네릭은 궁합이 좋지 않다.
- 가변인수 기능은 배열을 노출하여 추상화가 완벽하지 못하고, 배열과 제네릭의 타입 규칙이 서로 다르기 때문이다.
- 제네릭 varargs 매개변수는 타입 안전하지 않지만, 허용된다.
- 메서드에 제네릭(혹은 매개변수화된)varargs 매개변수를 사용하고자 한다면, 먼저 그 메서드가 타입 안전한지 확인한
다음 @SafeVarargs 어노테이션을 달자.