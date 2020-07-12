package br.com.cepp.maps.financas;

import br.com.cepp.maps.financas.service.ContaCorrenteService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FinancasApplicationTests {

    @Autowired
    private ContaCorrenteService service;

    @Test
    void contextLoads() {
        assertNotNull(this.service);
    }

}
