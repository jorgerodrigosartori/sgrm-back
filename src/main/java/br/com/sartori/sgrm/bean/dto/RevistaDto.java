package br.com.sartori.sgrm.bean.dto;

import br.com.sartori.sgrm.model.Revista;
import br.com.sartori.sgrm.util.UtilData;
import lombok.Getter;

@Getter
public class RevistaDto {

    private Integer numeroRevista;

    private String dataPublicacao;

    private String dataCarga;
    
    private String status;
    
    public RevistaDto(Integer rev, String dtPub, String dtCar, String st) {
    	
    	this.dataCarga = dtCar;
    	this.dataPublicacao = dtPub;
    	this.numeroRevista = rev;
    	this.status = st;
    }
    
    public RevistaDto(Revista rev) {
    	
    	this.dataCarga = UtilData.converteData(rev.getDataCarga(), "dd/MM/yyyy");
    	this.dataPublicacao = UtilData.converteData(rev.getDataPublicacao(), "dd/MM/yyyy");
    	this.numeroRevista = rev.getNumeroRevista();
    	this.status = rev.getStatus();
    }
}
