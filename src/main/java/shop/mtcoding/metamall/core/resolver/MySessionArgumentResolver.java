package shop.mtcoding.metamall.core.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.mtcoding.metamall.core.annotation.MySessionStore;
import shop.mtcoding.metamall.core.session.SessionUser;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Configuration
public class MySessionArgumentResolver implements HandlerMethodArgumentResolver {

	private final HttpSession session;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean check1 = parameter.getParameterAnnotation(MySessionStore.class) != null;
		boolean check2 = SessionUser.class.equals(parameter.getParameterType());
		return check1 && check2;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return session.getAttribute("sessionUser");
	}
}