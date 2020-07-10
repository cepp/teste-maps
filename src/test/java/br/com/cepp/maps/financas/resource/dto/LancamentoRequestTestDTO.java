package br.com.cepp.maps.financas.resource.dto;

import lombok.Data;

@Data
public class LancamentoRequestTestDTO implements InterfaceTestDTO {
    private static final long serialVersionUID = -824249137873518198L;

    private String valor;
    private String descricao;
    private String data;

}
