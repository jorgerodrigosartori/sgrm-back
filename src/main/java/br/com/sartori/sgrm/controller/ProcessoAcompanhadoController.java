package br.com.sartori.sgrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sartori.sgrm.service.ProcessoAcompanhadoService;

@RestController
@RequestMapping(path = { "/v1/processo-acompanhado" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProcessoAcompanhadoController { 
	
	@Autowired
	private ProcessoAcompanhadoService processoAcompanhadoService;

	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping("/{processo}/{semelhante}")
	public void marcarDesmarcarVerificarSemelhantes(@PathVariable(value = "processo") Long processo, @PathVariable(value = "semelhante") String semelhante) {
		
		processoAcompanhadoService.marcarDesmarcarVerificarSemelhantes(processo, semelhante);
	}
}