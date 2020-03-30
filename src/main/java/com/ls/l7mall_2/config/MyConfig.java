package com.ls.l7mall_2.config;

import com.ls.l7mall_2.filter.EncodingFilter;
import com.ls.l7mall_2.mapper.UserMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @author laijs
 * @date 2020-3-29-10:09
 */
@Configuration
public class MyConfig implements WebMvcConfigurer {
    
    @Bean
   public FilterRegistrationBean EncodingFilter(){
       FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
       filterRegistrationBean.setFilter(new EncodingFilter());
       filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
       return filterRegistrationBean;
   }
   
}
