package ufc.quixada.npi.ap.service;

import java.util.List;

import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.HorarioBloqueado;
import ufc.quixada.npi.ap.model.HorarioBloqueado.Dia;
import ufc.quixada.npi.ap.model.HorarioBloqueado.Horario;
import ufc.quixada.npi.ap.model.Professor;

public interface HorarioBloqueadoService {
	
	public HorarioBloqueado salvar(HorarioBloqueado horarioBloqueado) throws AlocacaoProfessorException, Exception;
	
	public HorarioBloqueado buscarHorarioBloqueado(Integer id);
	
	public HorarioBloqueado buscarHorarioBloqueadoPorProfessorEDiaEHorario(Professor professor, Dia dia, Horario horario);
	
	public List<HorarioBloqueado> buscarTodosHorariosBloqueados();
	
	public void excluir(Integer id) throws Exception;
	
}
