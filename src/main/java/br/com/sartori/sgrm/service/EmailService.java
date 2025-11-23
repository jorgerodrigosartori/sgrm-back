package br.com.sartori.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.bean.dto.MovimentacaoDto;
import br.com.sartori.sgrm.bean.dto.RevistaDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private RevistaService revistaService;
    
    @Autowired
    private ProcessoService processoService;
    
    public String emailUltimaRevista() throws MessagingException {
    	
    	
    	RevistaDto ultimaRevista = revistaService.consultaUltimaRevista();

		//List<MovimentacaoDto> movimentacoes = processoService.listarMovimentacoesMinhasMarcas();
		List<MovimentacaoDto> ultimasMovimentacoes = processoService.listarUltimasMovimentacoesMinhasMarcas();

		Integer qtTotalMinhas = processoService.getQtTotalMinhas();
		
    	StringBuilder html = new StringBuilder("");
		html.append("<div style='")
	    .append("background:#ffffff;")
	    .append("border:1px solid #ddd;")
	    .append("border-radius:8px;")
	    .append("padding:20px;")
	    .append("font-family:Arial, sans-serif;")
	    .append("max-width:600px;")
	    .append("margin:auto;")
	    .append("box-shadow:0 2px 6px rgba(0,0,0,0.1);")
	    .append("'>");

		html.append("<h2 style='color:#333; margin-top:0;'>Olá!</h2>");
	
		html.append("<p style='font-size:15px; color:#555; margin-top:0;'>")
		    .append("Uma nova revista foi importada para o sistema. Seguem informações:")
		    .append("</p>");
	
		html.append("<p style='font-size:15px; color:#333; margin:6px 0 18px 0;'>")
		    .append("<strong>Revista:</strong> ").append(ultimaRevista.getNumeroRevista())
		    .append(" &nbsp;&nbsp; ")
		    .append("<strong>Data publicação:</strong> ").append(ultimaRevista.getDataPublicacao())
		    .append("</p>");
		
		if(!ultimaRevista.getNumeroRevista().equals(ultimasMovimentacoes.get(0).getNumeroRevista())) {
			html.append("<p style='font-size:15px; color:red; margin:6px 0 18px 0;'>")
		    .append("<strong>* nenhuma movimentação de suas marcas nesta revista.</strong> </p>");
		}

        html.append("<div style='")
            .append("background:#ffffff;")
            .append("border:1px solid #ddd;")
            .append("border-radius:8px;")
            .append("padding:20px;")
            .append("font-family:Arial, sans-serif;")
            .append("max-width:600px;")
            .append("margin:auto;")
            .append("box-shadow:0 2px 6px rgba(0,0,0,0.1);")
            .append("'>");

        html.append("<h3 style='margin-top:0; color:#333; text-align:center;'>Resumo das Marcas Acompanhadas</h3>");

        html.append("<p style='font-size:16px; color:#555; margin-bottom:16px;'>")
            .append("<strong>Total de marcas acompanhadas:</strong> ")
            .append(qtTotalMinhas)
            .append("</p>");

        html.append("<p style='font-size:15px; color:#333; margin-top:20px; margin-bottom:8px;'>")
            .append("<strong>Últimas 5 movimentações:</strong>")
            .append("</p>");

        html.append("<table style='width:100%; border-collapse:collapse;'>")
            .append("<thead>")
            .append("<tr>")
            .append("<th style='text-align:left; padding:8px; border-bottom:1px solid #ccc;'>Revista</th>")
            .append("<th style='text-align:left; padding:8px; border-bottom:1px solid #ccc;'>Processo</th>")
            .append("<th style='text-align:left; padding:8px; border-bottom:1px solid #ccc;'>Marca</th>")
            .append("</tr>")
            .append("</thead>")
            .append("<tbody>");

        for (MovimentacaoDto mov : ultimasMovimentacoes) {
            html.append("<tr>")
                .append("<td style='padding:8px; border-bottom:1px solid #eee;'>")
                .append(mov.getNumeroRevista())
                .append("</td>")

                .append("<td style='padding:8px; border-bottom:1px solid #eee;'>")
                .append(mov.getNumeroProcesso())
                .append("</td>")

                .append("<td style='padding:8px; border-bottom:1px solid #eee;'>")
                .append(mov.getNomeMarca())
                .append("</td>")
                .append("</tr>");
        }
        html.append("</tbody>")
            .append("</table>")
            .append("</div>");
               
		html.append("<hr>");
		html.append("<p>Enviado em: <em>" + java.time.LocalDateTime.now() + "</em></p>");
		html.append("<div style='margin: 15px 0 25px 0; text-align:left;'>")
	    .append("<a href='https://sgrm-front-brr4.vercel.app/' ")
	    .append("style='display:inline-block; padding:10px 18px; background:#1976d2; color:#fff; ")
	    .append("text-decoration:none; border-radius:6px; font-size:14px;'>")
	    .append("Acessar o sistema")
	    .append("</a>")
	    .append("</div>");
        //enviarEmail(
        //    "jorge.sartori@gmail.com",
         //   "SGRM - Nova revista atualizada na base.",
         //   html.toString()
        //);
    	
    	return html.toString();
    }

    private void enviarEmail(String para, String assunto, String html) throws MessagingException {

    	MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(para);
        helper.setSubject(assunto);
        helper.setText(html, true);
        mailSender.send(message);
    }
}
