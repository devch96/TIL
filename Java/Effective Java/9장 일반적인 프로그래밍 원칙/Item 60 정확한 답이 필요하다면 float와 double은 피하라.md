# 정확한 답이 필요하다면 float와 double은 피하라

- float와 double 타입은 과학과 공학 계산용으로 설계되었다.
- 이진 부동소수점 연산에 쓰이며, 넓은 범위의 수를 빠르게 정밀한 `근사치`로 계산하도록 세심하게 설계되었다.

### float와 double 타입은 특히 금융 관련 계산과는 맞지 않다.

- 0.1 혹은 10의 음의 거듭제곱 수를 표현할 수 없기 때문이다.
- 계산결과가 틀리게 나오며, 반올림을 해도 틀린 답이 나올 수 있다.

### 금융 계산에는 BigDecimal, int, long을 사용해야 한다.

- BigDecimal은 속도가 느리고 쓰기 불편하다.
- BigDecimal의 대안으로 int 혹은 long 타입을 쓸 수도 있다.
- 하지만 이 경우에는 값의 크기가 제한되고, 소수점을 직접 관리해야 한다.

## 핵심 정리

- 정확한 답이 필요한 계산에는 float나 double을 피하라.
- 소수점 추적은 시스템에 맡기고, 코딩 시의 불편함이나 성능 저하를 신경 쓰지 않겠다면 BigDecimal을 사용.
- 성능이 중요하고 소수점을 직접 추적할 수 있고 숫자가 너무 크지 않다면 int나 long을 사용.
- 18자리가 넘어가면 BigDecimal을 사용해야 한다.

