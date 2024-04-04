# JSP 표준 태그 라이브러리(JSTL)

----------

## JSTL이란?

- JSP Standard Tag Library은 JSP에서 빈번하게 사용되는 조건문, 반복문 등을 처리해주는 태그를 모아 표준으로 만들어 놓은 라이브러리.
- JSTL을 사요하면 스크립틀릿 없이 태그만으로 작성할 수 있기 때문에 코드가 간결해지고 읽기 편해짐
- JSTL은 모두 5가지 종류의 태그를 제공함
  - Core 태그
    - 변수 선언, 조건문/반복문, URL 처리
    - 접두어: c
    - URI: http://java.sun.com/jsp/jstl/core
  - Formatting 태그
    - 숫자, 날짜, 시간 포맷 지정
    - 접두어: fmt
    - URI: http://java.sun.com/jsp/jstl/fmt
  - XML 태그
    - XML 파싱
    - 접두어: x
    - URI: http://java.sun.com/jsp/jstl/xml
  - Function 태그
    - 컬렉션, 문자열 처리
    - 접두어: fn
    - URI: http://java.sun.com/jsp/jstl/functions
  - SQL 태그
    - 데이터베이스 연결 및 쿼리 실행
    - 접두어: sql
    - URI: http://java.sun.com/jsp/jstl/sql
- Function 태그와 SQL 태그는 잘 이용하지 않음
  - EL에서 개발자가 정의한 메서드를 호출할 수 있기 떄문에(Function)
  - JDBC API를 주로 사용하기 때문에(SQL)
- JSTL을 사용하려는 JSP 파일에서는 taglib 지시어를 추가해야 하는데 이때 접두어와 URI가 사용됨

```html
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

---------------

## 코어(Core) 태그


- set
  - EL에서 사용할 변수를 설정.
  - setAttribute()메서드와 동일
- remove
  - 설정한 변수를 제거
  - removeAttribute()메서드와 동일
- if
  - 단일 조건문을 주로 처리
  - else문이 없음
- choose
  - 다중 조건을 처리할 때 사용
  - 하위에 when-otherwise 태그
- forEach
  - 반복문을 처리할 때 사용
  - 향상된 for문과 일반 for문 두 가지 형태
- forTokens
  - 구분자로 분리된 각각의 토큰을 처리할 때 사용
  - StringTokenizer 클래스와 동일
- import
  - 외부 페이지를 삽입할 때 사용
- redirect
  - 지정한 경로로 이동
  - sendRedirect()메서드와 동일한 기능
- url
  - 경로를 설정할 때 사용
- out
  - 내용을 출력할 때 사용
- catch
  - 예외 처리에 사용

### <c: set> 태그

- EL에서 사용할 변수나 자바빈스를 생성할 때 사용
- 영역에 속성을 저장할 때 사용하는 setAttribute() 메서드와 같은 역할

```html
<c:set var="변수명" value="값" scope="영역" />
또는
<c:set var="변수명" scope="영역">
    value 속성에 들어갈 값
</c:set>
```

- c:set 태그에서 사용하는 속성
  - var
    - 변수명을 설정
  - value
    - 변수에 할당할 값
  - scope
    - 변수를 생성할 영역을 지정
    - 디폴트: page
  - target
    - 자바빈즈를 설정
  - property
    - 자바빈즈의 속성, 멤버 변수의 값을 지정

```html
<c:set var="변수명" value="저장할 객체 혹은 컬렉션" scope="영역" />
<c:set target="var로 설정한 변수명" property="객체의 속성명" value="속성값"/>
```

```html
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="directVar" value="100" />
<c:set var="elVar" value="${directVar mod 5}"/>
<c:set var="expVar" value="<%= new Date() %>" />
<c:set var="betweenVar">변수값 이렇게 설정</c:set>
<h4>EL을 사용해 변수 출력</h4>
<ul>
    <li>directVar: ${pageScope.directVar}</li>
    <li>elVar: ${elVar}</li>
    <li>expVar: ${expVar}</li>
    <li>betweenVar: ${betweenVar}</li>
</ul>

