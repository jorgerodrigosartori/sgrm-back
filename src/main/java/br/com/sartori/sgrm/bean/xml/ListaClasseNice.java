package br.com.sartori.sgrm.bean.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@XmlRootElement(name = "lista-classe-nice")
public class ListaClasseNice {

    @XmlElement(name = "classe-nice")
    private List<ClasseNiceXml> classeNiceList;
}
