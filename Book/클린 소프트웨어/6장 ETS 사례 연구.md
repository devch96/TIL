# ETS 사례 연구

--------------

## 비지터 패턴

- 문제: 클래스 계층 구조에 새로운 메소드를 추가할 필요가 있지만, 그렇게 하는 작업은 고통스럽거나 설계를 해치게 된다.
  - 계층 구조에 새로운 메서드를 추가하면 변화에 대해 닫지 못하게 됨

### 디자인 패턴의 비지터 집합

- 비지터 집합에 속한 패턴은 기존 계층 구조를 수정하지 않고도 새로운 메서드를 계층 구조에 추가할 수 있게 해줌
  - 비지터
  - 비순환 비지터
  - 데코레이터
  - 확장 객체

### 비지터

```java
public interface Modem {
    void dial(String pno);
    void hangup();
    void send(char c);
    char recv();
    // 추가
    void accept(ModemVisitor v);
}

public interface ModemVisitor {
    void visit(HayesModem modem);
    void visit(ZoomModem modem);
    void visit(ErnieModem modem);
}

public class UnixModemConfigurator implements ModemVisitor {
    public void visit(HayesModem m) {
        m.configurationString = "~~";
    }
    public void visit(ZoomModem m) {
        m.configurationValue = 42;
    }
    public void visit(ErnieModem m) {
        m.internalPattern = "~~";
    }
}
```

- 이렇게 구조를 만들면 ModemVisitor에 새로운 파생형을 추가하는 방법으로 Modem 계층 구조를 전혀 건드리지 않을 수 있다.
- 비지터 패턴에서는 계층 구조에 메서드를 만드는 대신 비지터 파생형을 만드는 것이다.
  - 이중 디스패티라고 불린다
  - 다형성을 이용해서 어떤 메서드 본체를 부를지 결정 하는 작업(dispatch)를 두 번 수행하기 때문
    - accept, visit

### 비순환 비지터 패턴

- 비지터 패턴은 방문 대상인 계층 구조의 기반 클래스가 비지터 계층 구조의 기반 클래스에 의존한다.
- 방문 대상인 계층 구조의 모든 파생형마다 비지터 계층 구조의 기반 클래스에 함수가 하나씩 있다.
- 따라서 방문 대상인 계층 구조의 모든 파생형이 모두 의존 관계 순환에 빠지게 된다.
  - 비지터 구조를 점진적으로 컴파일하거나 방문 대상인 계층 구조에 새로운 파생형을 추가하기가 매우 어려워짐
- 비지터는 변경해야 할 계층 질서에 새로운 파생형을 자주 추가할 필요가 없는 프로그램에 효과적
- 새로운 파생형을 많이 만들어야 하거나 방문 대상인 계층 구조가 매우 변경되기 쉽다면 비순환 비지터를 사용해야 함
- 비순환 비지터는 Visitor 기반 클래스를 퇴화시키는 방법으로 의존 관계 순환을 깸

```java
public interface Modem {
    void dial(String pno);
    void hangup();
    void send(char c);
    char recv();
    void accept(ModemVisitor v);
}

public interface ModemVisitor {
}

public interface ErnieModemVisitor {
    void visit(ErnieModem m);
}
public interface HayesModemVisitor {
    void visit(HayesModem m);
}
public interface ZoomModemVisitor {
    void visit(ZoomModem m);
}
```
- 이렇게 하면 의존 관계 순환이 깨지고 새로운 방문 대상 파생형을 추가하거나 점진적 컴파일을 하기 쉬워지나 전보다 훨씬 복잡해진다는
단점이 있음.
- 형변환의 시점도 방문 대상 계층 구조의 너비에 따라 좌우될지도 모르므로 특정 시점을 잡기 어려움

#### 보고서 생성 프로그램에 비지터 사용하기

- 커다란 자료 구조 내부를 방문하면서 보고서를 생성하기 위한 용도로 비지터 패턴을 사용하는 일은 매우 흔한데, 자료 구조와 보고서 생성 코드를
분리할 수 있기 때문이다.
  - 새로운 보고서를 추가하려면 자료 구조의 코드를 고치는 대신 새로운 비지터를 추가하기만 하면 된다.
  - 여러 보고서들을 각각 별개의 컴포넌트에 놓을 수 있으므로 특정 보고서를 그 보고서가 필요한 고객에게만 독립적으로 배포할 수 있음

#### 비지터의 다른 용도

- 자료 구조를 여러 가지 방법으로 해석할 필요가 있는 애플리케이션이라면 언제나 Visitor 패턴을 사용ㅇ할 수 있음
- 환경 설정 자료 구조를 사용하는 경우도 사용할 수 있음
- 비지터를 사용하면 방문 대상인 자료 구조 자체와 그 자료 구조가 사용되는 용도가 독립적이게 되기 때문에 기존 자료 구조를 재컴파일하거나 재배치하지 않고도
이미 자료 구조가 설치된 곳에 새로운 비지터를 만들어 배치하거나 기존 비지터를 변경한 다음 재배치할 수 있음

### 데코레이터 패턴

- 비지터와 마찬가지로 기존 계층 구조를 바꾸지 않고도 메서드를 추가할 수 있다.

