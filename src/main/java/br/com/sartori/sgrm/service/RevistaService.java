package br.com.sartori.sgrm.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.bean.dto.RevistaDto;
import br.com.sartori.sgrm.model.Revista;
import br.com.sartori.sgrm.repository.IRevistaRepository;
import br.com.sartori.sgrm.repository.RevistaRepositoryCustom;
import br.com.sartori.sgrm.util.UtilData;

@Service
public class RevistaService {

	@Autowired
	private RevistaRepositoryCustom revistaRepositoryCustom;

	@Autowired
	private IRevistaRepository iRevistaRepository;

	@Autowired
	private RevistaAsyncService revistaAsyncService;
	
	private String URL_REVISTA_INPI = "https://revistas.inpi.gov.br/txt/RM";
	
	public RevistaDto cargaRevista(Integer numeroRevista) {
		
		Revista rev = new Revista();
		rev.setDataCarga(new Date());
		rev.setNumeroRevista(numeroRevista);
		rev.setStatus("P");
		iRevistaRepository.save(rev);	
		
		revistaAsyncService.processaCargaRevista(rev);
		System.out.println("Retornando para tela");
		return converte(rev);
	}

	public boolean verificaExisteNovaRevista(Integer numeroRevista) {
		
		try {
			String urlString = URL_REVISTA_INPI + numeroRevista + ".zip";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD"); // Usar HEAD para verificar sem baixar o conteúdo
            connection.setConnectTimeout(5000); // Tempo limite de conexão (5 segundos)
            connection.setReadTimeout(5000);    // Tempo limite de leitura (5 segundos)
            connection.connect();			
            if(connection.getResponseCode() == 200)
            	return true;
            else return false;
		}catch (Exception e) {
			return false;
		}
	}

	public List<RevistaDto> listarRevistas() {

		List<Revista> all = revistaRepositoryCustom.listarRevistas(8);
		return converte(all);
	}

	private List<RevistaDto> converte(List<Revista> all) {

		List<RevistaDto> retorno = new ArrayList<RevistaDto>();
		for (Revista r : all)
			retorno.add(converte(r));

		return retorno;
	}

	private RevistaDto converte(Revista r) {

		return new RevistaDto(r.getNumeroRevista(), UtilData.converteData(r.getDataPublicacao(), "dd/MM/yyyy"),
				UtilData.converteData(r.getDataCarga(), "dd/MM/yyyy"), r.getStatus());
	}
	
	public RevistaDto consultaUltimaRevista() {
		
		return converte(revistaRepositoryCustom.consultarUltimaRevista());
	}

	public RevistaDto consultaPrimeiraRevista() {
		
		return converte(revistaRepositoryCustom.consultarPrimeiraRevista());
	}
	
	public Integer consultaTotalRevistas() {
		
		return revistaRepositoryCustom.consultarQtTotalRevistas();
	}
	
}