package shop.mtcoding.metamall.core.annotation;

import lombok.NoArgsConstructor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 이 깃발이 적용된 곳에 에러로그를 기록한다.
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyErrorLogRecord {
}
