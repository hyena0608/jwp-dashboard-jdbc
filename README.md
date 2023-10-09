# JDBC 라이브러리 구현하기

## 1단계

- RowMapper
  - [x] 컬럼 매핑용 기능 구현
- JdbcTemplate
  - [x] 단건 조회 기능 구현
  - [x] 복수 조회 기능 구현
  - [x] 업데이트(executeUpdate) 기능 구현 

## 2단계

```mermaid
flowchart RL
    UserDao --- JdbcTemplate
    JdbcTemplate --- PreparedStatementExecuteTemplate --- DataSource
    JdbcTemplate --- ResultSetMappingTemplate
```
- [x] PreparedStatement 작업 처리 역할을 분리한다.
- [x] ResultSet 내 데이터를 매핑하는 역할을 분리한다.
- [x] 데이터베이스 쿼리 예외 발생시 DataAccessException 을 던지도록 수정한다.

## 3단계

- [x] TransactionTemplate 을 구현하여 사용자 비밀번호 변경과 사용자 로그를 하나의 트랜잭션에서 진행하도록 한다.

```mermaid
flowchart RL
    UserService --- TransactionTemplate
    UserService --- UserDao
    UserService --- UserHistoryDao
```

## 4단계

- [x] UserService를 인터페이스로 만들고 AppUserService와 TxUserSerivce를 구현한다.
- [x] TxUserService는 AppUserService와 TransactionTemplate을 필드로 갖는다.
- [x] TransactionManager가 트랜잭션(시작, 커밋, 롤백)을 관리하게 한다.

```mermaid
flowchart LR
    AppUserService --> |implements| UserService 
    
    TxUserService --> |implements| UserService 
    TxUserService --- UserService 
    TxUserService --- TransactionTemplate
    
    TransactionTemplate --- TransactionManager --- DataSource
```
