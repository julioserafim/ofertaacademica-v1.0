package ufc.quixada.npi.ap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ufc.quixada.npi.ap.model.ItemCheckList;

@Transactional
public interface ItemCheckListRepository extends JpaRepository<ItemCheckList, Integer>{

	@Modifying
	@Query("DELETE FROM CheckList WHERE id_item = :idItem")
	void deletarItem(@Param("idItem") Integer idItem);
	
}
