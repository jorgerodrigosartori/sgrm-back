package br.com.sartori.sgrm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sartori.sgrm.bean.dto.ProcessoDto;
import br.com.sartori.sgrm.service.ProcessoService;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(path = { "/v1/processo" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProcessoController { 
	
	@Autowired
	private ProcessoService processoService;

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/radical")
	public List<ProcessoDto> listarProcessosSemelhantes(@PathParam(value = "qtRevista") Integer qtRevista) {
		
		return processoService.listarProcessosPorConsultaRadical(qtRevista);
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/listar")
	public List<ProcessoDto> listarProcessosDaRevistas(@PathParam(value = "marca") String marca, @PathParam(value = "processo") Long processo) {
		
		return processoService.listarProcessos(marca, processo);
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/acompanhados")
	public List<ProcessoDto> listarProcessosAcompanhados() {
		
		return processoService.listarProcessosAcompanhados();
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping("/{processo}")
	public void adicionarProcessoAcompanhado(@PathVariable(value = "processo") Long processo) {
		
		processoService.adicionarProcessoAcompanhado(processo);
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/{processo}")
	public void removerProcessoAcompanhado(@PathVariable(value = "processo") Long processo) {
		
		processoService.removerProcessoAcompanhado(processo);
	}
	
}
