package shop.mtcoding.metamall.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.metamall.core.filter.JwtVerifyFilter;


@Configuration
public class FilterRegisterConfig {

    @Bean
    public FilterRegistrationBean<?> jwtVerifyFilterAddForItem() {
        FilterRegistrationBean<JwtVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtVerifyFilter());
        registration.addUrlPatterns("/validate-token");
        registration.addUrlPatterns("/item/*");
        registration.addUrlPatterns("/items");
        registration.addUrlPatterns("/order/*");
        registration.addUrlPatterns("/orders/*");
        registration.setOrder(1);
        return registration;
    }
}
