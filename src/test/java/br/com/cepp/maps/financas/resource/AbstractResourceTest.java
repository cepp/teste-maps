package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.AbstractDataTest;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class AbstractResourceTest extends AbstractDataTest {
    @Getter(value = AccessLevel.PACKAGE)
    @Autowired
    private MockMvc mockMvc;
}
