package ufc.quixada.npi.ap.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Papel;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.repository.CursoRepository;
import ufc.quixada.npi.ap.service.CursoService;

@Service
public class CursoServiceImpl implements CursoService {

	@Autowired
	private CursoRepository cursoRepository;
	
	@Override
	public Curso buscarCurso(Integer id) {
		return cursoRepository.findOne(id);
	}

	@Override
	public List<Curso> buscarTodosCursos() {
		return cursoRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
	}
	
	@Override
	public Curso buscarCursoPorCoordenadorOuVice(Professor professor) {
		return cursoRepository.findCursoByCoordenadorOrVice(professor);
	}

	public Curso buscarCursoPorCoordenador(Professor professor) {
		return cursoRepository.findCursoByCoordenadorOrVice(professor);
	}
	

	@Override
	public Curso buscarCursoPorViceCoordenador(Professor professor) {
		return cursoRepository.findByViceCoordenador(professor);
	}

	@Override
	public Curso buscarPorSigla(String sigla) {
		return cursoRepository.findBySigla(sigla);
	}
	

	@Override
	public Curso buscarPorOferta(Integer idOferta) {
		return cursoRepository.findByOferta(idOferta);
	}
	
	@Override
	public void salvar(Curso curso) throws AlocacaoProfessorException {
		
		Papel pl = new Papel();
		pl.setId(1);
		
		curso.getCoordenador().getPapeis().add(pl);
		curso.getViceCoordenador().getPapeis().add(pl);
		curso.setSigla(curso.getSigla().toUpperCase());
		cursoRepository.save(curso);
	}
}
