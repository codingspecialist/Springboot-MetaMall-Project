package shop.mtcoding.metamall.core.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.model.log.error.ErrorLog;
import shop.mtcoding.metamall.model.log.error.ErrorLogRepository;
import shop.mtcoding.metamall.model.log.login.LoginLog;
import shop.mtcoding.metamall.model.log.login.LoginLogRepository;
import shop.mtcoding.metamall.model.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAdvice {
    private final LoginLogRepository loginLogRepository;
    private final ErrorLogRepository errorLogRepository;
    private final HttpSession session;

    @AfterReturning(pointcut = "execution(public * login(..,*))", returning = "response")
    public void loginLogAdvice(JoinPoint jp, ResponseEntity<?> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            HttpServletRequest request = null;
            for (Object arg : jp.getArgs()) {
                if (arg instanceof HttpServletRequest) request = (HttpServletRequest) arg;
            }

            ResponseDto<?> responseDto = (ResponseDto<?>) response.getBody();
            User loginUser = (User) responseDto.getData();

            LoginLog loginLog = LoginLog.builder()
                    .userId(loginUser.getId())
                    .userAgent(request.getHeader("User-Agent"))
                    .clientIP(request.getRemoteAddr())
                    .build();
            loginLogRepository.save(loginLog);
        }
    }

    @After("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void errorLogAdvice(JoinPoint jp) {
        LoginUser loginSession = (LoginUser) session.getAttribute("loginUser");
        for (Object arg : jp.getArgs()) {
            if (arg instanceof RuntimeException) {
                if (loginSession != null) {
                    ErrorLog errorLog = ErrorLog.builder()
                            .userId(Long.valueOf(loginSession.getId()))
                            .msg(((RuntimeException) arg).getMessage())
                            .build();
                    errorLogRepository.save(errorLog);
                    return;
                }
                log.error(((RuntimeException) arg).getMessage());
            }
        }
    }
}
