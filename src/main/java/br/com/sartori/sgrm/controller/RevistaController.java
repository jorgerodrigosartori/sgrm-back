package br.com.sartori.sgrm.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sartori.sgrm.bean.dto.ProcessoDto;
import br.com.sartori.sgrm.bean.dto.RetornoDto;
import br.com.sartori.sgrm.bean.dto.RevistaDto;
import br.com.sartori.sgrm.model.DespachoProcesso;
import br.com.sartori.sgrm.model.Processo;
import br.com.sartori.sgrm.service.ProcessoService;
import br.com.sartori.sgrm.service.RevistaService;
import jakarta.mail.MessagingException;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(path = { "/v1/revista" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class RevistaController {

	@Autowired
	private RevistaService revistaService;

	@Autowired
	private ProcessoService processoService;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/carga/{revista}")
	public RevistaDto cargaRevista(@PathVariable(value = "revista") Integer revista) throws FileNotFoundException, MessagingException {

		return revistaService.cargaRevista(revista);

		// return new RevistaDto(revista, null, null, "P");
	}
	

	@GetMapping("/carga")
	public String cargaRevista() throws FileNotFoundException, MessagingException {
	
	
		RevistaDto consultaUltimaRevista = revistaService.consultaUltimaRevista();
		if(consultaUltimaRevista.getStatus().equals("E")) {
			revistaService.cargaRevista(consultaUltimaRevista.getNumeroRevista() + 1);
			return "Havia ocorrido um erro no processamento da revista " + consultaUltimaRevista.getNumeroRevista() + ". Processamento reiniciado.";
		}else if(consultaUltimaRevista.getStatus().equals("P")) {
			return "Carga da revista " + consultaUltimaRevista.getNumeroRevista() + " em processamento. Aguarde conclusão.";
		}else {
			// verifica se existe revista disponível para carga
			boolean existeNovaRevista = revistaService.verificaExisteNovaRevista(consultaUltimaRevista.getNumeroRevista() + 1);
			if(existeNovaRevista) {
				revistaService.cargaRevista(consultaUltimaRevista.getNumeroRevista() + 1);
				return "Inciada a carga da revista " + (consultaUltimaRevista.getNumeroRevista() + 1) + ". Aguarde conclusão.";
			} else {
				
				return "Nenhuma revista disponível para carga.";
			}
		}	
	}
	
	@GetMapping("/expurgo")
	public String expurgoRevista() {
	
		revistaService.expurgarRevistas(40);
		
		return "Expurgo concluido.";
				
	}
	
	
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/{revista}/processos")
	public List<ProcessoDto> listarProcessosDaRevistas(@PathVariable(value = "revista") Integer revista, @PathParam(value = "pagina") int pagina) {
		
		return processoService.listarProcessosDaRevistas(revista);
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	//@CrossOrigin(origins = "https://bc00-189-6-214-197.ngrok-free.app")
	@GetMapping("/lista")
	public List<RevistaDto> listarRevistas() {
		
		return revistaService.listarRevistas();
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/carga/{processo}/{revista}")
	public RetornoDto<String> carregarProcessoRevista(@PathVariable(value = "processo") Long processo, @PathVariable(value = "revista") Integer revista) throws Exception {
		
		RetornoDto<String> ret = new RetornoDto<>();
		String retorno = revistaService.carregarProcessoRevista(processo, revista);
		if(retorno != null && !retorno.trim().equals("")) {
			ret.setResultado(false);
			ret.setMensagem(retorno);
		}else {
			ret.setResultado(true);
		}
		return ret;
	}
}
