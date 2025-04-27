package br.com.sartori.sgrm.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DespachoProcessoId implements Serializable {

	private static final long serialVersionUID = 1L;
    
    @Column(name = "codigo", nullable = false, length = 10)
    private String codigo;

    @Column(name = "numero_revista", nullable = false)
    private Integer numeroRevista;

    @Column(name = "numero_processo", nullable = false)
    private Long numeroProcesso;
    
    public DespachoProcessoId() {}

    public DespachoProcessoId(String codigo, Integer numeroRevista, Long numeroProcesso) {
    	
        this.codigo = codigo;
        this.numeroRevista = numeroRevista;
        this.numeroProcesso = numeroProcesso;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DespachoProcessoId that = (DespachoProcessoId) o;
        return numeroRevista == that.numeroRevista &&
               Objects.equals(codigo, that.codigo) &&
               Objects.equals(numeroProcesso, that.numeroProcesso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, numeroRevista, numeroProcesso);
    }
}
