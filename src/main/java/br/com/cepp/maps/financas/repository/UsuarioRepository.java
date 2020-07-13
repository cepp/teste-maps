package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
