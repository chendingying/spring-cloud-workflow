package com.spring.cloud.identity.constant;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swigger2配置文件
 *
 * @author CDY
 * @time 2017/7/7.
 */
@Configuration
@EnableSwagger2
@ConditionalOnWebApplication
@ConditionalOnProperty(value = "swagger.enable", matchIfMissing = true)
public class SwaggerConfig {
    /**
     * SpringBoot默认已经将classpath:/META-INF/resources/和classpath:/META-INF/resources/webjars/映射
     */

    /**
     * 可以定义多个组，比如本类中定义把test和demo区分开了
     * （访问页面就可以看到效果了）
     *
     */

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(demoApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.spring"))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo demoApiInfo() {
        return new ApiInfoBuilder()
                .title("Apex基本框架项目")
                .description("Apex项目 API文档管理")
                .termsOfServiceUrl("www.apex.com")
                .contact("Hom")
                .version("1.0")
                .build();
    }
}
