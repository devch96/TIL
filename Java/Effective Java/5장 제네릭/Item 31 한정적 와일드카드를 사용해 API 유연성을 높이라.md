# 한정적 와일드카드를 사용해 API 유연성을 높이라

- 매개변수화 타입은 불공변(invariant)이다.
- List&lt;String>은 List&lt;Object>의 하위 타입이 아니다.(리스코프 치환 원칙에 어긋난다)

```java
public void pushAll(Iterable<E> src){
    for (E e : src){
        push(e);
    }
}
```
- 위 코드는 컴파일에 문제가 없지만 완벽하지 않다.
- Stack< Number>에 Integer을 넣는다면, Integer은 Number의 하위 타입이기 때문에 문제가 없을 것 같지만,
매개변수화 타입이 불공변이기 때문에 incompatible types 오류가 뜬다.
- 한정적 와일드카드라는 특별한 매개변수화 타입을 사용해 오류를 막아야 한다.

### E 생산자(producer) 매개변수에 와일드카드 타입 적용

```java
public void pushAll(Iterable<? extends E> src){
    for (E e : src){
        push(e);
    }
}
```
- `E의 Iterable`이 아니라 `E의 하위 타입의 Iterable`이라는 뜻이다.

```java
public void popAll(Collection<E> dst){
    while(!isEmpty()){
        dst.add(pop());
    }
}
```

- 컬렉션의 원소 타입이 스택의 원소 타입과 일치한다면 컴파일에 문제없다.
- Stack< Number>의 원소를 Object용 컬렉션으로 옮기려고하면 incompatible types 오류가 뜬다.

### E 소비자(consumer) 매개변수에 와일드카드 타입 적용

```java
public void popAll(Collections<? super E> dst){
    while(!isEmpty()){
        dst.add(pop());
    }
}
```
- `E의 Collection`이 아니라 `E의 상위 타입의 Collection`이라는 뜻이다.
- 유연성을 극대화하려면 원소의 생산자나 소비자용 입력 매개변수에 와일드카드 타입을 사용하라.
- 입력 매개변수가 생산자와 소비자 역할을 동시에 한다면 와일드카드 타입을 써도 좋을 게 없다.
- 타입을 정확히 지정해야 되는 상황이므로 와일드카드 타입을 쓰지 말아야한다.

### 어떤 와일드카드 타입을 써야 할까?

- 펙스(PECS): Producer-Extends, Consumer-Super
- 매개변수화 타입 T가 생산자라면 <? extends T>
- 소비자라면 <? super T>
- pushAll의 src 매개변수는 Stack이 사용할 E 인스턴스를 생산하므로 생산자.
- popAll의 dst 매개변수는 Stack으로부터 E 인스턴스를 소비하므로 소비자.
- 반환 타입에는 한정적 와일드카드 타입을 사용하면 안 된다. 유연성을 높여주기는커녕 클라이언트 코드에서도 와일드카드 타입을 써야하기 때문이다.

### 클래스 사용자가 와일드카드 타입을 신경 써야 한다면 그 API에 무슨 문제가 있을 가능성이 크다.

### Comparable, Comparator는 일반적으로 소비자이므로 <? super E>를 사용하는 편이 낫다.

### 타입 매개변수와 와일드카드 둘 중 어느것을 사용할까?

```java
public static <E> void swap(List<E> list, int i, int j);
public static void swap(List<?> list, int i, int j);
```

- public API라면 간단한 두 번째가 낫다.
- 메서드 선언에 타입 매개변수가 한 번만 나오면 와일드카드로 대체하라. 가 기본 규칙이다.
- 이때 비한정적 타입 매개변수라면 비한정적 와일드카드로, 한정적 타입 매개변수라면 한정적 와일드카드로 바꾸면 된다.
- 하지만 두 번째 swap에는 문제가 있다.
```java
public static void swap(List<?> list, int i, int j){
    list.set(i, list.set(j,list.get(i)));
}
```
- 위 코드는 컴파일이 되지 않는다.
- 원인은 리스트의 타입이 List<?>인데 여기에는 null 외에는 어떤 값도 넣을 수 없다는 데에 있다.
- 해결책은 와일드카드 타입의 실제 타입을 알려주는 메서드를 private 도우미 메서드로 따로 작성하여 활용하는 방법이다.
```java
public static void swap(List<?> list, int i, int j){
    swapHelper(list,i,j);
}

private static <E> void swapHelper(List<E> list, int i, int j){
    list.set(i, list.set(j,list.get(i)));
}
```
- swapHelper 메서드는 리스트가 어떤 타입인지 알고있다.
- swap 메서드 내부에서는 복잡한 제네릭 메서드를 이용했지만, 덕분에 외부에서는 와일드카드 기반으로 깔끔한 선언을 유지할 수 있었다.

## 핵심 정리

- 조금 복잡하더라도 와일드카드 타입을 적용하면 API가 훨씬 유연해진다.
- 널리 쓰일 라이브러리를 작성한다면 반드시 와일드카드 타입을 적절히 사용하자.
- PECS
- Comparable과 Comparator는 모두 소비자라는 사실.

