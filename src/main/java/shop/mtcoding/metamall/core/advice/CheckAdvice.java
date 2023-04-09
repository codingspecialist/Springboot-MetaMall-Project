package shop.mtcoding.metamall.core.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception403;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.user.User;

import javax.servlet.http.HttpSession;

@Aspect
@Order(2)
@Component
@RequiredArgsConstructor
public class CheckAdvice {

    private final HttpSession session;

    @Pointcut("@annotation(shop.mtcoding.metamall.core.annotation.PwdCk)")
    public void pwd(){}

    @Pointcut("@annotation(shop.mtcoding.metamall.core.annotation.RoleCk.User)")
    public void roleUser(){}

    @Pointcut("@annotation(shop.mtcoding.metamall.core.annotation.RoleCk.Seller)")
    public void roleSeller(){}

    @Pointcut("@annotation(shop.mtcoding.metamall.core.annotation.RoleCk.Admin)")
    public void roleAdmin(){}

    @Before("pwd()")
    public void pwdCheck(JoinPoint jp){
        boolean valid = true;
        for(Object arg : jp.getArgs()){
            if(arg instanceof UserRequest.SignUpDto){
                UserRequest.SignUpDto dto = (UserRequest.SignUpDto) arg;
                valid = dto.getPassword().equals(dto.getPasswordCheck());
            }
        }
        if(!valid) throw new Exception400("비밀번호가 일치하지 않습니다");
    }

    @Before("roleUser()")
    public void roleUserCheck(JoinPoint jp){
        boolean role = roleCheck(jp, "SELLER");
        if(!role) throw new Exception403("권한이 없습니다");
    }

    @Before("roleSeller()")
    public void roleSellerCheck(JoinPoint jp){
        boolean role = roleCheck(jp, "USER");
        if(!role) throw new Exception403("권한이 없습니다");
    }

    @Before("roleAdmin()")
    public void roleAdminCheck(JoinPoint jp){
        boolean role = roleCheck(jp, "ADMIN");
        if(!role) throw new Exception403("권한이 없습니다");
    }

    private boolean roleCheck(JoinPoint jp, String role){
        boolean result = true;
        for(Object arg:jp.getArgs()){
            if(arg instanceof User) {
                if(role.equals("ADMIN")) result = ((User) arg).getRole().equals(role);
                else result = !((User) arg).getRole().equals(role);
            }
        }
        return result;
    }
}
