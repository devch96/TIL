# 최적화는 신중히 하라

- 최적화 격언 세개
1. 맹목적인 어리석음을 포함해 그 어떤 핑계보다 효율성이라는 이름 아래 행해진 컴퓨터 죄악이 더 많다.(심지어 효율을 높이지도 못하면서) 윌리엄 울프
2. (전체의 97% 정도인) 자그마한 효율성은 모두 잊자. 섣부른 최적화가 만악의 근원이다. 도널드 크누스
3. 최적화를 할 때는 다음 두 규칙을 따르라. 첫 번째, 하지마라. 두 번째, (전문가 한정) 아직 하지 마라. 다시 말해
완전히 명백하고 최적화되지 않은 해법을 찾을 때까지는 하지 마라. M. A. 잭슨

### 빠른 프로그램보다는 좋은 프로그램을 작성하라

- 최적화는 좋은 결과보다는 해로운 결과로 이어지기 쉽고, 섣불리 진행하면 특히 더 그렇다.
- 성능 때문에 견고한 구조를 희생하지 말자.
- 좋은 프로그램이지만 원하는 성능이 나오지 않는다면 그 아키텍처 자체가 최적화할 수 있는 길을 안내해줄 것이다.
- 좋은 프로그램은 정보 은닉을 따르므로 개별 구성요소의 내부를 독립적으로 설계할 수 있기에 시스템의 나머지에 영향을 주지 않고도
각 요소를 다시 설계할 수 있다.

### 성능을 제한하는 설계를 피하라.

- 완성 후 변경하기가 가장 어려운 설계 요소는 바로 컴포넌트끼리, 혹은 외부 시스템과의 소통 방식이다.
- 이런 설계 요소들은 완성 후에는 변경하기 어렵거나 불가능할 수 있으며, 동시에 시스템 성능을 심각하게 제한할 수 있다.

### API를 설계할 때 성능에 주는 영향을 고려하라.

- public 타입을 가변으로 만들면(내부 데이터를 변경할 수 있게 만들면) 불필요한 방어적 복사를 수없이 유발할 수 있다.
- 컴포지션으로 해결할 수 있음에도 상속 방식으로 설계한 public 클래스는 상위 클래스에 영원히 종속되며 그 성능 제약까지도 물려받게 된다.
- 인터페이스도 있는데 굳이 구현 타입을 사용하는 것 역시 특정 구현체에 종속되게 하여, 나중에 더 빠른 구현체가 나오더라도 이용하지 못하게 된다.

### 각각의 최적화 시도 전후로 성능을 측정하라

- 시도한 최적화 기법이 성능을 누에 띄게 높이지 못하는 경우가 많고 심지어 더 나빠지게 할 때도 있다.

### 프로파일링 도구는 최적화 노력을 어디에 집중해야 할지 찾는 데 도움을 준다.

- 개별 메서드의 소비 시간과 호출 횟수 같은 런타임 정보를 제공하여, 집중할 곳은 물론 알고리즘을 변경해야 한다는 사실을
알려주기도 한다.

## 핵심 정리

- 빠른 프로그램을 작성하려 안달하지 말자.
- 좋은 프로그램을 작성하다 보면 성능은 따라오게 마련이다.
- API, 네트워크 프로토콜, 영구 저장용 데이터 포맷을 설계할 때는 성능을 염두에 두어야 한다.(바꾸기 어렵기 때문)
- 시스템 구현을 완료했다면 성능을 측정해보라.


