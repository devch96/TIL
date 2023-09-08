# 이왕이면 제네릭 타입으로 만들라

- 제네릭 타입과 메서드를 사용하는 일은 일반적으로 쉬운 편이지만 제네릭 타입을 새로 만드는 일은 조금 더 어렵다.
- 배워두면 그만한 값어치는 한다.

### Object 기반 스택. 제네릭이 절실함.

```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        elements[size] = null;
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```
- 원래 제네릭 타입 이어야 마땅하다.

### 제네릭 스택으로 가는 첫 단계

- 클래스 선언에 타입 매개변수를 추가하는 일이다.
- 타입 이름으로는 보통 E를 사용한다.
```java
public class Stack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new E[DEFAULT_INITIAL_CAPACITY]; // 오류 발생
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        E result = elements[--size];
        elements[size] = null;
        return result;
    }
    ...
}
```

- E와 같은 실체화 불가 타입(제네릭)으로는 배열을 만들 수 없어서 컴파일 오류가 생긴다.
- 적절한 해결책은 두가지다.

### 제네릭 배열 생성을 금지하는 제약을 대놓고 우회하는 방법

```java
public Stack() {
    elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
}
```

- 컴파일러는 오류 대신 경고를 보낼 것이다. (unchecked case)
- 일반적으로 타입 안전하지 않다.
- 이 비검사 형변환이 프로그램의 타입 안정성을 해치지 않음을 스스로 확인해야 한다.
- elements 배열이 private 필드에 저장되고, 클라이언트로 반환되거나 다른 메서드에 전달되는 일이 전혀 없고
push 메서드를 통해 배열에 저장되는 원소의 타입은 항상 E이기 때문에 이 비검사 형변환은 항상 안전하다.
- 따라서 @SuppressWarnings 어노테이션을 달고 주석을 단다.
```java
// 배열 elements는 push(E)로 넘어온 E 인스턴스만 담는다.
// 타입 안정성을 보장하지만
// 이 배열의 런타임 타입은 E[] 가 아닌 Object[]다.
@SuppressWarnings("unchecked")
public Stack() {
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
}
```

### elements 필드의 타입을 E[]에서 Object[]로 바꾸는 것.

```java
public class Stack<E> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY]; 
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        E result = elements[--size]; // 오류 발생
        elements[size] = null;
        return result;
    }
    ...
}
```

- incompatible types 오류가 뜬다.
- 배열이 반환한 원소를 E로 형변환하면 unchecked cast 경고가 뜬다.
- 적절히 숨기자
```java
public E pop() {
    if (size == 0) {
        throw new EmptyStackException();
    }
    // push에서 E 타입만 허용하므로 이 형변환은 안전하다.
    @SuppressWarnings("unchecked")
    E result = (E) elements[--size]; // 오류 발생
    elements[size] = null;
    return result;
}
```

### 어떤 방법이 더 좋나?

- 둘 다 지지를 얻고 있다.
- 첫 번째 방법(제네릭 배열 생성을 금지하는 제약을 대놓고 우회하는 방법)
  - 가독성이 좋다.(E[] 로 선언하기에 E 인스턴스만 받는 배열이라는 것을 알림)
  - 형변환을 배열 생성 시 단 한번만 사용함.
  - 현업에서는 첫 번째 방식을 더 선호하며 자주 사용함.
  - 하지만 E가 Object가 아닌 한 배열의 런타임 타입이 컴파일타임과 달라 힙 오염을 일으킨다.
- 두 번째 방법(필드의 타입을 E[]에서 Object[]로 바꾸는 것)
  - 원소를 읽을 때마다 형 변환을 해줘야 함.
  - 힙 오염이 생기지 않는다.

### 제네릭 타입은 타입 매개변수에 아무런 제약을 두지 않는다.

- 하지만 기본 타입은 사용할 수 없다.
- 제네릭 타입 시스템의 근본적인 문제이나, 박싱된 기본 타입을 사용해 우회할 수 있다.

### 타입 매개변수에 제약을 두는 제네릭 타입도 있다.

- &lt;E extends Delayed> 와 같은 것은 Delayed 클래스의 하위 타입만 받는다는 뜻이다.
- 이러한 타입 매개변수 E를 한정적 타입 매개 변수라 한다.

## 핵심 정리

- 클라이언트에서 직접 형변환해야 하는 타입보다 제네릭 타입이 더 안전하고 쓰기 편하다.
- 새로운 타입을 설계할 때는 형변환 없이도 사용할 수 있도록 하라.
- 기존 타입 중 제네릭이었어야 하는 게 있다면 제네릭 타입으로 변경하자.
  