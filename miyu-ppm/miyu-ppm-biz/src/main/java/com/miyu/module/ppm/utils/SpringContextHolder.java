package com.miyu.module.ppm.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/***
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 * @auther zhp
 * @create 2020/7/16
 */
@Service
public class SpringContextHolder implements ApplicationContextAware {
    // Spring应用上下文环境
    private static ApplicationContext applicationContext;
    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }
    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    /**
     * 获取对象
     *
     * @param name
     * @return Object
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**

     * 拿到ApplicationContext对象实例后就可以手动获取Bean的注入实例对象

     */

    public static <T> T getBean(Class<T> clazz) {

        return applicationContext.getBean(clazz);

    }


}
