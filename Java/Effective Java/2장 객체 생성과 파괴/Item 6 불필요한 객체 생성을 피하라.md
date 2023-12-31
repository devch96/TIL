# 불필요한 객체 생성을 피하라

- 똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다.
```java
String s1 = new String("bikini");

String s2 = "bikini";
```

- s1은 실행될 때마다 String 인스턴스를 새로 만든다.
- s2는 새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용한다.
- 생성자는 호출할 때마다 새로운 객체를 만들지만, 팩토리 메서드는 그렇지 않다.
- 불변 객체만이 아니라 가변 객체라 해도 사용 중에 변경 되지 않을 것임을 안다면 재사용할 수 있다.
- 생성 비용이 아주 비싼 객체가 반복해서 필요하다면 캐싱하여 재사용해야 한다.

```java
static boolean isRomanNumeral(String s){
    return s.matches(...);
}
```

- 위의 문제는 String.matches 내부에서 정규표현식용 Pattern 인스턴스가 한 번 쓰고 버려진다.
- 성능을 개선하려면 필요한 정규표현식을 표현하는 Pattern 인스턴스를 클래스 초기화 과정에서 직접 생성해 캐싱해두고
메서드가 호출될 때마다 이 인스턴스를 재사용하면 된다.

```java
public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile(...);
    
    static boolean isRomanNumeral(String s){
        return ROMAN.matcher(s).matches();
    }
}
```

- 개선된 방식의 클래스가 초기화된 후 이 메서드를 한 번도 호출하지 않는다면 ROMAN 필드는 쓸데없이 초기화된 꼴이다.
- isRomanlNumeral 메서드가 처음 호출될 때 필드를 초기화하는 지연 초기화로 불필요한 초기화를 없앨 수 있지만,
지연 초기화는 코드를 복잡하게 만들고 성능은 크게 개선되지 않을 때가 많이 때문에 권하지 않는다.

## 불필요한 객체를 만들어내는 오토박싱

- 오토박싱은 프로그래머가 기본 타입과 박싱된 기본 타입을 섞어 쓸 때 자동으로 상호 변환해주는 기술이다.
- 오토박싱은 기본 타입과 그게 애응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.
- 의미상으로는 차이가 없지만 성능에서는 그렇지 않다.
```java
private static long sum(){
    Long sum = 0L;
    for(long i = 0; i <= Integer.MAX_VALUE; i++)
        sum += i;
    return sum;
}
```
- long 타입인 i 가 Long 타입인 sum 에 더해질 때마다 Long 인스턴스가 만들어진다.
- 박싱된 기본 타입보다는 기본 타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의하자.
- `객체 생성은 비싸니 피해야 한다` 라고 오해하면 안된다.
- 프로그램의 명확성, 간결성, 기능을 위해서 객체를 추가로 생성하는 것이라면 일반적으로 좋은 일이다.
- 아주 무거운 객체가 아닌 다음에야 단순히 객체 생성을 피하고자 객체 풀을 만들지는 말자.
- DB연결같은 경우 생성 비용이 워낙 비싸 재사용하는 편이 낫지만 일반적으로는 자체 객체 풀은 코드를 헷갈리게 만들고 메모리 사용량을 늘리고 성능을
떨어뜨린다.
- 가벼운 객체용을 다룰 때는 직접 만든 객체 풀보다 훨씬 빠르다.

## 정리

- 똑같은 기능을 하는 객체를 새로 만드는 것보다 재사용하는 편이 나을때가 많다.
- 불필요한 객체를 생성하는 것은 성능에 영향을 준다.
- 오토박싱은 불필요한 객체를 생성함으로 주의해야 한다.
- 무조건 객체 생성은 비싸니 피해야 한다가 아니라 객체가 무거운지, 객체의 생성이 자주 일어나는지 판단하고
자신만의 객체 풀을 만들어 재사용해야 한다.