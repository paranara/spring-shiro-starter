package org.paranora.spring.test.web.configuration;

import org.paranora.spring.test.web.spring.SpringBeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableDiscoveryClient
@ComponentScan(basePackages = {"org.paranora.spring.test.web"})
public class ApplicationConfiguration {

    @Bean
    public BeanFactoryAware beanFactoryAware(){
        return new SpringBeanFactory();
    }

}