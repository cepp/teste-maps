package br.com.cepp.maps.financas.config;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.AtivoValor;
import br.com.cepp.maps.financas.model.Usuario;
import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import br.com.cepp.maps.financas.service.AtivoService;
import br.com.cepp.maps.financas.service.AtivoValorService;
import br.com.cepp.maps.financas.service.UsuarioService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Configuration
public class LoadDataConfig {

    @Bean
    public boolean carregarDados(AtivoService ativoService, AtivoValorService ativoValorService, UsuarioService usuarioService,
                                 PasswordEncoder passwordEncoder) {
        List<Ativo> ativos = new ArrayList<>();
        List<AtivoValor> ativoValores = new ArrayList<>();
        for(int i = 0; i < 127; i++) {
            final String codigoAtivo = String.format("ATIVO%d", i);
            final LocalDate dataEmissao = LocalDate.of(2020, 1, 2);
            final LocalDate dataVencimento = dataEmissao.plusMonths(1);
            final Ativo ativo = new Ativo(codigoAtivo, codigoAtivo, TipoAtivo.RV, dataEmissao, dataVencimento, new ArrayList<>());

            final BigDecimal valor = BigDecimal.valueOf(33.77474737).setScale(8, RoundingMode.DOWN);
            final AtivoValor ativoValor = new AtivoValor(null, dataEmissao, valor, ativo, new ArrayList<>());

            ativos.add(ativo);
            ativoValores.add(ativoValor);
        }
        ativoService.salvar(ativos);
        ativoValorService.salvar(ativoValores);

        log.info("Incluídos {} ativos e {} posições para o dia {}", ativos.size(), ativoValores.size(),
                LocalDate.of(2020, 1, 2));


        List<Usuario> usuarios = new ArrayList<>();
        Usuario root = new Usuario("root", passwordEncoder.encode("spiderman"), true,
                Collections.singletonList("ADMIN"));
        usuarios.add(root);

        for(int i = 0; i < 10; i++) {
            final String login = String.format("usuario%d", i);
            final String senha = passwordEncoder.encode(String.format("senha%d", i));
            Usuario usuario = new Usuario(login, senha, true, Collections.singletonList("USER"));
            usuarios.add(usuario);
        }

        usuarioService.salvar(usuarios);
        log.info("Incluídos {} usuarios", usuarios.size());

        return true;
    }
}
