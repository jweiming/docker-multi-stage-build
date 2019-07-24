package net.midgard.medrec.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import static com.google.common.base.Predicates.or;
import net.midgard.medrec.domain.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author thor
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Autowired
  private TypeResolver typeResolver;
  
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(paths())
            .build()
            .tags(new Tag("Sample SpringBootService", "All apis relating to MedRec"))
            .additionalModels(
                    typeResolver.resolve(Administrator.class));
  }
  
  @Bean
  UiConfiguration uiConfig() {
    return new UiConfiguration(
            "validatorUrl",// url
            "none", // docExpansion          => none | list
            "", // apiSorter             => alpha
            "schema", // defaultModelRendering => schema
            UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
            true, // enableJsonEditor      => true | false
            true, // showRequestHeaders    => true | false
            60000L);      // requestTimeout => in milliseconds, defaults to null (uses jquery xh timeout)
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title("Sample SpringBoot APIs")
            .version("1.0")
            .description("Sample SpringBoot REST APIs with Spring Boot")
            .contact(new Contact("Ali Mukadam", "http://localhost", "ali.mukadam@oracle.com"))
            .build();
  }

  private Predicate<String> paths() {
    return or(
            regex("/admin.*"));
  }
}
