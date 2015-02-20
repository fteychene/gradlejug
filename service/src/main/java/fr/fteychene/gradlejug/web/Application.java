package fr.fteychene.gradlejug.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"fr.fteychene.gradlejug"})
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet());
        registration.addUrlMappings("/services/*");
        return registration;
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public ServerProperties creatServerPropertiesBean() {
        ServerProperties n = new ServerProperties();
        // n.setPort(9000);
        return n;
    }

}