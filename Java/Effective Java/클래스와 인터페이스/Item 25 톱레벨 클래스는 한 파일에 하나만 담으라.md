# 톱레벨 클래스는 한 파일에 하나만 담으라

- 소스 파일 하나에 톱레벨 클래스를 여러 개 선언하더라도 자바 컴파일러는 불평하지 않는다.
- 하지만 아무런 득이 없고 심각한 위험을 감수해야 하는 행위다.
- 한 클래스를 여러 가지로 정의할 수 있으며, 그중 어느 것을 사용할지는 어느 소스 파일을 먼저 컴파일하냐에 따라 달라지기
때문이다.
```java
public class Main{
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }
}

// Utensil.java
class Utensil{
    static final String NAME = "pan";
}

class Dessert {
    static final String NAME = "cake";
}

// Dessert.java
class Utensil{
    static final String NAME = "pot";
}

class Dessert {
    static final String NAME = "pie";
}
```
- javac Main.java Dessert.java 명령으로 컴파일 한다면 컴파일러는 Main.java를 컴파일하고 그 안에서
Utensil 참조를 만나면 Utensil.java 파일을 살핀다. 다음 Dessert.java 컴파일할시 이미 정의된 클래스라고
컴파일러가 오류를 보낸다.(운이 좋은 상황이다.)
- javac Main.java나 javac Main.java Utensil.java 명령으로 컴파일하면 "pancake"를 출력한다.
- javac Dessert.java Main.java 명령으로 컴파일 하면 "potpie"를 출력한다.
- 컴파일러에 어느 소스 파일을 먼저 건네느냐에 따라 동작이 달라지는건 반드시 피해야 할 문제다.
- 해결책은 톱레벨 클래스들(Utensil과Dessert)을 서로 다른 소스 파일로 분리하면 된다.
- 같은 파일에 담고 싶다면 정적 멤버 클래스를 사용하면 된다.
```java
public class Main{
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }

    private static class Utensil{
        static final String NAME = "pan";
    }

    private static class Dessert {
        static final String NAME = "cake";
    }
}
```

## 핵심 정리

- 소스 파일 하나에는 반드시 톱레벨 클래스 하나만 담자.
- 이 규칙을 따르면 컴파일러가 한 클래스에 대한 정의를 여러 개 만들어 내는 일이 사라지고, 어떤 순서로 컴파일하든 
프로그램의 동작이 달라지는 일은 결코 일어나지 않을 것이다.

