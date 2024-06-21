package cn.wujiangbo.config.swagger;

import cn.wujiangbo.dto.system.SystemConfigDto;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.List;

/**
 * Swagger接口文档配置类
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    //默认路径：http://ip:port/swagger-ui.html

    /** 系统基础配置 */
    @Autowired
    private SystemConfigDto systemConfigDto;

    /** 是否开启swagger */
    @Value("${swagger.enabled}")
    private boolean enabled;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(enabled)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))//扫描所有有注解的api
                //.apis(RequestHandlerSelectors.any())//扫描所有
                .apis(RequestHandlerSelectors.basePackage("cn.wujiangbo.controller"))//扫描包
                .paths(PathSelectors.any())
                .build()
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes());
    }

    /**
     * 安全模式，这里指定token通过头请求头传递
     */
    private List<SecurityScheme> securitySchemes()
    {
        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        apiKeyList.add(new ApiKey("token", "token", In.HEADER.toValue()));
        return apiKeyList;
    }

    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("token", authorizationScopes));
        return securityReferences;
    }

    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title("标题：EasyJava平台_接口文档")
                // 描述
                .description("描述：用户前后端联调接口文档")
                // 作者信息
                .contact(new Contact(systemConfigDto.getAuthor(), null, systemConfigDto.getEmail()))
                // 版本
                .version("版本号:" + systemConfigDto.getVersion())
                .build();
    }
}