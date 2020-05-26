package ufc.quixada.npi.ap.controller;

import static ufc.quixada.npi.ap.util.Constants.SWAL_STATUS_ERROR;
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

import ufc.quixada.npi.ap.exception.AlocacaoProfessoresException;
import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Disciplina;
import ufc.quixada.npi.ap.model.Oferta;
import ufc.quixada.npi.ap.model.Periodo;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.model.RestricaoHorario;
import ufc.quixada.npi.ap.model.RestricaoHorario.Tipo;
import ufc.quixada.npi.ap.model.Turma;
import ufc.quixada.npi.ap.service.CursoService;
import ufc.quixada.npi.ap.service.DisciplinaService;
import ufc.quixada.npi.ap.service.OfertaService;
import ufc.quixada.npi.ap.service.PeriodoService;
import ufc.quixada.npi.ap.service.RestricaoHorarioService;
import ufc.quixada.npi.ap.service.TurmaService;
import ufc.quixada.npi.ap.util.Constants;
import ufc.quixada.npi.ap.validation.EmpilhamentoValidator;

@Controller
@RequestMapping(path = "/empilhamentos")
public class RestricaoHorarioController {

	@Autowired
	private RestricaoHorarioService empilhamentoService;

	@Autowired
	private DisciplinaService disciplinaService;

	@Autowired
	private OfertaService ofertaService;

	@Autowired
	private CursoService cursoService;

	@Autowired
	private PeriodoService periodoService;

	@Autowired
	private TurmaService turmaService;

	@Autowired
	private EmpilhamentoValidator empilhamentoValidator;

	@ModelAttribute("turmas")
	public List<Turma> todasTurmas() {
		return turmaService.buscarTodasTurmas();
	}

	@RequestMapping(path = { "" })
	public ModelAndView listarEmpilhamentos(Authentication auth) {

		Professor professor = (Professor) auth.getPrincipal();
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		
		List<RestricaoHorario> empilhamentos = empilhamentoService.buscarTodasRestricoesHorario();
		List<RestricaoHorario> distintos = empilhamentoService.buscarTodasRestricoesHorarioDistinto();
		List<Oferta> todasOfertas = ofertaService.buscarOfertaPorPeriodo(periodoAtivo);
		List<Turma> turmas = turmaService.buscarTodasTurmas();

		ModelAndView model = new ModelAndView(Constants.EMPILHAMENTO_LISTAR);
		model.addObject("todasOfertas", todasOfertas);
		model.addObject("restricaoHorarios", empilhamentos);
		model.addObject("restricaoHorariosDistinto", distintos);
		model.addObject("restricaoHorario", new RestricaoHorario());
		model.addObject("turmas", turmas);
		model.addObject("periodoAtivo", periodoAtivo);
		
		if (professor.isCoordenacao()) {
			Curso curso = cursoService.buscarCursoPorCoordenadorOuVice(professor);			
			model.addObject("cursoAtual", curso);
			List<Oferta> ofertas = ofertaService.buscarPorPeriodoAndCurso(periodoAtivo, curso);
			model.addObject("ofertas", ofertas);
		}
		return model;
	}

	@RequestMapping(path = { "/cadastrar" }, method = RequestMethod.GET)
	public ModelAndView cadastrarEmpilhamento(Authentication auth) {
		ModelAndView model = new ModelAndView(Constants.EMPILHAMENTO_CADASTRAR);
		Professor coordenador = (Professor) auth.getPrincipal();
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		Curso curso = cursoService.buscarCursoPorCoordenadorOuVice(coordenador);

		List<Oferta> ofertas = ofertaService.buscarPorPeriodoAndCurso(periodoAtivo, curso);
		List<Turma> turmas = turmaService.buscarTodasTurmas();

		model.addObject("ofertas", ofertas);
		model.addObject("turmas", turmas);
		model.addObject("restricaoHorario", new RestricaoHorario());
		model.addObject("periodoAtivo", periodoService.buscarPeriodoAtivo());

		return model;
	}

