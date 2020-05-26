package ufc.quixada.npi.ap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufc.quixada.npi.ap.model.Disciplina;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {

	Disciplina findDisciplinaByNome(String nome);
	
	Disciplina findDisciplinaByCodigo(String codigo);
	
	@Query("SELECT d FROM Disciplina as d WHERE d.arquivada is FALSE ORDER by d.nome")
	List<Disciplina> findDisciplinaByArquivadaFalse();
	
}
