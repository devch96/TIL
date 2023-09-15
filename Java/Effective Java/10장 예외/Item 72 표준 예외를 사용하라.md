# 표준 예외를 사용하라

- 코드를 재사용하는 것이 좋은 것 처럼 예외도 재사용하는 것이 좋다.
- 표준 예외를 재사용하면 API가 다른 사람이 익히고 사용하기 쉬워진다.
- 예외 클래스 수가 적을수록 메모리 사용량도 줄고 클래스를 적재하는 시간도 적게 걸린다.

### IllegalArgumentException

- 가장 많이 사용되는 예외로 호출자가 인수로 부적절한 값을 넘길 때 던지는 예외다.
  - ex) 반복 횟수를 지정하는 매개변수에 음수를 건넬 때

### IllegalStateException

- 대상 객체의 상태가 호출된 메서드를 수행하기에 적합하지 않을 때 던지는 예외다.
  - ex) 제대로 초기화되지 않은 객체를 사용하려 할 때

### 예외 구분

- 메서드가 던지는 모든 예외를 잘못된 인수나 상태라고 뭉뚱그릴 수 있겠지만, 특수한 일부는 따로 구분해 쓴다.
- null 값을 허용하지 않는 메서드에 null을 건네면 IllegalArgumentException이 아닌 NullPointerException을 던진다.
- 어떤 시퀀스의 허용 범위를 넘는 값을 건넬 때도 IllegalArgumentException 보다는 IndexOutOfBoundsException을 던진다.

### ConcurrentModificationException

- 단일 스레드에서 사용하려고 설계한 객체를 여러 스레드가 동시에 수정하려 할 때 던진다.

### UnsupportedOperationException

- 클라이언트가 요청한 동작을 대상 객체가 지원하지 않을 때 던진다.

### Exception, RuntimeException, Throwable, Error는 직접 재사용하지 말자.

- 이 클래스들은 추상 클래스라고 생각해야 한다.
- 이 예외들은 다른 예외들의 상위 클래스이므로 여러 성격의 예외들을 포괄하기에 안정적으로 테스트할 수 없다.

### IllegalStateException vs IllegalArgumentException

- 인수 값이 무엇이었든 어차피 실패했을 거라면 IllegalStateException, 그렇지 않으면 IllegalArgumentException.
