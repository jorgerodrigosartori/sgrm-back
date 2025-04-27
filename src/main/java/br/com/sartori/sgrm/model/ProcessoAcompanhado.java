package br.com.sartori.sgrm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "processo_acompanhado")
@Getter
@Setter
public class ProcessoAcompanhado {

    @Id
    @Column(name = "numero_processo", nullable = false)
    private Long numeroProcesso;
    
    @Column(name = "verifica_semelhantes", length = 1)
    private String semelhantes;
    
    public ProcessoAcompanhado() {
    	
    }
    
    public ProcessoAcompanhado(Long nu, String se) {
    	
    	this.numeroProcesso = nu;
    	this.semelhantes = se;
    }
    
    public ProcessoAcompanhado(Long nu) {
    	
    	this.numeroProcesso = nu;
    	this.semelhantes = "N";
    }
}