package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.AtivoUsuario;
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
import br.com.cepp.maps.financas.resource.handler.MovimentoValorDiferenteException;
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
    private final AtivoUsuarioService ativoUsuarioService;

    @Autowired
    public MovimentoService(final MovimentoRepository repository, final EstoqueService estoqueService,
                            final ContaCorrenteService contaCorrenteService, final AtivoValorService ativoValorService,
                            final AtivoUsuarioService ativoUsuarioService) {
        this.repository = repository;
        this.estoqueService = estoqueService;
        this.contaCorrenteService = contaCorrenteService;
        this.ativoValorService = ativoValorService;
        this.ativoUsuarioService = ativoUsuarioService;
    }

    @Transactional
    public void compra(@Valid @NotNull(message = "Objeto request é obrigatório") final MovimentoRequestDTO requestDTO,
                       @NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario) {
        this.incluirMovimentacao(requestDTO, TipoMovimento.COMPRA, codigoUsuario);
    }

    @Transactional
    public void venda(@Valid @NotNull(message = "Objeto request é obrigatório") final MovimentoRequestDTO requestDTO,
                      @NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario) {
        this.incluirMovimentacao(requestDTO, TipoMovimento.VENDA, codigoUsuario);
    }

    private void incluirMovimentacao(@Valid @NotNull(message = "Objeto request é obrigatório") final MovimentoRequestDTO requestDTO,
                                     @NotNull(message = "Campo 'tipoMovimento' é obrigatório") final TipoMovimento tipoMovimento,
                                     @NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario) {
        final Movimento movimento = this.converterDTOParaEntidade(requestDTO, tipoMovimento);
        final AtivoUsuario ativoUsuario = this.ativoUsuarioService.atualizar(requestDTO, codigoUsuario,
                movimento.getAtivoValor().getAtivo(), tipoMovimento);
        final Movimento movimentoComCodigoAtivoUsuario = movimento.comCodigoAtivoUsuario(ativoUsuario);
        this.estoqueService.atualizaEstoque(movimentoComCodigoAtivoUsuario);
        final Movimento movimentoPersistido = this.repository.save(movimentoComCodigoAtivoUsuario);
        final TipoNatureza tipoNatureza = TipoMovimento.COMPRA.equals(tipoMovimento) ? TipoNatureza.DEBITO : TipoNatureza.CREDITO;
        this.contaCorrenteService.atualizarSaldoMovimento(codigoUsuario, movimentoPersistido.getValor(), tipoNatureza, movimento.getDataMovimento());
    }

    private Movimento converterDTOParaEntidade(@Valid @NotNull(message = "Objeto request é obrigatório") final MovimentoRequestDTO requestDTO,
                                               @NotNull(message = "Campo 'tipoMovimento' é obrigatório") final TipoMovimento tipoMovimento) {
        final AtivoValor ativoValor = this.ativoValorService.buscarPorAtivoEData(requestDTO.getAtivo(), requestDTO.getData());
        this.validarDataMovimento(requestDTO, ativoValor.getAtivo());
        final BigDecimal valor = this.calcularValorMovimento(requestDTO, ativoValor);
        return new Movimento(null, ativoValor, requestDTO.getData(), requestDTO.getQuantidade(), valor, tipoMovimento, null);
    }

    public Page<MovimentoResponseDTO> buscarPosicaoPorPeriodo(@NotNull(message = "Campo 'dataInicio' é obrigatório") final LocalDate dataInicio,
                                                              @NotNull(message = "Campo 'dataFim' é obrigatório") final LocalDate dataFim,
                                                              @NotNull(message = "Paginação é obrigatória") final Pageable pageable,
                                                              @NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario) {
        Page<Movimento> movimentos = this.repository.findByDataMovimentoBetweenAndAtivoUsuario_CodigoUsuarioOrderByDataMovimentoDesc(dataInicio,
                dataFim, codigoUsuario, pageable).orElseThrow(() -> new MovimentoNaoEcontradoException(dataInicio, dataFim));

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
        final BigDecimal totalVenda = this.repository.somaValorPorAtivoTipoMovimento(ativo.getAtivo().getCodigo(), TipoMovimento.VENDA).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.DOWN);
        final BigDecimal totalCompra = this.repository.somaValorPorAtivoTipoMovimento(ativo.getAtivo().getCodigo(), TipoMovimento.COMPRA).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.DOWN);
        return totalVenda.subtract(totalCompra).setScale(2, RoundingMode.DOWN);
    }

    private BigDecimal calcularRendimento(AtivoValor ativoValor) {
        final BigDecimal totalPrecoCompra = this.repository.somaPrecoPorAtivoTipoMovimento(ativoValor.getAtivo().getCodigo(), TipoMovimento.COMPRA).orElse(BigDecimal.ZERO);
        Long quantidadeCompras = this.repository.countByAtivoValor_AtivoAndTipoMovimento(ativoValor.getAtivo(), TipoMovimento.COMPRA);
        final BigDecimal mediaPrecoCompra = quantidadeCompras == 0L ? BigDecimal.ZERO : totalPrecoCompra.divide(BigDecimal.valueOf(quantidadeCompras),2, RoundingMode.DOWN);
        final BigDecimal rendimento = quantidadeCompras == 0L ? BigDecimal.ZERO : ativoValor.getValor().divide(mediaPrecoCompra, 2, RoundingMode.DOWN);
        log.debug("{}: {}", ativoValor.getAtivo().getCodigo(), rendimento);
        return rendimento;
    }

    private BigDecimal calcularValorMovimento(@Valid @NotNull(message = "Objeto request é obrigatório") MovimentoRequestDTO requestDTO,
                                              @Valid @NotNull(message = "Ativo Valor é obrigatório") final AtivoValor ativoValor) {
        final BigDecimal valorCalculado = ativoValor.getValor().setScale(2, RoundingMode.DOWN).multiply(requestDTO.getQuantidade()).setScale(2, RoundingMode.DOWN);
        if(valorCalculado.compareTo(requestDTO.getValor()) != 0) {
            throw new MovimentoValorDiferenteException(requestDTO.getValor());
        }
        return valorCalculado;
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
