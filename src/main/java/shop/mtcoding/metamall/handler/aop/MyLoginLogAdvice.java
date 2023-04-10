package shop.mtcoding.metamall.handler.aop;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import shop.mtcoding.metamall.config.auth.LoginUser;
import shop.mtcoding.metamall.config.jwt.JwtAuthenticationFilter;
import shop.mtcoding.metamall.domain.LoginLog;
import shop.mtcoding.metamall.domain.User;
import shop.mtcoding.metamall.handler.exception.MyValidationException;
import shop.mtcoding.metamall.repository.LoginLogRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Aspect
//Aspect = PointCut + Advice
@Component
public class MyLoginLogAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final LoginLogRepository loginLogRepository;


    @Pointcut("execution(* shop.mtcoding.metamall.config.jwt.JwtAuthenticationFilter.successfulAuthentication(..))")
    public void loginSuccess() {
    }


    @Around("loginSuccess()")
    public Object loginSuccessLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("디버그 : 로그인 로그 남김");
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        Object result = joinPoint.proceed();
        LoginUser loginUser = (LoginUser) result;
        User user = loginUser.getUser();

        LoginLog loginLog = LoginLog.builder()
                .userId(user.getId())
                .userAgent(request.getHeader("User-Agent"))
                .clientIP(request.getRemoteAddr())
                .createdAt(LocalDateTime.now())
                .build();

        loginLogRepository.save(loginLog);

        return joinPoint.proceed();
    }

}
