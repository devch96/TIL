# 정의하려는 것이 타입이라면 마커 인터페이스를 사용하라

- 아무 메서드도 담고 있지 않고, 단지 자신을 구현하는 클래스가 특정 속성을 가짐을 표시해주는 인터페이스를
마커 인터페이스(marker interface)라 한다.(Serializable)
- Serializable은 자신을 구현한 클래스의 인스턴스는 ObjectOutputStream을 통해 작성할 수 있다고(직렬화할 수 있다고)알려준다.
- 마커 애너테이션이 등장하면서 마커 인터페이스는 구식이 되었다는 이야기가 있는데 사실이 아니다.

### 마커 인터페이스가 마커 애너테이션보다 좋은 점

1. 마커 인터페이스는 이를 구현한 클래스의 인스턴스들을 구분하는 타입으로 쓸 수 있으나, 마커 애너테이션은 그렇지 않다.
    - 마커 인터페이스는 어엿한 타입이기 때문에 컴파일타임에 오류를 잡을 수 있다.
    - ObjectOutputStream.writeObject 메서드는 인수로 받은 객체가 Serializable을 구현했을 거라고 가정한다.
    - Serializable 객체를 받았으면 좋았을 텐데 Object 객체를 받도록 설계되었다.(오류)

2. 마커 인터페이스는 마커 애너테이션보다 적용 대상을 더 정밀하게 지정할 수 있다.
    - 적용 대상(@Target)을 ElementType.TYPE으로 선언한 애너테이션은 모든 타입에 달 수 있다.
    - 특정 인터페이스를 구현한 클래스에만 적용하고 싶은 마커가 있다면 애너테이션은 적용하기 힘드나,
   마커 인터페이스로 정의했다면 마킹하고 싶은 클래스에서만 그 인터페이스를 구현하면 된다. 그러면
   마킹된 타입은 자동으로 그 인터페이스의 하위 타입임이 보장되는 것이다.

### 마커 애너테이션이 마커 인터페이스보다 좋은 점

- 거대한 애너테이션 시스템의 지원을 받는다는 점이다.
- 애너테이션을 적극 활용하는 프레임워크에서는 마커 애너테이션을 쓰는 쪽이 일관성을 지키는 데 유리할 것이다.

### 어떨 때 무엇을 써야 할까?

- 클래스와 인터페이스 외의 프로그램 요소(모듈, 패키지, 필드, 지역변수 등)에 마킹해야 할 때 애너테이션을 쓸 수밖에 없다.
- 마커를 클래스나 인터페이스에 적용해야 한다면 "이 마킹이 된 객체를 매개변수로 받는 메서드를 작성할 일이 있을까" 라고 자문해보자.
- 답이 "그렇다" 이면 마커 인터페이스를 써야 한다. 그렇게 해야 해당 메서드의 매개변수 타입으로 사용하여 컴파일타임에 오류를 잡아낼 수 있기 때문이다.

## 핵심 정리

- 새로 추가하는 메서드 없이 단지 타입 정의가 목적이라면 마커 인터페이스를 선택하자.
- 적용 대상이 ElementType.TYPE인 마커 애너테이션을 작성하고 있다면, 정말 애너테이션으로 구현하는 게 옳은지, 혹은 인터페이스가 낫지는 않을지 곰곰이
생각해보자.