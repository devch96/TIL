# 컬렉션 API 개선

- 컬렉션 API에는 성가시며, 에러를 유발하는 여러 단점이 존재한다.

----------------

## 컬렉션 팩토리

- 자바 9에서는 작은 컬렉션 객체를 쉽게 만들 수 있는 몇 가지 방법을 제공한다.
- 적은 요소를 포함하는 요소를 만드는 방법
```java
List<String> friends = new ArrayList<>();
friends.add("tom")
friends.add("ted")
friends.add("edward")
```
- 세 문자열을 저장하는데도 코드 4줄이 필요하다.
- Arrays.asList() 팩터리 메서드를 사용하면 코드를 간단히 줄일 수 있다.
```java
List<String> friends = Arrays.asList("tom","ted,"edward")
```
- 하지만 내부적으로 고정된 크기의 변환할 수 있는 배열로 구현되었기 때문에 요소를 추가하려하면 예외가 발생한다.

#### UnsupportedOperationException 예외 발생

- Set은 매끄럽지 못하게 만들며 내부적으로 불필요한 객체 할당을 필요로 한다.
- 결과를 변환할 수 있는 집합이다.
- 맵은 만들수 있는 방법이 없다.

### 리스트 팩터리

- List.of 팩터리 메서드를 이용해서 간단하게 리스트를 만들 수 있다.
```java
List<String> friends = List.of("tom","ted","edward");
```
- 이 메서드는 요소를 추가하지도(add()), 요소를 변경하지도(set()) 못한다
  - UnsupportedoperationException 발생
- 컬렉션이 의도치 않게 변하는 것을 막을 수 있다.
  - 요소 자체가 변하는 것은 막을 수 없다.
- 어떤 상황에서 새로운 컬렉션 팩터리 메서드 대신 스트림 API를 사용해 리스트를 만들어야 할까?
  - 테이터 처리 형식을 설정하거나 데이터를 변환할 필요가 없다면 팩터리 메서드

### 집합 팩터리

- Set.of 팩터리 메서드를 이용해서 간단하게 바꿀 수 없는 집합을 만들 수 있다.
```java
Set<String> friends = Set.of("tom","ted","edward");
```

### 맵 팩터리

- 자바 9에서는 두 가지 방법으로 바꿀 수 없는 맵을 초기화할 수 있다.

```java
Map<String, Integer> ageOfFriends = Map.of("tom",30,"ted",25,"edward",26); // 1
Map<String, Integer> ageOfFrends = Map.ofEntries(Map.entry("tom",30), Map.entry("ted",25), Map.entry("edward",26)); // 2
```

- 열 개 이하의 키와 값 쌍을 가진 작은 맵을 만들때는 1번 메서드가 유용하다.
- 그 이상은 Map.ofEntries 팩터리 메서드를 이용하는게 좋다. Map.entry 는 Map.Entry 객체를 만드는 새로운 팩터리 메서드다. 

---------------

## 리스트와 집합 처리

- 자바 8에서는 List, Set 인터페이스에 다음과 같은 메서드를 추가했다.
  - removeIf
    - 프레디케이트를 만족하는 요소를 제거한다.
    - List나 Set을 구현하거나 그 구현을 상속받은 모든 클래스에서 이용 가능
  - replaceAll
    - 리스트에서 이용할 수 있는 기능으로 UnaryOperator 함수를 이용해 요소를 바꾼다.
  - sort
    - List 인터페이스에서 제공하는 기능으로 리스트를 정렬한다.

### removeIf 메서드

- 숫자로 시작되는 참조 코드를 가진 트랜잭션을 삭제하는 코드
```java
for(Transaction transaction : transactions){
    if(Character.isDigit(transaction.getReferenceCode().charAt(0))){
        transaction.remove(transaction);
    }
}
```
- ConcurrentModificationException을 일으킨다
- 내부적으로 보자
```java
for(Iterator<Transaction> iterator = transactions.iterator();
        iterator.hasNext();){
    Transaction transaction = iterator.next();
    if(Character.isDigit(transaction.getReferenceCode().charAt(0))){
        transaction.remove(transaction);
    }
}
```
- 두 개별 객체가 컬렉션을 관리한다
  - Iterator 객체
    - next(), hasNext()를 이용해 질의
  - Collection 객체 자체
    - remove()를 호출해 요소를 삭제
