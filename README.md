# 스프링부트 총정리 (미니프로젝트)

## 배웠던 모든 기술 추가하기
- Filter
- JWT
- AOP
- RoleInterceptor(권한 체크 ADMIN 여부)
- SessionArgumentResolver(세션 주입하기)
- ExceptionAdvice(예외핸들러)
- ValidAdvice(유효성검사)
- ErrorLogAdvice(에러로그 데이터베이스 기록)
- SameUserIdAdvice(자원 소유자 확인)
- Exception400, Exception401, Exception403, Exception, 404처리
- MyDateUtil(시간 포멧해서 응답)
- MyFilterResponseUtil(필터는 예외핸들러를 사용못하기 때문에 재사용 메서드 생성)
- test/RegexTest(정규표현식 테스트) https://github.com/codingspecialist/junit-bank-class/tree/main/class-note/regex
- 스프링부트 새로운 설정설정 (로그, DB 파라메터, 404처리)
```yml
server:
  servlet:
    encoding:
      charset: utf-8
      force: true

spring:
  datasource:
    url: jdbc:h2:mem:test;MODE=MySQL
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    properties:
      hibernate:
        format_sql: true
      default_batch_fetch_size: 100 # in query 자동 작성
  # 404 처리하는 법
  mvc:
    throw-exception-if-no-handler-found: true
  web:
    resources:
      add-mappings: false

logging:
  level:
    '[shop.mtcoding.metamall]': DEBUG # DEBUG 레벨부터 에러 확인할 수 있게 설정하기
    '[org.hibernate.type]': TRACE # 콘솔 쿼리에 ? 에 주입된 값 보기
```