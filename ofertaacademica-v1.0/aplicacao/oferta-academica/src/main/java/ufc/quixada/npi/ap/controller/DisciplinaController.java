
package ufc.quixada.npi.ap.controller;

import static ufc.quixada.npi.ap.util.Constants.MSG_DISCIPLINA_CADASTRAR_SUCCESS;
import static ufc.quixada.npi.ap.util.Constants.MSG_DISCIPLINA_EDITAR_SUCCESS;
import static ufc.quixada.npi.ap.util.Constants.SWAL_STATUS_ERROR;
import static ufc.quixada.npi.ap.util.Constants.SWAL_STATUS_SUCCESS;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.Disciplina;
import ufc.quixada.npi.ap.model.Preferencia;
import ufc.quixada.npi.ap.model.Preferencia.Nivel;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.service.DisciplinaService;
import ufc.quixada.npi.ap.service.PreferenciaService;
import ufc.quixada.npi.ap.service.ProfessorService;
import ufc.quixada.npi.ap.util.Constants;
import ufc.quixada.npi.ap.validation.DisciplinaValidator;

@Controller
@RequestMapping("/disciplinas")
public class DisciplinaController {

	@Autowired
	public DisciplinaService disciplinaService;
	
	@Autowired
	private DisciplinaValidator disciplinaValidator;
	
	@Autowired
	private ProfessorService professorService;
	
	@Autowired
	private PreferenciaService preferenciaService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView listarDisciplinas() {
		ModelAndView modelAndView = new ModelAndView(Constants.DISCIPLINA_LISTAR);
		
		modelAndView.addObject("disciplinas", disciplinaService.buscarTodasDisciplinas());
		
		return modelAndView;
	}

	@RequestMapping(value = "/cadastrar", method = RequestMethod.GET)
	public ModelAndView cadastrarDisciplina(@ModelAttribute("disciplina") Disciplina disciplina) {
		
		ModelAndView modelAndView = new ModelAndView(Constants.DISCIPLINA_CADASTRAR);
		List<Disciplina> disciplinas = disciplinaService.buscarTodasDisciplinas();
		
		modelAndView.addObject("disciplinas", disciplinas);	
		return modelAndView;
	}

