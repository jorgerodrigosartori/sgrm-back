package br.com.sartori.sgrm.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.sartori.sgrm.model.DespachoProcesso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

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

	@SuppressWarnings("unchecked")
	public boolean existemDespachosRevista(Integer numeroRevista) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" Select dp ");
		sql.append(" from DespachoProcesso dp ");
		sql.append(" where dp.id.numeroRevista = :numeroRevista ");
		
		Query query = em.createQuery(sql.toString(), DespachoProcesso.class);
		query.setParameter("numeroRevista", numeroRevista);
		List<DespachoProcesso> lista = query.getResultList();
		if(lista.isEmpty())
			return false;
		else return true;
	}
	
	public int excluirDespachos(List<Long> processos, Integer numeroRevista) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" delete DespachoProcesso ");
		sql.append(" where id.numeroProcesso not in (:processos) ");
		sql.append("   and id.numeroRevista = :numeroRevista ");
		
		Query query = em.createQuery(sql.toString());
		query.setParameter("processos", processos);
		query.setParameter("numeroRevista", numeroRevista);
		return query.executeUpdate();
	}
}
