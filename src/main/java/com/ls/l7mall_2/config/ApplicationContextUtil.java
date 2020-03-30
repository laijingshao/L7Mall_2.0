package com.ls.l7mall_2.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author laijs
 * @date 2020-3-29-22:46
 */
@Configuration
public class ApplicationContextUtil implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;    
    }
    
    public static <T> T get(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
    
    public static Object get(String name){
        return applicationContext.getBean(name);
    }
}
