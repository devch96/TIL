# API 리팩터링

-------------

## 질의 함수와 변경 함수 분리하기

```javascript
function getTotalOutstandingAndSendBill(){
    const result = customer.invoices.reduce((total, each) => each.amount + total, 0);
    sendBill();
    return reuslt;
}

// 리팩터링
function totalOutstanding(){
    return customer.invoices.reduce((total, each) => each.amount + total, 0);
}

function sendBill(){
    emailGateway.send(formatBill(customer));
}
```

### 배경

- 우리는 외부에서 관찰할 수 있는 겉보기 부수효과가 전혀 없이 값을 반환해주는 함수를 추구해야 한다.
  - 이런 함수는 어느 때건 원하는 만큼 호출해도 아무 문제가 없다.
- 겉보기 부수효과가 있는 함수와 없는 함수는 명확히 구분하는 것이 좋은데 이를 위한 한 가지 방법은 질의 함수(읽기 함수)는 모두
부수효과가 없어야 한다라는 규칙을 따르는 것이다.
  - 이를 명령-질의 분리(command-query separation)라 한다.

### 절차

1. 대상 함수를 복제하고 질의 목적에 충실한 이름을 짓는다.
2. 새 질의 함수에서 부수효과를 모두 제거한다.
3. 정적 검사를 수행한다.
4. 원래 함수를 호출하는 곳을 모두 찾아내 호출하는 곳에서 반환 값을 사용한다면 질의 함수를 호출하도록 바꾸고, 원래 함수를 호출하는 코드를
바로 아래 줄에 새로 추가한다. 하나 수정할 때마다 테스트한다.
5. 원래 함수에서 질의 관련 코드를 제거한다.
6. 테스트한다.

------------

## 함수 매개변수화하기

```javascript
function tenPercentRaise(aPerson){
    aPerson.salary = aPerson.salary.multiply(1.1);
}
function fivePercentRaise(aPerson){
    aPerson.salary = aPerson.salary.multiply(1.05);
}

// 리팩터링
function raise(aPerson, factor){
    aPerson.salary = aPerson.salary.multiply(1 + factor);
}
```

### 배경

- 두 함수의 로직이 아주 비슷하고 단지 리터럴 값만 다르다면, 그 다른 값만 매개변수로 받아 처리하는 함수 하나로 합쳐서 중복을 없앨 수 있다.
- 매개변수 값만 바꿔서 여러 곳에서 쓸 수 있으니 함수의 유용성이 커진다.

### 절차

1. 비슷한 함수 중 하나를 선택한다.
2. 함수 선언 바꾸기로 리터럴들을 매개변수로 추가한다.
3. 이 함수를 호출하는 곳 모두에 적절한 리터럴 값을 추가한다.
4. 테스트한다.
5. 매개변수로 받은 값을 사용하도록 함수 본문을 수정한다. 하나 수정할 때마다 테스트한다.
6. 비슷한 다른 함수를 호출하는 코드를 찾아 매개변수화된 함수를 호출하도록 하나씩 수정한다. 하나 수정할 때마다 테스트한다.

-------------

## 플래그 인수 제거하기

```javascript
function setDimension(name, value){
    if (name === "height"){
        this._height = value;
        return;
    }
    if (name === "width"){
        this._width =- value;
        return;
    }
}

// 리팩터링
function setHeight(value) {this._height = value;}
function setWidth(value) {this._width = value;}
```

### 배경

- 플래그 인수란 호출되는 함수가 실행할 로직을 호출하는 쪽에서 선택하기 위해 전달하는 인수다.
- 플래그 인수가 있으면 호출할 수 있는 함수들이 무엇이고 어떻게 호출해야 하는지를 이해하기가 어려워진다.
  - 플래그 인수가 있으면 함수들의 기능 차이가 잘 드러나지 않는다.

### 절차

1. 매개변수로 주어질 수 있는 값 각각에 대응하는 명시적 함수들을 생성한다
2. 원래 함수를 호출하는 코드들을 모두 찾아서 각 리터럴 값에 대응되는 명시적 함수를 호출하도록 수정한다.

---------------------

## 객체 통째로 넘기기

```javascript
const low = aRoom.daysTempRange.low;
const high = aRoom.daysTempRange.high;
if (aPlan.withinRange(low,high))

// 리팩터링
if (aPlan.withInRange(aRoom.daysTempRange))
```

### 배경

