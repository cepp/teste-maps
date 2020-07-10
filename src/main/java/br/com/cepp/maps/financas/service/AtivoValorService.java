package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.AtivoValor;
import br.com.cepp.maps.financas.repository.AtivoValorRepository;
import br.com.cepp.maps.financas.resource.dto.AtivoValorRequestDTO;
import br.com.cepp.maps.financas.resource.handler.AtivoValorJaExisteException;
import br.com.cepp.maps.financas.resource.handler.AtivoValorNaoEncontradoException;
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
public class AtivoValorService {
    private final AtivoValorRepository repository;
    private final AtivoService ativoService;

    @Autowired
    public AtivoValorService(AtivoValorRepository repository, AtivoService ativoService) {
        this.repository = repository;
        this.ativoService = ativoService;
    }

    @Transactional
    public AtivoValor incluir(@Valid @NotNull(message = "Objeto request é obrigatório") final AtivoValorRequestDTO requestDTO) {
        if(this.repository.existsAtivoValorByAtivo_CodigoAndData(requestDTO.getCodigoAtivo(), requestDTO.getData())) {
            throw new AtivoValorJaExisteException(requestDTO.getCodigoAtivo(), requestDTO.getData());
        }

        final Ativo ativo = this.ativoService.buscarPorCodigo(requestDTO.getCodigoAtivo());
        final AtivoValor ativoValor = new AtivoValor(null, requestDTO.getData(), requestDTO.getValor(), ativo);

        return this.repository.save(ativoValor);
    }

    @Transactional
    public void remover(@NotEmpty(message = "Campo 'codigoAtivo' é obrigatório") final String codigoAtivo,
                        @NotNull(message = "Campo 'data' é obrigatório") final LocalDate data) {
        final AtivoValor ativoValor = this.buscarPorAtivoEData(codigoAtivo, data);
        this.repository.delete(ativoValor);
    }

    public AtivoValor buscarPorAtivoEData(@NotEmpty(message = "Campo 'codigoAtivo' é obrigatório") final String codigoAtivo,
                                                    @NotNull(message = "Campo 'data' é obrigatório") final LocalDate data) {
        return this.repository.findByAtivo_CodigoAndData(codigoAtivo, data)
                .orElseThrow(() -> new AtivoValorNaoEncontradoException(codigoAtivo, data));
    }
}
