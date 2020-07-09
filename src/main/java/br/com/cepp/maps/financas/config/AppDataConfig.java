package br.com.cepp.maps.financas.config;

import br.com.cepp.maps.financas.service.ContaCorrenteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppDataConfig {
    public static final String CODIGO_USUARIO_GLOBAL = "USR_GLOBAL";

    @Bean
    @Profile(value = {"test", "dev"})
    public String loadData(ContaCorrenteService contaCorrenteService) {
        contaCorrenteService.incluirContaCorrente(CODIGO_USUARIO_GLOBAL);
        return "";
    }
}
