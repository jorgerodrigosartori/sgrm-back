package br.com.sartori.sgrm.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.sartori.sgrm.bean.xml.DespachoXml;
import br.com.sartori.sgrm.bean.xml.ProcessoXml;
import br.com.sartori.sgrm.bean.xml.RevistaXml;
import br.com.sartori.sgrm.model.Despacho;
import br.com.sartori.sgrm.model.DespachoProcesso;
import br.com.sartori.sgrm.model.Processo;
import br.com.sartori.sgrm.model.Revista;
import br.com.sartori.sgrm.repository.IRevistaRepository;
import br.com.sartori.sgrm.util.UtilData;
import jakarta.transaction.Transactional;

@Service
public class RevistaAsyncService {

	@Autowired
	private IRevistaRepository iRevistaRepository;

	@Autowired
	private ProcessoService processoService;

	@Autowired
	private DespachoProcessoService despachoProcessoService;

	@Autowired
	private DespachoService despachoService;

	@Autowired
	private ClasseNiceService classeNiceService;
	
	private String URL_REVISTA_INPI = "https://revistas.inpi.gov.br/txt/RM";

	@Async
	public void processaCargaRevista(Revista rev) {

		try {
			RevistaXml revista = recuperaRevistas(rev.getNumeroRevista());
			rev.setDataPublicacao(UtilData.converteData(revista.getData(), "dd/MM/yyyy"));

			System.out.println(new Date() + " - Quantidade de processos: " + revista.getProcessos().size());
			System.out.println(new Date() + " - Atualiza tabela despachos ");
			HashMap<String, Despacho> mapaDespachos = despachoService.atualizaDespachos(revista);
			System.out.println(new Date() + " - Atualiza tabela classes ");
			classeNiceService.atualizaClassesNice(revista);

			System.out.println(new Date() + " - Inicia geracao objetos para gravação no banco ");

			List<Processo> listaProcessos = new ArrayList<Processo>();
			List<DespachoProcesso> listaDespachosProcesso = new ArrayList<DespachoProcesso>();

			
			List<Long> numerosProcessos = revista.getProcessos().stream()
	                .map(pro -> Long.valueOf(pro.getNumero()))
	                .toList();
					
			int batchSize = 100;
			Map<Long, Processo> processosExistentes = new HashMap<>();


			Integer contador = 0;
			for (int i = 0; i < numerosProcessos.size(); i += batchSize) {
				contador++;
			    int end = Math.min(i + batchSize, numerosProcessos.size());
			    List<Long> subLista = numerosProcessos.subList(i, end);

			    List<Processo> lote = processoService.consultaProcessos(subLista);
			    for (Processo p : lote) {
			        processosExistentes.put(p.getNumeroProcesso(), p);
			    }
			    System.out.println("Concluida consulta banco " + (contador * 100) + " de " + processosExistentes.size());
			}
			
			contador = 0;

			for (ProcessoXml pro : revista.getProcessos()) {

				contador++;
				Processo processo = processosExistentes.get(Long.valueOf(pro.getNumero()));
				if (processo == null) {
					processo = new Processo(Long.valueOf(pro.getNumero()));
				}
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

				listaProcessos.add(processo);

				for (DespachoXml des : pro.getDespachos().getDespachoList()) {
					Despacho d = mapaDespachos.get(des.getCodigo());
					DespachoProcesso desPro = new DespachoProcesso(rev.getNumeroRevista(), processo.getNumeroProcesso(),
							des.getCodigo());
					desPro.setDespacho(d);
					desPro.setProcesso(processo);
					desPro.setRevista(rev);
					listaDespachosProcesso.add(desPro);
				}
			}
			System.out.println(new Date() + " - Final geração objetos para gravar. Contador: " + contador + " - Iniciando gravação ");
			gravaBancoProcesso(listaProcessos);
			gravaBancoDespachoProcesso(listaDespachosProcesso);
			System.out.println(new Date() + " - Final processamento revista ");
			rev.setStatus("S");
		} catch (Exception e) {
			rev.setStatus("E");
			e.printStackTrace();
		}
		iRevistaRepository.save(rev);
	}

