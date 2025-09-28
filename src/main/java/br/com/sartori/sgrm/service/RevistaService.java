package br.com.sartori.sgrm.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.bean.dto.RevistaDto;
import br.com.sartori.sgrm.bean.xml.DespachoXml;
import br.com.sartori.sgrm.bean.xml.ProcessoXml;
import br.com.sartori.sgrm.bean.xml.RevistaXml;
import br.com.sartori.sgrm.model.Despacho;
import br.com.sartori.sgrm.model.DespachoProcesso;
import br.com.sartori.sgrm.model.Processo;
import br.com.sartori.sgrm.model.Revista;
import br.com.sartori.sgrm.repository.IDespachoRepository;
import br.com.sartori.sgrm.repository.IProcessoRepository;
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
	private IDespachoRepository iDespachoRepository;
	
	@Autowired
	private IProcessoRepository iProcessoRepository;
	
	@Autowired
	private RevistaAsyncService revistaAsyncService;
	
	private String URL_REVISTA_INPI = "https://revistas.inpi.gov.br/txt/RM";
	
	public RevistaDto cargaRevista(Integer numeroRevista) {
		
		Revista rev = new Revista();
		rev.setDataCarga(new Date());
		rev.setNumeroRevista(numeroRevista);
		rev.setStatus("P");
		rev.setIntegral("S");
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
	
	private String processaCargaRevista(Revista rev, Long idProcesso) {

		try {
			RevistaXml revista = revistaAsyncService.recuperaRevistas(rev.getNumeroRevista());
			rev.setDataPublicacao(UtilData.converteData(revista.getData(), "dd/MM/yyyy"));

			System.out.println(new Date() + " - Quantidade de processos: " + revista.getProcessos().size());

			boolean encontrou = false;
			for (ProcessoXml pro : revista.getProcessos()) {
				
				if(Long.valueOf(pro.getNumero()).equals(idProcesso)) {
				
					Optional<Processo> byId = iProcessoRepository.findById(idProcesso);
					Processo processo;
					if (byId.isPresent()) 
						processo = byId.get();
					else processo = new Processo(Long.valueOf(pro.getNumero()));
					
					if (pro.getDataDeposito() != null && processo.getDataDeposito() == null)
						processo.setDataDeposito(UtilData.converteData(pro.getDataDeposito(), "dd/MM/yyyy"));
					
					if (pro.getDataConcessao() != null && processo.getDataConcessao() == null)
						processo.setDataConcessao(UtilData.converteData(pro.getDataConcessao(), "dd/MM/yyyy"));
					
					if (pro.getDataVigencia() != null && processo.getDataVigencia() == null)
						processo.setDataVigencia(UtilData.converteData(pro.getDataVigencia(), "dd/MM/yyyy"));
					
					if (pro.getTitulares() != null && pro.getTitulares().getTitularList() != null
							&& pro.getTitulares().getTitularList().size() > 0 && processo.getTitular() == null)
						processo.setTitular(pro.getTitulares().getTitularList().get(0).getNomeRazaoSocial().length() > 500
								? pro.getTitulares().getTitularList().get(0).getNomeRazaoSocial().substring(0, 500)
										: pro.getTitulares().getTitularList().get(0).getNomeRazaoSocial());
					
					if (pro.getMarca() != null && processo.getNomeMarca() == null)
						processo.setNomeMarca(pro.getMarca().getNome());
					
					if (pro.getProcurador() != null && processo.getProcurador() == null)
						processo.setProcurador((pro.getProcurador() != null && pro.getProcurador().length() > 200)
								? pro.getProcurador().substring(0, 200)
										: pro.getProcurador());
					
					if (pro.getListaClasseNice() != null && pro.getListaClasseNice().getClasseNiceList() != null
							&& pro.getListaClasseNice().getClasseNiceList().size() > 0) {
						processo.setClasse(pro.getListaClasseNice().getClasseNiceList().get(0).getCodigo());
						processo.setStatus(pro.getListaClasseNice().getClasseNiceList().get(0).getStatus().length() > 50
								? pro.getListaClasseNice().getClasseNiceList().get(0).getStatus().substring(0, 50)
										: pro.getListaClasseNice().getClasseNiceList().get(0).getStatus());
					}
					List<DespachoProcesso> listaDespachosProcesso = new ArrayList<DespachoProcesso>();
					for (DespachoXml des : pro.getDespachos().getDespachoList()) {
						Despacho d = iDespachoRepository.findById(des.getCodigo()).get();
						DespachoProcesso desPro = new DespachoProcesso(rev.getNumeroRevista(), processo.getNumeroProcesso(),
								des.getCodigo());
						desPro.setDespacho(d);
						desPro.setProcesso(processo);
						desPro.setRevista(rev);
						listaDespachosProcesso.add(desPro);
					}
					iProcessoRepository.save(processo);
					revistaAsyncService.gravaBancoDespachoProcesso(listaDespachosProcesso);
					System.out.println(new Date() + " - Final processamento revista ");
					encontrou = true;
					break;
				}
			}
			if(!encontrou)
				return "Processo não encontrado na revista informada.";
		} catch (Exception e) {
			rev.setStatus("E");
			e.printStackTrace();
		} finally {
			rev.setStatus("S");
			iRevistaRepository.save(rev);
		}
		return "";
	}
	
	public String carregarProcessoRevista(Long idProcesso, Integer idRevista) {

		Optional<Revista> byId = iRevistaRepository.findById(idRevista);
		Revista revista;
		if(byId.isPresent()) {
			revista = byId.get();
			if(revista.getIntegral() == null || revista.getIntegral().equals("S")) {
				return "Esta revista (" + idRevista + ") já está integralmente carregada no banco.";
			}
		}else {
			revista = new Revista();
			revista.setIntegral("N");
			revista.setDataCarga(new Date());
			revista.setNumeroRevista(idRevista);
			iRevistaRepository.save(revista);
		}
		return processaCargaRevista(revista, idProcesso);
	}
	

}