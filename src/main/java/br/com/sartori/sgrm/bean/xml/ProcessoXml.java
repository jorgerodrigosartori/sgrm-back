package br.com.sartori.sgrm.bean.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ProcessoXml {

    @XmlAttribute(name = "numero")
    private String numero;

    @XmlAttribute(name = "data-deposito")
    private String dataDeposito;

    @XmlAttribute(name = "data-concessao")
    private String dataConcessao;
    
    @XmlAttribute(name = "data-vigencia")
    private String dataVigencia;
    
    @XmlElement(name = "despachos")
    private DespachosXml despachos;

    @XmlElement(name = "titulares")
    private TitularesXml titulares;

    @XmlElement(name = "marca")
    private MarcaXml marca;

    @XmlElement(name = "classes-vienna")
    private List<ClasseViennaXml> classesVienna;
    
    @XmlElement(name = "lista-classe-nice")
    private ListaClasseNice listaClasseNice;

    @XmlElement(name = "procurador")
    private String procurador;
}
