package ufc.quixada.npi.ap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufc.quixada.npi.ap.model.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer>{
	
	Professor findProfessorByCpf(String cpf);
	
	Professor findProfessorByApelido(String apelido);
	
	Professor findProfessorByEmail(String email);
	
	List<Professor> findAll();

	@Query("from Professor p where p.relacionamento is null ORDER BY nome")
	List<Professor> findProfessoresSemRelacionamento();

}