	@RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
	public ModelAndView cadastrarDisciplina(@ModelAttribute("disciplina") @Valid Disciplina disciplina, BindingResult result, RedirectAttributes redirectAttributes, ModelAndView modelAndView) throws AlocacaoProfessorException {
		
		disciplinaValidator.validate(disciplina, result);
		
		if (result.hasErrors()) {
			return cadastrarDisciplina(disciplina);
		} 
		
		disciplinaService.salvar(disciplina);
		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, MSG_DISCIPLINA_CADASTRAR_SUCCESS);
		modelAndView.setViewName(Constants.DISCIPLINA_REDIRECT_LISTAR);
		return modelAndView;
		
	}

	@RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
	public ModelAndView editarDisciplina(@PathVariable("id") Integer idDisciplina) {
		ModelAndView modelAndView = new ModelAndView(Constants.DISCIPLINA_EDITAR);
		
		Disciplina disciplina = disciplinaService.buscarDisciplina(idDisciplina);
		
		if (disciplina == null){
			modelAndView.setViewName(Constants.DISCIPLINA_REDIRECT_LISTAR);
			return modelAndView;
		}
		
		modelAndView.addObject("disciplina", disciplina);
		
		return modelAndView;
	}

	@RequestMapping(value = "/editar/{id}", method = RequestMethod.POST)
	public ModelAndView editarDisciplina(@ModelAttribute("disciplina") @Valid Disciplina disciplina,
			BindingResult result, RedirectAttributes redirect, ModelAndView modelAndView) {
		
		disciplinaValidator.validate(disciplina, result);
		
		if (result.hasErrors()) {
			modelAndView.setViewName(Constants.DISCIPLINA_EDITAR);
			modelAndView.addObject("disciplina", disciplina);
			
			return modelAndView;
		}
		
		try {
			disciplinaService.editar(disciplina);
		} catch (AlocacaoProfessorException e) {
			modelAndView.setViewName(Constants.DISCIPLINA_REDIRECT_EDITAR + disciplina.getId());
			redirect.addFlashAttribute(SWAL_STATUS_ERROR, e.getMessage());
					
			return modelAndView;
		}
		
		modelAndView.setViewName(Constants.DISCIPLINA_REDIRECT_LISTAR);
		redirect.addFlashAttribute(SWAL_STATUS_SUCCESS, MSG_DISCIPLINA_EDITAR_SUCCESS);
	
		return modelAndView;
	}

	@RequestMapping(value = "/{id}/arquivar", method = RequestMethod.GET)
	public ModelAndView arquivarDisciplina(@PathVariable("id") Disciplina disciplina, RedirectAttributes redirect,
			ModelAndView modelAndView) throws Exception {
		
		if(disciplina != null){
			disciplinaService.arquivarDisciplina(disciplina.getId());
			redirect.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_DISCIPLINA_EDITAR_SUCCESS);
		}
		
		modelAndView.setViewName(Constants.DISCIPLINA_REDIRECT_LISTAR);
		return modelAndView;
	}
	
	@RequestMapping(value = "/detalhes-disciplina/{id}", method = RequestMethod.GET)
	public ModelAndView detalhesDisciplina(@PathVariable("id") Integer id){
		ModelAndView modelAndView = new ModelAndView(Constants.DISCIPLINA_DETALHES);
		
		Disciplina disciplina = disciplinaService.buscarDisciplina(id);

		Preferencia preferencia = new Preferencia();
		
		modelAndView.addObject("professores", professorService.buscarTodosProfessores());
		
		modelAndView.addObject("professor", professorService.buscarTodosProfessores());

		modelAndView.addObject("disciplina", disciplina);
		modelAndView.addObject("niveis", Nivel.values());
		modelAndView.addObject("preferencia", preferencia);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/detalhes-disciplina/{id}/cadastrar", method = RequestMethod.POST)
	public ModelAndView cadastrarPreferenciaDiciplina(@PathVariable("id") Integer id,
			@ModelAttribute("preferencia") Preferencia preferencia, RedirectAttributes redirectAttribute) throws AlocacaoProfessorException{
		
		ModelAndView modelAndView = new ModelAndView();
		
		preferencia.setDisciplina(disciplinaService.buscarDisciplina(id));
		modelAndView.addObject("professores", professorService.buscarTodosProfessores());
		try{
			preferenciaService.salvar(preferencia);
			redirectAttribute.addFlashAttribute(SWAL_STATUS_SUCCESS,Constants.MSG_PREFERENCIA_CADASTRADA);
		}catch (AlocacaoProfessorException e){
			redirectAttribute.addFlashAttribute(SWAL_STATUS_ERROR, e.getMessage());
		}
		
		modelAndView.setViewName(Constants.DISCIPLINA_REDIRECT_LISTAR +"/detalhes-disciplina/"+id);
		return modelAndView; 
	}
	
	@RequestMapping(value = "detalhes-disciplina/{id}/excluir/{idp}", method = RequestMethod.GET)
	public ModelAndView excluirPreferenciaDisciplina(@PathVariable("id") Integer id,@PathVariable("idp") Integer idp,
													RedirectAttributes redirectAttribute){
		ModelAndView modelAndView = new ModelAndView();
		Disciplina disciplina = disciplinaService.buscarDisciplina(id);
		Professor professor = professorService.buscarProfessor(idp);
		preferenciaService.excluir(preferenciaService.buscarPreferenciaPorDisciplinaAndProfessor(disciplina, professor));
		redirectAttribute.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_PREFERENCIA_EXCLUIDA);
		modelAndView.setViewName(Constants.DISCIPLINA_REDIRECT_LISTAR +"/detalhes-disciplina/"+id);

		modelAndView.addObject("professor", professorService.buscarTodosProfessores());

		return modelAndView;
	}
	
}
