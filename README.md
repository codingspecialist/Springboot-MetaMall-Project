# Springboot-MetaMall-Project
---
## 1. 사용 기술
- Filter
- Jwt
- Aop
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
스프링부트 새로운 설정설정 (log4j, DB 파라메터 trace, 404처리)
- Hibernate + JPA
- 양방향 매핑
- Lazy Loading
- join fetch
- default_batch_fetch_size: 100 (인쿼리)
- 영속, 비영속, 준영속
- 더티체킹
- Repository Test
- Controller Test

## 2. hibernateLazyInitializer 해결법
---
원인은 MessageConverter가 비어있는 객체를 Lazy Loading할 때 발생한다. Jackson 라이브러리는 객체를 직렬화할 때 프록시 객체를 처리할 수 없기 때문이다.
- 직접 Lazy Loading 하기 (강력 추천 - 서비스 레이어에서 DTO 만들 때 사용하면 됨)
- join fetch 하기 (강력 추천)
- Eager 전략으로 변경하기 (현재 추천 - 서비스가 없으니까!!)
- fail-on-empty-beans: false (비 추천)
