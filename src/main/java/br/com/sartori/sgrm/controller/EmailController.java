package br.com.sartori.sgrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sartori.sgrm.service.EmailService;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/v1/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/ultima-revista")
    public String enviar() throws MessagingException {
    	

        return emailService.emailUltimaRevista();
    }
}
