package ufc.quixada.npi.ap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.HorarioBloqueado;
import ufc.quixada.npi.ap.model.HorarioBloqueado.Dia;
import ufc.quixada.npi.ap.model.HorarioBloqueado.Horario;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.repository.HorarioBloqueadoRepository;
import ufc.quixada.npi.ap.service.HorarioBloqueadoService;
import ufc.quixada.npi.ap.util.Constants;

@Service
public class HorarioBloqueadoServiceImpl implements HorarioBloqueadoService {

	@Autowired
	private HorarioBloqueadoRepository horarioBloqueadoRepository;

	@Override
	public HorarioBloqueado salvar(HorarioBloqueado horarioBloqueado) throws AlocacaoProfessorException, Exception {
		
		HorarioBloqueado horarioBloqueadoTesteBD = buscarHorarioBloqueadoPorProfessorEDiaEHorario(
				horarioBloqueado.getProfessor(), horarioBloqueado.getDia(), horarioBloqueado.getHorario());
		
		if(horarioBloqueadoTesteBD != null){
			throw new AlocacaoProfessorException(Constants.MSG_HORARIO_BLOQUEADO_CADASTRAR_EXISTENTE);
		}
		
		try{
			
			return horarioBloqueadoRepository.save(horarioBloqueado);
			
		} catch(Exception e){
			
			throw new Exception(Constants.MSG_HORARIO_BLOQUEADO_SQL_CADASTRAR);
			
		}
	}

	@Override
	public HorarioBloqueado buscarHorarioBloqueado(Integer id) {
		return horarioBloqueadoRepository.findOne(id);
	}
	
	@Override
	public HorarioBloqueado buscarHorarioBloqueadoPorProfessorEDiaEHorario(Professor professor, Dia dia,
			Horario horario) {
		return horarioBloqueadoRepository.findHorarioBloqueadoByProfessorAndDiaAndHorario(professor, dia, horario);
	}

	@Override
	public List<HorarioBloqueado> buscarTodosHorariosBloqueados() {
		return horarioBloqueadoRepository.findAll();
	}

	@Override
	public void excluir(Integer id) throws Exception {

		try {

			horarioBloqueadoRepository.delete(id);

		} catch (Exception exception) {

			throw new Exception(Constants.MSG_HORARIO_BLOQUEADO_SQL_EXCLUIR);

		}
	
	}

}
