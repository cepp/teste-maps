package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.Movimento;
import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface MovimentoRepository extends JpaRepository<Movimento, Long> {
    @Query(value = "SELECT sum(m.valor) FROM  Movimento m JOIN m.ativoValor a " +
            "WHERE  m.tipoMovimento = :tipoMovimento " +
            "AND a.ativo.codigo = :ativo")
    Optional<BigDecimal> somaPrecoPorAtivoTipoMovimento(String ativo, TipoMovimento tipoMovimento);
    @Query(value = "SELECT sum(m.valor) FROM  Movimento m JOIN m.ativoValor a " +
            "WHERE  m.tipoMovimento = :tipoMovimento " +
            "AND a.ativo.codigo = :ativo")
    Optional<BigDecimal> somaValorPorAtivoTipoMovimento(String ativo, TipoMovimento tipoMovimento);
    Long countByAtivoValor_AtivoAndTipoMovimento(Ativo ativo, TipoMovimento tipoMovimento);
}
