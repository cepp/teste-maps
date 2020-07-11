package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long> {
    Optional<ContaCorrente> findByCodigoUsuarioAndData(String codigoUsuario, LocalDate data);
    boolean existsByCodigoUsuarioAndData(String codigoUsuario, LocalDate data);
}
