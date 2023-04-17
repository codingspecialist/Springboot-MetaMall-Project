package shop.mtcoding.metamall.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @GetMapping()
 * public String ok(User user) 일때
 * User에 session 값을 자동 주입
 * public String ok(@MySessionStore User user)
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MySessionStore {
}
