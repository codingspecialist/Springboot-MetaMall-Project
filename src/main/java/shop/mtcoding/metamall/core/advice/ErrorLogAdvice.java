package shop.mtcoding.metamall.core.advice;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import shop.mtcoding.metamall.dto.user.UserRequest;

@Aspect
@Component
public class ErrorLogAdvice {

    //깃발에 별칭주기
    @Pointcut("@annotation(shop.mtcoding.metamall.core.anotation.ErrorLog)")
    public void errorLog(){}


    @Before("errorLog()")
    public void errorAdvice(JoinPoint jp) throws Throwable{
        Object[] args = jp.getArgs();

        for (Object arg : args) {
            if(arg instanceof UserRequest.LoginDto){

                System.out.println(((UserRequest.LoginDto) arg).getUsername()+"님 안녕");
            }
        }
    }
}
