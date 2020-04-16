package com.tollParking.library.tollParkingLibrary.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerSpringFoxConfig {

  /**
   * After the Docket bean is defined, its select() method returns an instance of
   * ApiSelectorBuilder, which provides a way to control the endpoints exposed by Swagger.
   * Predicates for selection of RequestHandlers can be configured with the help of
   * RequestHandlerSelectors and PathSelectors.
   * @return a {@link Docket}
   */
  @Bean
  public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                /* Using the following selectors,
                   we expose only methods defined in the controller folder in the swagger UI documentation
                 */
                .apis(RequestHandlerSelectors.basePackage("com.tollParking.library.tollParkingLibrary.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Toll Parking Library Api",
                "Swagger documentation for Toll Parking Library Api Rest API",
                "1.0",
                "urn:tos",
                new Contact(
                        "Toll Parking Library Api Contact",
                        "www.tollParkingLibraryApi.com",
                        "info@tollParkingLibraryApi.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList());
    }
}
