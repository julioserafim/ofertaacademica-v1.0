package ufc.quixada.npi.ap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Turma;
import ufc.quixada.npi.ap.repository.TurmaRepository;
import ufc.quixada.npi.ap.service.TurmaService;

@Service
public class TurmaServiceImpl implements TurmaService {

	@Autowired
	private TurmaRepository turmaRepository;
	
	@Override
	public List<Turma> buscarTodasTurmas() {
		return turmaRepository.findAll();
	}

	@Override
	public Turma buscarTurma(Integer id) {
		return turmaRepository.findOne(id);
	}
	
	@Override
	public List<Turma> buscarTurmasByCurso(Curso curso) {
		return turmaRepository.findTurmasByCurso(curso);
	}
	
}