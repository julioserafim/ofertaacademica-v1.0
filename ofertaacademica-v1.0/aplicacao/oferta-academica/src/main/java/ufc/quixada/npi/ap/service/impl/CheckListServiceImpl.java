package ufc.quixada.npi.ap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.model.CheckList;
import ufc.quixada.npi.ap.model.ItemCheckList;
import ufc.quixada.npi.ap.repository.CheckListRepository;
import ufc.quixada.npi.ap.repository.ItemCheckListRepository;
import ufc.quixada.npi.ap.service.CheckListService;


@Service
public class CheckListServiceImpl implements CheckListService{

	@Autowired
	CheckListService checklistService;
	
	@Autowired
	CheckListRepository checklistRepository;
	
	@Autowired
	ItemCheckListRepository itemCheckListRepository;
	
	@Override
	public void salvarItem(ItemCheckList itemChecklist) {
		
		itemCheckListRepository.save(itemChecklist);
		
		try{
			
			checklistRepository.salvarItem(itemChecklist.getId());
		}catch(Exception e){
			
			System.err.println(e.getMessage());
		}
		
	}
	
	@Override
	public void excluirItem(Integer id) {
	
		
		itemCheckListRepository.deletarItem(id);
		itemCheckListRepository.delete(id);
		
	}

	@Override
	public List<ItemCheckList> buscarTodosItensChecklist() {
		return itemCheckListRepository.findAll();
	}

	@Override
	public List<CheckList> buscarCheckListCurso(Integer idCurso) {
		return checklistRepository.buscarItensPorCurso(idCurso);
		
	}

	@Override
	public void limparCheckList() {
		checklistRepository.limparCheckList();
		
	}

	@Override
	public void atualizarItem(Integer id) {
		CheckList check = checklistRepository.findById(id);
		check.setResposta(!check.isResposta());
		checklistRepository.save(check);
		
	}


}
