# null 대신 Optional 클래스

## 값이 없는 상황을 어떻게 처리할까?

- 자동차와 자동차 보험을 갖고 있는 사람 객체를 중첩 구조로 구현
```java
public class Person{
    private Car car;
    public Car getCar(){
        return car;
    }
}

public class Car{
    private Insurance insurance;
    public Insurance getInsurance(){
        return insurance;
    }
}

public class Insurance{
    private String name;
    public String getName(){
        return name;
    }
}
```
```java
public String getCarInsuranceName(Person person){
    return person.getCar().getInsurance().getName();
}
```
- 자동차를 소유하지 않은 사람도 있기 때문에 null 참조를 반환하는 방식으로 자동차를 소유하고 있지 않음을 표현하지만
런타임에 NPE가 발생하면서 프로그램 실행이 중단된다

### 보수적인 자세로 NullPointerException 줄이기

- 예기치 않은 NPE를 피하려면 필요한 곳에 다양한 null 확인 코드를 추가해야 한다
```java
public String getCarInsuraceName(Person person){
    if(person!=null){
        Car car = person.getCar();
        if(car != null){
            Insurance insurance = car.getInsurance();
            if(insurance != null){
                return insurance.getName();
            }
        }
    }
    return "Unknown";
}
```
- 상식적으로 모든 회사에는 이름이 있으므로 보험회사의 이름이 null인지는 확인하지 않았다.
- 우리가 확실히 알고 있는 영역을 모델링할 때는 이런 지식을 활용해서 null 확인을 생략할 수 있지만 데이터를 자바 클래스로
모델링할 때는 이같은 사실을 단정하기가 어렶다.
- 변수가 null인지 의심되어 중첩 if 블록을 추가하면 코드 들여쓰기 수준이 증가하고, 중첩 if 블록을 없애면 메서드의 출구가 늘어난다.
```java
public String getCarInsuranceName(Person person){
    if(person == null){
        return "Unknown";
    }
    Car car = person.getCar();
    if(car == null){
        return "Unknown";
    }
        ...
}
```
- null을 맨 처음 만든 토니 호어도 십억 달러짜리 실수라고 표현할 만큼 값이 없다는 사실을 null 로 표현하는 것은 좋은 방법이 아니다

### null 때문에 발생하는 문제

- 에러의 근원
  - NPE는 흔히 발생하는 에러
- 코드를 어지럽힘
  - 중첩된 null 확인 코드를 추가해야 함
- 아무 의미가 없음
  - null은 아무 의미도 표현하지 않음
- 자바 철학에 위배
  - 자바는 개발자로부터 모든 포인터를 숨겼는데 그 예외가 null 포인터
- 형식 시스템에 구멍을 만듬
  - null은 무형식이며 정보를 포함하고 있지 않으므로 모든 참조 형식에 null 할당 가능
  - 이런 식으로 null이 할당되기 시작하면 시스템의 다른 부분으로 null이 퍼졌을 때 무슨 의미로 사용되었는지 알 수 없음

### 다른 언어는 null 대신 무얼 사용하나?

- 그루비 같은 언어에서는 안전 내비게이션 연산자를 도입해서 null 문제를 해결함
```groovy
def carInsuranceName = person?.car?.insurance?.name
```
- 호출 체인에 null인 참조가 있으면 결과로 null이 반환됨
- 하스켈, 스칼라 등의 함수형 언어에서는 선택형 값을 저장할 수 있는 Maybe라는 형식을 제공
  - 주어진 형식의 값을 갖거나 아니면 아무 값도 갖지 않을 수 있는 Maybe
- 자바 8은 선택형 값 개념의 영향을 받아 java.util.Optional을 제공

-------------------

## Optinal 클래스 소개

