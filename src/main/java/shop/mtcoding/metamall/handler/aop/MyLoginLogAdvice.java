package shop.mtcoding.metamall.handler.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import shop.mtcoding.metamall.config.auth.LoginUser;
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

    private final LoginLogRepository loginLogRepository;

    @Pointcut("execution(* shop.mtcoding.metamall.config.jwt.JwtAuthenticationFilter.successfulAuthentication(..))")
    public void loginSuccess() {
    }


    @Around("loginSuccess()")
    public Object loginSuccessLog(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        LoginUser loginUser = (LoginUser) joinPoint.proceed();
        User user = loginUser.getUser();

        LoginLog loginLog = LoginLog.builder()
                .userId(user.getId())
                .userAgent(request.getHeader("User-Agent"))
                .clientIP(request.getRemoteAddr())
                .createdAt(LocalDateTime.now())
                .build();

        loginLogRepository.save(loginLog);

        return loginUser;
    }

}
