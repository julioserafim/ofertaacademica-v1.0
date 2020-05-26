package ufc.quixada.npi.ap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Curso.Turno;
import ufc.quixada.npi.ap.model.Professor;

public interface CursoRepository extends JpaRepository<Curso, Integer>{
	
	Curso findByCoordenador(Professor coordenador);
	
	Curso findCursoById(Integer id);

	Curso findByViceCoordenador(Professor coordenador);

	Curso findCursoByCoordenador(Professor professor);

	@Query("SELECT c FROM Curso c WHERE c.coordenador =:professor OR c.viceCoordenador =:professor")
	Curso findCursoByCoordenadorOrVice(@Param("professor") Professor professor);

	Curso findBySigla(String sigla);
	
	Curso findCursoByCodigo(String codigo);
	
	Curso findCursoByTurno(Enum<Turno> turno);
	
	Curso findCursoByNome(String nome);

	@Query("SELECT o.turma.curso FROM Oferta o WHERE o.id = :idOferta")
	Curso findByOferta(@Param("idOferta") Integer idOferta);

}
