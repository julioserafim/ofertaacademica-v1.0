package ufc.quixada.npi.ap.service;

import java.util.List;
import java.util.Map;

import ufc.quixada.npi.ap.exception.AlocacaoProfessoresException;
import ufc.quixada.npi.ap.model.*;

public interface OfertaService {
	
	public void salvarOfertaPeriodoAtivo(Oferta oferta);
	
	public void salvarOferta(Oferta oferta) throws AlocacaoProfessoresException;
	
	public void excluir(Integer id);

	public Oferta buscarOferta(Integer id);

	public List<Oferta> buscarOfertaPorPeriodo(Periodo periodo);
	
	public List<Oferta> buscarOfertasPeriodoAtivo();
	
	public List<Oferta> buscarOfertasPeriodoAtivoPorProfessor(Professor professor);
	
	public List<Oferta> buscarPorPeriodo(Periodo periodo);
	
	public List<Oferta> buscarPorPeriodoAndCurso(Periodo periodo, Curso curso);
	
	public List<Oferta> buscarOfertasImportadasPeriodoAtivoPorPeriodoAndCurso(Periodo periodo, Periodo periodoAtivo, Curso curso);
	
	public List<Oferta> buscarOfertasNaoImportadasPeriodoAtivoPorPeriodoAndCurso(Periodo periodo, Periodo periodoAtivo, Curso curso);
	
	public void importarOfertas(List<Oferta> ofertas);

	void substituirOferta(List<Integer> idOfertas);
	
	boolean hasCompartilhamentoOuRestricaoHorario(Oferta oferta);
	
}

