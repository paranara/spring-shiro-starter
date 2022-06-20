package org.paranora.spring.test.web.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class SpringBeanFactory implements BeanFactoryAware {

    public static BeanFactory BeanFactory;

    public static Object getBean(String beanName) {
        return BeanFactory.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> clazs) {
        return clazs.cast(getBean(beanName));
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringBeanFactory.BeanFactory = beanFactory;
    }
}