<h4>자바 빈즈 생성</h4>
<c:set var="personVar1" value='<%= new Person("tom",15)%>' scope="request"/>
<ul>
    <li>이름: ${requestScope.personVar1.name}</li>
    <li>나이: ${personVar1.age}</li>
</ul>

<h4>자바 빈즈 생성 - target, property 사용</h4>
<c:set var="personVar2" value="<%= new Person()%>" scope="request"/>
<c:set target="${personVar2}" property="name"  value="boy"/>
<c:set target="${personVar2}" property="age" value="15"/>
<ul>
    <li>이름: ${personVar2.name}</li>
    <li>나이: ${personVar2.age}</li>
</ul>
```

### <c: remove> 태그

- c:set 태그로 설정한 변수를 제거할 때 사용함
- removeAttribute() 메서드와 같은 역할

```html
<c:remove var="변수명" scope="영역"/>
```

- 속성
  - var
    - 삭제할 변수명 설정
  - scope
    - 삭제할 변수의 영역을 지정
    - 디폴트: 모든 영역
- 영역을 지정하지 않을 경우 동일한 이름의 변수가 있다면 한꺼번에 삭제됨

### <c: if> 태그

- 자바의 if와 동일하게 제어 구문을 작성할 때 사용
- else가 별도로 없기 때문에 일련의 여러 조건을 나열하는 형태로 작성하기에는 어려움이 있음

```html
<c:if test="조건" var="변수명" scope="영역">
  조건이 true일 때 출력할 문장
</c:if>
```

- 속성
  - test
    - if문에서 사용할 조건 지정
  - var
    - 조건의 결과를 저장할 변수명을 지정
  - scope
    - 변수가 저장될 영역을 지정

```html
<c:set var="number" value="100"/>
<c:set var="string" value="JSP"/>
<h4>if 태그로 짝수/홀수 판별</h4>
<c:if test="${number mod 2 eq 0}" var="result">
    ${number}는 짝수입니다.
</c:if>
result: ${result}<br/>
<h4>문자열 비교와 else 구문 흉내내기</h4>
<c:if test="${string eq 'Java'}" var="result2">
    문자열은 Java 입니다.<br/>
</c:if>
<c:if test="${not result2}">
    'Java'가 아닙니다. <br/>
</c:if>
<h4>조건식 주의사항</h4>
<c:if test="100" var="result3">
    EL이 아닌 정수를 지정하면 false
</c:if>
result3: ${result3}
<c:if test="tRuE" var="result4">
    대소문자 구분 없이 "tRuE"인 경우
</c:if>
result4: ${result4}
<c:if test="${ true }" var="result5">
    EL 양쪽에 빈 공백이 있는 경우 false
</c:if>
result5: ${result5}
```

### <c: choose>, <c: when>, <c: otherwise> 태그

- <c: choose> 태그는 다중 조건을 통해 판단해야 할 때 사용함

```html
<c:choose>
  <c:when test="조건1">조건 1을 만족하는 경우</c:when>
  <c:when test="조건2">조건 2을 만족하는 경우</c:when>
  <c:otherwise>아무 조건도 만족하지 않는 경우</c:otherwise>
</c:choose>
```


### <c: forEach> 태그

- 반복을 위해 사용
- 일반 for문과 배열이나 컬렉션을 순회할 때 사용하는 향상된 for문

#### 일반 for문 형태

```html
<c:forEach var="변수명" begin="시작값" end="마지막값" step="증가값" />
// JAVA
<c:forEach var="i" begin="0" end="100" step="2"/>
for(int i = 0; i < 100; i+=2) {
    ...
}
```

#### 향상된 for문 형태

```html

