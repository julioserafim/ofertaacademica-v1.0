package ufc.quixada.npi.ap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ufc.quixada.npi.ap.model.Periodo;

@Repository
@Transactional
public interface PeriodoRepository extends JpaRepository<Periodo, Integer>{
	
	Periodo findPeriodoByAtivoTrue();
	
	@Query("FROM Periodo WHERE ativo = false ")
	List<Periodo> findPeriodosConsolidados();

	@Query("SELECT p FROM Periodo as p ORDER BY p.ano DESC, p.semestre DESC")
	List<Periodo> buscarTodosPeriodos();
	
	@Modifying
	@Query("UPDATE Periodo SET ativo = false WHERE ativo = TRUE")
	void resetarPeriodoAtivo();
	
}

