# 옵저버 패턴(Observer Pattern)

## 옵저버 패턴이란?

- 객체의 상태 변화를 관찰하는 관찰자 객체를 생성하여 사용하는 패턴.
- 객체의 변화가 발생하면 그에 따르는 종속객체들이 자동으로 변화가 통지되어 그에 따른 명령을 수행하도록 하는 
일대다의 의존성을 정의.
- 데이터의 변경이 발생했을 경우 상대 클래스나 객체에 의존하지 않으면서 데이터 변경을 통보하고자 할 때 유용.

## 옵저버 패턴의 구조

![Observer Pattern Structure](../../images/Observer.png)

- Subscriber(Observer)
    : 데이터의 변경을 통보 받는 인터페이스. Publisher에서 Subscriber의 update 메서드를 호출함으로써
    Publisher의 데이터 변경을 ConcreteSubscribers에게 통보한다.

- Publisher(Subject)
    : ConcreteSubscribers를 관리.

- ConcreteSubscribers(ConcreteObserver)
    : Publisher의 변경을 통보받는 클래스.


## 옵저버 패턴의 적용

- 한 객체의 상태가 변경되어 다른 객체들을 변경해야 할 필요성이 생겼을 때, 그리고 실제 객체 집합들을 미리 알 수 없거나
이러한 집합들이 동적으로 변경될 때 사용.
- 일부 객체들이 제한된 시간 동안 또는 특정 경우에만 다른 객체들을 관찰해야 할 때 사용.

