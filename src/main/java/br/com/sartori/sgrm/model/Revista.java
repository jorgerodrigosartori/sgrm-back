package br.com.sartori.sgrm.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "revista")
@Getter
@Setter
public class Revista {

    @Id
    @Column(name = "numero_revista")
    private Integer numeroRevista;

    @Column(name = "data_publicacao")
    private Date dataPublicacao;

    @Column(name = "data_carga", nullable = false)
    private Date dataCarga;

    @Column(name = "endereco_arquivo", nullable = false, length = 400)
    private String enderecoArquivo = "";

    @Column(name = "status", nullable = false, length = 1)
    private String status;
    
}