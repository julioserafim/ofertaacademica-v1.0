package ufc.quixada.npi.ap.service;


import java.util.List;

import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.Disciplina;
import ufc.quixada.npi.ap.model.Preferencia;
import ufc.quixada.npi.ap.model.Professor;


public interface PreferenciaService {
	
	public void salvar(Preferencia preferencia) throws  AlocacaoProfessorException;
	
	void excluir(Preferencia preferencia);
	
	public List<Preferencia> buscarTodasPreferencias();
	
	public Preferencia buscarPreferenciaDisciplina(Disciplina disciplina);
	
	public Preferencia buscarPreferenciaProfessor(Professor professor);
	
	public Preferencia buscarPreferenciaPorDisciplinaAndProfessor(Disciplina disciplina, Professor professor);
	
}