- Optional은 선택형 값을 캡슐화하는 클래스
- 값이 있으면 Optional 클래스는 값을 감싸고, 값이 없으면 Optional.empty 메서드로 Optional을 반환함.
- null 참조와 Optional.empty 참조는 비슷하면서도 차이가 크다
  - null 참조는 NPE 에러가 나지만 Optional.empty는 빈 Optional(싱글턴)을 참조하기 때문에 에러가 안남
  - 또한 null은 값이 없음이 아닌 모름 의 의미이기 때문에 올바른 값인지 잘못된 값인지 모름
- Optional을 이용하면 값이 없는 상황이 우리 데이터에 문제가 있는 것인지 아니면 알고리즘의 버그인지 명확하게 구분할 수 있음.

------------------------

## Optional 적용 패턴

### Optional 객체 만들기

#### 빈 Optional

```java
Optional<Car> optCar = Optional.empty();
```

#### null이 아닌 값으로 Optional 만들기

```java
Optional<Car> optCar = Optional.of(car);
```
- car이 null이라면 즉시 NPE가 발생
- Optional을 사용하지 않았다면 car의 프로퍼티에 접근하려 할 때 에러가 발생함

#### null값으로 Optional 만들기

```java
Optional<Car> optCar = Optional.ofNullable(car);
```
- car가 null이면 빈 Optional 객체가 반환됨

### 맵으로 Optional의 값을 추출하고 변환하기

```java
String name = null;
if(insurance != null){
    name = insurance.getName();
}
```
- 이름 정보에 접근하기 전에 insurance가 null인지 확인
```java
Optional<Insurance> optInsurance = Optional.ofNullabe(insurance);
Optional<String> name = optInsurance.map(Insurance::getName);
```
- 스트림의 map 메서드와 개념적으로 비슷하다.
- Optional 객체를 최대 요소의 개수가 한 개 이하인 데이터 컬렉션으로 생각하면 편하다
- Optional이 값을 포함하면 map의 인수로 제공된 함수가 값을 바꾸고 Optional이 비어있으면 아무 일도 일어나지 안흔ㄴ다.

### flatMap으로 Optional 객체 연결

```java
Optional<Person> optPerson = Optional.of(person);
Optional<String> name = optPerson.map(Person::getCar)
        .map(Car::getInsurance)
        .map(Insurance::getName);
```
- 위 코드는 가능할 것 같지만 컴파일되지 않는다.
- optPerson의 형식은 Optional이므로 map 메서드를 호출할 수 있다. 하지만 getCar는 Optional의 객체를 반환하므로
map의 연산은 Optional<Optional<Car>> 이 되어 중첩 Optioanl 객체 구조가 된다.
- flatMap을 사용하면 스트림에서 콘텐츠만 남기기 때문에 함수를 적용해서 생성된 모든 스트림이 하나의 스트림으로 평준화된다.

#### Optional로 자동차의 보험회사 이름 찾기

```java
public String getCarInsuranceName(Optional<Person> person){
    return person.flatMap(Person::getCar)
        .flatMap(Car::getInsurance)
        .map(Insurance::getName)
        .orElse("Unknown");
}
```
- Optional을 이용해서 값이 없는 상황을 처리하면 null을 확인하느라 조건 분기문을 추가해 코드를 복잡하게 만들지 않고도
쉽게 이해할 수 있는 코드를 완성한다.
- Optional을 사용하므로 도메인 모델과 관련한 암묵적인 지식(보험회사는 무조건 이름이 있다)에 의존하지 않고 명시적으로 형식 시스템을 정의할 수 있다.
- Optional을 인수로 받거나 Optional을 반환하는 메서드를 정의한다면 이 메서드를 사용하는 모든 사람에게 이 메서드가 빈 값을 받거나 빈 결과를 반환할 수
있음을 잘 문서화해서 제공하는 것과 같다.

### 도메인 모델에 Optional을 사용했을 때 데이터를 직렬화할 수 없는 이유

