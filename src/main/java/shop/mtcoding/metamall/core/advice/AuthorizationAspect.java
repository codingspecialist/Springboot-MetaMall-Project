package shop.mtcoding.metamall.core.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {

	@Before("@annotation(shop.mtcoding.metamall.annotation.PermissionCheck)")
	public void checkAuthorization(JoinPoint joinPoint) {
		// 권한 체크 로직 구현
		// 로그인한 사용자의 권한 정보와 메소드에 설정된 권한 정보를 비교하여 인가 여부 판단
		// 인가되지 않은 경우 예외 처리 등 필요한 작업 수행
	}
}