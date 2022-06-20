package org.paranora.ssoc.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * The type Cas shiro configuration.
 */
@Order(0)
@Configuration
public class CasShiroConfiguration extends ShiroConfiguration<CasShiroConfig>{

    @Override
    protected CasShiroConfig createConfig() {
        return new CasShiroConfig();
    }

//    protected DefaultWebShiroConfig config = new DefaultWebShiroConfig();
//
//    @Autowired
//    private List<ShiroConfigurer> configurers = Collections.emptyList();
//
//    @PostConstruct
//    public void init() {
//        for (ShiroConfigurer configurer : configurers) {
//            try {
//                configurer.configure(config);
//            } catch (Exception e) {
//                throw new IllegalStateException("Cannot configure shiro conf.", e);
//            }
//        }
//    }
//
//    /**
//     * 单点登出filter
//     * @return
//     */
////    @Bean
////    @Order(Ordered.HIGHEST_PRECEDENCE)
////    public FilterRegistrationBean singleSignOutFilter() {
////        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
////        filterRegistrationBean.setName("singleSignOutFilter");
////        BasicSingleSignOutFilter singleSignOutFilter = new BasicSingleSignOutFilter();
////        singleSignOutFilter.setCasServerUrlPrefix(shiroConfig.getCas().getServer().getPrefixUrl());
////        singleSignOutFilter.setIgnoreInitConfiguration(true);
////        filterRegistrationBean.setFilter(singleSignOutFilter);
////        filterRegistrationBean.addUrlPatterns(".*");
////        filterRegistrationBean.setEnabled(true);
////        return filterRegistrationBean;
////    }
//
//    /**
//     * 单点登出的listener
//     *
//     * @return
//     */
////    @SuppressWarnings({"rawtypes", "unchecked"})
////    @Bean
////    public ServletListenerRegistrationBean<?> singleSignOutHttpSessionListener() {
////        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
////        bean.setListener(new SingleSignOutHttpSessionListener());
////        bean.setEnabled(true);
////        return bean;
////    }
//
//    @Bean(name = "securityManager")
//    @ConditionalOnMissingBean({DefaultWebSecurityManager.class})
//    public DefaultWebSecurityManager generateSecurityManager() {
//        return config.generateSecurityManager();
//    }
//
//    @Bean(name = "shiroFilter")
//    @ConditionalOnMissingBean({ShiroFilterFactoryBean.class})
//    public ShiroFilterFactoryBean generateShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
//        return config.generateShiroFilterFactoryBean(securityManager);
//    }
//
////    @Bean
////    public SessionValidationScheduler sessionValidationScheduler(ValidatingSessionManager sessionManager){
////        QuartzSessionValidationScheduler sessionValidationScheduler=new QuartzSessionValidationScheduler();
////        sessionValidationScheduler.setSessionManager(sessionManager);
////        sessionValidationScheduler.setSessionValidationInterval(1800000);
////        return sessionValidationScheduler;
////    }
//
//    /**
//     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
//     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
//     * @return
//     */
//    @Bean
//    @DependsOn({"lifecycleBeanPostProcessor"})
//    @ConditionalOnMissingBean({DefaultAdvisorAutoProxyCreator.class})
//    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
//        return config.generateAdvisorAutoProxyCreator();
//    }
//
//    /**
//     * 开启 shiro aop注解支持
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean({AuthorizationAttributeSourceAdvisor.class})
//    @ConditionalOnBean({DefaultWebSecurityManager.class})
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
//        return config.generateAuthorizationAttributeSourceAdvisor(securityManager);
//    }

}

