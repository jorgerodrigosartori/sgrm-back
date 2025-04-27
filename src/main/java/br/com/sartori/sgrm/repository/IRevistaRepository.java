package br.com.sartori.sgrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sartori.sgrm.model.Revista;

public interface IRevistaRepository extends JpaRepository<Revista, Integer>{

	
}
