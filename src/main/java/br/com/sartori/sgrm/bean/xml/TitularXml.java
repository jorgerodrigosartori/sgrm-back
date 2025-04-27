package br.com.sartori.sgrm.bean.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.Setter;


@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class TitularXml {

    @XmlAttribute(name = "nome-razao-social")
    private String nomeRazaoSocial;

    @XmlAttribute(name = "pais")
    private String pais;

    @XmlAttribute(name = "uf")
    private String uf;
}
