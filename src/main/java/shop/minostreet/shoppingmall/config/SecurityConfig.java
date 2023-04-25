package shop.minostreet.shoppingmall.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.minostreet.shoppingmall.config.auth.LoginUser;
import shop.minostreet.shoppingmall.config.jwt.JwtAuthenticationFilter;
import shop.minostreet.shoppingmall.config.jwt.JwtAuthorizationFilter;
import shop.minostreet.shoppingmall.domain.LoginLog;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.domain.UserEnum;
import shop.minostreet.shoppingmall.repository.LoginLogRepository;
import shop.minostreet.shoppingmall.util.MyResponseUtil;

import javax.servlet.http.HttpServletRequest;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    //    @Autowired
//    LoginSuccessHandler loginSuccessHandler;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final LoginLogRepository loginLogRepository;

    @Bean   //IoC 컨테이너에 BCryptPasswordEncoder 객체 등록
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> authenticationSuccessListener() {
        return (ApplicationListener<AuthenticationSuccessEvent>) event -> {
            Authentication authentication = event.getAuthentication();
            log.debug("디버그 : onAuthenticationSuccess 호출됨");
            // 1. 로그인 유저 정보 가져오기
            LoginUser userDetails = (LoginUser) authentication.getPrincipal();
            User loginUser = userDetails.getUser();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            // 2. 최종 로그인 날짜 기록 (더티체킹 - update 쿼리 발생)
//        loginUser.(LocalDateTime.now());

            // 3. 로그 테이블 기록
            LoginLog loginLog = LoginLog.builder()
                    .userId(loginUser.getId())
                    .userAgent(request.getHeader("User-Agent"))
                    .clientIP(request.getRemoteAddr())
                    .build();
            loginLogRepository.save(loginLog);
        };
    }


    @Bean
    //1. JWT 서버를 만들기 위한 설정
    //2. JWT 필터 등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain 빈 등록됨");
        //iframe 사용안함 (인라인 프레임을 생성하는 태그로 타 사이트의 컨텐츠 혹은 타 페이지를 삽입)
        http.headers().frameOptions().disable();
        //csrf 사용안함
        //:Cross-Site Request Forgery
        // 타 사이트에서 인증된 사용자의 권한을 이용해서 공격하는 방식으로,
        // 페이지 로드시 CSRF 토큰을 생성해 해당 토큰을 가진 요청만 처리함으로써 방지한다.
        http.csrf().disable();
        //다른 서버의 자바스크립트 요청 거부 허용으로 (거부할 사항을 Null)
        //: Cross Origin Resource Sharing
        // 프론트엔드와 백엔드의 도메인을 다르게 설정해서 백엔드에서 프론트엔드의 요청만 응답하도록 설정한다.
        http.cors().configurationSource(configurationSource());

        //stateless 전략으로 사용하기 위해 jSessionId를 서버쪽에서 관리안하도록 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //react 같은 프론트엔드 프레임워크 이용하므로 제공되는 폼 사용안함
        http.formLogin().disable();

        //httpbasic 방식 사용안함 (팝업창 이용해서 사용자 인증하는 방식)
        http.httpBasic().disable();

        //jwt 필터 등록
//        http.apply(new CustomSecurityFilterManager(loginSuccessHandler));
        http.apply(new CustomSecurityFilterManager());


        //응답의 일관성을 만들기 위해 인증 실패 Exception 가로채기
        http.exceptionHandling().authenticationEntryPoint(

                (request, response, authenticationException) -> {
                    String uri = request.getRequestURI();
                    log.debug("디버그 : " + uri);

//                        MyResponseUtil.unAuthentication(response, "로그인이 필요합니다.");
                    MyResponseUtil.fail(response, "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);


                }
        );
        //인증이 되지 않은 사용자에 대한 예외처리하는 메서드로 파라미터는
        // ExceptionTranslationFilter로 필터링 되는 AuthenticationEntryPoint 객체
        //: AuthenticationEntryPoint의 commence 메서드는 파라미터로 request, response, AuthenticationException

        //응답의 일관성을 만들기 위해 권한 실패 Exception 가로채기
        http.exceptionHandling().accessDeniedHandler(

                //	void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
                //	구현 필요
                (request, response, e) -> {
                    String uri = request.getRequestURI();
                    log.debug("디버그 : " + uri);
                    MyResponseUtil.fail(response, "권한이 없습니다", HttpStatus.FORBIDDEN);
                }
        );

        //접근 권한 설정
        http.authorizeRequests()
                .antMatchers("/api/user/**").authenticated()
                .antMatchers("/api/seller/**").access("hasRole('ADMIN') or hasRole('SELLER')")
                .antMatchers("/api/admin/**").hasRole("ADMIN") //default prefix가 'ROLE_'
                .anyRequest().permitAll();

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : CorsConfigurationSource cors 설정돼 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");    //HTTP 메서드와 자바스크립트 요청 허용
        configuration.addAllowedOriginPattern("*");     //모든 IP 주소 허용(추후 프론트 엔드 쪽 IP 허용하도록 변경)
        configuration.setAllowCredentials(true);    //클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization");
        //: 실제 서버에서는 JWT 탈취 위험성 때문에 보안조치가 필요하다.
        // : cors-safelisted reponse header만 노출

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //작성한 설정을 모든 주소에 적용

        return source;
    }

    //JWT 필터 등록
    //(1) HttpSecurity가 없기 때문에 상속해서 캐스팅
    //: extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity>
    //(2) AuthenticationManager가 없기 때문에 생성
    //: AuthenticationManager authenticationManager=builder.getSharedObject(AuthenticationManager.class);
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        //        private final LoginSuccessHandler loginSuccessHandler;
//        public CustomSecurityFilterManager(LoginSuccessHandler loginSuccessHandler) {
//            this.loginSuccessHandler = loginSuccessHandler;
//        }
        public CustomSecurityFilterManager() {}

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
//            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, loginSuccessHandler));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));

//            super.configure(builder);
        }
    }
}
