package br.com.sartori.sgrm.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "despacho")
@Getter
@Setter
public class Despacho {

    @Id
    @Column(name = "codigo", length = 10, nullable = false)
    private String codigo;

    @Column(name = "nome", length = 1000, nullable = false)
    private String nome;
    
    public Despacho() {
    	
    }
    
    public Despacho(String cod, String no) {
    	
    	this.codigo = cod;
    	this.nome = no;
    }

}