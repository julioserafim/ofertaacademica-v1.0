package ufc.quixada.npi.ap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufc.quixada.npi.ap.model.Disciplina;
import ufc.quixada.npi.ap.model.Preferencia;
import ufc.quixada.npi.ap.model.Professor;

@Repository
public interface PreferenciaRepository extends JpaRepository<Preferencia, Integer>{
	
	Preferencia findPrefenreciaByDisciplina(Disciplina disciplina);

	Preferencia findPreferenciaByProfessor(Professor professor);
	
	Preferencia findByDisciplinaAndProfessor(Disciplina disciplina, Professor professor);
	
	
}
