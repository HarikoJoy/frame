package com.frame.hariko.springboot.web.swagger2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.frame.hariko.springboot.web.swagger2.controller.SwaggerUiController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.readers.parameter.ParameterTypeReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Configuration
@Order(90)
public class Swagger2AutoConfigure {

    @Configuration
    @ConditionalOnClass({SwaggerUiController.class})
    class Swagger2Configure {
        @Autowired
        private ApplicationContext applicationContext;
        @Bean(name="rpcApiInfo")
        public ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .description("<a href=\"swagger/eventDoc.html\">事件文档</a>、<a href=\"swagger/constantDoc.html\">常量文档</a>")
                    .title("Merlin框架rpc接口文档")
                    .build();
        }

        @Bean(name="restApiInfo")
        public ApiInfo restApiInfo() {
            return new ApiInfoBuilder()
                    .description("http://wiki.dashuf.com")
                    .title("Merlin框架restful接口文档")
                    .build();
        }

        /*@Bean(name="rpcDocket")
        public Docket docket(@Qualifier("rpcApiInfo") ApiInfo apiInfo) {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName("rpc")
                    .select()
                    .apis(input ->
                            AnnotatedElementUtils.hasAnnotation(input.declaringClass(), Component.class)
                                    ||  AnnotatedElementUtils.hasAnnotation(input.declaringClass(), Component.class)
                    )
                    .paths(PathSelectors.any())
                    .build()
                    .pathMapping("/")
                    .directModelSubstitute(LocalDate.class, Date.class)
                    .directModelSubstitute(LocalDateTime.class, Date.class)
                    .useDefaultResponseMessages(false)
                    .apiInfo(apiInfo)
                    .forCodeGeneration(true);
        }

        @Bean(name="restDocket")
        public Docket restDocket(@Qualifier("restApiInfo") ApiInfo apiInfo) {
            String basePackage = applicationContext.getBeansWithAnnotation(SpringBootApplication.class)
                    .get(applicationContext.getBeanNamesForAnnotation(SpringBootApplication.class)[0])
                    .getClass().getPackage().getName();
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName("rest")
                    .select()
                    .apis(input ->
                            (!AnnotatedElementUtils.hasAnnotation(input.declaringClass(), Component.class))
                                    &&  (!AnnotatedElementUtils.hasAnnotation(input.declaringClass(), Component.class))
                                    && input.declaringClass().getName().startsWith(basePackage)
                    )
                    .paths(PathSelectors.any())
                    .build()
                    .pathMapping("/")
                    .directModelSubstitute(LocalDate.class, Date.class)
                    .directModelSubstitute(LocalDateTime.class, Date.class)
                    .useDefaultResponseMessages(false)
                    .apiInfo(apiInfo)
                    .forCodeGeneration(true);
        }*/

        /*@Bean(name="parameterTypeReader")
        @Order(Ordered.HIGHEST_PRECEDENCE + 11)
        public ParameterBuilderPlugin parameterTypeReader(){

            return  new ParameterBuilderPlugin(){
                @Override
                public void apply(ParameterContext context) {
                    if (isContract(context) && (context.getOperationContext().httpMethod() == HttpMethod.POST)) {
                        context.parameterBuilder().parameterType("body");
                    } else {
                        ParameterTypeReader.findParameterType(context);
                    }
                }

                @Override
                public boolean supports(DocumentationType delimiter) {
                    return true;
                }

                private boolean isContract(ParameterContext context){
                    return context.getOperationContext().findControllerAnnotation(Component.class).isPresent()
                            || context.getOperationContext().findControllerAnnotation(Component.class).isPresent();
                }
            };
        }*/
    }

    @Configuration
    @ConditionalOnMissingClass({"com.dashuf.swagger.SwaggerUiController"})
    @ComponentScan(basePackages = {"com.dashuf.merlin.springboot.web.swagger2.controller"})
    @EnableSwagger2
    class DefaultSwagger2Configure {
        @Autowired
        private ApplicationContext applicationContext;
        @Bean
        @ConditionalOnMissingBean(ApiInfo.class)
        public ApiInfo defaultApiInfo() {
            return new ApiInfoBuilder()
                    .description("http://wiki.dashuf.com")
                    .title("Merlin框架自动化接口文档")
                    .build();
        }

        @Bean
        @ConditionalOnBean(ApiInfo.class)
        @ConditionalOnMissingBean(Docket.class)
        public Docket defaultDocket(@Autowired @Qualifier("restApiInfo") ApiInfo apiInfo) {
            String basePackage = applicationContext.getBeansWithAnnotation(SpringBootApplication.class)
                    .get(applicationContext.getBeanNamesForAnnotation(SpringBootApplication.class)[0])
                    .getClass().getPackage().getName();
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(basePackage))
                    .paths(PathSelectors.any())
                    .build()
                    .pathMapping("/")
                    .directModelSubstitute(LocalDate.class, Date.class)
                    .directModelSubstitute(LocalDateTime.class, Date.class)
                    .useDefaultResponseMessages(false)
                    .apiInfo(apiInfo)
                    .forCodeGeneration(true);
        }
    }

}
