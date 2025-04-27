package br.com.sartori.sgrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sartori.sgrm.model.Processo;

public interface IProcessoRepository extends JpaRepository<Processo, Long>{

	
}
