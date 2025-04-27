package br.com.sartori.sgrm.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.sartori.sgrm.model.Revista;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class RevistaRepositoryCustom {
	
	@Autowired
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Revista> listarRevistas(Integer quantidade) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select rev ");		
		sql.append(" from Revista rev ");	
		sql.append(" where rev.status = :status ");	
		sql.append(" order by rev.numeroRevista desc ");		
		
		Query query = em.createQuery(sql.toString());
		query.setParameter("status", "S");
		query.setMaxResults(quantidade);

		return query.getResultList();
	}

	public List<Integer> listarNumeroRevistas(Integer quantidade) {
		
		List<Revista> listarRevistas = listarRevistas(quantidade);
		
		return listarRevistas.stream()
                .map(Revista::getNumeroRevista)
                .toList();
	}
	
	public Revista consultarUltimaRevista() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select rev ");		
		sql.append(" from Revista rev ");		
		sql.append(" where rev.numeroRevista = (select max(re.numeroRevista) from Revista re) ");				
		Query query = em.createQuery(sql.toString(), Revista.class);
		return (Revista) query.getSingleResult();
	}

	public Revista consultarPrimeiraRevista() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select rev ");		
		sql.append(" from Revista rev ");		
		sql.append(" where rev.numeroRevista = (select min(re.numeroRevista) from Revista re) ");				
		Query query = em.createQuery(sql.toString(), Revista.class);
		return (Revista) query.getSingleResult();
	}
	
	public Integer consultarQtTotalRevistas() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) ");		
		sql.append(" from Revista rev ");		
		Query query = em.createQuery(sql.toString(), Long.class);
		return ((Long) query.getSingleResult()).intValue();
	}
	
}