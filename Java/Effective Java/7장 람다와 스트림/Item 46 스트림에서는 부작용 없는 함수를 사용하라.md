# 스트림에서는 부작용 없는 함수를 사용하라

- 스트림이 제공하는 표현력, 속도, 병렬성을 얻으려면 API뿐만 아니라 패러다임까지 함께 받아들여야 한다.
- 스트림 패러다임의 핵심은 계산을 일련의 변환(transformation)으로 재구성 하는 부분이다.
- 이때 각 변환 단계는 가능한 한 이전 단계의 결과를 받아 처리하는 순수 함수여야 한다.
- 순수 함수란 다른 가변 상태를 참조하지 않고, 함수 스스로도 다른 상태를 변경하지 않으며 입력만이 결과에 영향을 주는 함수를 말한다.

### 스트림 코드를 가장한 반복적 코드

```java
Map<String, Long> freq = new HashMap<>();
try(Stream<String> words = new Scanner(file).tokens()){
    words.forEach(word -> {
        freq.merge(word.toLowerCase(), 1L, Long::sum);
        });
}
```
- 스트림, 람다, 메서드 참조를 사용했지만 스트림 코드라 할 수 없다.
- 스트림 API의 이점을 살리지 못하여 같은 기능의 반복적 코드보다 길고, 읽기 어렵고, 유지보수에도 좋지 않다.
- 이 코드의 모든 작업이 종단 연산인 forEach에서 일어나는데 거기에서 람다를 실행하면서 문제가 생긴 것이다.

### 올바르게 작성한 코드

```java
Map<String, Long> freq = new HashMap<>();
try(Stream<String> words = new Scanner(file).tokens()){
    freq = words
        .collect(groupingBy(String::toLowerCase, counting()));
}
```

- forEach 종단 연산은 스트림 계산 결과를 보고할 때만 사용하고, 계산하는 데는 쓰지 말자.
- collector는 수집기로 toList(), toSet(), toCollection(collectionFactory)가 주인공이다.

### toMap(keyMapper, valueMapper)

- 스트림 원소를 키에 매핑하는 함수와 값에 매핑하는 함수를 인수로 받는다.
```java
// 문자열을 열거 타입 상수에 매핑
private static final Map<String, Operation> stringToEnum =
        Stream.of(value()).collect(toMap(Object::toString, e->e));


// 각 키와 해당 키의 특정 원소를 연관 짓는 맵을 생성하는 수집기
Map<Artist, Album> topHits = albums.collect(
        toMap(Album::artist, a->a, maxBy(comparing(Album::sales))));
```

## 핵심 정리

- 스트림 파이프라인 프로그래밍의 핵심은 부작용 없는 함수 객체에 있다.
- 종단 연산 중 forEach는 스트림이 수행한 계산 결과를 보고할 때만 이용해야 한다.
- 스트림을 올바로 사용하려면 수집기를 잘 알아둬야 한다.
- 수집기 팩터리중 중요한것은 toList, toSet, toMap, gourpingBy, joining 이다.