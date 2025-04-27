package br.com.sartori.sgrm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.bean.xml.ClasseNiceXml;
import br.com.sartori.sgrm.bean.xml.ProcessoXml;
import br.com.sartori.sgrm.bean.xml.RevistaXml;
import br.com.sartori.sgrm.model.ClasseNice;
import br.com.sartori.sgrm.repository.IClasseNiceRepository;

@Service
public class ClasseNiceService {
	
	@Autowired
	IClasseNiceRepository iClasseNiceRepository;
	
	public HashMap<Integer, ClasseNice> atualizaClassesNice(RevistaXml revista) {
		
		HashMap<Integer, ClasseNice> classes = new HashMap<Integer, ClasseNice>();
		List<ClasseNice> classesBanco = iClasseNiceRepository.findAll();
		for(ClasseNice d : classesBanco)
			classes.put(d.getCodigo(), d);
		
		for(ProcessoXml processo : revista.getProcessos()) {
			if(processo.getListaClasseNice() != null)
				for(ClasseNiceXml des : processo.getListaClasseNice().getClasseNiceList()) {				
					ClasseNice classe = classes.get(des.getCodigo());
					if(classe == null) {
						classe = new ClasseNice(des.getCodigo(), des.getEspecificacao());
						classes.put(des.getCodigo(), classe);
						iClasseNiceRepository.save(classe);
					}
				}
		}
		return classes;
	}
	
	public ClasseNice consultaDespacho(Integer codigo) {
		
		Optional<ClasseNice> optional = iClasseNiceRepository.findById(codigo);
		if(optional.isPresent())
			return optional.get();
		else return null;
	}
}