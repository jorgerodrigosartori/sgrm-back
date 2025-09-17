package br.com.sartori.sgrm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.bean.dto.DespachoDto;
import br.com.sartori.sgrm.bean.dto.MovimentacaoDto;
import br.com.sartori.sgrm.bean.dto.ProcessoDto;
import br.com.sartori.sgrm.model.DespachoProcesso;
import br.com.sartori.sgrm.model.Processo;
import br.com.sartori.sgrm.model.ProcessoAcompanhado;
import br.com.sartori.sgrm.repository.IProcessoAcompanhadoRepository;
import br.com.sartori.sgrm.repository.IProcessoRepository;
import br.com.sartori.sgrm.repository.MarcaAcompanhadaRepositoryCustom;
import br.com.sartori.sgrm.repository.ProcessoRepositoryCustom;
import br.com.sartori.sgrm.repository.RevistaRepositoryCustom;
import br.com.sartori.sgrm.util.UtilData;
import br.com.sartori.sgrm.util.UtilRadical;
import jakarta.transaction.Transactional;

@Service
public class ProcessoService {

	@Autowired
	DespachoService despachoService;

	@Autowired
	IProcessoRepository iProcessoRepository;
	
	@Autowired
	IProcessoAcompanhadoRepository iProcessoAcompanhadoRepository;
	
	@Autowired
	ProcessoRepositoryCustom processoRepositoryCustom;

	@Autowired
	MarcaAcompanhadaRepositoryCustom marcaAcompanhadaRepositoryCustom;
	
	@Autowired
	RevistaRepositoryCustom revistaRepositoryCustom;
	
	public Processo consultaProcesso(Long id) {

		Optional<Processo> optional = iProcessoRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else
			return null;
	}

	public List<Processo> consultaProcessos(List<Long> ids) {
		
		return processoRepositoryCustom.listarProcessos(ids);
	}
	
	public void salvarProcessos(List<Processo> pro) {

		iProcessoRepository.saveAll(pro);
	}

	public void salvarProcesso(Processo pro) {
		
		iProcessoRepository.save(pro);
	}
	
	@Transactional
	public void incluirSQL(Processo pro) {
		
		processoRepositoryCustom.incluirSQL(pro);
	}
	
	public List<ProcessoDto> listarProcessosDaRevistas(Integer revista) {

		List<DespachoProcesso> processos = processoRepositoryCustom.listarProcessosPorRevista(revista);

		return converte(processos);
	}

	private List<ProcessoDto> converte(List<DespachoProcesso> processos) {

		List<ProcessoDto> retorno = new ArrayList<ProcessoDto>();
		for (DespachoProcesso dp : processos) {
			ProcessoDto ret = new ProcessoDto();
			ret.setDataConcessao(UtilData.converteData(dp.getProcesso().getDataConcessao(), "dd/MM/yyyy"));
			ret.setDataDeposito(UtilData.converteData(dp.getProcesso().getDataDeposito(), "dd/MM/yyyy"));
			ret.setDataVigencia(UtilData.converteData(dp.getProcesso().getDataVigencia(), "dd/MM/yyyy"));
			ret.setNomeMarca(dp.getProcesso().getNomeMarca());
			ret.setNumeroProcesso(dp.getProcesso().getNumeroProcesso());
			ret.setTitular(dp.getProcesso().getTitular());
			retorno.add(ret);
		}
		return retorno;
	}

	public List<ProcessoDto> listarProcessosPorConsultaRadical(Integer qtRevista) {
		
		List<Integer> uiltimasRevistas = revistaRepositoryCustom.listarNumeroRevistas(qtRevista);		
		
		List<Processo> processosAcompanhados = processoRepositoryCustom.listarProcessosAcompanhados();
		
		List<ProcessoDto> retorno = converteListaProcesso(processosAcompanhados, false);
		
		//if(1==1) return retorno;
		
		retorno.parallelStream().forEach(pro -> {
		
			String nome = pro.getNomeMarca().replace(" ", "");	
			int chunkSize = 4;	
			List<String> fragmentos = splitIntoChunks(nome, chunkSize);
			List<Processo> lista = filtrarResultadoPorRelevancia(processoRepositoryCustom.listarProcessosProcessosPorFragmento(fragmentos, uiltimasRevistas, pro.getNumeroProcesso()), pro.getNomeMarca(), 50);
			pro.setProcessosSemelhantes(converteListaProcesso(lista, false));
			
		});
		

				
	/*	for(ProcessoDto pro : retorno) {
		
			String nome = pro.getNomeMarca().replace(" ", "");	
			int chunkSize = 4;	
			List<String> fragmentos = splitIntoChunks(nome, chunkSize);
			List<Processo> lista = filtrarResultadoPorRelevancia(processoRepositoryCustom.listarProcessosProcessosPorFragmento(fragmentos, revista, pro.getNumeroProcesso()), pro.getNomeMarca(), 50);
			pro.setProcessosSemelhantes(converteListaProcesso(lista, false));
		}
		*/
		return retorno.stream()
			    .filter(ret -> ret.getProcessosSemelhantes().size() > 0)
			    .toList();
	}

	public List<ProcessoDto> listarProcessos(String marca, Long processo) {
		
		
		List<Processo> lista = processoRepositoryCustom.listarProcessos(marca, processo);
		
		return converteListaProcesso(lista, false);
	}
	
