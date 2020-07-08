package br.com.cepp.maps.financas.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;

@Data
public class AtivoRequestTestDTO implements Serializable {
    private static final long serialVersionUID = -2394763346833853202L;

    private String codigo;
    private String preco;
    private String nome;
    private String tipoAtivo;

    @JsonIgnore
    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
