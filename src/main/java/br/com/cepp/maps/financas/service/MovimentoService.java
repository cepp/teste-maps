package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.Estoque;
import br.com.cepp.maps.financas.model.Movimento;
import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.repository.MovimentoRepository;
import br.com.cepp.maps.financas.resource.dto.EstoqueResponseDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.AtivoPeriodoInvalidoException;
import br.com.cepp.maps.financas.resource.handler.MovimentoFinalSemanaException;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@Validated
public class MovimentoService {
    private final MovimentoRepository repository;
    private final EstoqueService estoqueService;
    private final AtivoService ativoService;
    private final ContaCorrenteService contaCorrenteService;

    @Autowired
    public MovimentoService(MovimentoRepository repository, EstoqueService estoqueService, AtivoService ativoService,
                            ContaCorrenteService contaCorrenteService) {
        this.repository = repository;
        this.estoqueService = estoqueService;
        this.ativoService = ativoService;
        this.contaCorrenteService = contaCorrenteService;
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
        final Ativo ativo = this.ativoService.buscarPorCodigo(requestDTO.getAtivo());

        this.validarDataMovimento(requestDTO, ativo);

        // TODO: buscar valor do ativo pela data
//        final BigDecimal valor = ativo.getPreco()
//                .multiply(requestDTO.getQuantidade())
//                .setScale(0, RoundingMode.DOWN);
        final BigDecimal valor = BigDecimal.TEN;

        return new Movimento(null, ativo, requestDTO.getData(), requestDTO.getQuantidade(), valor,
                tipoMovimento);
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

    public Estoque buscarPosicaoPorCodigoEData(@NotEmpty(message = "Campo 'ativo' é obrigatório") String codigo,
                                               @NotNull(message = "Campo 'dataPosicao' é obrigatório") LocalDate dataPosicao) {
        return this.estoqueService.buscarPosicaoPorAtivo(codigo, dataPosicao);
    }

    public List<EstoqueResponseDTO> buscarPorDataPosicao(@NotNull(message = "Campo 'dataPosicao' é obrigatório") LocalDate dataPosicao) {
        List<Estoque> listaPosicao = this.estoqueService.buscarPorDataPosicao(dataPosicao);
        List<EstoqueResponseDTO> responseDTO = new ArrayList<>();
        listaPosicao.forEach(estoque -> {
            final BigDecimal totalCompra = calcularRendimento(estoque.getAtivo());

            final BigDecimal lucro = calcularLucro(estoque.getAtivo());
            log.debug("{}: {}", estoque.getAtivo().getCodigo(), lucro);

            final EstoqueResponseDTO estoqueResponseDTO = this.converterEntidadeParaDTO(estoque, lucro, totalCompra);
            responseDTO.add(estoqueResponseDTO);
        });

        return responseDTO;
    }

    private EstoqueResponseDTO converterEntidadeParaDTO(Estoque estoque, BigDecimal lucro, BigDecimal rendimento) {
        return new EstoqueResponseDTO(estoque.getAtivo().getCodigo(), estoque.getAtivo().getTipoAtivo(), estoque.getQuantidade(), estoque.getDataPosicao(), lucro, rendimento, estoque.getValor());
    }

    private BigDecimal calcularLucro(Ativo ativo) {
        final BigDecimal totalVenda = this.repository.somaValorPorAtivoTipoMovimento(ativo.getCodigo(), TipoMovimento.VENDA).orElse(BigDecimal.ZERO).setScale(0, RoundingMode.DOWN);
        final BigDecimal totalCompra = this.repository.somaValorPorAtivoTipoMovimento(ativo.getCodigo(), TipoMovimento.COMPRA).orElse(BigDecimal.ZERO).setScale(0, RoundingMode.DOWN);
        return totalVenda.subtract(totalCompra).setScale(0, RoundingMode.DOWN);
    }

    private BigDecimal calcularRendimento(Ativo ativo) {
        final BigDecimal totalPrecoCompra = this.repository.somaPrecoPorAtivoTipoMovimento(ativo.getCodigo(), TipoMovimento.COMPRA).orElse(BigDecimal.ZERO);
        Long quantidadeCompras = this.repository.countByAtivoAndTipoMovimento(ativo, TipoMovimento.COMPRA);
        final BigDecimal mediaPrecoCompra = quantidadeCompras == 0L ? BigDecimal.ZERO : totalPrecoCompra.divide(BigDecimal.valueOf(quantidadeCompras),0, RoundingMode.DOWN);
        //TODO: buscar o valor do ativo na data
        final BigDecimal rendimento = BigDecimal.ZERO;//quantidadeCompras == 0L ? BigDecimal.ZERO : ativo.getPreco().divide(mediaPrecoCompra, 0, RoundingMode.DOWN);
        log.debug("{}: {}", ativo.getCodigo(), rendimento);
        return rendimento;
    }
}
