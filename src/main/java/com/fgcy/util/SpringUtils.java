package com.fgcy.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @Author fgcy
 * @Date 2022/5/26
 */
@Component
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtils.applicationContext = applicationContext;
    }

    private static ApplicationContext getContext() {
        return applicationContext;
    }

    public static Object getBean(String beanId) {
        return SpringUtils.getBean(Object.class, beanId);
    }

    public static <T> T getBean(Class<T> clazz, String beanId) throws ClassCastException {
        ApplicationContext context = SpringUtils.getContext();
        Assert.isTrue(StringUtils.hasText(beanId), "beanId must not null!");
        boolean a = context.containsBean(beanId);
        Assert.isTrue(context.containsBean(beanId), "beanId :[" + beanId + "] is not exist!");
        Object bean = null;
        bean = context.getBean(beanId);
        return (T) bean;
    }



}
