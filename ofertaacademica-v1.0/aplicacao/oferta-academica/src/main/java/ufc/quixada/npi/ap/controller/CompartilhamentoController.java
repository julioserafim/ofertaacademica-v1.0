package ufc.quixada.npi.ap.controller;

import static ufc.quixada.npi.ap.util.Constants.MSG_COMPARTILHAMENTO_EDITADO;
import static ufc.quixada.npi.ap.util.Constants.MSG_COMPARTILHAMENTO_SOLICITADO;
import static ufc.quixada.npi.ap.util.Constants.SWAL_STATUS_SUCCESS;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.ap.annotation.RestricaoDePeriodo;
import ufc.quixada.npi.ap.annotation.RestricaoDePeriodoAjax;
import ufc.quixada.npi.ap.model.Compartilhamento;
import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.model.Turma;
import ufc.quixada.npi.ap.service.CompartilhamentoService;
import ufc.quixada.npi.ap.service.CursoService;
import ufc.quixada.npi.ap.service.TurmaService;
import ufc.quixada.npi.ap.util.Constants;
import ufc.quixada.npi.ap.validation.CompartilhamentoValidator;

@Controller
@RequestMapping(path = "/compartilhamentos")
public class CompartilhamentoController {
	
	private CompartilhamentoControllerProduct compartilhamentoControllerProduct = new CompartilhamentoControllerProduct();

	@Autowired
	private CompartilhamentoValidator compartilhamentoValidator;
	
	@ModelAttribute("cursos")
	public List<Curso> todosCursos() {
		return compartilhamentoControllerProduct.getCursoService().buscarTodosCursos();
	}
	
	@RequestMapping(path = {"/cadastrar"}, method = RequestMethod.GET)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView cadastrarCompartilhamento(@ModelAttribute("compartilhamento") Compartilhamento compartilhamento, Authentication auth){
		return compartilhamentoControllerProduct.cadastrarCompartilhamento(compartilhamento, auth);
	}
	
	@RequestMapping(path = {"/cadastrar"}, method = RequestMethod.POST)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView cadastrarCompartilhamento(
			@ModelAttribute("compartilhamento") @Valid Compartilhamento compartilhamento,
				BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes){
		
		/*compartilhamentoValidator.validate(compartilhamento, bindingResult);
		
		if (bindingResult.hasErrors()){
			modelAndView.setViewName(Constants.COMPARTILHAMENTO_CADASTRAR);
			modelAndView.addObject("turmas", turmaService.buscarTodasTurmas());
			return modelAndView;
		}*/
		
		return compartilhamentoControllerProduct.cadastrarCompartilhamento(compartilhamento, bindingResult,
				modelAndView, redirectAttributes);
	}

	@RequestMapping(path = {"/{id}/detalhar"}, method = RequestMethod.GET)
	public ModelAndView detalharCompartilhamento(@PathVariable(name = "id", required = true) Integer id){
		ModelAndView modelAndView = new ModelAndView(Constants.COMPARTILHAMENTO_DETALHAR);
		return modelAndView;
	}
	
	@RequestMapping(path = {"/{id}/editar"}, method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView editarCompartilhamento(@PathVariable(name = "id", required = true) Integer id, 
												@ModelAttribute("compartilhamento") Compartilhamento compartilhamento,
												Authentication auth){
		
		return compartilhamentoControllerProduct.editarCompartilhamento(id, compartilhamento, auth);
	}
	
	@RequestMapping(path = {"/{id}/editar"}, method = RequestMethod.POST)
	public ModelAndView editarCompartilhamento(@PathVariable(name = "id", required = true) Compartilhamento compartilhamento,
												@RequestParam(value="turma") Turma turma,
												@RequestParam(value="vagas") Integer vagas,
												@RequestParam(value="disjunto", required = false) boolean disjunto,
												ModelAndView modelAndView, RedirectAttributes redirectAttributes){

		return compartilhamentoControllerProduct.editarCompartilhamento(compartilhamento, turma, vagas, disjunto,
				modelAndView, redirectAttributes);
	}
	
	@RequestMapping(path = {"/{id}/excluir"}, method = RequestMethod.GET)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView excluirCompartilhamento(@PathVariable(name = "id") Compartilhamento compartilhamento,
            RedirectAttributes redirectAttributes, ModelAndView modelAndView, Authentication auth){
        return compartilhamentoControllerProduct.excluirCompartilhamento(compartilhamento, redirectAttributes,
				modelAndView, auth);
	}
	
	
}
