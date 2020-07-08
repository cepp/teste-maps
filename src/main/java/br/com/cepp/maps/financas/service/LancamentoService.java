package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.model.Lancamento;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.repository.LancamentoRepository;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Log4j2
@Service
public class LancamentoService {
    private final LancamentoRepository repository;

    @Autowired
    public LancamentoService(LancamentoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void incluirCredito(@Valid @NotNull(message = "Objeto request é obrigatório") final LancamentoRequestDTO requestDTO,
                               @Valid @NotNull(message = "Conta Corrente é obrigatório") final ContaCorrente contaCorrente) {
        this.incluirLancamento(requestDTO, TipoNatureza.CREDITO, contaCorrente);
    }

    @Transactional
    public void incluirDebito(@Valid @NotNull(message = "Objeto request é obrigatório") final LancamentoRequestDTO requestDTO,
                              @Valid @NotNull(message = "Conta Corrente é obrigatório") final ContaCorrente contaCorrente) {
        this.incluirLancamento(requestDTO, TipoNatureza.DEBITO, contaCorrente);
    }

    private void incluirLancamento(@Valid @NotNull(message = "Objeto request é obrigatório") final LancamentoRequestDTO requestDTO,
                                   @NotNull(message = "Campo 'natureza' é obrigatório") final TipoNatureza natureza,
                                   @Valid @NotNull(message = "Conta Corrente é obrigatório") final ContaCorrente contaCorrente) {
        log.info("Incluindo lançamento {}", natureza);
        log.debug("Request: {}", requestDTO);

        final Lancamento lancamento = this.converterDTOParaEntidade(requestDTO, natureza, contaCorrente);
        log.debug("Entidade: {}", lancamento);

        if(contaCorrente.getSaldoConta().compareTo(lancamento.getValor()) < 0 && natureza.isDebito()) {
            throw new SaldoInsuficienteException();
        }

        final Lancamento lancamentoPersistido = this.repository.save(lancamento);
        log.debug("Entidade: {}", lancamentoPersistido);

        log.info("Lançamento {} incluído", natureza);
    }

    private Lancamento converterDTOParaEntidade(final LancamentoRequestDTO requestDTO, final TipoNatureza tipoNaturza,
                                                final ContaCorrente contaCorrente) {
        return new Lancamento(null, requestDTO.getValor(), requestDTO.getData(), requestDTO.getDescricao(),
                tipoNaturza, contaCorrente.getId());
    }
}
