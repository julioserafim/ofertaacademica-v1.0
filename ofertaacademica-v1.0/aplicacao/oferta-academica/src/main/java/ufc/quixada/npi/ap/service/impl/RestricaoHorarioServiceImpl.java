package ufc.quixada.npi.ap.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.quixada.npi.ap.model.Periodo;
import ufc.quixada.npi.ap.model.RestricaoHorario;
import ufc.quixada.npi.ap.model.RestricaoHorario.Tipo;
import ufc.quixada.npi.ap.repository.RestricaoHorarioRepository;
import ufc.quixada.npi.ap.service.RestricaoHorarioService;
import ufc.quixada.npi.ap.service.PeriodoService;


@Service
public class RestricaoHorarioServiceImpl implements RestricaoHorarioService {

	@Autowired
	private RestricaoHorarioRepository restricaoHorarioRepository;
	
	@Autowired
	private PeriodoService periodoService;
	
	@Override
	public boolean salvarRestricaoHorarioPeriodoAtivo(RestricaoHorario restricaoHorario){
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		
		if (periodoAtivo != null){
			if(!buscarRestricaoHorarioAll(restricaoHorario)){
				restricaoHorario.setPeriodo(periodoAtivo);
				restricaoHorario.setHabilitada(false);
				restricaoHorarioRepository.save(restricaoHorario);
				return true;
			}
		}
		return false;
	}	
	
	@Override
	public RestricaoHorario salvarRestricaoHorario(RestricaoHorario restricaoHorario) {
		restricaoHorario.setHabilitada(false);
		return restricaoHorarioRepository.save(restricaoHorario);
	}

	@Override
	public List<RestricaoHorario> buscarTodasRestricoesHorario() {
		return restricaoHorarioRepository.findByTipo(Tipo.EMPILHAMENTO);
	}
	
	@Override
	public List<RestricaoHorario> buscarTodasRestricoesHorarioDistinto() {
		return restricaoHorarioRepository.findByTipo(Tipo.DISTINTO);
	}

	@Override
	public void excluir(Integer idRestricaoHorario) {
		restricaoHorarioRepository.delete(idRestricaoHorario);
	}

	@Override
	public RestricaoHorario buscarRestricaoHorario(Integer idRestricaoHorario) {
		return restricaoHorarioRepository.findOne(idRestricaoHorario);
	}

	@Override
	public boolean desabilitarEmpilhamento(Integer id) {
		RestricaoHorario empilhamento = restricaoHorarioRepository.findOne(id);
		
		empilhamento.setHabilitada(false);
		restricaoHorarioRepository.save(empilhamento);
		
		return true;
	}
	
	public boolean habilitarEmpilhamento(Integer id) {
		RestricaoHorario empilhamento = restricaoHorarioRepository.findOne(id);
	
		empilhamento.setHabilitada(true);
		restricaoHorarioRepository.save(empilhamento);
		
		return true;
	}

	@Override
	public boolean buscarRestricaoHorarioAll(RestricaoHorario restricaoHorario) {
		if(null!= restricaoHorarioRepository.findByPrimeiraOfertaAndSegundaOfertaAndTipo
				(restricaoHorario.getPrimeiraOferta(), restricaoHorario.getSegundaOferta(), restricaoHorario.getTipo())){
			return true;
		}
		return false;
	}
}
