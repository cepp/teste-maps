package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Estoque;
import br.com.cepp.maps.financas.model.Movimento;
import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import br.com.cepp.maps.financas.repository.EstoqueRepository;
import br.com.cepp.maps.financas.resource.handler.EstoqueNaoEncontradoException;
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
import java.time.LocalDate;
import java.util.List;
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
    public void atualizaEstoque(@Valid @NotNull(message = "Movimento é obrigatório") Movimento movimento) {
        Optional<Estoque> optionalEstoque = this.repository.findByAtivo_CodigoAndDataPosicao(movimento.getAtivo().getCodigo(), movimento.getDataMovimento());

        final Estoque estoque = optionalEstoque.orElse(new Estoque(null, BigDecimal.ZERO.setScale(2, RoundingMode.DOWN),
                movimento.getAtivo(), movimento.getDataMovimento(), BigDecimal.ZERO.setScale(0, RoundingMode.DOWN)));

        final BigDecimal quantidade = this.getBigDecimalAjustado(movimento.getQuantidade(), estoque.getQuantidade(), movimento.getTipoMovimento(), 2);
        final BigDecimal valor = this.getBigDecimalAjustado(movimento.getValor(), estoque.getValor(), movimento.getTipoMovimento(), 0);

        if(BigDecimal.ZERO.compareTo(quantidade) > 0 || BigDecimal.ZERO.compareTo(valor) > 0) {
            throw new SaldoInsuficienteException();
        }

        final Estoque estoqueAtualizado = estoque.comQuantidadeEValor(quantidade, valor);

        this.repository.save(estoqueAtualizado);
    }

    public Estoque buscarPosicaoPorAtivo(@NotEmpty(message = "Campo 'ativo' é obrigatório") String ativo,
                                         @NotNull(message = "Campo 'dataPosicao' é obrigatório") LocalDate dataPosicao) {
        return this.repository.findByAtivo_CodigoAndDataPosicao(ativo, dataPosicao).orElseThrow(() -> new EstoqueNaoEncontradoException(ativo, dataPosicao));
    }

    private BigDecimal getBigDecimalAjustado(final BigDecimal valor, final BigDecimal valorAcumulado, TipoMovimento tipoMovimento, Integer precisao) {
        final BigDecimal valorAjustado = TipoMovimento.COMPRA.equals(tipoMovimento) ? valor.negate() : valor;
        return valorAcumulado
                .add(valorAjustado)
                    .setScale(precisao, RoundingMode.DOWN);
    }

    public List<Estoque> buscarPorDataPosicao(@NotNull(message = "Campo 'dataPosicao' é obrigatório") LocalDate dataPosicao) {
        return this.repository.findByDataPosicao(dataPosicao).orElseThrow(() -> new EstoqueNaoEncontradoException(dataPosicao));
    }
}
