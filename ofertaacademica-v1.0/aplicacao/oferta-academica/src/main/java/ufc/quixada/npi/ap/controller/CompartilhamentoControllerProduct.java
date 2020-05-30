package ufc.quixada.npi.ap.controller;


import org.springframework.beans.factory.annotation.Autowired;
import ufc.quixada.npi.ap.service.CompartilhamentoService;
import ufc.quixada.npi.ap.service.TurmaService;
import ufc.quixada.npi.ap.service.CursoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ufc.quixada.npi.ap.annotation.RestricaoDePeriodo;
import ufc.quixada.npi.ap.util.Constants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.validation.Valid;
import ufc.quixada.npi.ap.model.Compartilhamento;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ufc.quixada.npi.ap.model.Turma;
import org.springframework.security.core.Authentication;
import ufc.quixada.npi.ap.model.Professor;

public class CompartilhamentoControllerProduct {
	private CompartilhamentoService compartilhamentoService;
	private TurmaService turmaService;
	private CursoService cursoService;

	public CursoService getCursoService() {
		return cursoService;
	}

	@RequestMapping(path = { "/cadastrar" }, method = RequestMethod.POST)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView cadastrarCompartilhamento(
			@ModelAttribute("compartilhamento") @Valid Compartilhamento compartilhamento, BindingResult bindingResult,
			ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
		compartilhamentoService.salvar(compartilhamento);
		redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_COMPARTILHAMENTO_SOLICITADO);
		modelAndView.setViewName(Constants.COMPARTILHAMENTO_REDIRECT_LISTAR);
		return modelAndView;
	}

	@RequestMapping(path = { "/{id}/editar" }, method = RequestMethod.POST)
	public ModelAndView editarCompartilhamento(
			@PathVariable(name = "id", required = true) Compartilhamento compartilhamento,
			@RequestParam(value = "turma") Turma turma, @RequestParam(value = "vagas") Integer vagas,
			@RequestParam(value = "disjunto", required = false) boolean disjunto, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		try {
			compartilhamento.setTurma(turma);
			compartilhamento.setVagas(vagas);
			compartilhamento.setDisjunto(disjunto);
			compartilhamentoService.salvar(compartilhamento);
		} catch (Exception e) {
			modelAndView.setViewName(Constants.PAGINA_ERRO_403);
			return modelAndView;
		}
		compartilhamentoService.salvar(compartilhamento);
		modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);
		redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_COMPARTILHAMENTO_EDITADO);
		return modelAndView;
	}

	@RequestMapping(path = { "/{id}/excluir" }, method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView excluirCompartilhamento(@PathVariable(name = "id") Compartilhamento compartilhamento,
			RedirectAttributes redirectAttributes, ModelAndView modelAndView, Authentication auth) {
		if (((Professor) auth.getPrincipal()).isDirecao() || compartilhamento.canChange(auth.getName())) {
			compartilhamentoService.excluir(compartilhamento.getId());
			redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_COMPARTILHAMENTO_EXCLUIR);
		} else {
			redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_ERROR, Constants.MSG_PERMISSAO_NEGADA);
		}
		modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);
		return modelAndView;
	}

	@RequestMapping(path = { "/cadastrar" }, method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView cadastrarCompartilhamento(@ModelAttribute("compartilhamento") Compartilhamento compartilhamento,
			Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(Constants.COMPARTILHAMENTO_CADASTRAR);
		modelAndView.addObject("turmas", turmaService.buscarTodasTurmas());
		modelAndView.addObject("cursoAtual",
				cursoService.buscarCursoPorCoordenadorOuVice((Professor) auth.getPrincipal()));
		return modelAndView;
	}

	@RequestMapping(path = { "/{id}/editar" }, method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView editarCompartilhamento(@PathVariable(name = "id", required = true) Integer id,
			@ModelAttribute("compartilhamento") Compartilhamento compartilhamento, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(Constants.COMPARTILHAMENTO_EDITAR);
		compartilhamento = compartilhamentoService.buscarCompartilhamento(id);
		Professor professor = (Professor) auth.getPrincipal();
		if (professor.isCoordenacao()) {
			modelAndView.addObject("turmas", cursoService.buscarCursoPorCoordenadorOuVice(professor).getTurmas());
		} else {
			modelAndView.addObject("turma", cursoService.buscarTodosCursos());
		}
		if (compartilhamento == null) {
			modelAndView.setViewName(Constants.COMPARTILHAMENTO_REDIRECT_LISTAR);
			return modelAndView;
		}
		modelAndView.addObject("compartilhamento", compartilhamento);
		return modelAndView;
	}
}