package br.com.sartori.sgrm.bean.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetornoDto<T> {
	
	private T objetoRetorno;
	
	private List<T> listaRetorno;
	
	private boolean resultado;
	
	private String mensagem;
}
