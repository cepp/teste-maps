package br.com.cepp.maps.financas.resource.dto;

import lombok.Data;

@Data
public class AtivoRequestTestDTO implements InterfaceTestDTO {
    private static final long serialVersionUID = -2394763346833853202L;

    private String codigo;
    private String nome;
    private String tipoAtivo;
    private String dataVencimento;
    private String dataEmissao;
}
