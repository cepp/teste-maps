package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.repository.AtivoRepository;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.AtivoJaExisteException;
import br.com.cepp.maps.financas.resource.handler.AtivoNaoEncontradoException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Log4j2
@Service
@Validated
public class AtivoService {
    private final AtivoRepository repository;

    @Autowired
    public AtivoService(AtivoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Ativo incluir(@Valid @NotNull(message = "Objeto request é obrigatório") AtivoRequestDTO ativoRequestDTO) {
        if(this.repository.existsById(ativoRequestDTO.getCodigo())) {
            throw new AtivoJaExisteException(ativoRequestDTO.getCodigo());
        }
        final Ativo ativo = this.converterDTOParaEntidade(ativoRequestDTO);
        return this.repository.save(ativo);
    }

    @Transactional
    public Ativo alterar(@Valid @NotNull(message = "Objeto request é obrigatório") AtivoRequestDTO ativoRequestDTO,
                         @NotEmpty(message = "Campo 'codigo é' obrigatório") String codigo) {
        final Ativo ativoBD = this.buscarPorCodigo(codigo);
        final Ativo ativoParaAtualizar = ativoBD.comPreco(ativoRequestDTO.getPreco()).comNome(ativoRequestDTO.getNome())
                .comTipoAtivo(ativoRequestDTO.getTipoAtivo());
        return this.repository.save(ativoParaAtualizar);
    }

    @Transactional
    public void remover(@NotEmpty(message = "Campo 'codigo' é obrigatório") String codigo) {
        final Ativo ativoBD = this.buscarPorCodigo(codigo);
        this.repository.delete(ativoBD);
    }

    private Ativo converterDTOParaEntidade(@Valid @NotNull(message = "Objeto request é obrigatório") AtivoRequestDTO ativoRequestDTO) {
        return new Ativo(ativoRequestDTO.getCodigo(), null, ativoRequestDTO.getNome(),
                ativoRequestDTO.getTipoAtivo()).comPreco(ativoRequestDTO.getPreco());
    }

    public Ativo buscarPorCodigo(@NotEmpty(message = "Campo 'codigo' é obrigatório") String codigo) {
        return this.repository.findById(codigo).orElseThrow(() -> new AtivoNaoEncontradoException(codigo));
    }
}
