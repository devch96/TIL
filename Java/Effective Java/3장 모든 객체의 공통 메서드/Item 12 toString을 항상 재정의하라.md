# toString을 항상 재정의하라

- Object의 기본 toString 메서드는 단순히 `클래스_이름@16진수로_표시한_해시코드` 를 반환할 뿐이다.
- toString의 일반 규약에 따르면 `간결하면서 사람이 읽기 쉬운 형태의 유익한 정보` 를 반환해야 한다.
- toString을 잘 구현한 클래스는 사용하기에도 좋고 디버깅하기 쉽다.
- toString 메서드는 객체를 println, printf, 문자열 연결 연산자(+), assert 구문에 넘길 때, 혹은 디버거가 객체를 
출력할 때 자동으로 불린다.
- 예컨대 작성한 객체를 참조하는 컴포넌트가 오류 메세지를 로깅할 때 자동으로 호출할 수 있다.

## toString은 그 객체가 가진 주요 정보 모두를 반환하는게 좋다.

- 하지만 객체가 거대하거나 객체의 상태가 문자열로 표현하기에 적합하지 않다면 요약 정보를 담아야 한다.
- 이상적으로는 스스로를 완벽히 설명하는 문자열이어야 한다.

## toString을 구현할 때는 반환값의 포맷을 문서화할지 정해야 한다.

- 전화번호나 행렬 같은 값 클래스라면 문서화하기를 권한다.
- 포맷을 명시하면 그 객체는 표준적이고, 명확하고, 사람이 읽을 수 있게 된다.
- 따라서 그 값 그대로 입출력에 사용하거나 CSV 파일처럼 사람이 읽을 수 있는 데이터 객체로 저장할 수도 있다.
- 포맷을 명시하기로 했다면 명시한 포맷에 맞는 문자열과 객체를 상호 전환할 수 있는 정적 팩토리나 생성자를 함께 제공해주면 좋다.
- 포맷을 한번 명시하면 평생 그 포맷에 얽매이게 된다.

## 포맷을 명시하든 아니든 의도는 명확히 밝혀야 한다.
```java
/**
 * 이 전화번호의 문자열 표현을 반환한다.
 *  이 문자열은 "XXX-YYY-ZZZZ"형태의 12글자로 구성된다.
 *  ...
 */

/**
 * 이 약물에 관한 대략적인 설명을 반환한다.
 * 다음은 이 설명의 일반적인 형태이나,
 * 상세 형식은 정해지지 않았으며 향후 변경될 수 있다.
 * ...
 */
```

## toString이 반환한 값에 포함된 정보를 얻어올 수 있는 API를 제공하자.

- 제공하지 않으면 이 정보가 필요한 프로그래머는 toString의 반환값을 파싱할 수 밖에 없고, 성능이 나빠지며 필요하지도 않은 작업이다.
- 접근자를 제공하지 않으면 그 포맷이 사실상 준-표준 API나 다름없어진다.

## 핵심 정리

- 모든 구체 클래스에서 Object의 toString을 재정의하자.(상위 클래스에서 이미 알맞게 재정의한 경우는 예외)
- toString을 재정의한 클래스는 사용하기도 쉽고 클래스를 사용한 시스템을 디버깅하기 쉽게 해준다.
- toString은 해당 객체에 관한 명확하고 유용한 정보를 읽기 좋은 형태로 반환해야 한다.


