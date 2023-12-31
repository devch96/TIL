# 템플릿 메서드 패턴(Template Method Pattern)

## 템플릿 메서드 패턴이란?

- 상위 클래스에서 처리의 흐름을 제어하며, 하위 클래스에서 처리 내용을 구체화하는 디자인 패턴.
- 공통되는 사항을 상위 추상 클래스에서 구현하며, 각 객체마다 다른 부분은 하위 클래스에서 구현.
- 상속을 통한 확장 개발 방법으로 코드의 중복을 줄이고 리팩토링에 유리하여 가장 많이 사용되는 패턴 중 하나.

## 템플릿 메서드 패턴의 구조

![Template Method Pattern Structure](../../images/TemplateMethod.png)

- Abstract Class
: 추상 클래스로 templateMethod를 정의한다.

- Concrete Class
: 부모 클래스에서 abstract로 정의된 templateMethod를 구현한다.

## 템플릿 메서드 패턴의 적용

- 클라이언트들이 알고리즘의 특정 단계들만 확장할 수 있도록 하고 싶을 때, 그러나 전체 알고리즘이나 알고리즘
구조는 확장하지 못하도록 하려고 할 때 사용.
- 약간의 차이가 있지만 거의 같은 알고리즘들을 포함하는 여러 클래스가 있을 때 사용.