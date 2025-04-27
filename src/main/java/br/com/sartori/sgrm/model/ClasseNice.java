package br.com.sartori.sgrm.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "classe_nice")
@Getter
@Setter
public class ClasseNice {

    @Id
    @Column(name = "codigo", nullable = false)
    private Integer codigo;

    @Column(name = "especificacao", length = 500)
    private String especificacao;
    
    public ClasseNice() {
    	
    }
    
    public ClasseNice(Integer cod, String esp) {
    	
    	this.codigo = cod;
    	this.especificacao = esp.length() > 500 ? esp.substring(0,500) : esp;
    }

}