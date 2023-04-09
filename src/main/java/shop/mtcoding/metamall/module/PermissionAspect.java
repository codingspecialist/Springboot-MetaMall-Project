package shop.mtcoding.metamall.module;


import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.mapping.Join;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shop.mtcoding.metamall.core.CodeEnum;
import shop.mtcoding.metamall.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Slf4j
@Component
public class PermissionAspect {


    @Pointcut("@annotation(shop.mtcoding.metamall.annotation.Permission)")
    public void cut(){}
    @Pointcut("@annotation(shop.mtcoding.metamall.annotation.Customer)")
    public void customer(){}

    @Around("cut()")
    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String role = request.getAttribute("role").toString();

        log.info("ASPECT ROLE CHEKCING ++> {}",role);
        if(role != null && role.equalsIgnoreCase("SELLER")){
            return proceedingJoinPoint.proceed();
        }else{
            HttpServletResponse response =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            //RequestContextHolder를 사용하면 현재 쓰레드에서 처리되고 있는 요청에 관한 HttpServletRequest와 HttpServletResponse를 가져올 수 있음
            Gson gson = new Gson();

            ResponseDto<?> responseDto =
                    new ResponseDto<>().code(CodeEnum.FORBIDDEN)
                            .msg(CodeEnum.FORBIDDEN.getMessage());

            String temp = gson.toJson(responseDto);

            log.warn("Attempt to access resources without permission {}",role);

            return ResponseEntity.status(CodeEnum.FORBIDDEN.getCode()).body(responseDto);
        }
    }

    @Around("customer()")
    public Object beforeCustomer(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String role = request.getAttribute("role").toString();

        if(role != null && (role.equalsIgnoreCase("CUSTOMER") || role.equalsIgnoreCase("SELLER"))){
            return proceedingJoinPoint.proceed();
        }else{
            HttpServletResponse response
                    = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

            Gson gson = new Gson();

            ResponseDto<?> responseDto =
                    new ResponseDto<>()
                            .code(CodeEnum.FORBIDDEN)
                            .msg(CodeEnum.FORBIDDEN.getMessage());

            String temp = gson.toJson(responseDto);

            log.warn("Attempt to access resources without permission {}",role);

            return ResponseEntity.status(CodeEnum.FORBIDDEN.getCode()).body(responseDto);

        }
    }



}
