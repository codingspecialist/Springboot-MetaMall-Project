package shop.minostreet.shoppingmall.handler.aop;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import shop.minostreet.shoppingmall.config.auth.LoginUser;
import shop.minostreet.shoppingmall.handler.exception.MyForbiddenException;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Aspect
@Component
public class MySameUserIdAdvice {

    private final HttpSession session;

    // 깃발에 별칭주기
    @Pointcut("@annotation(shop.minostreet.shoppingmall.handler.annotation.MySameUserIdCheck)")
    public void mySameUserId(){}

    @Before("mySameUserId()")
    public void sameUserIdAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();

        IntStream.range(0, parameters.length).forEach(
                (i) -> {
                    if(parameters[i].getName().equals("id") && parameters[i].getType() == Long.class){
                        LoginUser loginUser = (LoginUser) session.getAttribute("sessionUser");
                        Long id = (Long) args[i];
                        if(loginUser.getUser().getId() != id){
                            throw new MyForbiddenException("해당 페이지에 접근할 권한이 없습니다.");
                        }
                    }
                }
        );
    }
}