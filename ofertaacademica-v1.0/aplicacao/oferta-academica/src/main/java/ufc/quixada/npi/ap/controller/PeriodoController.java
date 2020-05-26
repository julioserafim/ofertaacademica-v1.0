package ufc.quixada.npi.ap.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.ap.model.Periodo;
import ufc.quixada.npi.ap.model.Periodo.Semestre;
import ufc.quixada.npi.ap.service.PeriodoService;
import ufc.quixada.npi.ap.util.Constants;
import ufc.quixada.npi.ap.validation.PeriodoValidator;

import static ufc.quixada.npi.ap.util.Constants.SWAL_STATUS_SUCCESS;

@Controller
@RequestMapping(path = "/periodos")
public class PeriodoController {

	@Autowired
	private PeriodoService periodoService;

	@Autowired
	private PeriodoValidator periodoValidator;

	@ModelAttribute("status")
	public List<Periodo.Status> getAllStatus() {
		return Arrays.asList(Periodo.Status.values());
	}

	@RequestMapping(path = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView listarPeriodos() {
		ModelAndView modelAndView = new ModelAndView(Constants.PERIODO_LISTAR);

		List<Periodo> periodos = periodoService.buscarTodosPeriodos();

		modelAndView.addObject("periodos", periodos);

		return modelAndView;
	}

	@RequestMapping(path = "/cadastrar", method = RequestMethod.GET)
	public ModelAndView cadastrarPeriodo(@ModelAttribute("periodo") Periodo periodo) {
		ModelAndView modelAndView = new ModelAndView(Constants.PERIODO_CADASTRAR);
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		modelAndView.addObject("periodo", periodo);
		modelAndView.addObject("semestres", Semestre.values());						
		modelAndView.addObject("periodoAtivo", periodoAtivo);					
		return modelAndView;
	}

	@RequestMapping(path = "/cadastrar", method = RequestMethod.POST)
	public ModelAndView cadastrarPeriodo(@ModelAttribute("periodo") @Valid Periodo periodo, RedirectAttributes redirect, BindingResult result,
			ModelAndView modelAndView) {

		periodoValidator.validate(periodo, result);
		if (result.hasErrors()) {
			modelAndView.setViewName(Constants.PERIODO_CADASTRAR);
			modelAndView.addObject("semestres", Semestre.values());
			return modelAndView;
		}
		periodoService.salvar(periodo);
		modelAndView.setViewName(Constants.PERIODO_REDIRECT_LISTAR);
		redirect.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_PERIODO_CADASTRAR_SUCCESS);
		return modelAndView;
	}

	@RequestMapping(path = "/{id}/detalhar")
	public ModelAndView detalhar(@PathVariable("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView(Constants.PERIODO_DETALHAR);
		return modelAndView;
	}

	@RequestMapping(path = "/{id}/excluir")
	public @ResponseBody boolean excluir(@PathVariable("id") Integer id) {
		try {
			periodoService.excluir(periodoService.buscarPeriodo(id));
		} catch (EmptyResultDataAccessException ex) {
			return false;
		}
		return true;
	}

	@PreAuthorize("hasAnyAuthority('DIRECAO')")
	@RequestMapping(path = "/{id}/editar", method = RequestMethod.GET)
	public ModelAndView editarPeriodo(@PathVariable("id") Integer id, @ModelAttribute("periodo") Periodo periodo) {
		ModelAndView modelAndView = new ModelAndView(Constants.PERIODO_EDITAR);
		modelAndView.addObject("periodo", periodoService.buscarPeriodo(id));
		modelAndView.addObject("semestres", Semestre.values());
		return modelAndView;
	}

	@RequestMapping(path = "/{id}/editar", method = RequestMethod.POST)
	public ModelAndView editarPeriodo(@ModelAttribute("periodo") @Valid Periodo periodo, BindingResult result,
									  RedirectAttributes redirect) {
		ModelAndView modelAndView = new ModelAndView();
		periodo.setAtivo(true);
		periodoValidator.validate(periodo, result);

		if (result.hasErrors()) {
			modelAndView.setViewName(Constants.PERIODO_EDITAR);
			periodo.getSemestre();
			modelAndView.addObject("semestres", Semestre.values());
			return modelAndView;
		}

		modelAndView.setViewName(Constants.PERIODO_REDIRECT_LISTAR);
		periodoService.salvarPeriodoAberto(periodo);
		redirect.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_PERIODO_EDITAR_SUCCESS);
		return modelAndView;
	}
}
