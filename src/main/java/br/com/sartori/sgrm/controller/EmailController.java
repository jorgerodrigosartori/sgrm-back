package br.com.sartori.sgrm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sartori.sgrm.bean.dto.MovimentacaoDto;
import br.com.sartori.sgrm.bean.dto.RevistaDto;
import br.com.sartori.sgrm.service.EmailService;
import br.com.sartori.sgrm.service.ProcessoService;
import br.com.sartori.sgrm.service.RevistaService;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/v1/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private RevistaService revistaService;
    
    @Autowired
    private ProcessoService processoService;
    
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/ultima-revista")
    public String enviar() throws MessagingException {
    	    	
    	RevistaDto ultimaRevista = revistaService.consultaUltimaRevista();

		//List<MovimentacaoDto> movimentacoes = processoService.listarMovimentacoesMinhasMarcas();
		List<MovimentacaoDto> ultimasMovimentacoes = processoService.listarUltimasMovimentacoesMinhasMarcas();

		Integer qtTotalMinhas = processoService.getQtTotalMinhas();

        return emailService.emailUltimaRevista(ultimaRevista, ultimasMovimentacoes, qtTotalMinhas);
    }
}
