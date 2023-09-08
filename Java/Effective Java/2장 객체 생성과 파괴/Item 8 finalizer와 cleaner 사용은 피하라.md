# finalizer와 cleaner 사용은 피하라

- 자바는 두가지 객체 소멸자를 제공한다.
- finalizer는 예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요하다.
- 자바 9에서는 finalizer를 deprecated로 하고 cleaner를 대안으로 소개했다.
- cleaner는 finalizer보다는 덜 위험하지만, 여전히 예측할 수 없고, 느리고, 일반적으로 불필요하다.

### finalizer와 cleaner는 즉시 수행된다는 보장이 없다.

- 객체에 접근 할 수 없게 된 후 finalizer나 cleaner가 실행되기까지 얼마나 걸릴지 알 수 없다.
- finalizer와 cleaner로는 제때 실행되어야 하는 작업을 절대 할 수 없다.
- 파일 닫기를 finalizer나 cleaner에 맡기면 실행을 게을리해서 파일을 계속 열어둔다면 프로그램이 실패할 수 있다.
- finalizer나 cleaner를 얼마나 신속히 수행할지는 전적으로 가비지 컬렉터 알고리즘에 달려있다.
- 자바 언어 명세는 어떤 스레드가 finalizer를 수행할지 명시하지 않으니 이 문제를 예방할 보편적인 해법은 finalizer를 사용하지 않는 방법 뿐이다.
- clearner는 자신을 수행할 스레드를 제어할 수 있다는 면에서 조금 낫지만 여전히 백그라운드에서 수행되며 가비지 컬렉터의 통제하에 있으니
즉각 수행되리라는 보장이 없다.

### 상태를 영구적으로 수정하는 작업에서는 절대 finalizer나 cleaner에 의존해서는 안된다.

- 자바 언어 명세는 finalizer나 cleaner의 수행 시점뿐 아니라 수행 여부조차 보장하지 않는다.
- 종료 작업을 전혀 수행하지 못한 채 프로그램이 중단될 수도 있다는 얘기다.
- 프로그램 생애주기와 상관ㅇ없는 데이터베이스 같ㅇ츤 공유 자원의 영구 락 해제를 finalizer나 cleaner에 맡겨 놓으면
분산 시스템 전체가 서서히 멈출 것이다.
- System.gc나 System.runFinalization은 finalizer나 cleaner가 실행될 가능성을 높여줄 수 있으나 보장해주진 않는다.
- 그리고 이 메서드들은 ThreadStop이라는 심각한 결함때문에 수십 년간 지탄받아 왔다.

### finalizer와 cleaner는 심각한 성능 문제도 동반한다.

- finalizer가 가비지 컬렉터의 효율을 떨어뜨리기 때문이다.
- cleaner도 클래스의 모든 인스턴스를 수거하는 형태로 사용하면 성능은 finalizer와 비슷하지만 안전망 형태로만 사용하면 훨씬 빨라진다.

### finalizer를 사용한 클래스는 finalizer 공격에 노출되어 심각한 보안 문제를 일으킬 수도 있다.

- 생성자나 직렬화 과정에서 예외가 발생하면, 이 생성되다 만 객체에서 악의적인 하위 클래스의 finalizer가 수행될 수 있게 된다.
- 이 finalizer는 정적 필드에 자신의 참조를 할당하여 가비지 컬렉터가 수집하지 못하게 막을 수 있다.
- 객체 생성을 막으려면 생성자에서 예외를 던지는 것만으로 충분하지만, finalizer가 있다면 그렇지 않다.
- final 이 아닌 클래스를 finalizer 공격으로부터 방어하려면 아무 일도 하지 않는 finalize 메서드를 만들고 final로 선언하자.

### finalizer와 cleaner의 사용처

- 자원의 소유자가 close 메서드를 호출하지 않는 것에 대비한 안전망 역할.
- FileInputStream, FileOutputStream, ThreadPoolExecutor에서 안전망 역할의 finalizer를 제공한다.
- 네이티브 피어(native peer)와 연결된 객체에서도 활용된다.
- 네이티브 피어란 일반 자바 객체가 네이티브 메서드를 통해 기능을 위임한 네이티브 객체를 말한다.
- 네이티브 피어는 자바 객체가 아니니 가비지 컬렉터는 그 존재를 알지 못한다.

## 핵심 정리

- cleaner(자바 8까지는 finalizer)는 안전망 역할이나 중요하지 않은 네이티브 자원 회수용으로만 사용하자.
- 이런 경우라도 불확실성과 성능 저하에 주의해야 한다.