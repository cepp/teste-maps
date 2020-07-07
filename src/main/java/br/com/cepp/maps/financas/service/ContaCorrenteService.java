package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.repository.ContaCorrenteRepository;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

@Log4j2
@Service
@Validated
public class ContaCorrenteService {
    private final ContaCorrenteRepository repository;

    @Autowired
    public ContaCorrenteService(ContaCorrenteRepository repository) {
        this.repository = repository;
    }

    public Optional<ContaCorrente> buscarContaCorrentePorCodigoUsuario(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") String codigoUsuario) {
        return this.repository.findByCodigoUsuario(codigoUsuario);
    }

    @Transactional
    public ContaCorrente atualizarSaldo(@Valid @NotNull(message = "Campo 'contaCorrente' é obrigatório")final ContaCorrente contaCorrente,
                                        @NotNull(message = "Campo 'valor' é obrigatório") final BigDecimal valor,
                                        @NotNull(message = "Campo 'natureza' é obrigatório") final TipoNatureza natureza) {
        if(contaCorrente.getSaldoConta().compareTo(valor) < 0 && natureza.isDebito()) {
            throw new SaldoInsuficienteException();
        }
        BigDecimal valorAtualizar = natureza.isDebito() ? valor.negate() : valor;
        BigDecimal saldoAtualizado = contaCorrente.getSaldoConta().add(valorAtualizar);
        return this.repository.save(contaCorrente.comSaldoAtualizado(saldoAtualizado));
    }

    @Transactional
    public ContaCorrente incluirContaCorrente(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") String codigoUsuario) {
        final ContaCorrente contaCorrente = new ContaCorrente(null, BigDecimal.ZERO, codigoUsuario);
        return this.repository.save(contaCorrente);
    }
}
