# 반환 타입으로는 스트림보다 컬렉션이 낫다

- 스트림은 반복을 지원하지 않기 때문에 스트림과 반복을 알맞게 조합해야 좋은 코드가 나온다.
- API를 스트림만 반환하도록 짜놓으면 반환된 스트림을 for-each로 반복하길 원하는 사용자는 불만을 토로할 것이다.
- Stream 인터페이스는 Iterable 인터페이스가 정의한 추상 메서드를 전부 포함할 뿐만 아니라 Iterable 인터페이스가 정의한 방식대로
동작하지만 Stream이 Iterable을 확장하지 않아서 for-each로 스트림을 반복할 수 없다.

### 스트림을 반복하기 위한 끔찍한 우회 방법

```java
// 자바 타입 추론의 한계로 컴파일 되지 않음.
for(ProccessHandle ph : ProcessHandle.allProcesses()::iterator){
        ...
}

// 우회
for(ProccessHandle ph : (Iterable<ProcessHandle>) ProcessHandle.allProcesses()::iterator)
```

### 차라리 어댑터를 사용해라 (Stream -> Iterable)

```java
public static <E> Iterable<E> iterableOf(Stream<E> stream){
    return stream::iterator;
}

for(ProcessHandle p : iterableOf(ProcessHandle.allProcesses())){
        ...
}
```

### 반대도 짜증난다 (Iterable -> Stream)

```java
public static <E> Stream<E> streamOf(Iterable<E> iterable){
    return StreamSupport.stream(iterable.spliterator(), false);
}
```

### 공개 API를 작성할 때는 모두를 배려해야 한다.

- 스트림 파이프라인을 사용하는 사람과 반복문에서 쓰려는 사람 모두를 배려해야 한다.
- Collection 인터페이스는 Iterable의 하위 타입이고 stream 메서드도 제공하니 반복과 스트림을 동시에 지원한다.
- 따라서 원소 시퀀스를 반환하는 공개 API의 반환 타입에는 Collection이나 그 하위 타입을 쓰는 게 일반적으로 최선이다.
- Arrays 역시 Arrays.asList와 Stream.of 메서드로 반복과 스트림을 지원할 수 있다.
- 하지만 단지 컬렉션을 반환한다는 이유로 덩치 큰 시퀀스를 메모리에 올려서는 안 된다.

### 반환할 시퀀스가 크지만 표현을 간결하게 할 수 있다면 전용 컬렉션을 구현하는 방안을 검토하자.

- 멱집합(한 집합의 모든 부분집합을 원소로 하는 집합)을 반환하는 상황.
- 원소의 개수가 n이면 멱집합의 원소 개수는 2^n
- 시퀀스가 커서 표준 컬렉션 구현체에 저장하려는 생각은 위험하고, AbstractList를 이요해 훌륭한 전용 컬렉션을 손쉽게 구현한다.

```java
public class PowerSet {
    public static final <E> Collections<Set<E>> of(Set<E> s) {
        List<E> src = new ArrayList(s);
        if(src.size() > 30){
            throw new IllegalArgumentException("Too Much");
        }
        return new AbstractList<Set<E>>{
            @Override
            public int size(){
                return 1<< src.size();
            }
            
            @Override
            public boolean contains(Obejct o){
                return o instanceof Set && src.containsAll((Set) o);
            }
            
            @Override
            public Set<E> get(int index){
                Set<E> result = new Hashset<>();
                for(int i = 0; index != 0; i++, index >>= 1){
                    if((index & 1) == 1){
                        result.add(src.get(i));
                    }
                }
                return result;
            }
        }
    }
}
```
- 입력 집합 원소 수가 30을 넘으면 예외를 던지는데 이것은 Collection을 반환 타입으로 쓸 때의 단점이다.
- Collection의 size 메서드가 int 값을 반환하므로 최대길이는 Integer.MAX_VALUE 제한된다.
- AbstractCollection을 활용해서 Collection 구현체를 작성할 때는 Iterable용 메서드 외 contains와 size 2개만 더 구현하면 된다.
- contains와 size를 구현하는 게 불가능할 때(반복이 시작되기 전에는 시퀀스의 내용을 확정할 수 없는 등의 사유)면 스트림이나 Iterable을 반환하는
것이 낫다.
- 별도의 메서드를 두어 두 방식 모두 제공해도 된다.

### 단순히 구현하기 쉬운 쪽을 선택하기도 한다

```java
public class SubLists{
    public static <E> Stream<List<E>> of(List<E> list){
        return Stream.concat(Stream.of(Collections.emptyList()),
                prefixes(list).flatMap(SubLists::suffixes));
    }
    
    private static <E> Stream<List<E>> prefixes(List<E> list){
        return IntStream.rangeClosed(1, list.size())
                .mapToObj(end -> list.subList(0,end));
    }
    
    private static <E> Stream<List<E>> suffixes(List<E> list){
        return IntStream.range(0, list.size())
                .mapToObj(start -> list.subList(start, list.size()));
    }
}
```

- 어떤 리스트의 부분리스트는 단순히 그 리스트의 프리픽스의 서픽스(혹은 서픽스의 프리픽스)에 빈 리스트 하나만 추가하면 된다.
- Stream.concat 메서드는 반횐되는 스트림에 빈 리스트를 추가하며, flatMap 메서드는 모든 프리픽스의 모든 서픽스로 구성된 하나의 스트림을 만든다.

## 핵심 정리

- 원소 시퀀스를 반환하는 메서드를 작성할 때는 이를 스트림으로 처리하기 원하는 사용자와, 반복으로 처리하길 원하는
사용자가 있다는것을 떠올리고 양쪽을 다 만족시키려 노력하자.
- 컬렉션을 반환할 수 있다면 그렇게 하라.
- 반환 전부터 이미 원소들을 컬렉션에 담아 관리하고 있거나, 관리할 원소의 개수가 적다면 표준 컬렉션에 담아 반환하라.
- 그렇지 않으면 전용 컬렉션을 구현할지 고민하라.
- 나중에 Stream 인터페이스가 Iterable을 지원하도록 자바가 수정된다면, 스트림을 반환하면 될 것이다.

