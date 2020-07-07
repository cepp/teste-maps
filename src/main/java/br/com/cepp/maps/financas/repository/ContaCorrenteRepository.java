package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, String> {

    Optional<ContaCorrente> findByCodigoUsuario(String codigoUsuario);
}
