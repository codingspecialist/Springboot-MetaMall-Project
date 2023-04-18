package shop.minostreet.shoppingmall.handler.aop;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import shop.minostreet.shoppingmall.config.auth.LoginUser;
import shop.minostreet.shoppingmall.domain.LoginLog;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.repository.LoginLogRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Aspect
//Aspect = PointCut + Advice
@Component
public class MyLoginLogAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final LoginLogRepository loginLogRepository;


    @Pointcut("execution(* shop.minostreet.shoppingmall.config.jwt.JwtAuthenticationFilter.successfulAuthentication(..))")
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
