package br.com.sartori.sgrm.bean.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimentacaoDto {
	
	private Long numeroProcesso;

    private Integer numeroRevista;
    
    private String nomeMarca;
}