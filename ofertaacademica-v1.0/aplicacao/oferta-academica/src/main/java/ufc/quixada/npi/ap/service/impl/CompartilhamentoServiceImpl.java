package ufc.quixada.npi.ap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.model.Compartilhamento;
import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Oferta;
import ufc.quixada.npi.ap.model.Periodo;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.repository.CompartilhamentoRepository;
import ufc.quixada.npi.ap.service.CompartilhamentoService;
import ufc.quixada.npi.ap.service.OfertaService;

@Service
public class CompartilhamentoServiceImpl implements CompartilhamentoService {
	
	@Autowired
	private CompartilhamentoRepository compartilhamentoRepository;
	
	@Autowired
	private OfertaService ofertaService;
	
	@Override
	public void salvar(Compartilhamento compartilhamento) {
		compartilhamentoRepository.save(compartilhamento);
	}
	
	public Compartilhamento buscarCompartilhamento(Integer id){
		return compartilhamentoRepository.findOne(id);
	}
	
	@Override
	public List<Compartilhamento> buscarTodosCompartilhamentos() {
		return compartilhamentoRepository.findAll();
	}
	
	@Override
	public void excluir(Integer id) {
		compartilhamentoRepository.delete(id);
	}
	
	@Override
	public List<Compartilhamento> buscarCompartilhamentosPorPeriodoAndCurso(Periodo periodo, Curso curso) {
		return compartilhamentoRepository.findCompartilhamentosByOferta_periodoAndTurma_curso(periodo, curso);
	}
	
	@Override
	public List<Compartilhamento> buscarCompartilhamentosNaoImportadosPorPeriodoAndCurso(Periodo periodo, Periodo periodoAtivo, Curso curso) {
		return compartilhamentoRepository.findCompartilhamentosNaoImportadosByPeriodoAndCurso(periodo, periodoAtivo, curso);
	}
	
	@Override
	public List<Compartilhamento> buscarCompartilhamentosImportadosPorPeriodoAndCurso(Periodo periodo, Periodo periodoAtivo, Curso curso) {
		return compartilhamentoRepository.findCompartilhamentosImportadosByPeriodoAndCurso(periodo, periodoAtivo, curso);
	}
	
	@Override
	public Map<String, Object> importarOfertasCompartilhadas(List<Integer> compartilhamentos, Periodo periodoAtivo, Curso cursoCoordenador) {
		
		Map<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("importada", false);			
		
		for (Integer id : compartilhamentos) {
			Compartilhamento compartilhamento = compartilhamentoRepository.findOne(id);
			
			if (Objects.nonNull(compartilhamento)) {
				if (!ofertaService.disciplinaAndTurmaIsEquals(periodoAtivo, compartilhamento)) {
					Oferta novaOferta = this.clonarOfertaCompartilhada(compartilhamento);
					ofertaService.salvarOfertaPeriodoAtivo(novaOferta);
					resultado.put("importada", true);
				}
			}
		}
		
		return resultado;
	}
	
	private Oferta clonarOfertaCompartilhada(Compartilhamento compartilhamento) {
		Oferta ofertaCompartilhada = compartilhamento.getOferta();
		
		Oferta oferta = new Oferta();
		
		oferta.setTurma(compartilhamento.getTurma());
		oferta.setDisciplina(ofertaCompartilhada.getDisciplina());
		oferta.setVagas(compartilhamento.getVagas());
		oferta.setTurno(ofertaCompartilhada.getTurno());
		oferta.setHorarioInicio(ofertaCompartilhada.getHorarioInicio());
		oferta.setAulasEmLaboratorio(ofertaCompartilhada.getAulasEmLaboratorio());
		oferta.setNumeroProfessores(ofertaCompartilhada.getNumeroProfessores());
		oferta.setObservacao(ofertaCompartilhada.getObservacao());

		if (!ofertaCompartilhada.getProfessores().isEmpty()) {
			List<Professor> professores = new ArrayList<>();
			professores.addAll(ofertaCompartilhada.getProfessores());
			
			oferta.setProfessores(professores);
		}
		
		return oferta;
	}
}