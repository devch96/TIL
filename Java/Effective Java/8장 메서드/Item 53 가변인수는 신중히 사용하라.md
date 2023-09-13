# 가변인수는 신중히 사용하라

- 가변인수(varargs)메서드는 명시한 타입의 인수를 0개 이상 받을 수 있다.
- 가변인수 메서드를 호출하면 가장 먼저 인수의 개수와 길이가 같은 배열을 만들고 인수들을 이 배열에 저장하여
가변인수 메서드에 건네준다.
- 인수를 1개 이상 받을 때도 있다.
```java
static int min(int... args){
    if(args.length == 0){
        throw new IllegalArgumentException("인수가 1개 이상 필요")
    }
        ...
}
```

- 하지만 위 방식은 컴파일 타임이 아니라 런타임에 오류가 발생하기 때문에 좋은 코드는 아니다.
- 차라리 인수를 (int firstArg, int... remaingArgs)로 받는게 낫다.

### 성능에 민감한 상황이라면 가변인수가 걸림돌이 될 수 있다.

- 가변인수 메서드는 호출될 때마다 배열을 새로 하나 할당하고 초기화하기 때문에 비용이 비싸다.
- 가변인수의 유연성이 필요할 때 선택할 수 있는 패턴은 같은 메서드를 인수의 갯수별로 여러개 만드는 것이다.
```java
public void foo(){}
public void foo(int a1){}
public void foo(int a1, int a2){}
public void foo(int a1, int a2, int a3){}
public void foo(int a1, int a2, int a3, int... rest){}
```
- EnumSet의 정적 팩터리도 이 기법을 사용해 열거 타입 집합 생성 비용을 최소화한다.

## 핵심 정리

- 인수 개수가 일정하지 않은 메서드를 정의해야 한다면 가변인수가 반드시 필요하다.
- 메서드를 정의할 때 필수 매개변수는 가변인수 앞에 두고, 가변인수를 사용할 때는 성능 문제까지 고려하자.