- 하나의 레코드에서 값 두어 개를 가져와 인수로 넘기는 코드를 보면 값 대신 레코드를 통째로 넘기고 함수 본문에서
필요한 값들을 꺼내 쓰도록 수정한다.
- 레코드를 통째로 넘기면 변화에 대응하기 쉽다.
  - 그 함수가 더 다양한 데이터를 사용하도록 바뀌어도 매개변수 목록은 수정할 필요가 없다.
  - 매개변수 목록이 짧아져서 일반적으로 함수 사용법을 이해하기 쉬워진다.
- 함수가 레코드 자체에 의존하기를 원치 않을 때는 이 리팩터링을 수행하지 않는다.
  - 레코드와 함수가 서로 다른 모듈에 속한 상황이면 특히 그렇다.
- 어떤 객체로부터 값 몇 개를 얻은 후 그 값들만으로 무언가를 하는 로직이 있다면, 그 로직을 객체 안으로 집어넣어야 함을 알려주는
악취로 봐야 한다.
- 다른 객체의 메서드를 호출하면서 호출하는 객체 자신이 가지고 있는 데이터 여러 개를 건네는 경우, 데이터 여러 개 대신 객체 자신의 참조
만 건네도록 수정할 수 있다(this)

### 절차

1. 매개변수들을 원하는 형태로 받는 빈 함수를 만든다.
2. 새 함수의 본문에서는 원래 함수를 호출하도록 하며, 새 매개변수와 원래 함수의 매개변수를 매핑한다.
3. 정적 검사를 수행한다.
4. 모든 호출자가 새 함수를 사용하게 수정한다. 하나씩 수정하며 테스트하자.
5. 호출자를 모두 수정했다면 원래 함수를 인라인한다.
6. 새 함수의 이름을 적절히 수정하고 모든 호출자에 반영한다.

-----------------

## 매개변수를 질의 함수로 바꾸기

```javascript
availableVacation(anEmployee, anEmployee.grade);

function availableVacation(anEmployee, grade){}

// 리팩터링
availableVacation(anEmployee)
function availableVacation(anEmployee){
    const grade = anEmployee.grade;
}
```

### 배경

- 매개변수 목록은 함수의 변동 요인을 모아놓은 곳이다.
- 피호출 함수가 스스로 '쉽게' 결정할 수 있는 값을 매개변수로 건네는 것도 일종의 중복이다.
- 해당 매개변수를 제거하면 값을 결정하는 책임 주체가 달라진다.
  - 매개변수가 있다면 결정 주체가 호출자
  - 매개변수가 없다면 피호출 함수
- 피호출 함수가 그 역할을 수행하기에 적합할 때만 책임 소재를 피호출 함수로 옮긴다.
- 매개변수를 제거하면 피호출 함수에 원치 않는 의존성이 생길때는 질의 함수로 바꾸지 말아야 한다.
  - 해당 함수가 알지 못했으면 하는 프로그램 요소에 접근해야 하는 상황

### 절차

1. 필요하다면 대상 매개변수의 값을 계산하는 코드를 별도 함수로 추출해놓는다.
2. 함수 본문에서 대상 매개변수로의 참조를 모두 찾아서 그 매개변수의 값을 만들어주는 표현식을 참조하도록 바꾼다. 하나 수정할 때마다 테스트한다.
3. 함수 선언 바꾸기로 대상 매개변수를 없앤다.

----------------

## 질의 함수를 매개변수로 바꾸기

```javascript
targetTemperature(aPlan)

function targetTemperature(aPlan){
    currentTemperature = thermostat.currentTemperature;
}

// 리팩터링

targetTemperature(aPlan, thermostat.currentTemperature)

function targetTemperature(aPlan, thermostat.currentTemperature){
    ...
}
```

### 배경

- 함수 안에 전역 변수를 참조한다거나, 제거하길 원하는 원소를 참조하는 경우 해당 참조를 매개변수로 바꿔
해결할 수 있다.
- 참조를 풀어내는 책임을 호출자로 옮기는 것이다.

### 절차

1. 변수 추출하기로 질의 코드를 함수 본문의 나머지 코드와 분리한다.
2. 함수 본문 중 해당 질의를 호출하지 않는 코드들을 별도 함수로 추출한다.
3. 방금 만든 변수를 인라인하여 제거한다.
4. 원래 함수도 인라인한다.
5. 새 함수의 이름을 원래 함수의 이름으로 고쳐준다.

---------------------

## 세터 제거하기

```javascript
class Person{
    get name() {...}
    set name(aString) {...}
}

// 리팩터링
class Person{
    get name() {...}
}
```

