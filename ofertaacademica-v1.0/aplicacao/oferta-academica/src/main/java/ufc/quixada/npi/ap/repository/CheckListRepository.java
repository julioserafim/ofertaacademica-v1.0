package ufc.quixada.npi.ap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ufc.quixada.npi.ap.model.CheckList;

@Repository
@Transactional
public interface CheckListRepository extends JpaRepository<CheckList, Integer>{

	CheckList findById(Integer id);
	@Procedure(procedureName="gerar_checklist")
	void salvarItem(@Param ("id_item")Integer id_item);
	
	@Query("SELECT item FROM CheckList AS item WHERE item.curso.id = :idCurso ORDER BY itemCheckList.id ASC")
	List<CheckList> buscarItensPorCurso(@Param("idCurso") Integer idCurso);

	@Modifying
	@Query("UPDATE CheckList SET resposta = false WHERE id != 0")
	void limparCheckList();

}
