package ufc.quixada.npi.ap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ufc.quixada.npi.ap.model.Oferta;
import ufc.quixada.npi.ap.model.RestricaoHorario;
import ufc.quixada.npi.ap.model.RestricaoHorario.Tipo;

@Repository
public interface RestricaoHorarioRepository extends JpaRepository<RestricaoHorario, Integer> {
	
	long countByPrimeiraOfertaOrSegundaOferta(Oferta primeira, Oferta segunda);
	
	@Query("FROM RestricaoHorario as r WHERE r.tipo = :tipo")
	List<RestricaoHorario> findByTipo(@Param("tipo") Tipo tipo);

	RestricaoHorario findByPrimeiraOfertaAndSegundaOfertaAndTipo(Oferta primeira,Oferta segunda, Tipo tipo); 
}
