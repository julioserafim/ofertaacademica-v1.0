package ufc.quixada.npi.ap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.ufc.quixada.npi.ldap.model.Usuario;
import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.Oferta;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.model.RelatorioCargaHorariaProfessor;
import ufc.quixada.npi.ap.repository.ProfessorRepository;
import ufc.quixada.npi.ap.service.OfertaService;
import ufc.quixada.npi.ap.service.ProfessorService;

@Service
public class ProfessorServiceImpl implements ProfessorService {

	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private OfertaService ofertaService;

	@Override
	public Professor salvar(Professor professor) throws AlocacaoProfessorException {

		professor(professor);
		if (professor.getRelacionamento() != null)
			professor.getRelacionamento().setRelacionamento(professor);

		return professorRepository.save(professor);
	}

	private void professor(Professor professor) {
		if (null == professor.getCargaHorariaMinima())
			professor.setCargaHorariaMinima(0);
		if (null == professor.getCargaHorariaMaxima())
			professor.setCargaHorariaMaxima(0);
		professor.setPassword("123");
		professor.setAtivo(true);
	}

	@Override
	public void editar(Professor professor) throws AlocacaoProfessorException {

		if (professor.getRelacionamento() != null) {
			professor.getRelacionamento().setRelacionamento(professor);
		}
		professor.setPassword("123");
		professorRepository.save(professor);
	}

	@Override
	public Professor buscarProfessor(Integer id) {
		return professorRepository.findOne(id);
	}

	@Override
	public List<Professor> buscarTodosProfessores() {
		return professorRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));

	}

	@Override
	public List<Professor> buscarTodosProfessoresSemRelacionamento() {
		return professorRepository.findProfessoresSemRelacionamento();
	}

	@Override
	public RelatorioCargaHorariaProfessor gerarRelatorioCargaHorariaProfessores() {
		RelatorioCargaHorariaProfessor relatorio = new RelatorioCargaHorariaProfessor();

		List<Professor> professores = professorRepository.findAll();

		for (Professor professor : professores) {
			List<Oferta> ofertasProfessor = ofertaService.buscarOfertasPeriodoAtivoPorProfessor(professor);
			
			int cargaHorariaAtual = 0;

			this.incrementarCargaHoraria(ofertasProfessor, cargaHorariaAtual);
			
			professor.setCargaHorariaAtual(cargaHorariaAtual);

			this.verificarCargaHorariaDoProfessor(cargaHorariaAtual, professor, relatorio);

		}

		return relatorio;
	}

	private void incrementarCargaHoraria(List<Oferta> ofertasProfessor, int cargaHorariaAtual) {
		for (Oferta o : ofertasProfessor){
			cargaHorariaAtual += o.getDisciplina().getCreditos();
		}
	}

	private void verificarCargaHorariaDoProfessor(int cargaHorariaAtual, Professor professor, RelatorioCargaHorariaProfessor relatorio){
		if (cargaHorariaAtual < professor.getCargaHorariaMinima()){
			relatorio.adicionarProfessorCargaHorariaInsuficiente(professor);
		}
		else if (cargaHorariaAtual > professor.getCargaHorariaMaxima()){
			relatorio.adicionarProfessorCargaHorariaExcedida(professor);
		}
		else{
			relatorio.adicionarProfessorCargaHorariaNormal(professor);
		}
	}

	@Override
	public Professor buscarProfessorCpf(String cpf) {
		return professorRepository.findProfessorByCpf(cpf);
	}

	@Override
	public void criar(Usuario usuario) throws AlocacaoProfessorException {
		
		Professor professor1 = new Professor();
		
		professor1.setCpf(usuario.getCpf());
		professor1.setEmail(usuario.getEmail());
		professor1.setNome(usuario.getNome());
		professor1.setApelido(usuario.getNome());
		
		salvar(professor1);
		
	}
}
