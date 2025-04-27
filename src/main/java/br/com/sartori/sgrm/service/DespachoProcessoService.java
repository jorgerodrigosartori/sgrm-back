package br.com.sartori.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.model.DespachoProcesso;
import br.com.sartori.sgrm.repository.DespachoProcessoRepositoryCustom;
import br.com.sartori.sgrm.repository.IDespachoProcessoRepository;
import jakarta.transaction.Transactional;

@Service
public class DespachoProcessoService {
	
	@Autowired
	IDespachoProcessoRepository iDespachoProcessoRepository;
	
	@Autowired
	DespachoProcessoRepositoryCustom despachoProcessoRepositoryCustom;
	
	public void salvar(DespachoProcesso des) {
		
		iDespachoProcessoRepository.save(des);
	}

	public void salvarAll(List<DespachoProcesso> des) {
		
		iDespachoProcessoRepository.saveAll(des);
	}
	
	@Transactional
	public void incluirSQL(DespachoProcesso des) {
		
		despachoProcessoRepositoryCustom.incluirDespachoProcessoSQL(des);
	}
	
}