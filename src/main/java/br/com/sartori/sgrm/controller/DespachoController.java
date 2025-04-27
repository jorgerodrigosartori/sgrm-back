package br.com.sartori.sgrm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sartori.sgrm.bean.dto.DespachoDto;
import br.com.sartori.sgrm.service.DespachoService;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(path = { "/v1/despacho" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class DespachoController {
	
	@Autowired
	private DespachoService despachoService;

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/listar")
	public List<DespachoDto> listarDespachosProcesso(@PathParam(value = "processo") Long processo) {
		
		return despachoService.listarDespachosProcesso(processo);
	}
	
	
}
