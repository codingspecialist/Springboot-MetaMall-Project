## 토이 프로젝트

1. 헤더에 JWT 노출
    ```java
    //(1) JS코드로 토큰에 접근해서 클라이언트의 로컬영역에 저장할 수 있도록 설정해야한다. (기본값이 disable)
    //(2) 실제 서버에서는 JWT 탈취 위험성 때문에 보안조치가 필요하다.
    //(3) 기본값은 cors-safelisted reponse header만 노출
    configuration.addExposedHeader("Authorization");
    ```
2. 테스트시 인증처리 용이하도록 설정
   ```java
    //(1) 테스트를 위한 코드 실행전에 로그인 필요
    //(2) 실제 로그인 로직 : jwt -> 인증 필터 -> 시큐리티 세션 생성
    //(3) JWT를 이용한 토큰 방식의 로그인보다는, 세션에 직접 LoginUser를 주입하는 방식으로 강제 로그인 진행
    //    @WithUserDetails(value = "ssar")    //DB에서 해당 유저를 조회해서 세션에 담아주는 어노테이션
    //    setUp()으로 추가 했음에도 setupBefore=TEST_METHOD 에러 발생
    //(4) TEST_METHOD 설정시 @WithUserDetails가 setUp() 메서드 수행 전에 실행시간이 같다.
    //(5) TEST_EXECUTION 설정으로 @WithUserDetails가 setUp() 메서드 수행 후에 실행하도록 한다.
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)    //DB에서 해당 유저를 조회해서 세션에 담아주는 어노테이션
    ```
3. 테스트를 위한 더미 오브젝트 작성 -더미 오브젝트는 id를 명시적으로 지정한다.
    ```java
    public class DummyObject {
        //모두 스태틱 메서드
        protected User newUser(String username){
            BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
            String encPassword = passwordEncoder.encode("1234");
            return User.builder()
                    .username(username)
    //                .password("1234")
                    .password(encPassword)
                    .email(username+"@nate.com")
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
        protected static User newMockUser(Long id,String username){
            BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
            String encPassword = passwordEncoder.encode("1234");
            return User.builder()
                    .id(id)
                    .username(username)
    //                .password("1234")
                    .password(encPassword)
                    .email(username+"@nate.com")
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    ```

4. 모키토 통합 테스트시 사용하는 어노테이션
    ```java
    @ActiveProfiles("test")
    //(1) dev 모드에서 발동하는 DummyInit의 유저가 삽입되므로, 테스트에서 test 프로퍼티파일 사용하도록 하는 설정
    //@Transactional
    @Sql("classpath:db/teardown.sql")
    //(2) 테스트코드에서는 @Transactional 어노테이션 사용하는 대신,
    // 외래키 제약조건 해제 후, 테이블 truncate 수행한다음 다시 외래키 제약조건 설정 
    @AutoConfigureMockMvc
    //(3) 모키토 환경에서 MockMvc 객체를 사용하기 위한 어노테이션
    @SpringBootTest(webEnvironment = WebEnvironment.MOCK)
    //(4) 웹 애플리케이션을 위한 Mock 객체를 빈으로 주입해주는 어노테이션
    ```
   