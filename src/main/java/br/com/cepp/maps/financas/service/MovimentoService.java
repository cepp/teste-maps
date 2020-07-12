package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.AtivoValor;
import br.com.cepp.maps.financas.model.Estoque;
import br.com.cepp.maps.financas.model.Movimento;
import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.repository.MovimentoRepository;
import br.com.cepp.maps.financas.resource.dto.EstoqueResponseDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoResponseDTO;
import br.com.cepp.maps.financas.resource.handler.AtivoPeriodoInvalidoException;
import br.com.cepp.maps.financas.resource.handler.MovimentoFinalSemanaException;
import br.com.cepp.maps.financas.resource.handler.MovimentoNaoEcontradoException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Log4j2
@Service
@Validated
public class MovimentoService {
    private final MovimentoRepository repository;
    private final EstoqueService estoqueService;
    private final ContaCorrenteService contaCorrenteService;
    private final AtivoValorService ativoValorService;

    @Autowired
    public MovimentoService(MovimentoRepository repository, EstoqueService estoqueService,
                            ContaCorrenteService contaCorrenteService, AtivoValorService ativoValorService) {
        this.repository = repository;
        this.estoqueService = estoqueService;
        this.contaCorrenteService = contaCorrenteService;
        this.ativoValorService = ativoValorService;
    }

    @Transactional
    public void compra(@Valid @NotNull(message = "Objeto request é obrigatório") MovimentoRequestDTO requestDTO,
                       @NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") String codigoUsuario) {
        this.incluirMovimentacao(requestDTO, TipoMovimento.COMPRA, codigoUsuario);
    }

    @Transactional
    public void venda(@Valid @NotNull(message = "Objeto request é obrigatório") MovimentoRequestDTO requestDTO,
                      @NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") String codigoUsuario) {
        this.incluirMovimentacao(requestDTO, TipoMovimento.VENDA, codigoUsuario);
    }

    private void incluirMovimentacao(@Valid @NotNull(message = "Objeto request é obrigatório") MovimentoRequestDTO requestDTO,
                                     @NotNull(message = "Campo 'tipoMovimento' é obrigatório") TipoMovimento tipoMovimento,
                                     @NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") String codigoUsuario) {
        final Movimento movimento = this.converterDTOParaEntidade(requestDTO, tipoMovimento);
        final Movimento movimentoPersistido = this.repository.save(movimento);
        this.estoqueService.atualizaEstoque(movimentoPersistido);
        final TipoNatureza tipoNatureza = TipoMovimento.COMPRA.equals(tipoMovimento) ? TipoNatureza.DEBITO : TipoNatureza.CREDITO;
        this.contaCorrenteService.atualizarSaldoMovimento(codigoUsuario, movimentoPersistido.getValor(), tipoNatureza, movimento.getDataMovimento());
    }

    private Movimento converterDTOParaEntidade(@Valid @NotNull(message = "Objeto request é obrigatório") MovimentoRequestDTO requestDTO,
                                               @NotNull(message = "Campo 'tipoMovimento' é obrigatório") TipoMovimento tipoMovimento) {
        final AtivoValor ativoValor = this.ativoValorService.buscarPorAtivoEData(requestDTO.getAtivo(), requestDTO.getData());
        this.validarDataMovimento(requestDTO, ativoValor.getAtivo());
        final BigDecimal valor = this.calcularValorMovimento(requestDTO, ativoValor);
        return new Movimento(null, ativoValor, requestDTO.getData(), requestDTO.getQuantidade(), valor, tipoMovimento);
    }

    public Page<MovimentoResponseDTO> buscarPosicaoPorPeriodo(@NotNull(message = "Campo 'dataInicio' é obrigatório") LocalDate dataInicio,
                                                              @NotNull(message = "Campo 'dataFim' é obrigatório") LocalDate dataFim,
                                                              @NotNull(message = "Paginação é obrigatória") Pageable pageable) {
        Page<Movimento> movimentos = this.repository.findByDataMovimentoBetweenOrderByDataMovimentoDesc(dataInicio, dataFim, pageable)
                .orElseThrow(() -> new MovimentoNaoEcontradoException(dataInicio, dataFim));

        if(movimentos.isEmpty()) {
            throw new MovimentoNaoEcontradoException(dataInicio, dataFim);
        }

        return movimentos.map(this::converterMovimentoParaDTO);
    }

