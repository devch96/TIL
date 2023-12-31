# 인터페이스는 구현하는 쪽을 생각해 설계하라

- 자바 8 전에는 기존 구현체를 깨뜨리지 않고는 인터페이스에 메서드를 추가할 방법이 없었다.
- 인터페이스에 메서드를 추가하면 구현체가 구현을 다시 해야하기 때문이다.
- 자바 8에 와서는 인터페이스에 디폴트 메서드를 추가하면 가능하지만 모든 클래스에서 디폴트 구현이 쓰이기 때문에 위험이 사라진 것은 아니다.
- 자바 8에서 핵심 컬렉션 인터페이스들에 람다를 활용하기 위한 다수의 디폴트 메서드가 추가 되었다.
- 자바 라이브러리의 디폴트 메서드는 코드 품질이 높고 범용 적이라 대부분의 상황에서 잘 돌아가지만 예외도 있다.

### 생각할 수 있는 모든 상황에서 불변식을 해치지 않는 디폴트 메서드를 작성하는 것은 어렵다.

- 기존에 구현한 클래스에 디폴드 메서드가 들어가면 어떠한 오류가 생길지 모른다.

### 디폴트 메서드는 컴파일에 성공하더라도 기존 구현체에 런타임 오류를 일으킬 수 있다.

- 흔한 일은 아니지만 일어나지 않으리라는 보장도 없다.
- 그렇기에 기존 인터페이스에 디폴트 메서드로 새 메서드를 추가하는 일은 꼭 필요한 경우가 아니면 피해야 한다.
- 디폴트 메서드는 인터페이스로부터 메서드를 제거하거나 기존 메서드의 시그니처를 수정하는 용도가 아님을 명심해야 한다.

### 인터페이스를 설계할 때는 여전히 세심한 주의를 기울여야 한다.

- 수많은 개발자가 그 인터페이스를 나름의 방식으로 구현할 것이니 다른 방식으로 최소한 세 가지는 구현해봐야 한다.
- 각 인터페이스의 인스턴스를 다양한 작업에 활용하는 클라이언트도 여러 개 만들어봐야 한다.
- 이런 작업들을 거치면 인터페이스를 릴리스하기 전에 결함을 찾아낼 수 있다.

### 인터페이스를 릴리스한 후에 결함을 수정하는 게 가능한 경우도 있겠지만, 절대 그 가능성에 기대서는 안된다.

## 정리

- 인터페이스에 디폴트 메서드를 추가하는 일은 어떠한 버그를 일으킬지 모른다.
- 각 구현된 메서드에서 재정의하지 않는 한 그 디폴트 메서드가 쓰이기 때문이다.
- 인터페이스는 나 이외의 다른 개발자들이 각자의 방법으로 구현할 수 있다.
- 그렇기에 조심하게 설계해야 하고 미리 결함들을 찾으려고 노력해야 한다.