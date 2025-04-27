package br.com.sartori.sgrm.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "despacho_processo")
@Getter
@Setter
public class DespachoProcesso {

	@EmbeddedId
	private DespachoProcessoId id;

    @ManyToOne
    @JoinColumn(name = "codigo", insertable = false, updatable = false)
    private Despacho despacho;

    @ManyToOne
    @JoinColumn(name = "numero_revista", insertable = false, updatable = false)
    private Revista revista;

    @ManyToOne
    @JoinColumn(name = "numero_processo", insertable = false, updatable = false)
    private Processo processo;
    
    public DespachoProcesso() {
    	
    }

	public DespachoProcesso(Integer numeroRevista, Long numeroProcesso, String codigo) {

		this.id = new DespachoProcessoId(codigo, numeroRevista, numeroProcesso);
	}
}