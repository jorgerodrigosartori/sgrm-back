package br.com.sartori.sgrm.bean.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class MarcaXml {

    @XmlAttribute(name = "apresentacao")
    private String apresentacao;

    @XmlAttribute(name = "natureza")
    private String natureza;

    @XmlElement(name = "nome")
    private String nome;

}
