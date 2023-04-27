package shop.mtcoding.metamall.core.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import shop.mtcoding.metamall.core.session.SessionUser;
import shop.mtcoding.metamall.model.log.error.ErrorLog;
import shop.mtcoding.metamall.model.log.error.ErrorLogRepository;

import javax.servlet.http.HttpSession;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class MyErrorLogAdvice {

    private final HttpSession session;
    private final ErrorLogRepository errorLogRepository;

    @Pointcut("@annotation(shop.mtcoding.metamall.core.annotation.MyErrorLogRecord)")
    public void myErrorLog() {}

    @Before("myErrorLog()")
    public void errorLogAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();

        for (Object arg : args) {
            if (arg instanceof Exception) {
                Exception e = (Exception) arg;
                SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
                if (sessionUser != null) {
                    ErrorLog errorLog = ErrorLog.builder().userId(sessionUser.getId()).msg(e.getMessage()).build();
                    errorLogRepository.save(errorLog);
                }
            }
        }
    }
}
