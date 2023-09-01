# 데코레이터 패턴(Decorator Pattern)

## 데코레이터 패턴이란?

- 객체의 결합을 통해 기능을 동적으로 유연하게 확장 할 수 있게 해주는 패턴.
- 객체에 추가적인 요건을 동적으로 첨가하며 기능 확장이 필요할 때 서브클래싱 대신 쓸 수 있는 유연한 대안.
- 기본 기능에 추가할 수 있는 기능의 종류가 많은 경우 각 추가 기능을 Decorator 클래스로 정의 한 후 필요한 Decorator 객체를
조합함으로써 추가 기능의 조합을 설계 하는 방식.

## 데코레이터 패턴의 구조

![Decorator Pattern Structure](../../images/Decorator.png)

- Component
    : ConcreteComponent와 Decorator가 구현할 인터페이스. 두 객체를 동등하게 다루기 위해 존재.

- ConcreteComponent
    : Decorate를 받을 객체. 기능 추가를 받을 기본 객체

- Decorator
    : Decorate를 할 객체의 추상 클래스 or 인터페이스. 기능 추가를 할 객체는 이 객체를 상속받음.

- ConcreteDecorator
    : Decorator를 상속받아 구현할 다양한 기능 객체. ConcreteComponent를 감싸는 용도.

## 데코레이터 패턴의 적용

- 객체들을 사용하는 코드를 훼손하지 않으면서 런타임(동적)에 추가 행동들을 객체들에 할당해야 할 때 사용.
- 상속을 사용하여 객체의 행동을 확장하는 것이 어색하거나 불가능할 때 사용