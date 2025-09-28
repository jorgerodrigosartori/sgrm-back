package br.com.sartori.sgrm.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.sartori.sgrm.model.DespachoProcesso;
import br.com.sartori.sgrm.model.Processo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class ProcessoRepositoryCustom {
	
	@Autowired
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public List<DespachoProcesso> listarProcessosPorRevista(Integer revista) {
		
    	System.out.println("Revista: " + revista);
        StringBuilder sql = new StringBuilder();
		sql.append(" SELECT dp ");
        sql.append(" FROM Processo p ");
        sql.append("    , DespachoProcesso dp ");
        sql.append("    , Despacho d");
        sql.append(" WHERE dp.id.numeroProcesso = p.numeroProcesso ");        
        sql.append("   AND dp.id.codigo = d.codigo ");
        sql.append("   AND dp.id.numeroRevista = :revista ");
        sql.append(" order by p.nomeMarca ");
 
        Query query = em.createQuery(sql.toString(), DespachoProcesso.class);
        query.setParameter("revista", revista);
        query.setMaxResults(20);
        query.setFirstResult(50);
        
        query.setParameter("revista", revista);
       	return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Processo> listarProcessosProcessosPorFragmento(List<String> fragmentos, List<Integer> revistas, Long processo) {

        StringBuilder sql = new StringBuilder();
		sql.append(" SELECT distinct (p) ");
        sql.append(" FROM Processo p ");
        sql.append("    , DespachoProcesso dp ");
        sql.append(" WHERE p.numeroProcesso <> :processo ");
        sql.append("   AND p.numeroProcesso = dp.id.numeroProcesso ");
        sql.append("   AND dp.id.numeroRevista in (:revistas) ");
        
        for(int i = 0; i < fragmentos.size(); i++) {
        	String fragmento = fragmentos.get(i);
        	if (i == 0)
        		sql.append(" AND ( (p.nomeMarca) like '%" + fragmento.toUpperCase() + "%'");
        	else sql.append(" or (p.nomeMarca) like '%" + fragmento.toUpperCase() + "%'");
        }
        sql.append(" ) ");
        sql.append(" order by p.nomeMarca ");
 
        Query query = em.createQuery(sql.toString(), Processo.class);
        query.setParameter("processo", processo);
        query.setParameter("revistas", revistas);

       	return query.getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public List<DespachoProcesso> listarAcompanhados() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT dp ");
		sql.append(" FROM Processo p ");
		sql.append("    , ProcessoAcompanhado pa ");
		sql.append("    , DespachoProcesso dp ");
		sql.append(" WHERE p.numeroProcesso = pa.numeroProcesso ");
		sql.append("   AND p.numeroProcesso = dp.id.numeroProcesso");		
		sql.append(" order by p.nomeMarca, dp.revista.dataPublicacao ");
		
		Query query = em.createQuery(sql.toString(), DespachoProcesso.class);	
		return query.getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Processo> listarProcessosAcompanhados() {
		
        StringBuilder sql = new StringBuilder();
		sql.append(" SELECT p ");
        sql.append(" FROM Processo p ");
        sql.append("    , ProcessoAcompanhado pa ");
        sql.append(" WHERE p.numeroProcesso = pa.numeroProcesso ");        
        sql.append("   AND pa.semelhantes = :semelhantes ");        
        sql.append(" order by p.nomeMarca ");
        
        Query query = em.createQuery(sql.toString(), Processo.class);
        query.setParameter("semelhantes", "S");
        
       	return query.getResultList();
	}

	
	public Integer getQtProcessos() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(*) ");
		sql.append(" FROM Processo p ");
		
		Query query = em.createQuery(sql.toString(), Long.class);	
		return ((Long) query.getSingleResult()).intValue();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Processo> listarProcessos(String marca, Long processo) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT p ");
		sql.append(" FROM Processo p ");
		sql.append(" WHERE 1 = 1 ");
		
		if(processo != null)
			sql.append(" AND p.numeroProcesso = :processo ");
		else sql.append(" AND p.nomeMarca like :marca");
		
		sql.append(" order by p.nomeMarca");
		
		Query query = em.createQuery(sql.toString(), Processo.class);
		
		if(processo != null)
			query.setParameter("processo", processo);
		else query.setParameter("marca", "%" + marca.toUpperCase() + "%");
		
		return query.getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Processo> listarProcessos(List<Long> processos) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT p ");
		sql.append(" FROM Processo p ");
		sql.append(" WHERE p.numeroProcesso in (:processos) ");
		sql.append(" order by p.nomeMarca ");
		
		Query query = em.createQuery(sql.toString(), Processo.class);
		query.setParameter("processos", processos);
		
		return query.getResultList();
	}	
	
	public void incluirSQL(Processo pro) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO processo (numero_processo, nome_marca, data_deposito, data_concessao, data_vigencia, titular) ");
		sql.append(" VALUES (:processo, :marca, :dataDeposito, :dataConcessao, :dataVigencia, :titular) ");

		Query query = em.createNativeQuery(sql.toString());

		query.setParameter("processo", pro.getNumeroProcesso());
		query.setParameter("marca", pro.getNomeMarca());
		query.setParameter("dataDeposito", pro.getDataDeposito());
		query.setParameter("dataConcessao", pro.getDataConcessao());
		query.setParameter("dataVigencia", pro.getDataVigencia());
		query.setParameter("titular", pro.getTitular());

		query.executeUpdate();
	}
}
