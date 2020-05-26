package ufc.quixada.npi.ap.service;

import java.util.List;

import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Professor;

public interface CursoService{
	
	public Curso buscarCurso(Integer id);

	public List<Curso> buscarTodosCursos();
	
	public Curso buscarCursoPorCoordenador(Professor professor);

	public Curso buscarCursoPorViceCoordenador(Professor professor);

	public Curso buscarCursoPorCoordenadorOuVice(Professor professor);
	
	public Curso buscarPorSigla(String sigla);
	
	public Curso buscarPorOferta(Integer idOferta);
	
	public void salvar(Curso curso) throws AlocacaoProfessorException;
}
