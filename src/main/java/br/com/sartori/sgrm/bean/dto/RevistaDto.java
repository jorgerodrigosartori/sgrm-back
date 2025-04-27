package br.com.sartori.sgrm.bean.dto;

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
}
