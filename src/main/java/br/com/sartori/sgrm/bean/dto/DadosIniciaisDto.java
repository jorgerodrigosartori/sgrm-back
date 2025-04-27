package br.com.sartori.sgrm.bean.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DadosIniciaisDto {
	
	private RevistaDto ultimaRevista;

	private RevistaDto primeiraRevista;
	
	private Integer totalRevistas;
	
	private Integer numeroProximaRevista;
	
	private Integer totalProcessos;
	
	private Integer totalMinhasMarcas;
	
	private List<MovimentacaoDto> movimentacoes; 
}