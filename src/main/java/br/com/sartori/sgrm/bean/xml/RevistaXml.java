package br.com.sartori.sgrm.bean.xml;

import java.util.List;

import javax.xml.bind.annotation.*;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "revista")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class RevistaXml {

    @XmlAttribute(name = "numero")
    private int numero;

    @XmlAttribute(name = "data")
    private String data;

    @XmlElement(name = "processo")
    private List<ProcessoXml> processos;
}