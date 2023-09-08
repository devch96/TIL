# 배열보다는 리스트를 사용하라

- 배열과 제네릭 타입에는 중요한 차이가 두 가지 있다.
  - 배열은 공변(covariant)이지만 제네릭은 불공변(invariant)다.
    - Sub가 Super의 하위 타입이라면 배열 Sub[]는 배열 Super[]의 하위 타입이 된다.
    - 공변: 함께 변한다는 뜻.
    ```java
    /// 런타임 실패 (지양)
    Object[] objectArray = new Long[1];
    objectArray[0] = "문자열"; // ArrayStoreException을 던진다.
            
    // 컴파일 실패
    List<Object> ol = new ArrayList<Long>();
    ol.add("문자열");
    
    ```
  - 배열은 실체화(reify)된다.
    - 배열은 런타임에도 자신이 담기로 한 원소의 타입을 인지하고 확인한다.
    - 따라서 Long 배열에 String을 넣으려 하면 ArrayStoreException이 발생한다.
    - 하지만 제네릭은 타입 정보가 런타임에는 소거(erasure)된다.
    - 원소 타입을 컴파일타임에만 검사하며, 런타임에는 알 수조차 없다는 뜻이다.
    - 소거는 제네릭이 지원되기 전의 레거시 코드와 제네릭 타입을 함께 사용할 수 있게 해주는 매커니즘이다.
  - 제네릭은 실체화 불가 타입(non-reifiable type)이다.
    - 실체화되지 않아서 런타임에는 컴파일타임보다 타입 정보를 적게 가진다.
    - 소거 메커니즘 때문에 매개변수화 타입 가운데 실체화될 수 있는 타입은 비한정적 와일드카드 타입 뿐이다.

### 제네릭 배열 생성을 허용하지 않는 이유

```java
List<String>[] stringLists = new List<String>[1]; // 1
List<Integer> intList = List.of(42); // 2
Object[] objects = stringLists; // 3
objects[0] = intList; // 4
String s = stringLists[0].get(0) // 5
```

- 제네릭 생성 배열이 허용된다고 가정.(1)
- 원소가 하나인 Integer List 생성 (2)
- String List를 Object 배열에 할당. 배열은 공변이니 문제 없음 (3)
- 제네릭은 소거이기 때문에 런타임시 Integer List는 그냥 List가 되버리고 Integer List 배열도 List[]가 되버림. 따라서 ArrayStoreException 발생 X (4)
- String List 에 Integer List가 들어 있으므로 에러 발생 (5)

## 핵심 정리

- 배열과 제네릭에는 매우 다른 타입 규칙이 적용된다.
- 배열은 공변이고 실체화되는 반면, 제네릭은 불공변이고 타입 정보가 소거된다.
- 그 결과 배열은 런타임에는 타입 안전하지만 컴파일타임에는 그렇지 않다. 제네릭은 반대다.
- 둘을 섞어 쓰다가 컴파일 오류나 경고를 만나면 배열을 리스트로 대체하는 방법을 적용해보자.
