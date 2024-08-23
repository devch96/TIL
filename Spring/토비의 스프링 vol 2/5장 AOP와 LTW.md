# AOP와 LTW

------------

## 애스펙트 AOP

### 프록시 기반 AOP

#### 프록시 기반 AOP 개발 스타일의 종류와 특징

- AOP는 모듈화된 부가기능(어드바이스)과 적용 대상(포인트컷)의 조합을 통해 여러 오브젝트에 산재해서 나타나는 공통적인 기능을
손쉽게 개발하고 관리할 수 있는 기술

#### 자동 프록시 생성기와 프록시 빈

- 스프링 AOP를 사용한다면 어떤 개발 방식을 적용하든 모두 프록시 방식의 AOP
- Client와 Target이라는 두 개의 클래스가 있고, Client는 Target을 DI 받아 사용하는 관계라고 가정했을 때 프록시를 이용해서
Client와 Target의 코드에는 전혀 영향을 주지 않은 채로 Client가 Target을 이용하는 과정에서 부가기능을 제공하도록 만들려면 Client가 Target을
직접 알고 있으면 안됨

```java
public class Client {
    @Autowired
    Target target;
}
```

- DI를 적용하는 데 기술적인 문제는 없으나 Client가 이미 자신이 사용할 오브젝트의 클래스를 알고 있기 때문에 DI를 위반한다
  - DI의 원리와 목적이 무시됨
  - Client가 사용할 오브젝트를 DI 컨테이너와 설정을 통해 바꿀 수 없기 때문
  - DI 원리에 의존하고 있는 데코레이터 패턴이 적용된 프록시 방식이 적용되지 못함
- Client가 Target이라는 구체 클래스를 직접 의존하는 대신 Target이 구현하고 있는 인터페이스를 이용해 의존하도록 만들어야 함

```java
public class Client {
    @Autowired
    Interface intf;
}

public class Target implements Interface{
    ...
}

public class Proxy implements Interface {
    private Interface next;
    public void setNext(Interface next) {
        this.next = next;
    }
}
```

- DI 설정을 조작해서 Client -> Proxy -> Target 순서로 의존관계를 맺게 하면 Proxy가 Client와 Target의 호출 과정에 끼어들어서 부가기능을 제공할 수 있게 됨
- Proxy를 빈으로 등록하면 @Autowired를 통한 타입 자동와이어링을 사용할 수 없음
  - Interface를 구현한 두 개의 빈이 만들어지기 때문에 자동 빈 선택이 불가능해짐
- 위와 같이 수동으로 프록시를 빈으로 직접 등록한다면 빈 이름을 지정하거나 @Qualifier를 사용해 빈 선정조건을 더 부여해주어야 함
- 스프링은 자동 프록시 생성기를 이용해 컨테이너 초기화 중 만들어진 빈을 바꿔치기해 프록시 빈을 자동으로 등록해줌
- 자동 프록시 생성기는 프록시 빈을 별도로 추가하고 DI 설정만 바꿔주는 것이 아닌 프록시를 적용할 대상 자체를 아예 자신이 포장해서 마치 그 빈처럼 동작하게함
- 자동 프록시 생성기가 만들어주는 프록시는 새로운 빈으로 추가되는 것이 아닌 AOP 대상 타깃 빈을 대체함

#### 프록시의 종류