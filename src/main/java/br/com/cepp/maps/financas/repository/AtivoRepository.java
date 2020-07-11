package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtivoRepository extends JpaRepository<Ativo, String> {
    boolean existsByCodigo(String codigo);
}
