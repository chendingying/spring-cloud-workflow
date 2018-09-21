package com.spring.cloud.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cloud.common.client.jdbc.JdbcClient;
import com.spring.cloud.common.client.rest.RestClient;
import com.spring.cloud.common.client.rest.ServiceUrl;
import com.spring.cloud.common.exception.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.util.Locale;

/**
 * 程序相关配置
 * @author cdy
 * @create 2018/9/4
 */
@Configuration
public class ApplicationConfig {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @ConfigurationProperties(prefix="serviceUrl")
    public ServiceUrl serviceUrl() {
        return new ServiceUrl();
    }


    @Bean
    public ExceptionFactory exceptionFactory() {
        return new ExceptionFactory(messageSource);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    @Bean
    public JdbcClient jdbcClient() {
        return new JdbcClient(jdbcTemplate);
    }

    @Bean
    public RestClient restClient() {
        return new RestClient(restTemplate, serviceUrl());
    }
}
