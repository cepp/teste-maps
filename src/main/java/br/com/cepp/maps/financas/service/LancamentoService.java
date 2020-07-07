package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.model.Lancamento;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.repository.LancamentoRepository;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.ContaNaoEncontradaException;
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
    private final ContaCorrenteService contaCorrenteService;

    @Autowired
    public LancamentoService(LancamentoRepository repository, ContaCorrenteService contaCorrenteService) {
        this.repository = repository;
        this.contaCorrenteService = contaCorrenteService;
    }

    @Transactional
    public void incluirCredito(@Valid @NotNull(message = "Objeto request é obrigatório") final LancamentoRequestDTO requestDTO) {
        this.incluirLancamento(requestDTO, TipoNatureza.CREDITO);
    }

    @Transactional
    public void incluirDebito(@Valid @NotNull(message = "Objeto request é obrigatório") final LancamentoRequestDTO requestDTO) {
        this.incluirLancamento(requestDTO, TipoNatureza.DEBITO);
    }

    private void incluirLancamento(@Valid @NotNull(message = "Objeto request é obrigatório") final LancamentoRequestDTO requestDTO,
                                   @NotNull(message = "Campo 'natureza' é obrigatório") final TipoNatureza natureza) {
        log.info("Incluindo lançamento {}", natureza);
        log.debug("Request: {}", requestDTO);

        final Lancamento lancamento = this.converterDTOParaEntidade(requestDTO, natureza);
        log.debug("Entidade: {}", lancamento);

        final Lancamento lancamentoPersistido = this.repository.save(lancamento);
        log.debug("Entidade: {}", lancamentoPersistido);

        log.info("Lançamento {} incluído", natureza);
    }

    private Lancamento converterDTOParaEntidade(final LancamentoRequestDTO requestDTO, final TipoNatureza tipoNaturza) {
        final ContaCorrente contaCorrente = this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(requestDTO.getCodigoUsuario())
                .orElseThrow(() -> new ContaNaoEncontradaException(requestDTO.getCodigoUsuario()));

        final ContaCorrente contaCorrenteSaldoAtualizado = this.contaCorrenteService.atualizarSaldo(contaCorrente,
                requestDTO.getValor(), tipoNaturza);

        return new Lancamento(null, requestDTO.getValor(), requestDTO.getData(), requestDTO.getDescricao(),
                tipoNaturza, contaCorrenteSaldoAtualizado.getId());
    }
}
