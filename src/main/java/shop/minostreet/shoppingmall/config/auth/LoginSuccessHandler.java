//package shop.minostreet.shoppingmall.config.auth;
//
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import shop.minostreet.shoppingmall.domain.LoginLog;
//import shop.minostreet.shoppingmall.domain.User;
//import shop.minostreet.shoppingmall.repository.LoginLogRepository;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//@Component
//public class LoginSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final Logger log = LoggerFactory.getLogger(getClass());
//    private final LoginLogRepository loginLogRepository;
//    public LoginSuccessHandler(LoginLogRepository loginLogRepository) {
//        this.loginLogRepository=loginLogRepository;
//    }
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        log.debug("디버그 : onAuthenticationSuccess 호출됨");
//        // 1. 로그인 유저 정보 가져오기
//        LoginUser userDetails = (LoginUser) authentication.getPrincipal();
//        User loginUser = userDetails.getUser();
//
//        // 2. 최종 로그인 날짜 기록 (더티체킹 - update 쿼리 발생)
////        loginUser.(LocalDateTime.now());
//
//        // 3. 로그 테이블 기록
//        LoginLog loginLog = LoginLog.builder()
//                .userId(loginUser.getId())
//                .userAgent(request.getHeader("User-Agent"))
//                .clientIP(request.getRemoteAddr())
//                .build();
//        loginLogRepository.save(loginLog);
//    }
//}
