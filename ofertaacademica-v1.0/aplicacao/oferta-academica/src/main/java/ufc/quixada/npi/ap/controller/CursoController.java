package ufc.quixada.npi.ap.controller;


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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;
import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Curso.Turno;
import ufc.quixada.npi.ap.model.Papel;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.service.CursoService;
import ufc.quixada.npi.ap.service.ProfessorService;
import ufc.quixada.npi.ap.util.Constants;
import ufc.quixada.npi.ap.validation.CursoValidator;


@Controller
@RequestMapping("/cursos")
public class CursoController {
	
	@Autowired
	public CursoService cursoService;
	
	@Autowired
	public ProfessorService professorService;
	
	@Autowired
	CursoValidator validator;
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView listarCursos() {
		
		ModelAndView modelAndView = new ModelAndView(Constants.CURSO_LISTAR);
		
		List<Curso> cursos = cursoService.buscarTodosCursos();	
		
		modelAndView.addObject("cursos", cursos);
		
		return modelAndView;
	}
	
	
	@RequestMapping(path = "/cadastrar", method = RequestMethod.GET)
	public ModelAndView cadastrarCurso(@ModelAttribute("curso") Curso curso) {
		
		
		ModelAndView modelAndView = new ModelAndView(Constants.CURSO_CADASTRAR);
		List<Curso> cursos = cursoService.buscarTodosCursos();
		List<Professor> professores = professorService.buscarTodosProfessores();
		
		modelAndView.addObject("cursos", cursos);
		modelAndView.addObject("turnos", Turno.values());
		modelAndView.addObject("professores", professores);
		
		return modelAndView;
	}
	
	@RequestMapping(path = "/cadastrar", method = RequestMethod.POST)
	public ModelAndView cadastrarCurso(@ModelAttribute("curso") @Valid Curso curso, BindingResult result, RedirectAttributes redirectAttributes, ModelAndView modelAndView) throws AlocacaoProfessorException {
			
			validator.validate(curso, result);
			
			if(result.hasErrors()){
				return cadastrarCurso(curso);
			}
			
			if(null == curso.getTurno())
				curso.setTurno(Turno.MANHA);
			if(null == curso.getSemestres())
				curso.setSemestres(8);
			
		cursoService.salvar(curso);
		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_CURSO_CADASTRADO);
		modelAndView.setViewName(Constants.CURSO_REDIRECT_LISTAR);		
		return modelAndView;
		
	}
	
	@RequestMapping(path = "/editar/{id}", method = RequestMethod.GET)
	public ModelAndView editarCurso(@PathVariable("id") Integer idCurso) {
		ModelAndView modelAndView = new ModelAndView(Constants.CURSO_EDITAR);
		
		Curso curso = cursoService.buscarCurso(idCurso);
		List<Professor> professores = professorService.buscarTodosProfessores();
			
		if (curso == null){
			modelAndView.setViewName(Constants.CURSO_REDIRECT_LISTAR);
			return modelAndView;
		}
		
		modelAndView.addObject("curso", curso);
		modelAndView.addObject("turnos", Turno.values());
		modelAndView.addObject("professores", professores);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/editar/", method = RequestMethod.POST)
	public @ResponseBody ModelAndView editarCurso(@ModelAttribute("curso") @Valid Curso curso, BindingResult result, RedirectAttributes redirectAttributes, ModelAndView modelAndView) throws AlocacaoProfessorException {
		
		Papel pl = new Papel();
		pl.setId(1);
		
		Curso cursoBD = cursoService.buscarCurso(curso.getId());
		Professor coordenadorAntigo = cursoBD.getCoordenador();
		Professor viceCoordenadorAntigo = cursoBD.getViceCoordenador();
		
		coordenadorAntigo.getPapeis().remove(pl);
		viceCoordenadorAntigo.getPapeis().remove(pl);
		
		professorService.editar(coordenadorAntigo);
		professorService.editar(viceCoordenadorAntigo);
		
		validator.validate(curso, result);
		
		if(result.hasErrors()){
			modelAndView.setViewName(Constants.CURSO_EDITAR);
			
			List<Curso> cursos = cursoService.buscarTodosCursos();
			List<Professor> professores = professorService.buscarTodosProfessores();
			
			modelAndView.addObject("cursos", cursos);
			modelAndView.addObject("turnos", Turno.values());
			modelAndView.addObject("professores", professores);
			
			return modelAndView;
		}
			
		cursoService.salvar(curso);
		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_CURSO_EDITADO);
		modelAndView.setViewName(Constants.CURSO_REDIRECT_LISTAR);
			
		return modelAndView;
	}
}
