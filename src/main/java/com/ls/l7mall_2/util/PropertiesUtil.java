package com.ls.l7mall_2.util;

import com.ls.l7mall_2.config.ApplicationContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;


/**
 * @author laijs
 * @date 2020-3-29-22:44
 */
public class PropertiesUtil {

    public static String getProperty(String key){
        Environment environment = ApplicationContextUtil.get(Environment.class);
        String property = environment.getProperty(key.trim());
        if(StringUtils.isBlank(property)){
            return null;
        }
        return property.trim();
    }

    public static String getProperty(String key,String defaultValue){
        Environment environment = ApplicationContextUtil.get(Environment.class);
        String property = environment.getProperty(key.trim());
        if(StringUtils.isBlank(property)){
            property = defaultValue;
        }
        return property.trim();
    }
    
}
