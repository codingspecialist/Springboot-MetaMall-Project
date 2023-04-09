package shop.mtcoding.metamall.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.metamall.core.filter.MyJwtVerifyFilter;


@Configuration
public class MyFilterRegisterConfig {

    @Bean
    public FilterRegistrationBean<?> jwtVerifyFilterAdd() {
        FilterRegistrationBean<MyJwtVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MyJwtVerifyFilter());
        registration.addUrlPatterns("/users/*");
        registration.addUrlPatterns("/products/*");
        registration.addUrlPatterns("/orders/*");
        registration.addUrlPatterns("/admin/*");
        registration.setOrder(1);
        return registration;
    }

}
