package br.com.sartori.sgrm.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class MarcaAcompanhadaRepositoryCustom {
	
	@Autowired
	private EntityManager em;

	public Long recuperaProximoCodigo() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT nextval('sq_marca_acompanhada') ");		
		
		Query query = em.createQuery(sql.toString());

		return (Long) query.getSingleResult();
	}
	
	public Long qtMarcasAcompanhadas() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(*) from ProcessoAcompanhado ");		
		
		Query query = em.createQuery(sql.toString());

		return (Long) query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> qtMovimentacoesMarcasAcompanhadas() {
				
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT des.id.numeroRevista, count(*) ");
		sql.append(" FROM Revista rev ");
		sql.append("    , Processo pro ");
		sql.append("    , DespachoProcesso des ");
		sql.append("    , ProcessoAcompanhado aco ");
		sql.append(" where rev.numeroRevista = des.id.numeroRevista ");
		sql.append("   and pro.numeroProcesso = des.id.numeroProcesso ");
		sql.append("   and pro.numeroProcesso = aco.numeroProcesso ");
		sql.append(" group by des.id.numeroRevista ");
		sql.append(" order by des.id.numeroRevista desc ");
		Query query = em.createQuery(sql.toString());
		query.setMaxResults(5);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> ultimasMovimentacoesMarcasAcompanhadas() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT des.id.numeroRevista, pro.numeroProcesso, pro.nomeMarca ");
		sql.append(" FROM Revista rev ");
		sql.append("    , Processo pro ");
		sql.append("    , DespachoProcesso des ");
		sql.append("    , ProcessoAcompanhado aco ");
		sql.append(" where rev.numeroRevista = des.id.numeroRevista ");
		sql.append("   and pro.numeroProcesso = des.id.numeroProcesso ");
		sql.append("   and pro.numeroProcesso = aco.numeroProcesso ");
		//sql.append(" group by des.id.numeroRevista ");
		sql.append(" order by des.id.numeroRevista desc ");
		Query query = em.createQuery(sql.toString());
		query.setMaxResults(5);
		
		return query.getResultList();
	}
	
}
