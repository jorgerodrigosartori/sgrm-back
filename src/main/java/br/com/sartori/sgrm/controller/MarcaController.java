package br.com.sartori.sgrm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sartori.sgrm.bean.dto.ProcessoAcompanhadoDto;
import br.com.sartori.sgrm.bean.request.MarcaAcompanhadaReq;
import br.com.sartori.sgrm.service.MarcaService;

@RestController
@RequestMapping(path = { "/v1/marca" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class MarcaController {
	
	@Autowired
	private MarcaService marcaService;

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/listar")
	public List<ProcessoAcompanhadoDto> listarProcessosDaRevistas() {
		
		return marcaService.listarMarcasAcompanhadas();
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping
	public void salvarMarca(@RequestBody MarcaAcompanhadaReq marca) {
		
		marcaService.salvar(marca);
	}
	
}
