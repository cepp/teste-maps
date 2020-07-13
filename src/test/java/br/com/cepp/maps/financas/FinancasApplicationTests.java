package br.com.cepp.maps.financas;

import br.com.cepp.maps.financas.config.LoadDataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FinancasApplicationTests {

    @Autowired
    private LoadDataConfig loadDataConfig;

    @Test
    void contextLoads() {
        assertNotNull(this.loadDataConfig);
    }

}
