package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.repository.ContaCorrenteRepository;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.ContaNaoEncontradaException;
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
import java.math.RoundingMode;

@Log4j2
@Service
@Validated
public class ContaCorrenteService {
    private final ContaCorrenteRepository repository;
    private final LancamentoService lancamentoService;

    @Autowired
    public ContaCorrenteService(ContaCorrenteRepository repository, LancamentoService lancamentoService) {
        this.repository = repository;
        this.lancamentoService = lancamentoService;
    }

    public ContaCorrente buscarContaCorrentePorCodigoUsuario(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") String codigoUsuario) {
        return this.repository.findByCodigoUsuario(codigoUsuario).orElseThrow(() -> new ContaNaoEncontradaException(codigoUsuario));
    }

    @Transactional
    public ContaCorrente atualizarSaldo(@Valid @NotNull(message = "Campo 'contaCorrente' é obrigatório")final ContaCorrente contaCorrente,
                                        @NotNull(message = "Campo 'valor' é obrigatório") final BigDecimal valor,
                                        @NotNull(message = "Campo 'natureza' é obrigatório") final TipoNatureza natureza) {
        if(contaCorrente.getSaldoConta().compareTo(valor) < 0 && natureza.isDebito()) {
            throw new SaldoInsuficienteException();
        }
        BigDecimal valorAtualizar = natureza.isDebito() ? valor.negate() : valor;
        BigDecimal saldoAtualizado = contaCorrente.getSaldoConta().setScale(0, RoundingMode.DOWN)
                .add(valorAtualizar.setScale(0, RoundingMode.DOWN)).setScale(0, RoundingMode.DOWN);
        return this.repository.save(contaCorrente.comSaldoAtualizado(saldoAtualizado));
    }

    @Transactional
    public ContaCorrente incluirContaCorrente(@NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") String codigoUsuario) {
        final ContaCorrente contaCorrente = new ContaCorrente(null, BigDecimal.ZERO.setScale(0, RoundingMode.DOWN), codigoUsuario);
        return this.repository.save(contaCorrente);
    }

    @Transactional
    public void incluirCredito(@Valid @NotNull(message = "Objeto request é obrigatório") final LancamentoRequestDTO requestDTO,
                               @NotEmpty(message = "Usuário é obrigatório") final String codigoUsuario) {
        final ContaCorrente contaCorrente = this.buscarContaCorrentePorCodigoUsuario(codigoUsuario);
        final ContaCorrente contaCorrenteSaldoAtualizado = this.atualizarSaldo(contaCorrente, requestDTO.getValor(), TipoNatureza.CREDITO);
        this.lancamentoService.incluirCredito(requestDTO, contaCorrenteSaldoAtualizado);
    }

    @Transactional
    public void incluirDebito(@Valid @NotNull(message = "Objeto request é obrigatório") final LancamentoRequestDTO requestDTO,
                              @NotEmpty(message = "Usuário é obrigatório") final String codigoUsuario) {
        final ContaCorrente contaCorrente = this.buscarContaCorrentePorCodigoUsuario(codigoUsuario);
        final ContaCorrente contaCorrenteSaldoAtualizado = this.atualizarSaldo(contaCorrente, requestDTO.getValor(), TipoNatureza.DEBITO);
        this.lancamentoService.incluirDebito(requestDTO, contaCorrenteSaldoAtualizado);
    }

    @Transactional
    public ContaCorrente atualizarSaldoMovimento(@Valid @NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario,
                                                 @NotNull(message = "Campo 'valor' é obrigatório") final BigDecimal valor,
                                                 @NotNull(message = "Campo 'natureza' é obrigatório") final TipoNatureza natureza) {
        ContaCorrente contaCorrente = this.buscarContaCorrentePorCodigoUsuario(codigoUsuario);
        return this.atualizarSaldo(contaCorrente, valor, natureza);
    }
}
