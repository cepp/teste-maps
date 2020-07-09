package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.Estoque;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    @EntityGraph(attributePaths = {"ativo"})
    Optional<Estoque> findByAtivo_CodigoAndDataPosicao(String ativo, LocalDate dataPosicao);
}
