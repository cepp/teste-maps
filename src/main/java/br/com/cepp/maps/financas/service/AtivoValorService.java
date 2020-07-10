package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.repository.AtivoValorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Log4j2
@Service
@Validated
public class AtivoValorService {
    private final AtivoValorRepository repository;

    @Autowired
    public AtivoValorService(AtivoValorRepository repository) {
        this.repository = repository;
    }
}
