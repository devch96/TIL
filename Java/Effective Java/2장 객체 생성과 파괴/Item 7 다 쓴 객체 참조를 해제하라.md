# 다 쓴 객체 참조를 해제하라

- 자바처럼 가비지 컬렉터가 있는 언어라고 해서 메모리 관리에 더 이상 신경 쓰지 않아도 된다고 오해할 수 있는데 아니다.

### 메모리 누수가 일어나는 위치는 어디인가?

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
        return elements[--size];
    }
    private void ensureCapacity(){
        if(elements.length == size){
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

- 위 코드에서는 스택이 커졌다가 줄어들었을 때 스택에서 꺼내진 객체들을 가비지 컬렉터가 회수하지 않는다.
- 이 스택이 그 객체들의 다 쓴 참조(obsolete reference)를 여전히 가지고 있기 때문이다.
- 다 쓴 참조란 문자 그대로 앞으로 다시 쓰지 않을 참조를 뜻한다.
- 객체 참조 하나를 살려두면 가비지 컬렉터는 그 객체뿐 아니라 그 객체가 참조하는 모든 객체(또 그 객체들이 참조하는 모든 객체)를 회수해가지 못한다.

### 제대로 구현한 pop 메서드

```java
public Object pop(){
    if(size == 0){
        throw new EmptyStackException()
    }
    Object result = elements[--size];
    elements[size] = null;
    return result;
}
```

- 다 쓴 참조를 null 처리하면 이미 null 처리한 참조를 실수로 사용하려 하면 NPE를 던지면서 종료된다.
- 프로그램 오류는 가능한 한 조기에 발견하는 것이 좋다.

## 객체 참조를 null 처리하는 일은 예외적인 경우여야 한다.

- 모든 객체를 다 쓰자마자 일일이 null 처리할 필요도 없고 바람직하지도 않다.
- 다 쓴 참조를 해제하는 가장 좋은 방법은 그 참조를 담은 변수를 유효 범위 밖으로 밀어내는 것이다.

### 자기 메모리를 직접 관리하는 클래스라면 프로그래머는 항시 메모리 누수에 주의해야 한다.

- null 처리는 자기 메모리를 직접 관리하는 클래스에서 해야 한다.
- Stack은 객체 자체가 아니라 객체 참조를 담는 elements 배열로 저장소 풀을 만들어 원소들을 관리한다.
- 배열의 활성 영역에 속한 원소들이 사용되고 비활성 영역은 쓰이지 않는데 가비지 컬렉터가 이 사실을 알 길이 없는 것이 문제다.
- 가비지 컬렉터 입장에서는 활성 영역, 비활성 영역에서 참조하는 객체들은 똑같이 참조받고 있는 객체일 뿐이다.

### 캐시 역시 메모리 누수를 일으키는 주범이다.

- 객체 참조를 캐시에 넣고 그 객체를 다 쓴 뒤로도 한참을 놔두는 일이 있다.
- 캐시를 만들 때 보통은 캐시 엔트리의 유효 기간을 정확히 정의하기 어렵기 때문에 시간이 지날수록 엔트리의 가치를 떨어뜨리는 방식을 흔히 사용한다.

## 핵심 정리

- 메모리 누수는 겉으로 잘 드러나지 않아 시스템에 수년간 잠복하는 사례도 있다.
- 누수는 철저한 코드 리뷰나 힙 프로파일러 같은 디버깅 도구를 동원해야만 발견되기도 한다.
- 예방법을 익혀두는 것이 매우 중요하다.

## 정리

- 자기 자신의 메모리를 관리하는 클래스에서는 메모리 누수를 생각하면서 코드를 짜야한다.
