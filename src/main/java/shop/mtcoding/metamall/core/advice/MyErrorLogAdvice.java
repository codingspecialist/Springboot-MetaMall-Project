package shop.mtcoding.metamall.core.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import shop.mtcoding.metamall.core.session.SessionUser;
import shop.mtcoding.metamall.model.log.error.ErrorLog;
import shop.mtcoding.metamall.model.log.error.ErrorLogRepository;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Aspect
@Component
public class MyErrorLogAdvice {

    private final HttpSession session;
    private final ErrorLogRepository errorLogRepository;

    // 깃발에 별칭주기
    @Pointcut("@annotation(shop.mtcoding.metamall.core.annotation.MyErrorLog)")
    public void myErrorLog(){}

    @Before("myErrorLog()")
    public void errorLogAdvice(JoinPoint jp) {
        System.out.println(1);
        Object[] args = jp.getArgs();

        for (Object arg : args) {
            System.out.println(2);
            if(arg instanceof Exception){
                System.out.println(3);
                Exception e = (Exception) arg;
                SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
                if(sessionUser != null){
                    System.out.println(4);
                    ErrorLog errorLog =ErrorLog.builder().userId(sessionUser.getId()).msg(e.getMessage()).build();
                    errorLogRepository.save(errorLog);
                }
            }
        }
    }
}
