package br.com.sartori.sgrm.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.sartori.sgrm.model.ClasseNice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class ClasseNiceRepositoryCustom {
	
	@Autowired
	private EntityManager em;

	public void incluirClasseNiceSQL(ClasseNice des) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO classe_nice (codigo, especificacao) ");
		sql.append(" VALUES (:codigo, :especificacao) ");
		
		Query query = em.createNativeQuery(sql.toString());

		query.setParameter("codigo", des.getCodigo());
		query.setParameter("especificacao", des.getEspecificacao());

		query.executeUpdate();
	}
}
