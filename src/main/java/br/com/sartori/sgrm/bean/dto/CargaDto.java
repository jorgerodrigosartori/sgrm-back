package br.com.sartori.sgrm.bean.dto;

import java.util.List;

import br.com.sartori.sgrm.model.DespachoProcesso;
import br.com.sartori.sgrm.model.Processo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargaDto {
	
	private Integer revista;
	
	private List<Processo> listaProcessos;
	
	private List<DespachoProcesso> listaDespachosProcesso;

	public CargaDto(Integer rev) {
		this.revista = rev;
	}
}
