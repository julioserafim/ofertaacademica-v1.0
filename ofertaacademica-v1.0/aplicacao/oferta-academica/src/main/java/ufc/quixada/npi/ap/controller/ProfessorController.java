package ufc.quixada.npi.ap.controller;


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
import ufc.quixada.npi.ap.model.HorarioBloqueado;
import ufc.quixada.npi.ap.model.HorarioBloqueado.Dia;
import ufc.quixada.npi.ap.model.HorarioBloqueado.Horario;
import ufc.quixada.npi.ap.model.Papel;
import ufc.quixada.npi.ap.model.Preferencia;
import ufc.quixada.npi.ap.model.Preferencia.Nivel;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.service.DisciplinaService;
import ufc.quixada.npi.ap.service.HorarioBloqueadoService;
import ufc.quixada.npi.ap.service.PreferenciaService;
import ufc.quixada.npi.ap.service.ProfessorService;
import ufc.quixada.npi.ap.util.Constants;
import ufc.quixada.npi.ap.validation.ProfessorValidator;

@Controller
@RequestMapping(value = "/professores")
public class ProfessorController {

	@Autowired
	private ProfessorService professorService;

	@Autowired
	private PreferenciaService preferenciaService;
	
	@Autowired
	private DisciplinaService disciplinaService;
	
	@Autowired
	ProfessorValidator validator;

	@Autowired
	private HorarioBloqueadoService horarioBloqueadoService;
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView listarProfessores() {
		ModelAndView modelAndView = new ModelAndView(Constants.PROFESSOR_LISTAR);

		List<Professor> professores = professorService.buscarTodosProfessores();
		
		modelAndView.addObject("professores", professores);
		modelAndView.addObject("cargaHorariaAtual", professores);


		return modelAndView;
	}
	
	@RequestMapping(value = "/cadastrar-professor", method = RequestMethod.GET)
	public ModelAndView cadastrarProfessor(@ModelAttribute("professor") Professor professor) {
		
		ModelAndView modelAndView = new ModelAndView(Constants.PROFESSOR_CADASTRAR);
		List<Professor> professores = professorService.buscarTodosProfessoresSemRelacionamento();
		modelAndView.addObject("professores", professores);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/cadastrar-professor", method = RequestMethod.POST)
	public ModelAndView cadastrarProfessor(@ModelAttribute("professor") @Valid Professor professor, BindingResult result, RedirectAttributes redirectAttributes, ModelAndView modelAndView) throws AlocacaoProfessorException{
		
		validator.validate(professor, result);
		
		if(result.hasErrors()){
			return cadastrarProfessor(professor);
		}
		
		professorService.salvar(professor);
		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_PROFESSOR_CADASTRADO);
		modelAndView.setViewName(Constants.PROFESSOR_REDIRECT_LISTAR);
		
		return modelAndView;

	}

	@RequestMapping(value = "/editar-professor/{id}", method = RequestMethod.GET)
	public ModelAndView editarProfessor(@PathVariable("id") Integer idProfessor) {
		ModelAndView modelAndView = new ModelAndView(Constants.PROFESSOR_EDITAR);
        
		Professor professor = professorService.buscarProfessor(idProfessor);
		
		
		if (professor == null){
			modelAndView.setViewName(Constants.PROFESSOR_REDIRECT_LISTAR);
			return modelAndView;
		}
		
		if(professor.getRelacionamento() == null){
			List<Professor> professores = professorService.buscarTodosProfessoresSemRelacionamento();
			modelAndView.addObject("professores", professores);
		}else{
			List<Professor> professores = professorService.buscarTodosProfessores();
			modelAndView.addObject("professores", professores);
		}

		modelAndView.addObject("professor", professor);

		return modelAndView;
	}

