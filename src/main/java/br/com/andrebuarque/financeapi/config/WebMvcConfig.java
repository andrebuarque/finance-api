package br.com.andrebuarque.financeapi.config;

import br.com.andrebuarque.financeapi.interceptor.AccessTokenInterceptor;
import br.com.andrebuarque.financeapi.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserService userService;

    public WebMvcConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessTokenInterceptor(userService));
    }
}
