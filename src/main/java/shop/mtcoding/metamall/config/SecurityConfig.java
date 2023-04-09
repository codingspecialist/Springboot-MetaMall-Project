package shop.mtcoding.metamall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/login", "/h2-console/**", "/join").permitAll() // login 페이지는 누구나 접근 가능
                .anyRequest().authenticated() // 그 외 요청은 인증된 사용자만 접근 가능
                .and()
                .headers().frameOptions().disable() // X-Frame-Options 비활성화
                .and()
                .formLogin()
                .loginPage("/login").permitAll() // login 페이지는 누구나 접근 가능
                .loginProcessingUrl("/login") // POST 요청 허용
                .defaultSuccessUrl("/")
                .and()
                .logout().logoutSuccessUrl("/login").permitAll() // 로그아웃은 누구나 접근 가능
                .and()
                .csrf().disable(); // CSRF 보안 기능 비활성화
    }
}
