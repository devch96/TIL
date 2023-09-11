# 타입 안전 이종 컨테이너를 고려하라

- 제네릭은 Set, Map 등의 컬렉션과 ThreadLocal, AtomicReference 등의 단일원소 컨테이너에도 흔히 쓰인다.
- 이런 모든 쓰임에서 매개변수화되는 대상은 원소가 아닌 컨테이너 자신이다.
- 컨테이너에서 매개변수화할 수 있는 타입의 수가 제한된다.
- Set에는 원소의 타입을 뜻하는 하나의 타입 매개변수만 있으면 되며, Map에는 키와 값의 타입을 뜻하는 2개만 필요하다.

### 타입 안전 이종 컨테이너 패턴

- 데이터베이스의 행(row)은 임의 개수의 열(column)을 가질 수 있는데, 모든 열을 타입 안전하게 이요할 수 있다면 멋질 것이다.
- 컨테이너 대신 키를 매개변수화한 다음, 컨테이너에 값을 넣거나 뺄 때 매개변수화한 키를 함께 제공하면 된다.
- 제네릭 타입 시스템이 값의 타입이 키와 같음을 보장해줄 것이다.
- 이러한 설계 방식을 타입 안전 이종 컨테이너 패턴(type safe heterogeneous container pattern)이라 한다.

### 타입 안전 이종 컨테이너 패턴 예

- 타입별로 즐겨 찾는 인스턴스를 저장하고 검색할 수 있는 Favorites 클래스.
- 각 타입의 Class 객체를 매개변수화한 키 역할로 사용하면 되는데, class의 클래스가 제네ㄹ릭이기 때문이다.
- class 리터럴의 타입은 Class가 아닌 Class< T>다.
- 컴파일타임 타입 정보와 런타임 타입 정보를 알아내기 위해 메서드들이 주고받는 class 리터럴을 타입 토큰(type token)이라 한다.

```java
public class Favorites{
    public <T> void putFavorite(Class<T> type, T instance);
    public <T> T getFavorite(Class<T> type);
}

public static void main(String[] args) {
    Favorites f = new Favorites();
    
    f.putFavorite(String.class, "Java");
    f.putFavorite(Integer.class, 0xcafebabe);
    f.putFavorite(Class.class, Favorites.class);

    String favoriteString = f.getFavorite(String.class);
    int favoriteInteger = f.getFavorite(Integer.class);
    Class<?> favoriteClass = f.getFavorite(Class.class);
    
    ...
}
```
- Favorites 인스턴스는 타입 안전하다. String을 요청했는데 Integer를 반환하는 일은 절대 없다.
- 모든 키의 타입이 제각각이라, 일반적인 맵과 달리 여러 가지 타입의 원소를 담을 수 있다.
- 따라서 Favorites는 타입 안전 이종 컨테이너다.

### 구현

```java
public class Favorites{
    private Map<Class<?>, Object> favorites = new HashMap<>();
    
    public <T> void putFavorite(Class<T> type, T instance){
        favorites.put(Objects.requireNonNull(type), instance);
    }
    
    public <T> T getFavorite(Class<T> type){
        return type.cast(favorites.get(type));
    }
}
```

- getFavorite 코드에서는 주어진 Class 객체에 해당하는 값을 favorites 맵에서 꺼낸다.
- 이 객체가 바로 반환해야 할 객체가 맞지만, 잘못된 컴파일타임 타입을 갖고 있기 때문에 T로 바꿔 반환해야 한다.
- cast 메서드는 형변환 연산자의 동적 버전이다.

### 제약 

1. 악의적인 클라이언트가 Class 객체를 제네릭이 아닌 로 타입으로 넘기면 Favorites 인스턴스의 타입 안전성이 깨진다.
   - f.putFavorite((Class)Integer.class, "Integer의 인스턴스가 아니다");
   - int favoriteInteger = f.getFavorite(Integer.class); // ClassCastException
   - Favorites가 타입 불변식을 어기는 일이 없도록 보장하려면 putFavorite 메서드에서 인수로 주어진 instance의 타입이 type으로 명시한 타입과 같은지 확인하면 된다.
    ```java
    favorites.put(Objects.requireNonNull(type), type.cast(instance));
    ```
2. 실체화 불가 타입에는 사용할 수 없다.
   - 즐겨 찾는 String이나 String[]은 저장할 수 있어도 List&lt;String>은 저장할 수 없다.
   - List&lt;String>용 Class 객체를 얻을 수 없기 때문이다.
   - List&lt;Integer>.class, List&lt;String>.class 가 허용된다면, List.class와 같은 Class 객체를 공유하는 것으로
   객체 내부가 혼란스러워 진다.
   - 만족스러운 우회로는 없다.
     - 슈퍼 타입 토큰(super type token)으로 해결하려는 시도도 있다.

### 한정적 타입 토큰

- Favorites가 사용하는 타입 토큰인 비한정적이다.
- 타입을 제한하고 싶을 때는 한정적 타입 토큰을 활용하면 가능하다.
- 한정적 타입 토큰이란 단순히 한정적 타입 매개변수나 한정적 와일드카드를 사용하여 표현 가능한 타입을 제한하는 타입 토큰이다.

### 어노테이션 API

- 어노테이션 API는 한정적 타입 토큰을 적극적으로 사용한다.
```java
static Annotation getAnnotation(AnnotatedElement element, String annotationTypeName){
    Class<?> annotationType = null; // 비한정적 타입 토큰
        try{
            annotationType = Class.forName(annotationTypeName);
        }catch(Exception ex){
            throw new IllegalArgumentException(ex);
        }
        return element.getAnnotation(
                annotationType.asSubclass(Annotation.class));
        )
        }
```
- asSubclass 메서드는 호출된 인스턴스 자신의 Class 객체를 인수가 명시한 클래스로 형변환한다.
- 형변환된다는 것은 이 클래스가 인수로 명시한 클래스의 하위 클래스란 뜻이다.

## 핵심 정리

- 컬렉션 API로 대표되는 일반적인 제네릭 형태에서는 한 컨테이너가 다룰 수 있는 타입 매개변수의 수가 고정되어 있다.
- 하지만 컨테이너 자체가 아닌 키를 타입 매개변수로 바꾸면 이런 제약이 없는 타입 안전 이종 컨테이너를 만들 수 있다.
- 타입 안전 이종 컨테이너는 Class를 키로 쓰며, 이런 식으로 쓰이는 Class 객체를 타입 토큰이라 한다.
