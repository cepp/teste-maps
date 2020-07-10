package br.com.cepp.maps.financas.resource.dto;

import lombok.Data;

@Data
public class MovimentoRequestTestDTO implements InterfaceTestDTO {
    private static final long serialVersionUID = 5809934958182144403L;

    private String ativo;
    private String data;
    private String quantidade;
}
