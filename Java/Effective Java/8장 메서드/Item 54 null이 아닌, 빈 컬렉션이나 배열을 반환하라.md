# null이 아닌, 빈 컬렉션이나 배열을 반환하라

- null을 반환한다면, 클라이언트는 이 null 상황을 처리하는 코드를 추가로 작성해야 한다.
```java
private final List<Cheese> cheeseInStock = ...;

public List<Cheese> getCheese(){
    return cheeseInStock.isEmpty() ? null
        : new ArrayList<>(cheeseInStock);
}

List<Cheese> cheeses = getCheese();
if(cheeses != null && ...){
        ...
}
```
- 컬렉션이나 배열 같은 컨테이너가 비었을 때 null을 반환하는 메서드를 사용할 때면 항시 이와 같은 방어 코드를 넣어줘야 한다.
- 실제로 객체가 0개일 가능성이 거의 없는 상황에서는 수년 뒤에야 오류가 발생할 수도 있다.

### 빈 컨테이너를 할당하는 데도 비용이 드니 null을 반환하는 쪽이 낫다는 주장도 있다.

- 하지만 이는 두 가지 면에서 틀린 주장이다.
1. 성능 분석 결과 빈 컨테이너를 할당하는 것이 성능 저하의 주범이라고 확인되지 않는 한 이 정도 의 성능 차이는 신경 쓸 수준이 못 된다.
2. 빈 컬렉션과 배열은 굳이 새로 할당하지 않고도 반환할 수 있다.
    ```java
    public List<Cheese> getCheese(){
            return  new ArrayList<>(cheeseInStock);
    }
    ```

### 사용 패턴에 따라 빈 컬렉션 할당이 성능을 눈에 띄게 떨어뜨릴 수도 있다.

- 매번 똑간은 빈 `불변` 컬렉션을 반환하면 된다.
- List일 경우 Collections.emptyList, Set일 경우 Collections.emptySet, Map일 경우 Collections.emptyMap을 사용하면 된다.
- 하지만 이것 또한 최적화에 해당하니 전후의 성능을 측정하여 실제로 개선 되는지 확인해야 한다.
```java
public List<Cheese> getCheese(){
    return cheeseInStock.isEmpty() ? Collections.emptyList
        : new ArrayList<>(cheeseInStock);
}
```

### 배열을 쓸 때도 null을 반환하지 말고 길이가 0인 배열을 반환하라.
```java
public List<Cheese> getCheese(){
    return cheeseInStock.toArray(new Cheese[0]);
}
```

- 이 방식이 성능을 떨어뜨릴 것 같다면 길이 0짜리 배열을 미리 선언해두고 매번 그 배열을 반환하면 된다.(길이 0인 배열은 모두 불변이기 때문)
```java
private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];
public List<Cheese> getCheese(){
    return cheeseInStock.toArray(EMPTY_CHEESE_ARRAY);
}
```
- return cheeseInStock.toArray(EMPTY_CHEESE_ARRAY)를 해도 괜찮은 이유는 List.toArray(T[] a) 메서드는 주어진 배열 a가 충분히 크면
a 안에 원소를 담아 반환하고, 그렇지 않으면 T[] ㅏ입 배열을 새로 만들어 그 안에 원소를 담아 반환한다. 따라서 원소가 하나라도 존재하면
Cheese[] 타입의 배열을 새로 생성해 반환하고, 원소가 0개면 EMPTY_CHEESE_ARRAY를 반환한다.
- 하지만 단순히 성능을 개선할 목적이라면 toArray에 넘기는 배열을 미리 할당하는 건 오히려 성능이 떨어진다는 연구 결과도 있다

## 핵심 정리

- null이 아닌, 빈 배열이나 컬렉션을 반환하라.
- null을 반환하는 API는 사용하기 어렵고 오류 처리 코드도 늘어날 뿐이다.