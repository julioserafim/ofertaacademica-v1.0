package ufc.quixada.npi.ap.service;

import java.util.List;

import ufc.quixada.npi.ap.model.CheckList;
import ufc.quixada.npi.ap.model.ItemCheckList;

public interface CheckListService {
	
	public void salvarItem(ItemCheckList itemChecklist);
	
	public void atualizarItem(Integer id);
	
	public void excluirItem(Integer id);
	
	public List<ItemCheckList> buscarTodosItensChecklist();

	public List<CheckList> buscarCheckListCurso(Integer idCurso);
	
	public void limparCheckList();
}
