package ufc.quixada.npi.ap.service;

import java.util.List;

import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Turma;

public interface TurmaService {
	
	public Turma buscarTurma(Integer id);
	
	public List<Turma> buscarTodasTurmas();
	
	public List<Turma> buscarTurmasByCurso(Curso curso);

}