- 반복자의 상태는 컬렉션의 상태와 서로 동기화되지 않기에 문제가 발생.
- 이러한 패턴은 removeIf로 바꿀 수 있다. 코드가 단순해지고 버그도 예방 가능하다.
- removeIf는 삭제할 요소를 가리키는 프레디케이트를 인수로 받는다.
```java
transactions.removeIf(transaction -> 
        Character.isDigit(transaction.getReferenceCode().charAt(0)));
```

### replaceAll 메서드

- List 인터페이스의 replaceAll 메서드를 이용해 리스트의 각 요소를 새로운 요소로 바꿀 수 있다.
```java
referenceCodes.stream()
        .map(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1))
        .collect(Collectors.toList())
```
- 스트림 API 를 사용할 순 있지만 새로운 컬렉션을 만든다는 단점이 있다.
- 기존 컬렉션을 바꾸는 것
```java
for(ListIterator<String> iterator = referenceCodes.listIterator();
        iterator.hasNext();){
    String code = iterator.next();
    iterator.set(Character.toUpperCase(code.charAt(0) + code.substring(1)));
}
```
- 코드가 복잡하고 컬렉션 객체와 Iterator 객체를 혼용하면 반복과 컬렉션 변경이 동시에 이뤄지기 때문에 문제를 일으킬 가능성이 높다.
```java
referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));
```
- replaceAll 메서드를 이용하면 편하면서도 에러 방지를 할 수 있다.

---------------------

## 맵 처리

- 자바 8에서는 Map 인터페이스에 몇 가지 디폴트 메서드를 추가했다.
- 자주 사용되는 패턴을 개발자가 직접 구현할 필요가 없도록 이들 메서드를 추가한 것이다.

### forEach 메서드

- 맵에서 키와 값을 반복하면서 확인하는 작업은 귀찮은 작업이다.
```java
for(Map.Entry<String, Integer> entry : ageOfFriends.entrySet()){
    String friend = entry.getKey();
    Integer age = entry.getValue();
    System.out.println(friend + " is " + age + " years old");
}
```
- 자바 8에서부터 Map 인터페이스는 BiConsumer(키와 값을 인수로 받음)를 인수로 받는 forEach 메서드를 지원한다.
```java
ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old"));
```

### 정렬 메서드

- 다음 두 개의 새로운 유틸리티를 이용하면 맵의 항목을 값 또는 키를 기준으로 정렬할 수 있다.
  - Entry.comparingByValue
  - Entry.comparingByKey
```java
Map<String, String> favoriteMovies = Map.ofEntries(entry("tom","Star Wars"),
        entry("ted","Matrix"),
        entry("edward","James Bond"));

favoriteMovies.entrySet().stream().sorted(Entry.comparingByKey())
        .forEachOrdered(System.out:;println);
```

### getOrDefault 메서드

- 기존에는 찾으려는 키가 존재하지 않으면 null이 반환되므로 NullPointerException을 방지하려 널 체크를 해야한다.
- getOrDefault 메서드는 첫 번째 인수로 키를, 두 번째 인수로 기본값을 받으며 맵에 키가 존재하지 않으면 두 번째 인수로 받은 기본값을 반환한다.
- 키가 존재하더라도 값이 널인 경우엔 getOrDefault가 널을 반환한다.

### 계산 패턴

- 맵에 키가 존재하는지 여부에 따라 어떤 동작을 실행하고 결과를 저장해야 하는 상황
  - 키를 이용해 값비싼 동작을 실행해서 얻은 결과를 캐시하려 한다
  - 키가 존재하면 결과를 다시 계산할 필요가 없다
- 다음 세 가지 연산이 이런 상황에서 도움을 준다.
  - computeIfAbsent
    - 제공된 키에 해당하는 값이 없으면(값이 없거나 널), 키를 이용해 새 값을 계산하고 맵에 추가
  - computeIfPresent
    - 제공된 키가 존재하면 새 값을 계산하고 맵에 추가
  - compute
    - 제공된 키로 새 값을 계산하고 맵에 저장
- 정보를 캐시할 때 computeIfAbsent를 활용할 수 있다.
```java
Map<String, byte[]> dataToHash = new HashMap<>();
MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
lines.forEach(line ->
        dataToHash.computeIfAbsent(line, this::calculateDigest));
private byte[] calculateDigest(String key){
    return messageDigest.digest(key.getBytes(StandardCharsets.UTF_8));
}
```
- computeIfPresent 메서드는 현재 키와 관련된 값이 맵에 존재하며 널이 아닐 때만 새 값을 계산한다.

### 삭제 패턴

