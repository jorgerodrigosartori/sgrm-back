package br.com.sartori.sgrm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.bean.dto.DespachoDto;
import br.com.sartori.sgrm.bean.xml.DespachoXml;
import br.com.sartori.sgrm.bean.xml.ProcessoXml;
import br.com.sartori.sgrm.bean.xml.RevistaXml;
import br.com.sartori.sgrm.model.Despacho;
import br.com.sartori.sgrm.model.DespachoProcesso;
import br.com.sartori.sgrm.repository.DespachoProcessoRepositoryCustom;
import br.com.sartori.sgrm.repository.IDespachoRepository;
import br.com.sartori.sgrm.util.UtilData;

@Service
public class DespachoService {
	
	@Autowired
	IDespachoRepository iDespachoRepository;
	
	@Autowired
	DespachoProcessoRepositoryCustom despachoProcessoRepositoryCustom;
	
	public HashMap<String, Despacho> atualizaDespachos(RevistaXml revista) {
		
		HashMap<String, Despacho> despachos = new HashMap<String, Despacho>();
		List<Despacho> despachosBanco = iDespachoRepository.findAll();
		for(Despacho d : despachosBanco)
			despachos.put(d.getCodigo(), d);
		
		for(ProcessoXml processo : revista.getProcessos()) {			
			for(DespachoXml des : processo.getDespachos().getDespachoList()) {				
				Despacho despacho = despachos.get(des.getCodigo());
				if(despacho == null) {
					despacho = new Despacho(des.getCodigo(), des.getNome());
					despachos.put(des.getCodigo(), despacho);
					iDespachoRepository.save(despacho);
				}
			}
		}
		return despachos;
	}
	
	public Despacho consultaDespacho(String codigo) {
		
		Optional<Despacho> optional = iDespachoRepository.findById(codigo);
		if(optional.isPresent())
			return optional.get();
		else return null;
	}

	public List<DespachoDto> listarDespachosProcesso(Long processo) {
		
		List<DespachoProcesso> despachos = despachoProcessoRepositoryCustom.listarDespachosProcesso(processo);
		List<DespachoDto> retorno = new ArrayList<DespachoDto>();
		
		for(DespachoProcesso des : despachos)
			retorno.add(converteObjeto(des));
		
		return retorno;
	}
	
	public DespachoDto converteObjeto(DespachoProcesso dp) {
		
		DespachoDto des = new DespachoDto();
		des.setData(UtilData.converteData(dp.getRevista().getDataPublicacao(), "dd/MM/yyyy"));
		des.setNumeroRevista(dp.getRevista().getNumeroRevista());
		des.setDespacho(dp.getDespacho().getNome());
		return des;
	}
	
	
}