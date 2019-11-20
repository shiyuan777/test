package com.jixunkeji.configuration;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @TODO:
 * @Author:shiyuan
 * @Data :2019/9/4 10:14
 * @Version:1.0
 **/
@Order(2)
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
//@Profile({"prod","local"})
public class Swagger2Configuration {
    @Bean
    public Docket createRestApi() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> heads = new ArrayList<Parameter>();
        parameterBuilder.name("token")
                .description("访问令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        heads.add(parameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))//这是注意的代码
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("蓝牙锁接口文档")
                .description("蓝牙锁相关接口的文档")
                .termsOfServiceUrl("http://www.baidu.com")
                .version("1.0")
                .build();
    }
}
