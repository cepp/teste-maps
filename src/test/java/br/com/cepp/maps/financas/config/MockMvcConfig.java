package br.com.cepp.maps.financas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Configuration
public class MockMvcConfig {
    public static final String UTF_8 = "UTF-8";

    @Bean
    public MockMvc mockMvc(WebApplicationContext context) {
        return MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding(UTF_8);
                    chain.doFilter(request, response);
                }, "/*")
                .apply(springSecurity())
                .build();
    }
}
