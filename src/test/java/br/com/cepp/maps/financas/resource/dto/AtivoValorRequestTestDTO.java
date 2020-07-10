package br.com.cepp.maps.financas.resource.dto;

import lombok.Data;

@Data
public class AtivoValorRequestTestDTO implements InterfaceTestDTO {
    private static final long serialVersionUID = 79019694315392990L;

    private String codigoAtivo;
    private String data;
    private String valor;

}