<c:forEach var="변수명" items="컬렉션 혹은 배열" />
// JAVA
<c:forEach var="number" items="numbers"/>
for(int number : numbers) {
    ...
}
```

### <c: forTokens> 태그

- 자바의 StringTokenizer 클래스처럼 구분자를 기준으로 문자열을 나눠 토큰의 개수만큼 반복해줌

```html
<c:forTokens items="문자열" delims="문자열 구분자" var="변수명"/>
```

### <c: import> 태그

- <jsp: include> 액션 태그와 같이 외부 파일을 현재 위치에 삽입할 때 사용함
- 같은 웹 애플리케이션에 속하지 않은 외부의 페이지도 삽입할 수 있음

```html
<c:import url="페이지 경로 혹은 URL" scope="영역"/>
```

### <c: redirect> 태그

- response 내장 객체의 sendRedirect()와 동일하게 페이지 이동을 처리함
- 매개변수를 전달하고 싶다면 <c: param> 태그를 사용하면 됨

### <c: url> 태그

- 지정한 경로와 매개변수를 이용해서 컨텍스트 루트를 포함한 URL을 생성함
- 생성된 URL은 a 태그의 href 속성이나 form 태그의 action 속성에 사용할 수 있음

### <c: out> 태그

- JSP의 표현식처럼 변수를 출력할 때 사용함

```html
<c:out value="출력할 변수" default="기본값" escapeXml="특수문자 처리 유무"/>
```

### <c: catch> 태그

- 발생한 예외를 잡아 처리하는 역할.
- 예외가 발생하면 지정한 변수에 에러 메시지가 저장되어 전달됨

```html
<c:catch var="변수명">
  실행 코드
</c:catch>
```

---------------

## 국제화(Formatting) 태그

- 국가별로 다양한 언어, 날짜, 시간, 숫자 형식을 설정할 때 사용됨
- 지시어

```html
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
```

- 숫자 포맷
  - formatNumber
    - 숫자 포맷을 설정
  - parseNumber
    - 문자열을 숫자 포맷으로 변환
- 날짜 포맷
  - formatDate
    - 날짜나 시간의 포맷을 설정
  - parseDate
    - 문자열을 날짜 포맷으로 변환
- 타임존 설정
  - setTimeZone
    - 시간대 설정 정보를 변수에 저장
  - timeZone
    - 시간대를 설정
- 로케일 설정
  - setLocale
    - 통화 기호나 시간대를 설정한 지역에 맞게 표시
  - requestEncoding
    - 요청 매개변수의 문자셋을 설정

### 숫자 포맷팅 및 파싱

```html
<fmt:formatNumber value="출력할 숫자" type="출력 양식" var="변수 설정" 
      groupingUsed="구분 기호 사용 여부" pattern="숫자 패턴" scope="영역" />

<fmt:parseNumber value="파싱할 문자열" type="출력 양식" var="변수 설정" 
      integerOnly="정수만 파싱" pattern="패턴" scope="영역"/>
```

```html
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h4>숫자 포맷 설정</h4>
<c:set var="number1" value="12345"/>
콤마 O: <fmt:formatNumber value="${number1}"/><br/>
콤마 X: <fmt:formatNumber value="${number1}" groupingUsed="false" /><br/>
<fmt:formatNumber value="${number1}" type="currency" var="printNum1"/>
통화기호: ${printNum1} <br/>
<fmt:formatNumber value="${number1}" type="percent" var="printNum2"/>
퍼센트: ${printNum2} <br/>

<h4>문자열을 숫자로 변경</h4>
<c:set var="number2" value="6,789.01"/>
<fmt:parseNumber value="${number2}" pattern="00,000.00" var="printNum3"/>
소수점까지: ${printNum3}<br/>
<fmt:parseNumber value="${number2}" integerOnly="true" var="printNum4"/>
정수만: ${printNum4}
```

### 날짜 포맷 및 타임존

```html
<fmt:formatDate value="출력할 날짜" type="출력 양식" var="변수 설정" 
    dateStyle="날짜 스타일" timeStyle="시간 스타일" pattern="날짜 패턴" scope="영역"/>
```

### 로케일 설정

- 국가별로 다른 통화 기호나 날짜를 표현할 때 사용

```html
<fmt:setLocale value="ko_kr"/>
<fmt:setLocale value="ja_JP"/>
<fmt:setLocale value="en_US"/>
```

-----------

## 핵심 요약

- Core 태그
  - 프로그래밍 언어에서 가장 기본이 되는 변수 선언, 조건문, 반복문 등을 대체하는 태그를 제공
- Formatting 태그
  - 국가별로 다양한 언어, 날짜와 시간, 숫자 형식을 설정할 때 사용