	//@Transactional
	private void gravaBancoProcesso(List<Processo> listaProcessos) {

		int size = 1000;
		List<List<Processo>> partitionedLists = new ArrayList<List<Processo>>();
		for (int i = 0; i < listaProcessos.size(); i += size) {
			int end = Math.min(i + size, listaProcessos.size());
			partitionedLists.add(new ArrayList<>(listaProcessos.subList(i, end)));
		}

		System.out.println(new Date() + " - Grava processos: " + listaProcessos.size());
		// partitionedLists.parallelStream().forEach(p ->
		// processoService.salvarProcessos(p));

		int qtLista = 0;
		for (List<Processo> lista : partitionedLists) {
			qtLista++;
			salvarProcessos(lista);
			System.out.println(new Date() + " - Grava processos: " + qtLista + " de " + partitionedLists.size());
		// despachoProcessoService.salvarAll(lista);
		}
	}
	
	@Transactional
	private void salvarProcessos(List<Processo> lista) {
		
		processoService.salvarProcessos(lista);
	}

	//@Transactional
	private void gravaBancoDespachoProcesso(List<DespachoProcesso> listaDespachosProcesso) {

		int size = 1000;
		List<List<DespachoProcesso>> partitionedLists = new ArrayList<List<DespachoProcesso>>();
		for (int i = 0; i < listaDespachosProcesso.size(); i += size) {
			int end = Math.min(i + size, listaDespachosProcesso.size());
			partitionedLists.add(new ArrayList<>(listaDespachosProcesso.subList(i, end)));
		}

		System.out.println(new Date() + " - Grava despachos de processos: " + listaDespachosProcesso.size());
		int qtListas = 0;
		for (List<DespachoProcesso> lista : partitionedLists) {
			qtListas++;
			salvarDespachoProcesso(lista);
			System.out.println(new Date() + " - Grava despachos de processos: " + qtListas + " de " + partitionedLists.size());
		}
	}
	
	@Transactional
	private void salvarDespachoProcesso(List<DespachoProcesso> lista) {
		
		despachoProcessoService.salvarAll(lista);
	}
	
	private RevistaXml recuperaRevistas(Integer numeroRevista) {

		System.out.println(" ");
		System.out.println("*****************************************************************************");
		System.out.println(new Date() + " - Inicio da leitura do arquivo zip revista: " + numeroRevista);
		System.out.println("*****************************************************************************");
		RevistaXml revista = null;
		String urlString = URL_REVISTA_INPI + numeroRevista + ".zip";
		String xmlFileName = "RM" + numeroRevista + ".xml"; // Nome do arquivo XML dentro do ZIP

		try {
			// Baixando o arquivo ZIP da URL
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			InputStream inputStream = connection.getInputStream();

			// Criando um buffer para ler os dados do arquivo ZIP
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > 0) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byteArrayOutputStream.close();
			inputStream.close();

			// Lendo o arquivo ZIP a partir do conteúdo baixado
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
			ZipEntry entry;

			// Procurando o arquivo XML no ZIP
			System.out.println(new Date() + " - Carrega objetos com XML");
			while ((entry = zipInputStream.getNextEntry()) != null) {
				if (entry.getName().equals(xmlFileName)
						|| entry.getName().equals(xmlFileName.substring(2, xmlFileName.length()))) {
					// System.out.println("Found XML file: " + entry.getName());

					// Usando JAXB para converter o XML em objetos Java
					JAXBContext jaxbContext = JAXBContext.newInstance(RevistaXml.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					revista = (RevistaXml) unmarshaller.unmarshal(zipInputStream);
					break;
				}
				zipInputStream.closeEntry();
			}
			zipInputStream.close();

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		System.out.println(new Date() + " - Fim leitura arquivo");
		return revista;
	}
}