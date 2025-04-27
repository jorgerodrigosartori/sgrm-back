package br.com.sartori.sgrm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.bean.dto.ProcessoAcompanhadoDto;
import br.com.sartori.sgrm.bean.request.MarcaAcompanhadaReq;
import br.com.sartori.sgrm.model.ProcessoAcompanhado;
import br.com.sartori.sgrm.repository.IProcessoAcompanhadoRepository;
import br.com.sartori.sgrm.repository.MarcaAcompanhadaRepositoryCustom;

@Service
public class MarcaService {
	
	@Autowired
	private IProcessoAcompanhadoRepository processoAcompanhadoRepository;
	
	@Autowired
	private MarcaAcompanhadaRepositoryCustom marcaRepositoryCustom;
	
	public List<ProcessoAcompanhadoDto> listarMarcasAcompanhadas() {

		List<ProcessoAcompanhado> todos = processoAcompanhadoRepository.findAll();
		return criarObjetosRetorno(todos);
	}

	private List<ProcessoAcompanhadoDto> criarObjetosRetorno(List<ProcessoAcompanhado> todos) {
		
		List<ProcessoAcompanhadoDto> ret = new ArrayList<ProcessoAcompanhadoDto>();
		todos.forEach(t -> {
			ProcessoAcompanhadoDto r = criaObjetoRetorno(t);
			ret.add(r);
		});
		return ret;
	}

	private ProcessoAcompanhadoDto criaObjetoRetorno(ProcessoAcompanhado t) {

		ProcessoAcompanhadoDto res = new ProcessoAcompanhadoDto();
		res.setProcesso(t.getNumeroProcesso());
		res.setSemelhantes(t.getSemelhantes());
		return res;
	}

	public void salvar(MarcaAcompanhadaReq marca) {

		if(marca.getCodigo() == null) {
			Long codigo = marcaRepositoryCustom.recuperaProximoCodigo();
			marca.setCodigo(codigo);
		}
		ProcessoAcompanhado mar = criarObjeto(marca);
		processoAcompanhadoRepository.save(mar);
	}
	
	private ProcessoAcompanhado criarObjeto(MarcaAcompanhadaReq marca) {
		
		ProcessoAcompanhado mar = new ProcessoAcompanhado(marca.getProcesso(), "N");
		//mar.setNumeroProcesso(marca.getProcesso());
		//mar.setSemelhantes("N");
		return mar;
	}
	
	public Long qtMarcasAcompanhadas() {
		
		return marcaRepositoryCustom.qtMarcasAcompanhadas();
	}
	
}