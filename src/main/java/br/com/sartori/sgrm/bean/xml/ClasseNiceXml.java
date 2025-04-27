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
public class ClasseNiceXml {
    
    @XmlAttribute(name = "codigo")
    private int codigo;

    @XmlElement(name = "especificacao")
    private String especificacao;

    @XmlElement(name = "status")
    private String status;

}
