package br.com.cepp.maps.financas.config;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile({"dev", "test"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${app.build.version:}")
    private String appBuildVersion;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("br.com.cepp.maps.financas.resource"))
                .paths(PathSelectors.any()).build()
                .apiInfo(apiInfo())
                .securitySchemes(Lists.newArrayList(apiKey()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("API Finanças Pessoais")
                .description("Projeto de Finanças pessoais para a prova da MAPS")
                .version(appBuildVersion)
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Finanças Pessoais")
                        .version(this.appBuildVersion)
                        .contact(new Contact().name("Desenvolvedor").email("ceppantoja@gmail.com").url("https://br.linkedin.com/in/ceppantoja/pt"))
                        .description("API criada para participar do processo seletivo da MAPS para desenvolvedor Java")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))

                .schemaRequirement("http", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .description("Servidor de autenticação TOTVS")
                        .name("Servidor de autenticação TOTVS")
                        .in(SecurityScheme.In.HEADER)
                        .scheme("basic"));
    }
}
