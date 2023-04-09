package shop.mtcoding.metamall.core.advice;


import com.auth0.jwt.interfaces.DecodedJWT;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shop.mtcoding.metamall.core.jwt.JwtProvider;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthorizeAdvice {

    //깃발에 별칭주기
    @Pointcut("@annotation(shop.mtcoding.metamall.core.anotation.Authorize)")
    public void authorize(){}


    @Around("authorize()")
    public Object authAdvice(ProceedingJoinPoint jp) throws Throwable{

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
        DecodedJWT decodedJWT = JwtProvider.verify(jwt);

        return jp.proceed();
    }
}
