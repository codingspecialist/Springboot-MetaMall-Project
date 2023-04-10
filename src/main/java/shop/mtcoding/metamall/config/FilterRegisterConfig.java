package shop.mtcoding.metamall.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.metamall.core.filter.JwtVerifyFilter;
import shop.mtcoding.metamall.core.filter.SellerFilter;


@Configuration
public class FilterRegisterConfig {
    @Bean
    public FilterRegistrationBean<?> jwtVerifyFilterAdd() {
        FilterRegistrationBean<JwtVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtVerifyFilter());
        registration.addUrlPatterns("/user/*");
        registration.addUrlPatterns("/seller/*");
        registration.setOrder(1);

        return registration;
    }

    @Bean
    public FilterRegistrationBean<?> sellerFilterAdd() {
        FilterRegistrationBean<SellerFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SellerFilter());
        registration.addUrlPatterns("/seller/*");
        registration.setOrder(1);

        return registration;
    }
}
