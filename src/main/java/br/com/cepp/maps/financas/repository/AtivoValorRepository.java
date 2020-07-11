package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.AtivoValor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AtivoValorRepository extends JpaRepository<AtivoValor, Long> {
    @EntityGraph(attributePaths = {"ativo"})
    Optional<AtivoValor> findByAtivo_CodigoAndData(String codigoAtivo, LocalDate data);
    boolean existsAtivoValorByAtivo_CodigoAndData(String codigoAtivo, LocalDate data);
      boolean existsByAtivo_CodigoAndDataAndMovimentosIsEmpty(String codigo, LocalDate data);
}
