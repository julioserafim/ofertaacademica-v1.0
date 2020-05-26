package ufc.quixada.npi.ap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.model.CheckList;
import ufc.quixada.npi.ap.model.Periodo;
import ufc.quixada.npi.ap.model.Periodo.Status;
import ufc.quixada.npi.ap.repository.PeriodoRepository;
import ufc.quixada.npi.ap.service.PeriodoService;

@Service
public class PeriodoServiceImpl implements PeriodoService{
	
	@Autowired
	private PeriodoRepository periodoRepository;
	
	@Autowired
	private CheckListServiceImpl checklistService;
	
	@Override
	public void salvar(Periodo periodo){
		
		periodoRepository.resetarPeriodoAtivo();
		
		periodo.setAtivo(true);
		checklistService.limparCheckList();
		periodoRepository.save(periodo);		
	}
	
	@Override
	public void salvarPeriodoAberto(Periodo periodo) {		

		periodoRepository.save(periodo);
	}
	
	public List<Periodo> buscarTodosPeriodos(){
		return periodoRepository.buscarTodosPeriodos();
	}
	
	public void excluir(Periodo periodo){
		periodoRepository.delete(periodo);
	}
	
	public Periodo buscarPeriodo(Integer id){
		return periodoRepository.findOne(id);
	}

	@Override
	public Periodo buscarPeriodoAtivo() {
		return periodoRepository.findPeriodoByAtivoTrue();
	}

	@Override
	public List<Periodo> buscarPeriodosConsolidados() {
		return periodoRepository.findPeriodosConsolidados();
	}
	
}	