```java
public interface Modem {
    void dial(String pno);
    void setSpeakerVolume(int volume);
    String getPhoneNumber();
    int getSpeakerVolume();
}

public class HayesModem implements Modem {
    private String itsPhoneNumber;
    private int itsSpeakerVolume;
    
    public void dial(String pno) {
        itsPhoneNumber = pno;
    }
    public void setSpeakerVolume(int volume) {
        itsSpeakerVolume = volume;
    }
    public String getPhoneNumber() {
        return itsPhoneNumber;
    }
    public int getSpeakerVolume() {
        return itsSpeakerVolume;
    }
}

public class LoudDialModem implements Modem {
    private Modem itsModem;
    
    public LoudDialModem(Modem m) {
        itsModem = m;
    }
    public void dial(String pno){
        itsModem.setSpeakerVolume(10);
        itsModem.dial(pno);
    }
    public void setSpeakerVolume(int volume) {
        itsModem.setSpeakerVolume(volume);
    }
    public String getPhoneNumber() {
        return itsModem.getPhoneNumber();
    } 
    public int getSpeakerVolume() {
        return itsModem.getPhoneNumber();
    }
}
```

#### 다중 데코레이터

- 동일한 계층 구조에 데코레이터가 2개 이상 있는 경우도 있음
- 기본 데코레이터를 만들고, 그 데코레이터를 상속받아 자신이 필요한 메서드들만 재정의하면 됨

### 확장 객체 패턴

- 다른 방법보다 더 복잡하지만 훨씬 더 강력하고 유연하다.
- 계층 구조에 들어 있는 객체마다 특별한 확장 객체의 리스트를 유지하고 확장 객체를 이름으로 찾을 수 있는 메서드도 하나 제공한다.
- 확장 객체는 계층 구조에 속한 원래 객체를 조작할 수 있는 메서드들을 제공한다.

### 결론

- 비지터 패턴 집합은 어떤 클래스 계층 구조에 들어 있는 클래스들을 고치지 않고도 그들의 행위를 변경할 수 있는 여러 가지 방법을 제공한다.
  - OCP를 지키는 일에 도움이 됨
- 비지터 패턴들은 유횩적이여서 남용하기 쉽다.
- 패턴들이 도움이 된다면 사용하되 이들의 필요성에 대해 합리적인 회의주의적 태도를 계속 유지해야 한다.
  - 비지터로도 해결할 수 있지만 더 간단하게 해결할 수 있는 경우도 많음

--------------------

## 스테이트 패턴

- 유한 상태 오토마타는 복잡한 시스템의 행위를 조사하거나 정의할 수 있는 간결하면서도 명쾌한 방법을 제공

### 스테이트 패턴

- 스테이트 패턴은 중첩된 switch/case 문의 효율성과 상태 테이블을 해석하는 기법의 유연성을 결합한 패턴

```java
public interface TurnStileState {
    void coin(TurnStile t);
    void pass(TurnStile t);
}

public class LockedTurnstileState implements TurnStileState {
    public void coin(Turnstile t) {
        t.setUnlocked;
        t.unlock();
    }
    public void pass(Turnstile t){
        t.alarm();
    }
}

public class UnlockedturnstileState implements TurnstileState {
    public void coin(TurnStile t){
        t.thankyou();
    }
    public void pass(Turnstile t){
        t.setLocked();
        t.lock();
    }
}

public class Turnstile {
    private static TurnStileState lockedState = new LockedTurnstileState();
    private static TurnStileState unlockedState = new UnlockedturnstileState();
    
    private TurnstileController turnstileController;
    private TurnStileState state = lockedState;
    
    public Turnstile(TurnstileController action) {
        turnstileController = action;
    }
    
    public void coin() {
        state.coin(this);
    }
    public void pass() {
        state.pass(this);
    }
    public void setLocked() {
        state = lockedState;
    }
    public void setUnlocked() {
        state = unlockedState;
    }
    public boolean isLocked() {
        return state == lockedState;
    }
    public boolean isUnlocked() {
        return state == unlockedState;
    }
    void thankyou() {
        turnstileController.thankyou();
    }
    ...
}
```
- TurnstileState는 변수가 없으므로 인스턴스를 하나 이상 만들 필요가 없기 때문에 정적 변수에 담음
- 스테이트와 스트래터지
  - 스트래터지와 비슷함
    - 모두 컨텍스트 클래스가 있으며
    - 파생형이 여러 개 있는 다형적인 기반 클래스에 위임
  - 스테이트는 파생형이 컨텍스트 클래스에 대한 참조를 갖고 있다는 점이 차이점
    - 이 참조를 토앻 컨텍스트 클래스의 어떤 메서드를 부를지 선택해서 호출
  - 스테이트 패턴의 모든 적용 사례는 스트래터지 패턴이라고 볼 수 있지만, 스트래터지 패턴의 적용 사례가 모두 스테이트 패턴인 것은 아님
- 스테이트 패턴의 비용과 장점
  - 논리와 행동을 매우 분명히 분리하게 해줌
    - 행동은 Context 클래스에서 구현, 논리는 State 클래스의 파생형들 사이에 분산
  - 비용은 갑절이 듬
    - State 파생형 작성 작업 어려움
      - 상태가 20개면?
    - 논리가 분산됨
      - 상태 기계의 논리를 모두 볼 수 있는 장소가 없음
