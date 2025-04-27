package br.com.sartori.sgrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sartori.sgrm.model.ProcessoAcompanhado;

public interface IProcessoAcompanhadoRepository extends JpaRepository<ProcessoAcompanhado, Long>{

	
}
