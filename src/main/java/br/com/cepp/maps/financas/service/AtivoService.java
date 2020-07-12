package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.repository.AtivoRepository;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.AtivoJaExisteException;
import br.com.cepp.maps.financas.resource.handler.AtivoNaoEncontradoException;
import br.com.cepp.maps.financas.resource.handler.AtivoUtilizadoException;
import br.com.cepp.maps.financas.resource.handler.ValidacaoNegocioException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;

@Log4j2
@Service
@Validated
public class AtivoService {
    private final AtivoRepository ativoRepository;

    @Autowired
    public AtivoService(AtivoRepository ativoRepository) {
        this.ativoRepository = ativoRepository;
    }

    @Transactional
    public Ativo incluir(@Valid @NotNull(message = "Objeto request é obrigatório") AtivoRequestDTO ativoRequestDTO) {
        if(this.ativoRepository.existsById(ativoRequestDTO.getCodigo())) {
            throw new AtivoJaExisteException(ativoRequestDTO.getCodigo());
        }
        final Ativo ativo = this.converterDTOParaEntidade(ativoRequestDTO);
        return this.ativoRepository.save(ativo);
    }

    @Transactional
    public Ativo alterar(@Valid @NotNull(message = "Objeto request é obrigatório") AtivoRequestDTO ativoRequestDTO,
                         @NotEmpty(message = "Campo 'codigo é' obrigatório") String codigo) {
        this.validarDatas(ativoRequestDTO.getDataEmissao(), ativoRequestDTO.getDataVencimento());
        final Ativo ativoBD = this.buscarPorCodigo(codigo);
        final Ativo ativoParaAtualizar = ativoBD.comNome(ativoRequestDTO.getNome())
                .comTipoAtivo(ativoRequestDTO.getTipoAtivo()).comDataEmissao(ativoRequestDTO.getDataEmissao())
                .comDataVencimento(ativoRequestDTO.getDataVencimento());
        return this.ativoRepository.save(ativoParaAtualizar);
    }

    @Transactional
    public void remover(@NotEmpty(message = "Campo 'codigo' é obrigatório") String codigo) {
        final Ativo ativoBD = this.buscarPorCodigo(codigo);

        if(this.ativoRepository.existsByCodigoAndPosicoesIsNotEmpty(codigo)) {
            throw new AtivoUtilizadoException(codigo);
        }

        this.ativoRepository.delete(ativoBD);
    }

    private Ativo converterDTOParaEntidade(@Valid @NotNull(message = "Objeto request é obrigatório") AtivoRequestDTO ativoRequestDTO) {
        this.validarDatas(ativoRequestDTO.getDataEmissao(), ativoRequestDTO.getDataVencimento());
        return new Ativo(ativoRequestDTO.getCodigo(), ativoRequestDTO.getNome(), ativoRequestDTO.getTipoAtivo(),
                ativoRequestDTO.getDataEmissao(), ativoRequestDTO.getDataVencimento(), new ArrayList<>());
    }

    protected void validarDatas(LocalDate dataEmissao, LocalDate dataVencimento) {
        if(dataEmissao.compareTo(dataVencimento) >= 0) {
            throw new ValidacaoNegocioException("Data Emissão deve ser sempre anterior à Data Vencimento");
        }
    }

    public Ativo buscarPorCodigo(@NotEmpty(message = "Campo 'codigo' é obrigatório") String codigo) {
        return this.ativoRepository.findById(codigo).orElseThrow(() -> new AtivoNaoEncontradoException(codigo));
    }

    public boolean existsAtivoPorCodigo(@NotEmpty(message = "Campo 'codigo' é obrigatório") String codigo) {
        return this.ativoRepository.existsByCodigo(codigo);
    }
}
