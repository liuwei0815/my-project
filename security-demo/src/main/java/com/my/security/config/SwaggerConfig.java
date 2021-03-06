package com.my.security.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
	 
	    @Bean
	    public Docket createRestApi() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com.my.security.controller"))
	                .paths(PathSelectors.any())
	                .build();
	    }
	    
	    


		@Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("swagger-ui.html")
	                .addResourceLocations("classpath:/META-INF/resources/");
	        registry.addResourceHandler("/webjars/**")
	                .addResourceLocations("classpath:/META-INF/resources/webjars/");
	        //自定义页面
	        registry.addResourceHandler("login-demo.html")
            .addResourceLocations("classpath:/resources/");
	        //公共部分自带登录
	        registry.addResourceHandler("login-simple.html")
            .addResourceLocations("classpath:/resources/");
	        //session 失效页面
	        registry.addResourceHandler("sessioninvalid.html")
            .addResourceLocations("classpath:/resources/session/");
	        // 退出登录页面
	      //session 失效页面
	        registry.addResourceHandler("demo-signOut.html")
            .addResourceLocations("classpath:/resources/");
	        
	        
	    }
	 
	 
	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("开发api展示")
	                .description("Swagger2构建RESTful APIs")
	                .termsOfServiceUrl("http://localhost:7017/")
	                //.contact("")
	                .version("1.0")
	                .build();
	    }
 
}