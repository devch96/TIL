# ordinal 인덱싱 대신 EnumMap을 사용하라

- 이따금 배열이나 리스트에서 원소를 꺼낼 때 ordinal 메서드로 인덱스를 얻는 코드가 있다.

```java
class Plant {
    enum LifeCycle {ANNUAL, PERENNIAL, BIENNIAL}

    final String name;
    final LifeCycle lifeCycle;

    Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }
}

public static void main(String[] args) {
    Set<Plant>[] plantsByLifeCycle =
            (Set<Plant>[]) new Set[LifeCycle.values().length];
    for (int i = 0; i < plantsByLifeCycle.length; i++) {
        plantsByLifeCycle[i] = new HashSet<>();
    }
    for(Plant p : garden){
        plantsByLifeCycle[p.lifeCycle.ordinal()].add(p);
    }
}
```
- 배열은 제네릭과 호환되지 않으니 비검사 형변환을 수행해야 하고 깔끔히 컴파일되지 않는다.
- 정수는 열거 타입과 달리 타입 안전하지 않기 때문에 정확한 정숫값을 사용한다는 것을 직접 보증해야 한다.
- 위의 배열은 실질적으로 열거 타입 상수를 값으로 매핑하는 일을 하니 Map을 사용할 수도 있을 것이다.

### EnumMap

```java
Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
    new EnumMap<>(Plant.LifeCycle.class);
for(Plant.LifeCycle lc : Plant.LifeCycle.values()){
    plantsByLifeCycle.put(lc, new HashSet<>());
}
for(Plant p : garden){
    plantsByLifeCycle.get(p.lifeCycle).add(p);
}
```
- 더 짧고 명료하고 안전하고 성능도 원래 버전과 비등하다.
- EnumMap의 성능이 ordinal을 쓴 배열에 비견되는 이유는 내부에서 배열을 사용하기 때문이다.
- 내부 구현 방식을 숨겨서 Map의 타입 안전성과 배열의 성능을 모두 얻어낸 것이다.
- EnumMap의 생성자가 받는 키 타입의 Class 객체는 한정적 타입 토큰으로, 런타임 제네릭 타입 정보를 제공한다.
- 스트림을 사용해 맵을 관리하면 코드를 더 줄일 수 있다.
```java
Arrays.stream(garden).collect(groupingBy(p -> p.lifeCycle));
Arrays.stream(garden).collect(groupingBy(p -> p.lifeCycle, () -> new EnumMap<>(LifeCycle.class), toSet()));
```


## 핵심 정리

- 배열의 인덱스를 얻기 위해 ordinal을 쓰는 것은 일반적으로 좋지 않으니, EnumMap을 사용하라.
- 다차원 관계는 EnumMap<..., EnumMap<...>>으로 표현하라.
- Enum.ordinal을 웬만해서는 사용하지 말아야 한다.