	@RequestMapping(value = "/editar-professor/", method = RequestMethod.POST)
	public ModelAndView editarProfessor(@ModelAttribute("professor") @Valid Professor professor, BindingResult result, RedirectAttributes redirectAttributes) throws AlocacaoProfessorException {

		ModelAndView modelAndView = new ModelAndView(Constants.PROFESSOR_REDIRECT_LISTAR);
		
		Professor professorComRelacionamento = professorService.buscarProfessor(professor.getId());
		if(professorComRelacionamento.getRelacionamento() != null){
			Professor professorRelacionado = professorComRelacionamento.getRelacionamento();
			professorRelacionado.setRelacionamento(null);
		}
		
		professorComRelacionamento.setRelacionamento(null);
		
		List<Papel> papeis = professorComRelacionamento.getPapeis();
		professor.setPapeis(papeis);
		
		validator.validate(professor, result);
		
		if(result.hasErrors()){
			modelAndView.setViewName(Constants.PROFESSOR_EDITAR);
			
			List<Professor> professores = professorService.buscarTodosProfessores();
			
			modelAndView.addObject("professores", professores);
			modelAndView.addObject("professor", professor);

			return modelAndView;
			
		}

		professorService.editar(professor);
		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_PROFESSOR_EDITADO);
		modelAndView.setViewName(Constants.PROFESSOR_REDIRECT_LISTAR);
		return modelAndView;
	}

	@RequestMapping(value = "/detalhes-professor/{id}", method = RequestMethod.GET)
	public ModelAndView detalhesProfessor(@PathVariable("id") Integer id){
		ModelAndView model = new ModelAndView(Constants.PROFESSOR_DETALHES);
		
		Professor professor = professorService.buscarProfessor(id);

		Preferencia preferencia = new Preferencia();
		
		model.addObject("disciplinas", disciplinaService.buscarTodasDisciplinas());
		model.addObject("professor", professor);
		model.addObject("niveis", Nivel.values());
		model.addObject("preferencia", preferencia);
		model.addObject("horarioBloqueado", new HorarioBloqueado());

		return model;
	}

	
	@RequestMapping(value = "/detalhes-professor/{idProfessor}/cadastrar", method = RequestMethod.POST)
	public ModelAndView cadastrarPreferenciaProfessor(@PathVariable("idProfessor") Professor professor,
			@ModelAttribute("preferencia") Preferencia preferencia,RedirectAttributes redirectAttributes) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		preferencia.setProfessor(professor);

		try {
			preferenciaService.salvar(preferencia);
			redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, "Preferência Cadastrada!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(SWAL_STATUS_ERROR, e.getMessage());
		}
		
		modelAndView.setViewName(Constants.PROFESSOR_REDIRECT_LISTAR +"/detalhes-professor/" + professor.getId());
		return modelAndView;
		
	}
	
	@RequestMapping(value = "detalhes-professor/{id}/excluir/{idp}", method = RequestMethod.GET)
	public ModelAndView excluirPreferenciaProfessor(@PathVariable("id") Integer id,@PathVariable("idp") Integer idp,
													RedirectAttributes redirectAttribute){
		ModelAndView modelAndView = new ModelAndView();
		Disciplina disciplina = disciplinaService.buscarDisciplina(id);
		Professor professor = professorService.buscarProfessor(idp);
		preferenciaService.excluir(preferenciaService.buscarPreferenciaPorDisciplinaAndProfessor(disciplina, professor));
		redirectAttribute.addFlashAttribute(SWAL_STATUS_SUCCESS, "Preferência excluída com sucesso!");
		modelAndView.setViewName(Constants.PROFESSOR_REDIRECT_LISTAR +"/detalhes-professor/"+idp);
		return modelAndView;
	}
	
	@RequestMapping(value = "/cadastrar-horario-bloqueado", method = RequestMethod.POST)
	public ModelAndView cadastrarHorarioBloqueado(@ModelAttribute("horarioBloqueado") HorarioBloqueado horarioBloqueado,
			RedirectAttributes redirectAttributes) {

		ModelAndView model = new ModelAndView(Constants.PROFESSOR_REDIRECT_DETALHES + "/" + horarioBloqueado.getProfessor().getId());

		try {

			horarioBloqueadoService.salvar(horarioBloqueado);
			redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_HORARIO_BLOQUEADO_CADASTRADO);

		} catch (Exception exception) {

			redirectAttributes.addFlashAttribute(SWAL_STATUS_ERROR, exception.getMessage());
		}

		return model;

	}

	@RequestMapping(value = "/excluir-horario-bloqueado/{id}/{dia}/{horario}", method = RequestMethod.GET)
	public ModelAndView excluirHorarioBloqueado(@PathVariable("id") Professor professor,
			@PathVariable("dia") Dia dia, @PathVariable("horario") Horario horario,
			RedirectAttributes redirectAttributes) {
		
		ModelAndView model = new ModelAndView(Constants.PROFESSOR_REDIRECT_DETALHES + "/" + professor.getId());
		
		HorarioBloqueado horarioBloqueado = horarioBloqueadoService.buscarHorarioBloqueadoPorProfessorEDiaEHorario(professor, dia, horario);
		
		try {

			horarioBloqueadoService.excluir(horarioBloqueado.getId());
			redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_HORARIO_BLOQUEADO_EXCLUIDO);

		} catch (Exception exception) {

			redirectAttributes.addFlashAttribute(SWAL_STATUS_ERROR, exception.getMessage());

		}
		
		return model;
	}
}
