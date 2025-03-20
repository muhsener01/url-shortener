package demo.muhsener01.urlshortener.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    public static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    public static <T> T getBean(Class<T> securityConstantsClass) {
        if (CONTEXT == null)
            throw new IllegalStateException("Context is null in SpringContext");

        return CONTEXT.getBean(securityConstantsClass);
    }


    public static Object getBean(String beanName) {
        if (CONTEXT == null)
            throw new IllegalStateException("Context is null in SpringContext");

        return CONTEXT.getBean(beanName);
    }
}
