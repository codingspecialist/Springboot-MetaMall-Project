package shop.mtcoding.metamall.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.metamall.core.filter.JwtVerifyFilter;


@Configuration
public class FilterRegisterConfig {
    @Bean
    public FilterRegistrationBean<?> jwtVerifyFilterAdd() {
        FilterRegistrationBean<JwtVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtVerifyFilter());
        registration.addUrlPatterns("/user/*"); //토큰
        registration.addUrlPatterns("/products/*"); //토큰
        registration.addUrlPatterns("/orders/*"); //토큰
        registration.addUrlPatterns("/admin/*");
        registration.addUrlPatterns("/seller/*");
        registration.setOrder(1);
        return registration;
    }
}