package ufc.quixada.npi.ap.service;

import java.util.List;


import ufc.quixada.npi.ap.model.RestricaoHorario;

public interface RestricaoHorarioService {
	
	public RestricaoHorario salvarRestricaoHorario(RestricaoHorario restricaoHorario);
	
	public boolean salvarRestricaoHorarioPeriodoAtivo(RestricaoHorario restricaoHorario);
	
	public void excluir(Integer id);
	
	public RestricaoHorario buscarRestricaoHorario(Integer idRestricaoHorario);
	
	boolean buscarRestricaoHorarioAll(RestricaoHorario restricaoHorario);
	
	public List<RestricaoHorario> buscarTodasRestricoesHorario();
	
	public List<RestricaoHorario> buscarTodasRestricoesHorarioDistinto();
	
	boolean desabilitarEmpilhamento(Integer id);
	
	boolean habilitarEmpilhamento(Integer id);
	
}
