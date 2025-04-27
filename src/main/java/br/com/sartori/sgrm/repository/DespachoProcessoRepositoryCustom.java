package br.com.sartori.sgrm.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.sartori.sgrm.model.DespachoProcesso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class DespachoProcessoRepositoryCustom {
	
	@Autowired
	private EntityManager em;

	public void incluirDespachoProcessoSQL(DespachoProcesso des) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO despacho_processo (codigo, numero_revista, numero_processo) ");
		sql.append(" VALUES (:codigo, :numeroRevista, :numeroProcesso) ");
		
		Query query = em.createNativeQuery(sql.toString());

		query.setParameter("codigo", des.getId().getCodigo());
		query.setParameter("numeroRevista", des.getId().getNumeroRevista());
		query.setParameter("numeroProcesso", des.getId().getNumeroProcesso());

		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<DespachoProcesso> listarDespachosProcesso(Long numeroProcesso) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" Select dp ");
		sql.append(" from DespachoProcesso dp ");
		sql.append(" where dp.id.numeroProcesso = :numeroProcesso ");
		sql.append(" order by dp.id.numeroRevista ");
		
		Query query = em.createQuery(sql.toString(), DespachoProcesso.class);
		query.setParameter("numeroProcesso", numeroProcesso);
		return query.getResultList();
	}

}
