package br.com.sartori.sgrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sartori.sgrm.bean.dto.DadosIniciaisDto;
import br.com.sartori.sgrm.service.ProcessoService;
import br.com.sartori.sgrm.service.RevistaService;

@RestController
@RequestMapping(path = { "/v1/dados-iniciais" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class DadosIniciaisController {
	
	@Autowired
	private RevistaService revistaService;

	@Autowired
	private ProcessoService processoService;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping()
	public DadosIniciaisDto consultarDadosIniciais() {
		
		DadosIniciaisDto dadosIniciais = new DadosIniciaisDto();
		
		dadosIniciais.setUltimaRevista(revistaService.consultaUltimaRevista());
		dadosIniciais.setPrimeiraRevista(revistaService.consultaPrimeiraRevista());
		dadosIniciais.setTotalRevistas(revistaService.consultaTotalRevistas());
		
		boolean existeNovaRevista = revistaService.verificaExisteNovaRevista(dadosIniciais.getUltimaRevista().getNumeroRevista() + 1);
		if(existeNovaRevista)
			dadosIniciais.setNumeroProximaRevista(dadosIniciais.getUltimaRevista().getNumeroRevista() + 1);
		else dadosIniciais.setNumeroProximaRevista(null);
		
		dadosIniciais.setMovimentacoes(processoService.listarMovimentacoesMinhasMarcas());
		dadosIniciais.setTotalProcessos(processoService.getQtTotalProcessos());
		dadosIniciais.setTotalMinhasMarcas(processoService.getQtTotalMinhas());
		
		return dadosIniciais;
	}
}