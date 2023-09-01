# 반복자 패턴(Iterator Pattern)

## 반복자 패턴이란?

- 어떠한 객체의 집합을 순서대로 명령을 처리할 수 있게 해주는 디자인 패턴.
- 컬렉션 구현 방법을 노출시키지 않으면서도 그 집합체안에 들어있는 모든 항목에 접근할 수 있게 해주는 패턴.

## 반복자 패턴의 구조

![Iterator Pattern Structure](../../images/Iterator.png)

- Iterator
    : 순서대로 객체를 검색하는 인터페이스를 정한다.

- ConcreteIterator
    : Iterator에서의 인터페이스를 구현한다.

- IterableCollection
    : Iterator의 역할을 만드는 인터페이스를 정한다.

- ConcreteCollection
    : IterableCollection의 인터페이스를 구현한다.


## 반복자 패턴의 적용

- 컬렉션이 내부에 복잡한 데이터 구조가 있지만 이 구조의 복잡성을 보안, 편의상의 이유로 클라이언트에게 숨기고 싶을 때 사용
- 순회 코드의 중복을 줄일 때 사용
- 코드가 다른 데이터 구조들을 순회할 때 사용. 혹은 데이터 구조의 유형을 미리 알 수 없을 때 사용.