	@RequestMapping(path = { "/cadastrarEmpilhamento" }, method = RequestMethod.POST)
	public ModelAndView cadastrarEmpilhamento(@ModelAttribute("restricaoHorario") @Valid RestricaoHorario restricaoHorario, BindingResult result,ModelAndView modelAndView, Authentication auth,RedirectAttributes redirectAttributes) throws AlocacaoProfessoresException{
		empilhamentoValidator.validate(restricaoHorario, result);

		if (result.hasErrors()) {
			Professor professor = (Professor) auth.getPrincipal();
			modelAndView.addObject("cursoAtual", cursoService.buscarCursoPorCoordenadorOuVice(professor));
			modelAndView.setViewName(Constants.EMPILHAMENTO_CADASTRAR);
			return modelAndView;
		}
		restricaoHorario.setTipo(Tipo.EMPILHAMENTO);
		if(empilhamentoService.salvarRestricaoHorarioPeriodoAtivo(restricaoHorario)){
			redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_EMPILHAMENTO_CADASTRAR);
		} else {
			redirectAttributes.addFlashAttribute(SWAL_STATUS_ERROR, Constants.MSG_EMPILHAMENTO_CADASTRAR_EXISTENTE);
		}
		modelAndView.setViewName(Constants.EMPILHAMENTO_REDIRECT_LISTAR);
		return modelAndView;
	}

	@RequestMapping(path = { "/cadastrarDistinto" }, method = RequestMethod.POST)
	public ModelAndView cadastrarDistinto(@ModelAttribute("restricaoHorario") @Valid RestricaoHorario restricaoHorario,
			BindingResult result, ModelAndView modelAndView, Authentication auth,RedirectAttributes redirectAttributes) throws AlocacaoProfessoresException{
		empilhamentoValidator.validate(restricaoHorario, result);

		if (result.hasErrors()) {
			Professor professor = (Professor) auth.getPrincipal();
			modelAndView.addObject("cursoAtual", cursoService.buscarCursoPorCoordenadorOuVice(professor));
			modelAndView.setViewName(Constants.EMPILHAMENTO_CADASTRAR);
			return modelAndView;
		}
		restricaoHorario.setTipo(Tipo.DISTINTO);
		if(!empilhamentoService.salvarRestricaoHorarioPeriodoAtivo(restricaoHorario)){
			modelAndView.setViewName(Constants.EMPILHAMENTO_REDIRECT_LISTAR);
			redirectAttributes.addFlashAttribute(SWAL_STATUS_ERROR, Constants.DISTINTO_CADASTRAR_EXISTENTE);
		}

		ModelAndView modelRetorno = new ModelAndView(Constants.EMPILHAMENTO_REDIRECT_LISTAR);
		return modelRetorno;
	}

	@RequestMapping(path = { "/{id}/excluir" }, method = RequestMethod.GET)
	public ModelAndView excluirEmpilhamento(@PathVariable("id") RestricaoHorario restricao, RedirectAttributes redirectAttributes,
			ModelAndView modelAndView, Authentication auth) {
		Professor professor = (Professor) auth.getPrincipal();
		if (restricao == null || (!professor.isDirecao() && !restricao.canChange(auth.getName()))) {
			redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_ERROR, Constants.MSG_PERMISSAO_NEGADA);
		} else {
			empilhamentoService.excluir(restricao.getId());
			redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_RESTRICAO_HORARIO_EXCLUIDO);
		}
		modelAndView.setViewName(Constants.EMPILHAMENTO_REDIRECT_LISTAR);
		return modelAndView;
	}

	/*@RequestMapping(path = {"/{id}/editar"}, method = RequestMethod.GET)
	public ModelAndView editarCompartilhamento(@PathVariable("id") Integer id, Authentication auth){
		
		Professor coordenador = (Professor) auth.getPrincipal();

		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		Curso curso = cursoService.buscarCursoPorCoordenadorOuVice(coordenador);

		List<Oferta> ofertas = ofertaService.buscarPorPeriodoAndCurso(periodoAtivo, curso);
		RestricaoHorario restricaoHorario = empilhamentoService.buscarRestricaoHorario(id);

		ModelAndView modelAndView = new ModelAndView(Constants.EMPILHAMENTO_EDITAR);
		modelAndView.addObject("ofertas", ofertas);
		modelAndView.addObject("restricaoHorario", restricaoHorario);

		return modelAndView;
	}

	@RequestMapping(path = { "/{id}/editar" }, method = RequestMethod.POST)
	public ModelAndView editarCompartilhamento(@PathVariable(name = "id", required = true) Integer id,
			@ModelAttribute("restricaoHorario") @Valid RestricaoHorario empilhamento, BindingResult bindingResult,
			ModelAndView modelAndView) {

		empilhamentoValidator.validate(empilhamento, bindingResult);

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName(Constants.EMPILHAMENTO_EDITAR);
			List<Disciplina> disciplinas = disciplinaService.buscarDisciplinasNaoArquivadas();
			modelAndView.addObject("disciplinasNaoArquivadas	", disciplinas);
			return modelAndView;
		}

		try {
			empilhamento.setTipo(Tipo.EMPILHAMENTO);
			empilhamentoService.salvarRestricaoHorarioPeriodoAtivo(empilhamento);
		} catch (Exception e) {
			modelAndView.setViewName(Constants.PAGINA_ERRO_403);
			return modelAndView;
		}

		modelAndView.setViewName(Constants.EMPILHAMENTO_REDIRECT_LISTAR);
		return modelAndView;
	}

	@RequestMapping(path = { "/{id}/editarDistinto" }, method = RequestMethod.POST)
	public ModelAndView editarDistinto(@PathVariable(name = "id", required = true) Integer id,
			@ModelAttribute("empilhamento") @Valid RestricaoHorario empilhamento, BindingResult bindingResult,
			ModelAndView modelAndView) {

		empilhamentoValidator.validate(empilhamento, bindingResult);

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName(Constants.EMPILHAMENTO_EDITAR);
			List<Disciplina> disciplinas = disciplinaService.buscarDisciplinasNaoArquivadas();
			modelAndView.addObject("disciplinasNaoArquivadas	", disciplinas);
			return modelAndView;
		}

		try {
			empilhamento.setTipo(Tipo.DISTINTO);
			empilhamentoService.salvarRestricaoHorarioPeriodoAtivo(empilhamento);
		} catch (Exception e) {
			modelAndView.setViewName(Constants.PAGINA_ERRO_403);

			return modelAndView;
		}

		modelAndView.setViewName(Constants.EMPILHAMENTO_REDIRECT_LISTAR);
		return modelAndView;
	}*/

	@RequestMapping(value = "/{id}/desabilitar", method = RequestMethod.GET)
	public ModelAndView desabilitarEmpilhamento(@PathVariable(name = "id", required = true) Integer id, RedirectAttributes redirectAttributes,
				ModelAndView modelAndView, Authentication auth) {
		Professor professor = (Professor) auth.getPrincipal();
		 if (!professor.isDirecao()) {
			 redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_ERROR, Constants.MSG_PERMISSAO_NEGADA);
		 } else {
			 empilhamentoService.desabilitarEmpilhamento(id);
			 redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_RESTRICAO_HORARIO_DESABILITADO);
		 }
		modelAndView.setViewName(Constants.EMPILHAMENTO_REDIRECT_LISTAR);
		return modelAndView;
	}

	@RequestMapping(value = "/{id}/habilitar", method = RequestMethod.GET)
	public ModelAndView habilitarEmpilhamento(@PathVariable(name = "id", required = true) Integer id, RedirectAttributes redirectAttributes,
			  ModelAndView modelAndView, Authentication auth) {
		Professor professor = (Professor) auth.getPrincipal();
		if (!professor.isDirecao()) {
			redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_ERROR, Constants.MSG_PERMISSAO_NEGADA);
		} else {
			empilhamentoService.habilitarEmpilhamento(id);
			redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_RESTRICAO_HORARIO_HABILITADO);
		}
		modelAndView.setViewName(Constants.EMPILHAMENTO_REDIRECT_LISTAR);
		return modelAndView;
	}

	@RequestMapping(path = { "/{id}/detalhar" })
	public ModelAndView visualizarEmpilhamento(@PathVariable("id") Integer id,
			@RequestParam(required = false) String erro) {
		RestricaoHorario empilhamento = empilhamentoService.buscarRestricaoHorario(id);

		ModelAndView model = new ModelAndView(Constants.EMPILHAMENTO_DETALHAR);
		model.addObject("empilhamento", empilhamento);
		model.addObject("erro", erro);

		return model;
	}

}
