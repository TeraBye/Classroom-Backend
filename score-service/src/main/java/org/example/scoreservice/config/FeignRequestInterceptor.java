package org.example.scoreservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes instanceof ServletRequestAttributes servletRequestAttributes){
            HttpServletRequest httpRequest = servletRequestAttributes.getRequest();
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null){
                requestTemplate.header("Authorization", authHeader);
            }
        }
    }
}
