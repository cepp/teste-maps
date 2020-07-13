package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Usuario;
import br.com.cepp.maps.financas.repository.UsuarioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Log4j2
@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void salvar(final List<Usuario> usuarios) {
        this.repository.saveAll(usuarios);
    }
}