- 제공된 키에 해당하는 맵 항목을 제거하는 remove 메서드는 이미 알고 있다.
- 자바 8에서는 키가 특정한 값과 연관되었을 때만 항목을 제거하는 오버로드 버전 메서드를 제공한다.
```java
favoriteMovies.remove(key, value);
```

### 교체 패턴

- 두 개의 메서드가 맵에 추가되었다.
  - replaceAll
    - BiFunction을 적용한 결과로 각 항목의 값을 교체한다.
    - List의 replaceAll과 비슷한 동작을 수행한다
  - Replace
    - 키가 존재하면 맵의 값을 바꾼다
    - 키가 특정 값으로 매핑되었을 때만 값을 교체하는 오버로드 버전도 있다.
```java
favoriteMovies.replaceAll((friend, movie) -> movie.toUpperCase());
```

### 합침

- 두 개의 맵을 합칠때는 putAll을 사용할 수 있다.
- 중복된 키가 없다면 putAll은 잘 동작한다
- 값을 좀 더 유연하게 합쳐야 한다면 새로운 merge 메서드를 이용할 수 있다.
- merge 메서드는 중복된 키를 어떻게 합칠지 결정하는 BiFunction을 인수로 받는다.
```java
friends.forEach((k,v) ->
        everyone.merge(k,v,(movie1, movie2) -> movie1 + "&" + movie2));
```
- merge를 이용해 초기화 검사를 구현할 수 있다.
```java
moviesToCount.merge(movieName, 1L, (key,count) -> count + 1L);
```
- 지정된 키와 연관된 값이 없거나 값이 널이면 merge는 키를 널이 아닌 값과 연결한다. 아니면
merge는 연결된 값을 주어진 매핑 함수의 결과 값으로 대치하거나 결과가 널이면 항목을 제거한다.

----------------

## 개선된 ConcurrentHashMap

- ConcurrentHashMap 클래스는 동시성 친화적이며 최신 기술을 반영한 HashMap 버전이다.
- ConcurrentHashMap은 내부 자료구조의 특정 부분만 잠궈 동시 추가, 갱신 작업을 허용하기에 동기화된 Hashtable 버전에 비해
읽기 쓰기 연산 성능이 월등하다.

### 리듀스와 검색

- ConcurrentHashMap은 스트림에서 봤던 것과 비슷한 종류의 세 가지 새로운 연산을 지원한다.
  - forEach
    - 각 (키,값) 상에 주어진 액션을 실행
  - reduce
    - 모든 (키,값) 쌍을 제공된 리듀스 함수를 이용해 결과로 합침
  - search
    - 널이 아닌 값을 반환할 때까지 각 (키,값) 쌍에 함수를 적용
- 각각의 함수들은 뒤에 Key, Value, Entry를 붙여 연산 형태를 바꿀 수 있다.
- 이들 연산은 ConcurrentHashMap의 상태를 잠그지 않고 연산을 수행하기 때문에 계산이 진행되는 동안 바뀔 수 있는
객체, 값, 순서 등에 의존하지 않아야 한다.
- 이들 연산에 병렬성 기준값(threshold)를 지정해야 한다.
- 맵의 크기가 주어진 기준값보다 작으면 순차적 연산을 하고 낮으면 공통 스레드 풀을 이용해 병렬적 연산을 한다.
```java
ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
long parallelismThreshold = 1;
Optinal<Integer> maxValue = Optional.ofNullable(map.reduceValues(parallelismThreshold, Long::max));
```
- int, long, double 등의 기본값에는 전용 each reduce 연산이 있으므로 박싱 작업을 할 필요 없이 효율적으로 작업을 처리할 수 있다.

### 계수

- ConcurrentHashMap 클래스는 맵의 매핑 개수를 반환하는 mappingCount 메서드를 제공한다.
- size 메서드 대신 int를 반환하는 mappingCount 메서드를 사용하는 것이 좋은데 매핑의 개수가 int의 범위를 넘어서는 이후의
상황을 대처할 수 있기 때문이다.

### 집합뷰

- ConcurrentHashMap 클래스는 집합 뷰로 변환하는 keySet이라는 새 메서드를 제공한다.
- 맵을 바꾸면 집합도 바뀌고 반대로 집합을 바꾸면 맵도 바뀐다
- newKeySet을 사용하면 유지된다.

-----------------

## 마치며

- 자바 9는 바꿀 수 없는 리스트, 집합, 맵을 쉽게 만들 수 있도록 List.of, Set.of, Map.of, Map.ofEntries 등의 컬렉션 팩터리를 지원한다.
- 이들 컬렉션 팩터리가 반환한 객체는 바꿀 수 없다.
