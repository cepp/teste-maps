package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.AtivoValor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtivoValorRepository extends JpaRepository<AtivoValor, Long> {
}
