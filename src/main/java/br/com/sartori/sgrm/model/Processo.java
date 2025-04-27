package br.com.sartori.sgrm.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "processo")
@Getter
@Setter
public class Processo {

    @Id
    @Column(name = "numero_processo", nullable = false)
    private Long numeroProcesso;

    @Column(name = "nome_marca", length = 500)
    private String nomeMarca;

    @Column(name = "data_deposito")
    private Date dataDeposito;

    @Column(name = "data_concessao")
    private Date dataConcessao;

    @Column(name = "data_vigencia")
    private Date dataVigencia;

    @Column(name = "titular", length = 500)
    private String titular;
    
    @Column(name = "procurador", length = 200)
    private String procurador;
    
    @Column(name = "classe")
    private Integer classe;
    
    @Column(name = "status", length = 50)
    private String status;
    
    @OneToOne
    @JoinColumn(name = "numero_processo", insertable = false, updatable = false)
    private ProcessoAcompanhado acompanhado;
    
    @Transient
    private List<DespachoProcesso> despachos;
    
    public Processo() {
    	
    }
    
    public Processo(Long nu) {
    	this.numeroProcesso = nu;
    }

}
