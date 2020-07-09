package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.Estoque;
import br.com.cepp.maps.financas.model.Movimento;
import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.repository.MovimentoRepository;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
        this.contaCorrenteService.atualizarSaldoMovimento(codigoUsuario, requestDTO.getValor(), tipoNatureza);
    }

    private Movimento converterDTOParaEntidade(@Valid @NotNull(message = "Objeto request é obrigatório") MovimentoRequestDTO requestDTO,
                                               @NotNull(message = "Campo 'tipoMovimento' é obrigatório") TipoMovimento tipoMovimento) {
        final Ativo ativo = this.ativoService.buscarPorCodigo(requestDTO.getAtivo());
        return new Movimento(null, ativo, requestDTO.getData(), requestDTO.getQuantidade(), requestDTO.getValor(),
                tipoMovimento);
    }

    public Estoque buscarPosicaoPorCodigoEData(@NotEmpty(message = "Campo 'ativo' é obrigatório") String codigo,
                                               @NotNull(message = "Campo 'dataPosicao' é obrigatório") LocalDate dataPosicao) {
        return this.estoqueService.buscarPosicaoPorAtivo(codigo, dataPosicao);
    }
}
