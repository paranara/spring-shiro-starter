package org.paranora.spring.test.web.configuration;

import org.paranora.spring.test.web.spring.SpringBeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  paranora
 * @description :  TODO
 * @date :  2021/4/2 16:13
 */
@Configuration
//@EnableDiscoveryClient
@ComponentScan(basePackages = {"org.paranora.spring.test.web"})
public class ApplicationConfiguration {

    @Bean
    public BeanFactoryAware beanFactoryAware(){
        return new SpringBeanFactory();
    }

}