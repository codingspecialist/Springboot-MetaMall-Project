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
        registration.addUrlPatterns("/products/*");
        registration.addUrlPatterns("/orders/*");
        registration.addUrlPatterns("/admin/*"); // ADMIN
        registration.addUrlPatterns("/seller/*"); // ADMIN, SELLER
        registration.addUrlPatterns("/user/*");
        registration.setOrder(1);
        return registration;
    }
}
