package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Estoque;
import br.com.cepp.maps.financas.model.Movimento;
import br.com.cepp.maps.financas.repository.EstoqueRepository;
import br.com.cepp.maps.financas.resource.handler.EstoqueNaoEncontradoException;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import br.com.cepp.maps.financas.utils.BigDecimalUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Log4j2
@Service
@Validated
public class EstoqueService {
    private final EstoqueRepository repository;

    @Autowired
    public EstoqueService(EstoqueRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void atualizaEstoque(@Valid @NotNull(message = "Movimento é obrigatório") final Movimento movimento) {
        Optional<Estoque> optionalEstoque = this.repository.findByAtivo_CodigoAndDataPosicao(movimento.getAtivoValor().getAtivo().getCodigo(), movimento.getDataMovimento());

        final Estoque estoque = optionalEstoque.orElse(new Estoque(null, BigDecimal.ZERO.setScale(2, RoundingMode.DOWN),
                movimento.getAtivoValor().getAtivo(), movimento.getDataMovimento(), BigDecimal.ZERO.setScale(2, RoundingMode.DOWN)));

        final BigDecimal quantidade = BigDecimalUtils.ajustarValor(movimento.getQuantidade(), estoque.getQuantidade(), movimento.getTipoMovimento());
        final BigDecimal valor = BigDecimalUtils.ajustarValor(movimento.getValor(), estoque.getValor(), movimento.getTipoMovimento());

        if(BigDecimal.ZERO.compareTo(quantidade) > 0 || BigDecimal.ZERO.compareTo(valor) > 0) {
            throw new SaldoInsuficienteException();
        }

        final Estoque estoqueAtualizado = estoque.comQuantidadeEValor(quantidade, valor);

        this.repository.save(estoqueAtualizado);
    }

    public Page<Estoque> buscarPorDataPosicao(@NotNull(message = "Campo 'dataPosicao' é obrigatório") final LocalDate dataPosicao,
                                              @NotNull(message = "Paginação é obrigatória") final Pageable pageable) {
        Page<Estoque> pageEstoque = this.repository.findByDataPosicao(dataPosicao, pageable)
                .orElseThrow(() -> new EstoqueNaoEncontradoException(dataPosicao));

        if(pageEstoque.isEmpty()) {
            throw new EstoqueNaoEncontradoException(dataPosicao);
        }

        return pageEstoque;
    }
}
