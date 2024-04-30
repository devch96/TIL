# AOP

- IoC/DI, 서비스 추상화와 더불어 스프링의 3대 기반기술
- 스프링에 적용된 가장 인깅 있는 AOP의 적용 대상은 선언적 트랜잭션 기능

## 트랜잭션 코드의 분리

### 메서드 분리

- 트랜잭션 경계설정과 비즈니스 로직이 공존하는 메서드

```java
public void upgradeLevels() throws Exception {
    TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
    try{
        ... // 비즈니스 로직
        this.transactionManager.commit(status);
    } catch (Exception e) {
        this.transactionManager.rollback(status);
        throw e;
    }
}
```

## 다이내믹 프록시와 팩토리 빈

### 프록시와 프록시 패턴, 데코레이터 패턴