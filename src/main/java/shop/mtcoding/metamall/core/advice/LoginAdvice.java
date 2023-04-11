package shop.mtcoding.metamall.core.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shop.mtcoding.metamall.core.annotation.Auth;
import shop.mtcoding.metamall.core.exception.Exception401;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.IntStream;

@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class LoginAdvice {
    private final UserRepository userRepository;

    @Around("execution(* shop.mtcoding.metamall.controller.*.*(..))")
    public Object loginUserAdviceAround(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Object[] args = jp.getArgs();
        Parameter[] parameters = method.getParameters();

        int loginUserAopIndex = IntStream.range(0, parameters.length)
                .filter(i -> parameters[i].isAnnotationPresent(Auth.class))
                .findFirst()
                .orElse(-1);

        if (loginUserAopIndex >= 0) {
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = req.getSession();
            LoginUser sessionUser = (LoginUser) session.getAttribute("loginUser");
            User principal = userRepository.findById(sessionUser.getId()).orElseThrow(
                    () -> new Exception401("인증되지 않았습니다")
            );
            args[loginUserAopIndex] = principal;

            // 해당 메서드 실행
            return jp.proceed(args);
        }

        return jp.proceed();
    }
}