### 배경

- 세터 제거하기 리팩터링이 필요한 상황은 주로 두 가지다.
  - 사람들이 무조건 접근자 메서드를 통해서만 필드를 다루려 할 때.
    - 생성자 안에서도
    - 생성자에서만 호출하는 세터가 생겨나곤 함
  - 클라이언트에서 생성 스크립트를 사용해 객체를 생성할 때
    - 생성 스크립트란 생성자를 호춣란 후 일련의 세터를 호출하여 객체를 완성하는 형태의 코드

### 절차

1. 설정해야 할 값을 생성자에서 받지 않는다면 그 값을 받을 매개변수를 생성자에 추가한다. 그런 다음 생성자 안에서 적절한 세터를 호출한다.
2. 생성자 밖에서 세터를 호출하는 곳을 찾아 제거하고, 대신 새로우 ㄴ생성자를 사용하도록 한다. 하나 수정할 때마다 테스트한다.
3. 세터 메서드를 인라인한다. 가능하다면 해당 필드를 불변으로 만든다.
4. 테스트한다.

---------------

## 생성자를 팩터리 함수로 바꾸기

```javascript
leadEnginner = new Employee(document.leadEngineer, 'E');

// 리팩터링
leadEnginner = createEngineer(document.leadEnginner);
```

### 배경

- 팩터리 함수에는 생성자에 있는 제약이 없다.
  - 자바 생성자는 반드시 그 생성자를 정의한 클래스의 인스턴스를 반환해야 한다.
  - 서브클래스의 인스턴스나 프록시를 반환할 수 없다.
  - 생성자의 이름도 고정된다.

### 절차

1. 팩터리 함수를 만든다. 팩터리 함수의 본문에서는 원래의 생성자를 호출한다.
2. 생성자를 호출하던 코드를 팩터리 함수 호출로 바꾼다.
3. 하나씩 수정할 때마다 테스트한다.
4. 생성자의 가시 범위가 최소가 되도록 제한한다.

---------------

## 함수를 명령으로 바꾸기

```javascript
function score(candidate, medicalExam, scoringGuide){
    let result = 0;
    let healthLevel = 0;
    ...
}

// 리팩터링
class Scorer{
    constructor(candidate, medicalExam, scoringGuide){
        this._candidate = candidate;
        this._medicalExam = medicalExam;
        this._scoringGuide = scoringGuide;
    }
    
    execute(){
        this._result = 0;
        this._healthLevel = 0;
        ...
    }
}
```

### 배경

- 함수를 그 함수만으로 윟나 객체 안으로 캡슐화하면 더 유용해지는 상황이 있다.
- 이런 객체를 가리켜 명령 객체 혹은 단순히 명령이라 한다.
- 명령 객체 대부분은 메서드 하나로 구성되며, 이 메서드를 요청해 실행하는 것이 이 객체의 목적이다.

### 절차

1. 대상 함수의 기능을 옮길 빈 클래스를 만든다. 클래스 이름은 함수 이름에 기초해 짓는다.
2. 방금 생성한 빈 클래스로 함수를 옮긴다.
3. 함수의 인수들 각각은 명령의 필드로 만들어 생성자를 통해 설정할지 고민해본다.

----------

## 명령을 함수로 바꾸기

```javascript
class ChageCalculator{
    constructor(customer, usage){
        this._customer = customer;
        this._usage = usage;
    }
    execute(){
        return this._customer.rate * this._usage;
    }
}

// 리팩터링
function charge(customer, usage){
    return customer.rate * usage;
}
```

### 배경

- 명령 객체는 복잡한 연산을 다룰 수 있는 강력한 메커니즘을 제공하지만 공짜가 아니다.
- 그저 함수를 하나 호출해 정해진 일을 수행하는 용도이고, 로직이 크게 복잡하지 않다면 명령 객체는 장ㅈ점보다 단점이 크니 평범한 함수로
바꿔주는 게 낫다.

### 절차

1. 명령을 생성하는 코드와 명령의 실행 메서드를 호출하는 코드를 함께 함수로 추출한다.
2. 명령의 실행 함수가 호출하는 보조 메서드들 각각을 인라인한다.
3. 함수 선언 바꾸기를 적용하여 생성자의 매개변수 모두를 명령의 실행 메서드로 옮긴다.
4. 명령의 실행 메서드에서 참조하는 필드듣ㄹ 대신 대응하는 매개변수를 사용하게끔 바꾼다. 하나씩 수정할 때마다 테스트한다.
5. 생성자 호출과 명령의 실행 메서드 호출을 호출자안으로 인라인한다.
6. 테스트한다.
7. 죽은 코드 제거하기로 명령 클래스를 없앤다.

