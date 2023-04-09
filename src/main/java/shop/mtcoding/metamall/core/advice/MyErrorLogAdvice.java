package shop.mtcoding.metamall.core.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.model.log.error.ErrorLog;
import shop.mtcoding.metamall.model.log.error.ErrorLogRepository;
import shop.mtcoding.metamall.model.user.User;

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
                LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
                if(loginUser != null){
                    System.out.println(4);
                    ErrorLog errorLog =ErrorLog.builder().userId(loginUser.getId()).msg(e.getMessage()).build();
                    errorLogRepository.save(errorLog);
                }
            }
        }
    }
}
