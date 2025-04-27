package br.com.sartori.sgrm.bean.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarcaAcompanhadaReq {
	
	private Long codigo;

    private String marca;

    private String radicais;

    private Long processo;
}