package ufc.quixada.npi.ap.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.ap.util.Constants;
import ufc.quixada.npi.ap.model.CheckList;
import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.ItemCheckList;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.service.CheckListService;
import ufc.quixada.npi.ap.service.CursoService;

@Controller
@RequestMapping(path = "/checklist")
public class CheckListController {

	@Autowired
	public CheckListService checkListService;
	
	@Autowired
	public CursoService cursoService;
	
	@RequestMapping(path = { "", "/checklist-direcao"}, method = RequestMethod.GET)
	public ModelAndView mostrarItensCheckListDirecao(@ModelAttribute("itemCheckList") ItemCheckList itemCheckList){
		
		ModelAndView modelAndView = new ModelAndView(Constants.CHECKLIST_DIRECAO);
		
		List<ItemCheckList> listItensChecklist = checkListService.buscarTodosItensChecklist();
		List<CheckList> checkListSI = checkListService.buscarCheckListCurso(1);
		List<CheckList> checkListES = checkListService.buscarCheckListCurso(2);
		List<CheckList> checkListRC = checkListService.buscarCheckListCurso(3);
		List<CheckList> checkListCC = checkListService.buscarCheckListCurso(4);
		List<CheckList> checkListDD = checkListService.buscarCheckListCurso(5);
		List<CheckList> checkListEC = checkListService.buscarCheckListCurso(6);
		

		
		modelAndView.addObject("listaItensChecklist", listItensChecklist);
		modelAndView.addObject("checkListSI",checkListSI);
		modelAndView.addObject("checkListES",checkListES);
		modelAndView.addObject("checkListRC",checkListRC);
		modelAndView.addObject("checkListCC",checkListCC);
		modelAndView.addObject("checkListDD",checkListDD);
		modelAndView.addObject("checkListEC",checkListEC);
		
		return modelAndView;
	}
	
	@RequestMapping(path = { "", "/checklist-coordenacao"}, method = RequestMethod.GET)
	public ModelAndView mostrarItensCheckListCoordenacao(@ModelAttribute("itemCheckList") ItemCheckList itemCheckList,  Authentication auth){
		
		ModelAndView modelAndView = new ModelAndView(Constants.CHECKLIST_COORDENACAO);

		Professor pessoa = (Professor) auth.getPrincipal();
		Curso cursoCoordenador = cursoService.buscarCursoPorCoordenadorOuVice(pessoa);
		
		int idCurso = cursoCoordenador.getId();		
		List<ItemCheckList> listItensChecklist = checkListService.buscarTodosItensChecklist();
		List<CheckList> checkListCurso = checkListService.buscarCheckListCurso(idCurso);		
		
		modelAndView.addObject("itensCheckList",listItensChecklist);
		modelAndView.addObject("checkListCoordenador", checkListCurso);
		
		return modelAndView;
	}
	
	@RequestMapping(path = {"/salvarItem"}, method = RequestMethod.POST)
	public ModelAndView cadastrarItemCheckList(@ModelAttribute("itemCheckList") @Valid ItemCheckList itemCheckList,RedirectAttributes redirectAttributes, BindingResult result,
				ModelAndView modelAndView){

				checkListService.salvarItem(itemCheckList);
				
				redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_ITEM_CHECKLIST_CADASTRADO);
				modelAndView.setViewName(Constants.CHECKLIST_REDIRECT_DIRECAO);	
				
				return modelAndView;
	}
	
	@RequestMapping(path = {"/{id}/excluirItem"}, method = RequestMethod.GET)
	public ModelAndView excluirItemCheckList(@PathVariable("id") ItemCheckList itemCheckList, RedirectAttributes redirectAttributes, ModelAndView modelAndView)
	throws Exception{

		try{
			
			checkListService.excluirItem(itemCheckList.getId());
			redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_ITEM_CHECKLIST_EXCLUÌDO);
			modelAndView.setViewName(Constants.CHECKLIST_REDIRECT_DIRECAO);
			
		}catch(EmptyResultDataAccessException ex){
			return null;
		}
		
		return modelAndView;
	}

	@RequestMapping(path = {"/checklist-coordenacao/{id}/atualizarItemCheckList"}, method = RequestMethod.GET)
	public ModelAndView atualizarItemCheckList(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, ModelAndView modelAndView){
				
		checkListService.atualizarItem(id);	
			
		redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_ITEM_CHECKLIST_ATUALIZADO);	
		modelAndView.setViewName(Constants.CHECKLIST_REDIRECT_COORDENACAO);

		return modelAndView;
	} 
}
