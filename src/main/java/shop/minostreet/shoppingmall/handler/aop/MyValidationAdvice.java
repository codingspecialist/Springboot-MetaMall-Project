package shop.minostreet.shoppingmall.handler.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import shop.minostreet.shoppingmall.handler.exception.MyValidationException;

import java.util.HashMap;
import java.util.Map;

@Aspect
//Aspect = PointCut + Advice
@Component
public class MyValidationAdvice {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {

    }

    //@Before, @After
    @Before("postMapping() || putMapping()")    //1. @PostMapping(), @PutMapping() 어노테이션이 붙은 모든 메서드에서
    //: joinPoint 메서드 실행 전 후 제어 가능한 어노테이션
    public void validationAdvice(JoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();  //joinPoint의 매개변수
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                //2. 에러가 존재할 경우 -> 예외 던짐
                BindingResult bindingResult = (BindingResult) arg;
                //담긴 에러를 처리
                if (bindingResult.hasErrors()) {
                    //Map으로 담는다
                    Map<String, String> errorMap = new HashMap<>();
                    for (FieldError error : bindingResult.getFieldErrors()) {
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    }
                    //return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
                    //유효성 검사 예외를 던진다.
                    throw new MyValidationException("유효성검사 실패", errorMap);
                }
            }
        }
    }
}
/**
 * 유효성 검사
 * get, delete, post, put에서 body가 존재하는 post, put만 존재
 */