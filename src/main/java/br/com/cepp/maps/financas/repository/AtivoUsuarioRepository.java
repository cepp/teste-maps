package br.com.cepp.maps.financas.repository;

import br.com.cepp.maps.financas.model.AtivoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AtivoUsuarioRepository extends JpaRepository<AtivoUsuario, Long> {
    Optional<AtivoUsuario> findByAtivo_CodigoAndDataPosicaoAndCodigoUsuario(String codigo, LocalDate dataMovimento, String codigoUsuario);
}
