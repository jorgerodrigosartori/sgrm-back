package br.com.sartori.sgrm.bean.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ClasseViennaXml {

	@XmlAttribute(name = "codigo")
	private String codigo;

	@XmlAttribute(name = "edicao")
	private String edicao;

}
