package ufc.quixada.npi.ap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.exception.AlocacaoProfessoresException;
import ufc.quixada.npi.ap.model.*;
import ufc.quixada.npi.ap.repository.CompartilhamentoRepository;
import ufc.quixada.npi.ap.repository.OfertaRepository;
import ufc.quixada.npi.ap.repository.RestricaoHorarioRepository;
import ufc.quixada.npi.ap.service.OfertaService;
import ufc.quixada.npi.ap.service.PeriodoService;

import static ufc.quixada.npi.ap.util.Constants.MAX_CREDITOS_TURMA;

@Service
public class OfertaServiceImpl implements OfertaService {

	@Autowired
	private OfertaRepository ofertaRepository;

	@Autowired
	private PeriodoService periodoService;
	
	@Autowired
	private CompartilhamentoRepository compartilhamentoRepository;
	
	@Autowired
	private RestricaoHorarioRepository restricaoHorarioRepository;
	
	@Override
	public void salvarOfertaPeriodoAtivo(Oferta oferta) {
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		oferta.setPeriodo(periodoAtivo);
		ofertaRepository.save(oferta);
	}
	
	public void salvarOferta(Oferta oferta) throws AlocacaoProfessoresException {
		if(oferta.getPeriodo()==null){
			throw new AlocacaoProfessoresException("Período de Oferta inválido");
		}
		Integer totalCreditos = ofertaRepository.getTotalCreditosTurmaPorPeriodo(oferta.getPeriodo().getId(), oferta.getTurma().getId());
		if(totalCreditos==null){
			totalCreditos = 0;
		}
		Integer novoTotalCreditos = totalCreditos + oferta.getDisciplina().getCreditos();
		if(novoTotalCreditos!=null && novoTotalCreditos > MAX_CREDITOS_TURMA) {
			throw new AlocacaoProfessoresException("O limite de "+MAX_CREDITOS_TURMA+" créditos para a turma do "+oferta.getTurma().getSemestre().getDescricao()+" semestre foi atingido.");
		}
		ofertaRepository.save(oferta);
	}

	@Override
	public Oferta buscarOferta(Integer id) {
		return ofertaRepository.findOne(id);
	}
	
	@Override
	public List<Oferta> buscarOfertaPorPeriodo(Periodo periodo) {
		return ofertaRepository.findOfertaByPeriodo(periodo);
	}
	
	@Override
	public List<Oferta> buscarOfertasPeriodoAtivo() {
		return ofertaRepository.findByPeriodoAtivoTrue();
	}
	
	@Override
	public List<Oferta> buscarOfertasPeriodoAtivoPorProfessor(Professor professor) {
		return ofertaRepository.findOfertasByPeriodo_AtivoTrueAndProfessores(professor);
	}
	
	@Override
	public void excluir(Integer id) {
		ofertaRepository.delete(id);
	}
	
	@Override
	public List<Oferta> buscarPorPeriodoAndCurso(Periodo periodo, Curso curso) {
		return ofertaRepository.findOfertasByPeriodoAndTurma_Curso(periodo, curso);
	}
	
	@Override
	public List<Oferta> buscarOfertasImportadasPeriodoAtivoPorPeriodoAndCurso(Periodo periodo, Periodo periodoAtivo, Curso curso) {
		return ofertaRepository.findOfertasImportadasByPeriodoAndCurso(periodo, periodoAtivo, curso);
	}
	
	@Override
	public List<Oferta> buscarOfertasNaoImportadasPeriodoAtivoPorPeriodoAndCurso(Periodo periodo, Periodo periodoAtivo, Curso curso) {
		return ofertaRepository.findOfertasNaoImportadasByPeriodoAndCurso(periodo, periodoAtivo, curso);
	}
	
	@Override
	public void importarOfertas(List<Oferta> ofertas) {
		if (ofertas != null) {
			for (Oferta oferta : ofertas) {
				this.salvarOfertaPeriodoAtivo(this.clonarOferta(oferta));
			}
		}

		/*boolean contem;
		boolean adicionado = true;
		
		Map<String, Object> resultado = new HashMap<String, Object>();

		for (Integer id : ofertas) {
			Oferta oferta = ofertaRepository.findOne(id);
			
			if (oferta != null) {
				
				contem = false;
				
				for (Oferta o : ofertaRepository.findOfertaByPeriodo(periodoAtivo)) {
					if (o.getDisciplina().equals(oferta.getDisciplina()) 
							&& o.getTurma().equals(oferta.getTurma())) {
						contem = true;
						break;
					}
				}
				
				if (!contem) {
					Oferta novaOferta = this.clonarOferta(oferta);
					
					this.salvarOfertaPeriodoAtivo(novaOferta);
					
					if (adicionado)
						resultado.put("importada", true);
					
					adicionado = false;
				}
			}
		}

		if (adicionado) 
			resultado.put("importada", false);
		
		return resultado;*/
	}
	
	private Oferta clonarOferta(Oferta o) {
		Oferta oferta = new Oferta();
		
		oferta.setTurma(o.getTurma());
		oferta.setDisciplina(o.getDisciplina());
		oferta.setVagas(o.getVagas());
		oferta.setMesmoDia(o.isMesmoDia());
		oferta.setTurno(o.getTurno());
		oferta.setHorarioInicio(o.getHorarioInicio());
		oferta.setAulasEmLaboratorio(o.getAulasEmLaboratorio());
		oferta.setNumeroProfessores(o.getNumeroProfessores());
		oferta.setObservacao(o.getObservacao());

		if (!o.getProfessores().isEmpty()) {
			List<Professor> professores = new ArrayList<>();
			professores.addAll(o.getProfessores());
			
			oferta.setProfessores(professores);
		}
		
		return oferta;
	}
	
	@Override
	public void substituirOferta(List<Integer> idOfertas) {
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		List<Oferta> novasOfertas = new ArrayList<>();
		
		for (Integer id : idOfertas) {
			Oferta oferta = ofertaRepository.findOne(id);
			
			if (oferta != null) {
				for (Oferta o : ofertaRepository.findOfertaByPeriodo(periodoAtivo)){
					if (o.getDisciplina().equals(oferta.getDisciplina())) {
						ofertaRepository.delete(o);
						
						Oferta novaOferta = clonarOferta(o);
						novaOferta.setPeriodo(periodoAtivo);
						novasOfertas.add(novaOferta);
					}
				}
			}
		}

		ofertaRepository.save(novasOfertas);
	}

	@Override
	public List<Oferta> buscarPorPeriodo(Periodo periodo) {
		return ofertaRepository.findOfertaByPeriodo(periodo);
	}
	
	public boolean hasCompartilhamentoOuRestricaoHorario(Oferta oferta) {
		long totalCompartilhamentos = compartilhamentoRepository.countByOferta(oferta);
		long totalRestricaoHorario = restricaoHorarioRepository.countByPrimeiraOfertaOrSegundaOferta(oferta, oferta);
		
		if(totalCompartilhamentos > 0 || totalRestricaoHorario > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean disciplinaAndTurmaIsEquals(Periodo periodoAtivo, Compartilhamento compartilhamento) {
		
		for (Oferta oferta : buscarOfertaPorPeriodo(periodoAtivo)) {
			if (oferta.getDisciplina().equals(compartilhamento.getOferta().getDisciplina()) 
					&& oferta.getTurma().equals(compartilhamento.getTurma())) {
				return true;
			}
		}
		
		return false;
	}

}

