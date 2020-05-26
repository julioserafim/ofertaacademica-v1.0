package ufc.quixada.npi.ap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufc.quixada.npi.ap.model.HorarioBloqueado;
import ufc.quixada.npi.ap.model.HorarioBloqueado.Dia;
import ufc.quixada.npi.ap.model.HorarioBloqueado.Horario;
import ufc.quixada.npi.ap.model.Professor;

@Repository
public interface HorarioBloqueadoRepository extends JpaRepository<HorarioBloqueado, Integer> {

	HorarioBloqueado findHorarioBloqueadoByProfessorAndDiaAndHorario(Professor professor, Dia dia, Horario horario);

}
