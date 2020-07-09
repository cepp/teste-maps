package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.AbstractDataTest;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
public abstract class AbstractResourceTest extends AbstractDataTest {
    @Getter(value = AccessLevel.PACKAGE)
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    protected static final String UTF_8 = "UTF-8";

    @BeforeEach
    public final void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding(UTF_8);
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }
}
