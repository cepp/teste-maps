package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.repository.ContaCorrenteRepository;
import br.com.cepp.maps.financas.resource.handler.ContaNaoEncontradaException;
import br.com.cepp.maps.financas.resource.handler.ContaPosicaoJaExisteException;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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

    @Transactional
    public ContaCorrente atualizarSaldo(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório")final String codigoUsuario,
                                        @NotNull(message = "Campo 'valor' é obrigatório") final BigDecimal valor,
                                        @NotNull(message = "Campo 'natureza' é obrigatório") final TipoNatureza natureza,
                                        @NotNull(message = "Campo 'dataMovimento' é obrigatório") final LocalDate dataMovimento) {
        final ContaCorrente contaCorrente = this.buscarPorCodigoUsuario(codigoUsuario, dataMovimento)
                .orElseGet(() -> this.incluirPosicaoContaCorrente(codigoUsuario, dataMovimento));

        if(contaCorrente.getSaldoConta().compareTo(valor) < 0 && natureza.isDebito()) {
            throw new SaldoInsuficienteException();
        }

        BigDecimal valorAtualizar = natureza.isDebito() ? valor.negate() : valor;
        BigDecimal saldoAtualizado = contaCorrente.getSaldoConta().setScale(2, RoundingMode.DOWN)
                .add(valorAtualizar.setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
        return this.repository.save(contaCorrente.comSaldoAtualizado(saldoAtualizado));
    }

    @Transactional
    public ContaCorrente incluirPosicaoContaCorrente(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario,
                                                     @NotNull(message = "Campo 'dataMovimento' é obrigatório") final LocalDate dataMovimento) {
        if(this.repository.existsByCodigoUsuarioAndData(codigoUsuario, dataMovimento)) {
            throw new ContaPosicaoJaExisteException(codigoUsuario, dataMovimento);
        }

        BigDecimal saldoAnterior = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
        final LocalDate dataInicial = LocalDate.now();
        if(dataInicial.compareTo(dataMovimento) != 0) {
            if (!this.repository.existsByCodigoUsuario(codigoUsuario)) {
                final ContaCorrente posicaoInicial = new ContaCorrente(null, saldoAnterior, codigoUsuario, dataInicial);
                this.repository.save(posicaoInicial);
            }

            saldoAnterior = this.buscarSaldoAnteriorPorUsuarioEData(codigoUsuario, dataMovimento);
        }

        final ContaCorrente contaCorrente = new ContaCorrente(null, saldoAnterior, codigoUsuario, dataMovimento);
        return this.repository.save(contaCorrente);
    }

    @Transactional
    public ContaCorrente atualizarSaldoMovimento(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario,
                                                 @NotNull(message = "Campo 'valor' é obrigatório") final BigDecimal valor,
                                                 @NotNull(message = "Campo 'natureza' é obrigatório") final TipoNatureza natureza,
                                                 @NotNull(message = "Campo 'data' é obrigatório") final LocalDate data) {
        return this.atualizarSaldo(codigoUsuario, valor, natureza, data);
    }

    public ContaCorrente buscarContaCorrentePorCodigoUsuario(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario,
                                                             @NotNull(message = "Campo 'dataMovimento' é obrigatório") final LocalDate dataMovimento) {
        return this.buscarPorCodigoUsuario(codigoUsuario, dataMovimento).orElseThrow(() -> new ContaNaoEncontradaException(codigoUsuario, dataMovimento));
    }

    private Optional<ContaCorrente> buscarPorCodigoUsuario(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario,
                                                           @NotNull(message = "Campo 'dataMovimento' é obrigatório") final LocalDate dataMovimento) {
        return this.repository.findByCodigoUsuarioAndData(codigoUsuario, dataMovimento);
    }

    public boolean existePosicaoPorUsuarioData(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario,
                                               @NotNull(message = "Campo 'data' é obrigatório") final LocalDate data) {
        return this.repository.existsByCodigoUsuarioAndData(codigoUsuario, data);
    }

    public BigDecimal buscarSaldoAnteriorPorUsuarioEData(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario,
                                                         @NotNull(message = "Campo 'data' é obrigatório") final LocalDate data) {
        Optional<ContaCorrente> contaCorrente = this.repository.getFirstByCodigoUsuarioAndDataLessThanOrderByDataDesc(codigoUsuario, data);
        return contaCorrente.isPresent() ? contaCorrente.get().getSaldoConta() : BigDecimal.ZERO;
    }
}
