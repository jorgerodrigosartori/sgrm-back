package br.com.sartori.sgrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.model.ProcessoAcompanhado;
import br.com.sartori.sgrm.repository.IProcessoAcompanhadoRepository;

@Service
public class ProcessoAcompanhadoService {

	@Autowired
	IProcessoAcompanhadoRepository iProcessoAcompanhadoRepository;
	
	public void marcarDesmarcarVerificarSemelhantes(Long processo, String semelhante) {

		ProcessoAcompanhado pa = new ProcessoAcompanhado(processo, semelhante);
		iProcessoAcompanhadoRepository.save(pa);
	}
	
}