--------------

## 수정된 값 반환하기

```javascript
let totalAscent = 0;
calculateAscent();

function calculateAscent(){
    for (let i = 1; i < points.length; i++) {
        const verticalChange = points[i].elevation - points[i-1].elevation;
        totalAscent += (verticalChange > 0) ? verticalChange : 0;
    }
}

// 리팩터링
const totalAscent = calculateAscent();

function calculateAscent(){
    let result;
    for (let i = 1; i < points.length; i++) {
        const verticalChange = points[i].elevation - points[i-1].elevation;
        result += (verticalChange > 0) ? verticalChange : 0;
    }
    return result;
}
```

### 배경

- 데이터가 어떻게 수정되는지를 추적하는 일은 코드에서 이해하기 가장 어려운 부분 중 하나다.
- 데이터가 수정된다면 그 사실을 명확히 알려주어서 어느 함수가 무슨 일을 하는지 쉽게 알 수 있게 하는 일이 대단히 중요하다.
- 이 리팩터링은 값 하나를 계산한다는 분명한 목적이 있는 함수들에 가장 효과적이고, 반대로 값 여러 개를 갱신하는 함수에는 효과적이지 않다.

### 절차

1. 함수가 수정된 값을 반환하게 하여 호출자가 그 값을 자신의 변수에 저장하게 한다.
2. 테스트한다.
3. 피호출 함수 안에 반환할 값을 가리키는 새로운 변수를 선언한다.
4. 테스트한다.
5. 계산이 선언과 동시에 이뤄지도록 통합한다.
6. 테스트한다.
7. 피호출 함수의 변수 이름을 새 역할에 어울리도록 바꿔준다.
8. 테스트한다.

-------------

## 오류 코드를 예외로 바꾸기

```javascript
if(data)
    return new ShippingRules(data);
else
    return -23;

// 리팩터링
if(data)
    return new ShippingRules(data);
else
    throw new OrderProcessingError(-23);
```

### 배경

- 예외는 프로그래밍 언어에서 제공하는 독립적인 오류 처리 메커니즘이다. 오류가 발견되면 예외를 던진다.
- 예외는 정교한 메커니즘이지만 대다수의 다른 정교한 메커니즘과 같이 정확하게 사용할 때만 최고의 효과를 낸다.
- 예외는 정확히 예상 밖의 동작일 때만 쓰여야 한다.

### 절차

1. 콜스택 상위에 해당 예외를 처리할 예외 핸들러를 작성한다.
2. 테스트한다.
3. 해당 오류 코드를 대체할 예외와 그 밖의 예외를 구분할 식별 방법을 찾는다.
4. 정적 검사를 수행한다.
5. catch절을 수정하여 직접 처리할 수 있는 예외는 적절히 대처하고 그렇지 않은 예외는 다시 던진다.
6. 테스트한다
7. 오류 코드를 반환하는 곳 모두에서 예외를 던지도록 수정한다. 하나씩 수정할 때마다 테스트한다.
8. 모두 수정했다면 그 오류 코드를 콜스택 위로 전달하는 코드를 모두 제거한다. 하나씩 수정할 때마다 테스트한다.

--------------

## 예외를 사전확인으로 바꾸기

```javascript
double getValueForPeriod (int periodNumber){
    try{
        return values[periodNumber];
    }catch (ArrayIndexOutOfBoundsException e){
        return 0;
    }
}

// 리팩터링
double getValueForPeriod (int periodNumber){
    return (periodNumber >= values.length) ? 0 : values[periodNumber];
}
```

### 배경

- 예외는 듰밖의 오류라는 말 그대로 예외적으로 동작할 때만 쓰여야 한다.
- 함수 수행 시 문제가 될 수 있는 조건을 함수 호출 전에 검사할 수 있다면, 예외를 던지는 대신 호출하는 곳에서 조건을 검사하도록 해야한다.

### 절차

1. 예외를 유발하는 상황을 검사할 수 있는 조건문을 추가한다. catch 블록의 코드를 조건문의 조건절 중 하나로 옮기고, 남은 try 블록의
코드를 다른 조건절로 옮긴다.
2. catch 블록에 어서션을 추가하고 테스트한다.
3. try문과 catch 블록을 제거한다.
4. 테스트한다.