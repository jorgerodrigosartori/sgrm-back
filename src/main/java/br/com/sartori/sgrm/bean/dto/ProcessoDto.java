package br.com.sartori.sgrm.bean.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessoDto {
	
	private Long numeroProcesso;

    private String nomeMarca;

    private String dataDeposito;

    private String dataConcessao;

    private String dataVigencia;

    private String titular;	
    
    private String status;
    
    private Integer classe;
    
    private Integer qtDespachos;
    
    private String acompanhado;
    
    private List<DespachoDto> despachos;
    
    private List<ProcessoDto> processosSemelhantes;
    
    private String semelhantes;
    
    private String primeiraData;
    
    private String ultimaData;

}