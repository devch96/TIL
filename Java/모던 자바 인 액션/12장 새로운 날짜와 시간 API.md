# 새로운 날짜와 시간 API

- 자바 8에서는 지금까지의 날짜와 시간 문제를 개선하는 새로운 날짜와 시간 API를 제공했다.
- 자바 1.0에서는 java.util.Date 클래스 하나로 날짜와 시간 관련 기능을 제공했다.
- 날짜를 의미하는 Date라는 클래스의 이름과 달리 Date 클래스는 특정 시점을 날짜가 아닌 밀리초 단위로 표현한다.
- 1900년을 기준으로 하는 오프셋, 0에서 시작하는 달 인덱스 등 모호한 설계로 유용성이 떨어졌다.
- 결과가 직관적이지 않고 Date 클래스의 toString으로 반환되는 문자열을 추가로 활용하기가 어렵다.
- Date는 JVM 기본시간대인 중앙 유럽 시간대를 사용했다.
- 자바 1.1에서는 Date클래스의 여러 메서드를 사장(deprecated) 시키고, Calendar라는 클래스를 대안으로 제공했으나
쉽게 에러를 일으키는 설계 문제를 갖고 있었다.

-----------------------

## LocalDate, LocalTime, Instant, Duration, Period 클래스

### LocalDate와 LocalTime 사용

- LocalDate 인스턴스는 시간을 제외한 날짜를 표현하는 불변 객체다.
- LocalDate 객체는 어떤 시간대 정보도 포함하지 않는다.
- 정적 팩터리 메서드 of로 LocalDate 인스턴스를 만들 수 있다.
- 또한 연도, 달, 요일 등을 반환하는 메서드를 제공한다.
- 팩터리 메서드 now는 시스템 시계의 정보를 이용해서 현재 날짜 정보를 얻는다.
- parse 메서드에 DateTimeFormatter를 전달할 수도 있다.

### 날짜와 시간 조합

- LocalDateTime은 LocalDate와 LocalTime을 쌍으로 갖는 복합 클래스다.
- 날짜와 시간을 모두 표현할 수 있으며 직접 만들 수도 있고 날짜와 시간을 조합할 수도 있다.
- LocalDateTime의 toLocalDate나 toLocalTime 메서드로 LocalDate나 LocalTime 인스턴스를 추출할 수 있다.

### Instant 클래스 : 기계의 날짜와 시간

- 사람은 보통 주, 날짜, 시간, 분으로 날짜와 시간을 계산하지만 기계에서는 이와 같은 단위로 시간을 표현하기가 어렵다.
- 기계의 관점에서는 연속된 시간에서 특정 지점을 하나의 큰 수로 표현하는 것이 가장 자연스러운 시간 표현 방법이다.
- Instant 클래스는 유닉스 에포크 시간(1970년 1월 1일 0시 0분 0초 UTC)을 기준으로 특정 지점까지의 시간을 초로 표현한다.
- 팩터리 메서드 ofEpochSecond에 초를 넘겨줘서 Instant 클래스 인스턴스를 만들 수 있다.
- Instant 클래스도 사람이 확인할 수 있도록 시간을 표시해주는 정적 팩터리 메서드 now를 제공한다.
- Instant에서는 Duration과 Period 클래스를 함께 활용할 수 있다.

### Duration과 Period 정의

- 두 시간 객체 사이의 지속시간 duration
- Duration 클래스의 정적 팩터리 메서드 between으로 두 시간 객체 사이의 지속시간을 만들 수 있다.
- 두 개의 LocalTime, 두 개의 LocalDateTime, 두 개의 Instant로 Duration을 만들 수 있다.
- 년, 월, 일로 시간을 표현할 때는 Period 클래스를 사용한다.

------------------

## 날짜 조정, 파싱, 포매팅

- withAttribute 메서드로 기존의 LocalDate를 바꾼 버전을 직접 간단하게 만들 수 있다.
- plusWeeks, minusYears 등과 같이 선언형으로 속성을 바꿀 수도 있다.

### TemporalAdjuster 사용하기

- 때로는 다음 주 일요일, 돌아오는 평일, 어떤 달의 마지막 날 등 좀 더 복잡한 날짜 조정 기능이 필요할 것이다.
- 오버로드된 버전의 with 메서드에 좀 더 다양한 동작을 수행할 수 있도록 하는 기능을 제공하는 TemporalAdjuster를 전달하는 방법으로
문제를 해결할 수 있다.
```java
LocalDate date1 = LocalDate.of(2014,3,18);
LocalDate date2 = date1.with(TemporalAdjusters.nextOrSame(dayOfWeek.SUNDAY));
LocalDate date3 = date2.with(TemporalAdjusters.lastDayOfMonth());
```

### 날짜와 시간 객체 출력과 파싱

- 날짜와 시간 관련 작업에서 포매팅과 파싱은 서로 떨어질 수 없는 관계다.
- DateTimeFormatter는 정적 팩터리 메서드와 상수를 이용해서 손쉽게 포매터를 만들 수 있다.
```java
LocalDate date = LocalDate.of(2014,3,18);
String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); //20140318
String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18

LocalDate date1 = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
LocalDate date2 = LocalDate.parse("2014-03-19", DateTimeFormatter.ISO_LOCAL_DATE);
```
- DateTimeFormatter 클래스는 특정 패턴으로 포매터를 만들 수 있는 정적 팩터리 메서드도 제공된다.
```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
LocalDate date 1 = LocalDate.of(2014,3,18);
string formattedDate = date1.format(formatter);
LocalDate date2 = LocalDate.parse(formattedDate, formatter);
```
- ofPattern 메서드도 Locale로 포매터를 만들 수 있도록 오버로드된 메서드를 제공한다.

------------------

## 다양한 시간대와 캘린더 활용 방법

### 시간대 사용하기

- 표준 시간이 같은 지역을 묶어서 시간대(time zone) 규칙 집합을 정의한다.
```java
ZoneId romeZone = ZoneId.of("Europe/Rome");
```
- 지역ID는 {지역}/{도시} 형식으로 이루어지며 IANA Time Zone Database에서 제공하는 지역 집합 정보를 사용한다.

-------------------

## 마치며

- 자바 8 이전 버전에서 제공하는 기존의 Date 클래스와 관련 클래스에서는 여러 불일치점들과 가변성, 어설픈 오프셋, 기본값, 잘못된 이름 결정
등의 설계 결함이 존재했다.
- 날짜와 시간 API에서는 날짜와 시간 객체는 모두 불변이다.
- 사람과 기계가 편리하게 날짜와 시간 정보를 관리할 수 있도록 두 가지 표현 방식을 제공한다.
- 날짜와 시간 객체를 절대적인 방법과 상대적인 방법으로 처리할 수 있으며 기존 인스턴스를 변환하지 않도록 처리 결과로 새로운 인스턴스가 생성된다.
- TemporalAdjuster를 이용하면 단순히 값을 바꾸는 것 이상의 복잡한 동작을 수행할 수 있으며 자신만의 커스텀 날짜 변환 기능을 정의할 수 있다.