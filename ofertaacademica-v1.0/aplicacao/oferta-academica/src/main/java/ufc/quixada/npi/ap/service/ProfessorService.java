package ufc.quixada.npi.ap.service;

import java.util.List;

import br.ufc.quixada.npi.ldap.model.Usuario;
import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.model.RelatorioCargaHorariaProfessor;

public interface ProfessorService {
	
	public Professor salvar(Professor professor) throws AlocacaoProfessorException;
	
	public void editar(Professor professor) throws AlocacaoProfessorException;
		
	public Professor buscarProfessor(Integer id);
	
	public Professor buscarProfessorCpf(String cpf);
	
	public List<Professor> buscarTodosProfessores();
	
	public List<Professor> buscarTodosProfessoresSemRelacionamento();

	public RelatorioCargaHorariaProfessor gerarRelatorioCargaHorariaProfessores();
	
	public void criar(Usuario usuario) throws AlocacaoProfessorException;
	
}