	public List<ProcessoDto> listarProcessosAcompanhados() {
		
		
		List<DespachoProcesso> listaDespacho = processoRepositoryCustom.listarAcompanhados();
		
		HashMap<Long, Processo> mapa = new HashMap<Long, Processo>();
		for(DespachoProcesso des : listaDespacho) {
			
			Processo pro = mapa.get(des.getId().getNumeroProcesso());
			if(pro == null) {
				pro = des.getProcesso();
				pro.setDespachos(new ArrayList<DespachoProcesso>());
				mapa.put(des.getId().getNumeroProcesso(), pro);
			}
			pro.getDespachos().add(des);
		}
		List<Processo> lista = new ArrayList<Processo>();
		for(Long id : mapa.keySet()) {
			
			Processo p = mapa.get(id);
			
			lista.add(p);
		}
		return converteListaProcesso(lista, true);
	}
	
	private List<Processo> filtrarResultadoPorRelevancia(List<Processo> listaProcessos, String marca, Integer percentual) {
		
		System.out.println("Total selecionado: " + listaProcessos.size());
		List<Processo> retorno = new ArrayList<Processo>();

		for (Processo p : listaProcessos) {
			
			String registro = p.getNomeMarca().toLowerCase().replace(" ", "");
			
			if(registro.contains(marca) || marca.contains(registro)){
				retorno.add(p);
				//System.out.println("Marca: " + marca + " - Relevancia: " + " Valor comparado: " + p.getNumeroProcesso() + " - " + p.getNomeMarca());
			}else {
				Integer percentualRelevancia = UtilRadical.getPercentualRelevancia(p.getNomeMarca(), marca.toString());
				if(percentualRelevancia > percentual) {
					retorno.add(p);
					//System.out.println("Marca: " + marca + " - Relevancia: " + percentualRelevancia + " Valor comparado: " + p.getNumeroProcesso() + " - " + p.getNomeMarca());
				}
			}
		}
		return retorno;
	}

	private List<ProcessoDto> converteListaProcesso(List<Processo> lista, boolean completo) {

		List<ProcessoDto> retorno = new ArrayList<ProcessoDto>();

		lista.forEach(p -> {
			ProcessoDto pro = new ProcessoDto();
			pro.setNumeroProcesso(p.getNumeroProcesso());
			pro.setNomeMarca(p.getNomeMarca());
			pro.setStatus(p.getStatus());
			pro.setClasse(p.getClasse());
			
			if(completo) {
				pro.setDespachos(new ArrayList<DespachoDto>());
				for(DespachoProcesso dp : p.getDespachos()) {
					pro.getDespachos().add(despachoService.converteObjeto(dp));
				}
				pro.setQtDespachos(pro.getDespachos().size());
				pro.setDataConcessao(UtilData.converteData(p.getDataConcessao(), "dd/MM/yyyy"));
				pro.setDataDeposito(UtilData.converteData(p.getDataDeposito(), "dd/MM/yyyy"));
				pro.setDataVigencia(UtilData.converteData(p.getDataVigencia(), "dd/MM/yyyy"));
				pro.setNomeMarca(p.getNomeMarca());
				pro.setNumeroProcesso(p.getNumeroProcesso());
				pro.setTitular(p.getTitular());
				pro.setSemelhantes(p.getAcompanhado().getSemelhantes());
			}			
			pro.setAcompanhado(processoEstaSencoAcompanhado(p.getNumeroProcesso()));
			retorno.add(pro);
		});

		return retorno;
	}

	private String processoEstaSencoAcompanhado(Long numeroProcesso) {
		
		Optional<ProcessoAcompanhado> byId = iProcessoAcompanhadoRepository.findById(numeroProcesso);
		if(byId.isPresent())
			return "S";
		else return "N";
	}

	private static List<String> splitIntoChunks(String input, int chunkSize) {

		List<String> chunks = new ArrayList<>();

		if (input.length() < 4) {
			chunks.add(input);
			return chunks;
		}

		int length = input.length();

		for (int i = 0; i < length; i += chunkSize) {
			String chunk;
			if (i + chunkSize <= length) {
				chunk = input.substring(i, i + chunkSize);
			} else {
				chunk = input.substring(i);
				int remaining = chunkSize - chunk.length();
				chunk = input.substring(i - remaining, i) + chunk;
			}
			chunks.add(chunk);
		}
		return chunks;
	}
	
	public void adicionarProcessoAcompanhado(Long processo) {
		
		ProcessoAcompanhado pa = new ProcessoAcompanhado(processo);
		iProcessoAcompanhadoRepository.save(pa);
		
	}

	public void removerProcessoAcompanhado(Long processo) {
		
		ProcessoAcompanhado pa = new ProcessoAcompanhado(processo);
		iProcessoAcompanhadoRepository.delete(pa);
		
	}

	public List<MovimentacaoDto> listarMovimentacoesMinhasMarcas() {

		List<MovimentacaoDto> retorno = new ArrayList<MovimentacaoDto>();
		List<Object[]> list = marcaAcompanhadaRepositoryCustom.qtMovimentacoesMarcasAcompanhadas();
		for(Object[] linha : list ) {
			MovimentacaoDto mo = new MovimentacaoDto();
			mo.setNumeroProcesso((Long) linha[1]);
			mo.setNumeroRevista((Integer) linha[0]);
			retorno.add(mo);
		}
		
		return retorno;
	}

	public Integer getQtTotalProcessos() {

		return processoRepositoryCustom.getQtProcessos();
	}

	public Integer getQtTotalMinhas() {

		return marcaAcompanhadaRepositoryCustom.qtMarcasAcompanhadas().intValue();
	}
	
}