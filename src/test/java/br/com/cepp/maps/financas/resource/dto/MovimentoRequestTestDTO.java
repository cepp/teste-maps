package br.com.cepp.maps.financas.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;

@Data
public class MovimentoRequestTestDTO implements Serializable {
    private static final long serialVersionUID = 5809934958182144403L;

    private String ativo;
    private String data;
    private String quantidade;

    @JsonIgnore
    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}