- Optional 클래스의 설계자는 Optional의 용도가 선택형 반환값을 지원하는 것이라고 명확하게 못박았다.
- 필드 형식으로 사용할 것을 가정하지 않았으므로 Serializable 인터페이스를 구현하지 않는다.
- 도메인 모델에 Optional을 사용한다면 직렬화모델을 사용하는 도구나 프레임워크에서 문제가 생길 수 있다.
- 직렬화 모델에 Optional이 필요하다면 Optional로 값을 반환받을 수 있는 메서드를 추가하는 방식을 권장한다.

```java
public class Person {
    private Car car;

    public Optional<Car> getCarAsOptional() {
        return Optional.ofNullable(car);
    }
}
```

### Opional 스트림 조작

- 자바 9에서는 Optional을 포함하는 스트림을 쉽게 처리할 수 있도록 Optional에 stream() 메서드를 추가했다.
- 스트림 요소를 조작하려면 변환, 필더 등의 일련의 여러 긴 체인이 필요한데 Optional로 값이 감싸있으므로 과정이 조금 더 복잡해진다.
```java
public Set<String> getCarInsuranceNames(List<Person> persons){
    return persons.stream()
        .map(Person::getCar)
        .map(optCar -> optCar.flatMap(Car::getInsurance))
        .map(optIns -> optIns.map(Insurance::getName))
        .flatMap(Optional::stream)
        .collect(toSet());
}
```
- 빈 Optional을 제거하고 값을 언랩해야 하는 것이 문제다.
- 언랩하지 않은 Optional 객체들을 스트림으로 갖고 한번에 푸는 패턴이 더 유용하다
```java
Stream<Optional<String>> stream = ...
Set<String> result = stream.filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toSet());
```

### 디폴트 액션과 Optional 언랩

- get()
  - 값을 읽는 가장 간단한 메서드면서 동시에 가장 안전하지 않은 메서드
  - 값이 있으면 해당 값을 반환, 없으면 NoSuchElementException을 발생
  - Optional 값이 반드시 있다고 가정할 수 있는 상황이 아니면 사용하지 않아야 함
  - 중첩된 null 확인 코드를 넣는 상황과 크게 다르지 않음
- orElse(T other)
  - Optional이 값을 포함하지 않았을 때 기본값을 제공할 수 있음
- orElseGet(Supplier other)
  - orElse 메서드에 대응하는 게으른 버전의 메서드
  - Optional 값이 없을 때만 Supplier 가 실행됨.
  - 디폴트 메서드를 만드는 데 시간이 걸리거나(효율성 때문에) Optional이 비어있을 때만 기본값을 생성하고 싶다면
    (기본값이 반드시 필요한 상황) 사용해야 함
- orElseThrow(Supplier exceptionSupplier)
  - Optional이 비어있을 때 예외를 발생시킨다는 점에서 get과 비슷하나 발생시킬 예외의 종류를 선택할 수 있음.
- ifPresent(Consumer consumer)
  - 값이 존재할 때 인수로 넘겨준 동작을 실행할 수 있음.
  - 값이 없으면 아무 일도 일어나지 않음
- ifPresentOrElse(Consumer action, Runnable emptyAction)
  - Runnable을 인수로 받는다는 점만 ifPresent와 다름

### 두 Optional 합치기

- Optional 클래스는 Optional이 값을 포함하는지 여부를 알려주는 isPresent라는 메서드도 제공
```java
public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car){
    if(person.isPresent() && car.isPresent()){
        return Optional.of(findCheapestInsurance(person.get(), car.get()));
    }else{
        return Optional.empty();
    }
}
```
- 이 구현 코드는 null 확인 코드와 크게 다를바 없다.
```java
public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car){
    return person.flatMap(p -> car.map(c -> findCheapestInsurance(p,c)));
}
```

### 필터로 특정값 거르기

