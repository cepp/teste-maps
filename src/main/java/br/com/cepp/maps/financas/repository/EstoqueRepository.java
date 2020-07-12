package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.Estoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    @EntityGraph(attributePaths = {"ativo"})
    Optional<Estoque> findByAtivo_CodigoAndDataPosicao(String ativo, LocalDate dataPosicao);
    @EntityGraph(attributePaths = {"ativo"})
    Optional<Page<Estoque>> findByDataPosicao(LocalDate dataPosicao, Pageable pageable);
}
