package shop.mtcoding.metamall.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.metamall.core.filter.JwtVerifyFilter;

/**
 * 인증처리 구간
 */

@Configuration
public class FilterRegisterConfig {
    @Bean
    public FilterRegistrationBean<?> jwtVerifyFilterAdd() {
        FilterRegistrationBean<JwtVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtVerifyFilter());
        registration.addUrlPatterns("/users/*");
        registration.addUrlPatterns("/products/*");
        registration.addUrlPatterns("/orders/*");
        registration.addUrlPatterns("/admin/*");
        registration.addUrlPatterns("/seller/*");
        registration.setOrder(1);
        return registration;
    }
}
