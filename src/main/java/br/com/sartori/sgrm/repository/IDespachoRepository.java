package br.com.sartori.sgrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sartori.sgrm.model.Despacho;

public interface IDespachoRepository extends JpaRepository<Despacho, String>{

	
}