```java
Insurance insurance = ...;
if(insurance != null & "CambridgeInsurance".equals(insurange.getName())){
        ...
}

Optional<Insurance> optInsurance = ...;
optInsurance.filter(insurance -> "CambridgeInsurance".equals(insurance.getName()))
        .ifPresent( ... )
```
- filter는 프레디케이트를 인수로 받는다.
- Optional 객체가 값을 가지며 프레디케이트와 일치하면 filter 메서드는 그 값을 반환하고 그렇지 않으면 빈 Optional 객체를 반환한다.

### Optional 클래스의 메서드

- empty
  - 빈 Optional 인스턴스 반환
- filter
  - 값이 존재하며 프레디케이트와 일치하면 값을 포함하는 Optional 반환, 값이 없거나 프레디케이트와 일치하지 않으면
  빈 Optional 반환
- flatMap
  - 값이 존재하면 인수로 제공된 함수를 적용한 결과 Optional 반환, 값이 없으면 빈 Optional 반환
- get
  - 값이 존재하면 값을 반환, 없으면 NoSuchElementException 발생
- ifPresent
  - 값이 존재하면 지정된 Consumer 실행, 없으면 아무 일도 일어나지 않음
- isPresentOrElse
  - 값이 존재하면 지정된 Consumer 실행, 없으면 아무 일도 일어나지 않음
- isPresent
  - 값이 존재하면 true, 없으면 false
- map
  - 값이 존재하면 제공된 매핑 함수 적용
- of
  - 값이 존재하면 값을 감싸는 Optional, 없으면 NPE
- ofNullable
  - 값이 존재하면 값을 감싸는 Optional, null 이면 빈 Optional 반환
- or
  - 값이 존재하면 같은 Optional, 없으면 Supplier에서 만든 Optional 반환
- orElse
  - 값이 존재하면 값, 없으면 기본값 반환
- orElseGet
  - 값이 존재하면 값, 없으면 Supplier에서 제공하는 값 반환
- orElseThrow
  - 값이 존재하면 값, 없으면 Supplier에서 생성한 예외
- stream
  - 값이 존재하면 존재하는 값만 포함하는 스트림, 없으면 빈 스트림 반환

--------------------

## Optional을 사용한 실용 예제

### 잠재적으로 null이 될 수 있는 대상을 Optional로 감싸기

- Map의 get 메서드는 요청한 키에 대응하는 값을 찾지 못했을 떄 null을 반환한다.
- null 보다는 Optional을 반환하는 것이 더 좋다.
- get의 시그니처는 고칠 수 없지만, 반환하는 값을 Optional로 감쌀수는 있다.
```java
Object value = map.get("key");

Optional<Object> value = Optional.ofNullable(map.get("key"));
```

### 예외와 Optional 클래스

- null을 반환하는 대신 예외를 발생시킬 때도 있다.
- Integer.parseInt(String)은 문자열을 정수로 바꾸지 못할 때 NumberFormatException을 발생시킨 다.
```java
public static Optional<Integer> stringToInt(String s){
    try{
        return Optional.of(Integer.parseInt(s));
    }catch(NumberFormatException e){
        return Optional.empty();
    }
}
```

### 기본형 Optional을 사용하지 말아야 하는 이유

- 스트림처럼 Optional도 OptionalInt, OptionalLong, OptionalDouble등의 클래스를 제공한다.
- 스트림에서는 오토박싱/언박싱을 안하기에 기본형을 사용했을 때 성능 향상이 있지만 Optional은 최대 요소 수가 한개이므로 기본형 특화 클래스로 성능 개선이 불가하다.
- 또한 map, flatMap, filter 등을 지원하지 않는다

----------------

## 마치며

- 자바 8에서는 값이 있거나 없음을 표현할 수 있는 클래스 Optional을 제공한다.
- 팩터리 메서드 Optional.empty, Optional.of, Optional.ofNullable 등을 이용해서 Optional 객체를 만들 수 있다.
- Optional 클래스는 스트림과 비슷한 연산을 수행하는 map, flatMap, filter 등의 메서드를 제공한다.
- Optional로 값이 없는 상황을 적절하게 처리하도록 강제할 수 있다.
