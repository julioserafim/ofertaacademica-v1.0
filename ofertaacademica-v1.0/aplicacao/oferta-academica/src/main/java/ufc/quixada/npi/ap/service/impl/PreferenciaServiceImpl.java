package ufc.quixada.npi.ap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.Disciplina;
import ufc.quixada.npi.ap.model.Preferencia;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.repository.PreferenciaRepository;
import ufc.quixada.npi.ap.service.PreferenciaService;

@Service
public class PreferenciaServiceImpl implements PreferenciaService {
	
	@Autowired
	private PreferenciaRepository preferenciaRepository;
	
	@Override
	public void salvar(Preferencia preferencia) throws AlocacaoProfessorException {
		Preferencia aux = preferenciaRepository.findByDisciplinaAndProfessor(preferencia.getDisciplina(), preferencia.getProfessor());
		if(aux == null)
			preferenciaRepository.save(preferencia);
		else
			throw new AlocacaoProfessorException("Essa preferência já existe.");
	}
	
	@Override
	public void excluir(Preferencia preferencia){
		preferenciaRepository.delete(preferencia);
	}
	
	@Override
	public List<Preferencia> buscarTodasPreferencias(){
		return preferenciaRepository.findAll();
	}
	
	@Override
	public Preferencia buscarPreferenciaDisciplina(Disciplina disciplina){
		return preferenciaRepository.findPrefenreciaByDisciplina(disciplina);
	}
	
	@Override
	public Preferencia buscarPreferenciaProfessor(Professor professor){
		return preferenciaRepository.findPreferenciaByProfessor(professor);
	}
	
	@Override
	public Preferencia buscarPreferenciaPorDisciplinaAndProfessor(Disciplina disciplina, Professor professor){
		return preferenciaRepository.findByDisciplinaAndProfessor(disciplina, professor);
	}
	
}