    public Page<EstoqueResponseDTO> buscarPorDataPosicao(@NotNull(message = "Campo 'dataPosicao' é obrigatório") LocalDate dataPosicao,
                                                         Pageable pageable) {
        Page<Estoque> listaPosicao = this.estoqueService.buscarPorDataPosicao(dataPosicao, pageable);
        return listaPosicao.map(estoque ->  this.converterEstoqueParaDTO(estoque, dataPosicao));
    }

    private BigDecimal calcularLucro(AtivoValor ativo) {
        final BigDecimal totalVenda = this.repository.somaValorPorAtivoTipoMovimento(ativo.getAtivo().getCodigo(), TipoMovimento.VENDA).orElse(BigDecimal.ZERO).setScale(0, RoundingMode.DOWN);
        final BigDecimal totalCompra = this.repository.somaValorPorAtivoTipoMovimento(ativo.getAtivo().getCodigo(), TipoMovimento.COMPRA).orElse(BigDecimal.ZERO).setScale(0, RoundingMode.DOWN);
        return totalVenda.subtract(totalCompra).setScale(0, RoundingMode.DOWN);
    }

    private BigDecimal calcularRendimento(AtivoValor ativoValor) {
        final BigDecimal totalPrecoCompra = this.repository.somaPrecoPorAtivoTipoMovimento(ativoValor.getAtivo().getCodigo(), TipoMovimento.COMPRA).orElse(BigDecimal.ZERO);
        Long quantidadeCompras = this.repository.countByAtivoValor_AtivoAndTipoMovimento(ativoValor.getAtivo(), TipoMovimento.COMPRA);
        final BigDecimal mediaPrecoCompra = quantidadeCompras == 0L ? BigDecimal.ZERO : totalPrecoCompra.divide(BigDecimal.valueOf(quantidadeCompras),0, RoundingMode.DOWN);
        final BigDecimal rendimento = quantidadeCompras == 0L ? BigDecimal.ZERO : ativoValor.getValor().divide(mediaPrecoCompra, 0, RoundingMode.DOWN);
        log.debug("{}: {}", ativoValor.getAtivo().getCodigo(), rendimento);
        return rendimento;
    }

    private BigDecimal calcularValorMovimento(@Valid @NotNull(message = "Objeto request é obrigatório") MovimentoRequestDTO requestDTO,
                                              @Valid @NotNull(message = "Ativo Valor é obrigatório") final AtivoValor ativoValor) {
        return ativoValor.getValor().multiply(requestDTO.getQuantidade()).setScale(0, RoundingMode.DOWN);
    }

    private void validarDataMovimento(@Valid @NotNull(message = "Objeto request é obrigatório") MovimentoRequestDTO requestDTO,
                                      @Valid @NotNull(message = "Ativo é obrigatório") Ativo ativo) {
        if(DayOfWeek.SATURDAY.equals(requestDTO.getData().getDayOfWeek()) || DayOfWeek.SUNDAY.equals(requestDTO.getData().getDayOfWeek())) {
            throw new MovimentoFinalSemanaException();
        }

        if(requestDTO.getData().compareTo(ativo.getDataEmissao()) < 0 || requestDTO.getData().compareTo(ativo.getDataVencimento()) > 0) {
            throw new AtivoPeriodoInvalidoException(requestDTO.getData(), ativo.getCodigo());
        }
    }

    private EstoqueResponseDTO converterEstoqueParaDTO(Estoque estoque, LocalDate dataPosicao) {
        final AtivoValor ativoValor = this.ativoValorService.buscarPorAtivoEData(estoque.getAtivo().getCodigo(),
                dataPosicao);
        final BigDecimal rendimento = calcularRendimento(ativoValor);

        final BigDecimal lucro = calcularLucro(ativoValor);
        log.debug("{}: {}", estoque.getAtivo().getCodigo(), lucro);

        return new EstoqueResponseDTO(estoque.getAtivo().getCodigo(), estoque.getAtivo().getTipoAtivo(), estoque.getQuantidade(),
                estoque.getDataPosicao(), lucro, rendimento, estoque.getValor());
    }

    private MovimentoResponseDTO converterMovimentoParaDTO(Movimento movimento) {
        return new MovimentoResponseDTO(movimento.getAtivoValor().getAtivo().getCodigo(), movimento.getDataMovimento(),
                movimento.getQuantidade(), movimento.getValor(), movimento.getTipoMovimento());
    }
}
