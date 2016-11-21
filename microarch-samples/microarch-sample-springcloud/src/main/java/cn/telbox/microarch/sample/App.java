package cn.telbox.microarch.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;


@SpringBootApplication
@EnableSwagger2
public class App
{
    public static void main( String[] args ) {
    SpringApplication.run(App.class, args);
    }

    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("SpringCloudDemo")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/login.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring REST Sample with Swagger")
                .description("Spring REST Sample with Swagger")
                .termsOfServiceUrl("http://www.apache.org/licenses/")
                .license("Apache License Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/")
                .version("2.0")
                .build();
    }
}
