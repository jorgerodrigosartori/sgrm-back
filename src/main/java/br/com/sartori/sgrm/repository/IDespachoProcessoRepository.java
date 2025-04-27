package br.com.sartori.sgrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sartori.sgrm.model.DespachoProcesso;
import br.com.sartori.sgrm.model.DespachoProcessoId;

public interface IDespachoProcessoRepository extends JpaRepository<DespachoProcesso, DespachoProcessoId>{

	
}
