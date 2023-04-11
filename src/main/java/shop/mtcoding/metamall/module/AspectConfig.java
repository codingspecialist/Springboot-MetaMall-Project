package shop.mtcoding.metamall.module;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import shop.mtcoding.metamall.core.CodeEnum;
import shop.mtcoding.metamall.dto.ResponseDto;

import java.util.HashMap;
import java.util.Map;


/**
 *
 *  BindngResult 체크
 */
@Aspect
@Slf4j
@Component
public class AspectConfig {

    @Pointcut("@annotation(shop.mtcoding.metamall.annotation.BindingCheck)")
    public void pointCut(){}


    @Around("pointCut()")
    public Object module(ProceedingJoinPoint joinPoint) throws Throwable {
        String type = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();

        log.info("VALIDATION TYPE : {}",type);
        log.info("VALIDATION METHOD : {}",method);

        Object[] args = joinPoint.getArgs();

            for(Object arg : args){
                if(arg instanceof BindingResult){
                    BindingResult bindingResult = (BindingResult) arg;

                    if(bindingResult.hasErrors()){
                        Map<String,String> errorMap = new HashMap<>();

                        bindingResult.getFieldErrors().forEach(fieldError -> {
                            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                        });

                        ResponseDto<?> responseDto = new ResponseDto()
                                .code(CodeEnum.INVALID_ARGUMENT)
                                .data(errorMap)
                                .msg(CodeEnum.INVALID_ARGUMENT.getMessage());

                        return ResponseEntity.status(CodeEnum.INVALID_ARGUMENT.getCode()).body(responseDto);
                    }
                }
            }
        return joinPoint.proceed();

    }


}
