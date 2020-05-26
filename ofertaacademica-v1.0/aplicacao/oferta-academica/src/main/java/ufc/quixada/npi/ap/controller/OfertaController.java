package ufc.quixada.npi.ap.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import ufc.quixada.npi.ap.exception.AlocacaoProfessoresException;
import ufc.quixada.npi.ap.model.Compartilhamento;
import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Disciplina;
import ufc.quixada.npi.ap.model.Oferta;
import ufc.quixada.npi.ap.model.Periodo;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.model.Turma;
import ufc.quixada.npi.ap.model.Turma.Semestre;
import ufc.quixada.npi.ap.repository.CursoRepository;
import ufc.quixada.npi.ap.service.CompartilhamentoService;
import ufc.quixada.npi.ap.service.CursoService;
import ufc.quixada.npi.ap.service.DisciplinaService;
import ufc.quixada.npi.ap.service.OfertaService;
import ufc.quixada.npi.ap.service.PeriodoService;
import ufc.quixada.npi.ap.service.ProfessorService;
import ufc.quixada.npi.ap.service.TurmaService;
import ufc.quixada.npi.ap.util.Constants;
import ufc.quixada.npi.ap.validation.CompartilhamentoValidator;
import ufc.quixada.npi.ap.validation.OfertaValidator;

import static ufc.quixada.npi.ap.util.Constants.*;

@Controller
@RequestMapping(value = "/ofertas")
public class OfertaController {

	@Autowired
	CursoRepository crepo; 

	@Autowired
	private OfertaService ofertaService;

	@Autowired
	private ProfessorService professorService;

	@Autowired
	private TurmaService turmaService;

	@Autowired
	private DisciplinaService disciplinaService;

	@Autowired
	private OfertaValidator ofertaValidator;

	@Autowired
	private PeriodoService periodoService;

	@Autowired
	private CursoService cursoService;

	@Autowired
	private CompartilhamentoService compartilhamentoService;

	@Autowired
	private CompartilhamentoValidator compartilhamentoValidator;


	@ModelAttribute("professores")
	public List<Professor> todosProfessores() {
		return professorService.buscarTodosProfessores();
	}

	@ModelAttribute("cursos")
	public List<Curso> todosCursos() {
		return cursoService.buscarTodosCursos();
	}

	@RequestMapping(value = {"", "/", "/{id}"}, method = RequestMethod.GET)
	public ModelAndView listarOfertasIndex(Authentication auth, @PathVariable(name="id", required=false) Curso cursoAtual) {
		ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_LISTAR);

		if(cursoAtual == null) {
			Professor professor = (Professor) auth.getPrincipal();
			cursoAtual = cursoService.buscarCursoPorCoordenadorOuVice(professor);
			if(cursoAtual == null){
				cursoAtual = cursoService.buscarPorSigla("SI");
			}
		}

		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		
		modelAndView.addObject("cursoAtual", cursoAtual);
		modelAndView.addObject("turmas", cursoAtual.getTurmas());
		modelAndView.addObject("periodo", periodoAtivo);
		modelAndView.addObject("periodos", periodoService.buscarPeriodosConsolidados());

		List<Oferta> ofertasDoCurso = ofertaService.buscarPorPeriodoAndCurso(periodoAtivo, cursoAtual);
		List<Compartilhamento> compartilhamentosDoCurso = compartilhamentoService.buscarCompartilhamentosPorPeriodoAndCurso(periodoAtivo, cursoAtual);

		Map<Semestre, List<Oferta>> mapOfertasPorSemestre = mapearOfertasPorCursoESemestre(ofertasDoCurso,
				cursoAtual.getTurmas());
		Map<Semestre, List<Compartilhamento>> mapCompartilhamentosPorSemestre = mapearCompartilhamentosPorCursoESemestre(
				compartilhamentosDoCurso, cursoAtual.getTurmas());

		modelAndView.addObject("ofertas", mapOfertasPorSemestre);
		modelAndView.addObject("compartilhamentos", mapCompartilhamentosPorSemestre);

		return modelAndView;
	}

	public Map<Semestre, List<Oferta>> mapearOfertasPorCursoESemestre(List<Oferta> ofertasDoCurso,
			List<Turma> turmasDoCurso) {

		Map<Semestre, List<Oferta>> mapOfertasPorSemestre = new HashMap<>();

		for (Turma turma : turmasDoCurso) {
			List<Oferta> ofertasPorSemestre = new ArrayList<>();
			for (Oferta oferta : ofertasDoCurso) {
				if (turma.getSemestre().equals(oferta.getTurma().getSemestre())) {
					ofertasPorSemestre.add(oferta);
				}
			}
			mapOfertasPorSemestre.put(turma.getSemestre(), ofertasPorSemestre);
		}

		return mapOfertasPorSemestre;
	}

	public Map<Semestre, List<Compartilhamento>> mapearCompartilhamentosPorCursoESemestre(
			List<Compartilhamento> compartilhamentosDoCurso, List<Turma> turmasDoCurso) {

		Map<Semestre, List<Compartilhamento>> mapCompartilhamentosPorSemestre = new HashMap<>();

		for(Turma turma : turmasDoCurso){
			List<Compartilhamento> compartilhamentosPorSemestre = new ArrayList<>();
			for(Compartilhamento compartilhamento : compartilhamentosDoCurso){
				if(turma.getSemestre().equals(compartilhamento.getTurma().getSemestre())){
					compartilhamentosPorSemestre.add(compartilhamento);
				}
			}
			mapCompartilhamentosPorSemestre.put(turma.getSemestre(), compartilhamentosPorSemestre);
		}

		return mapCompartilhamentosPorSemestre;
	}

	@RequestMapping(value = "/cadastrar", method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView cadastrarOferta(@ModelAttribute("oferta") Oferta oferta, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_CADASTRAR);
		Professor professor = (Professor) auth.getPrincipal();
		modelAndView.addObject("action", "cadastrar");
		modelAndView.addObject("cursoAtual", cursoService.buscarCursoPorCoordenadorOuVice(professor));
		modelAndView.addObject("periodoAtivo", periodoService.buscarPeriodoAtivo());
		modelAndView.addObject("disciplinas", disciplinaService.buscarDisciplinasNaoArquivadas());		
		modelAndView.addObject("turmas", turmaService.buscarTodasTurmas());

		return modelAndView;
	}

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView cadastrarOferta(@ModelAttribute("oferta") @Valid Oferta oferta,BindingResult bindingResult, 
			ModelAndView modelAndView,RedirectAttributes redirectAttributes, Authentication auth) {

		ofertaValidator.validate(oferta, bindingResult);
		Professor professor = (Professor) auth.getPrincipal();
		Periodo periodo = periodoService.buscarPeriodoAtivo();
		if(professor.isCoordenacao() && (periodo.isCoordenacao() || periodo.isAjuste())){
			oferta.setPeriodo(periodo);
		}
		if(professor.isDirecao() && (periodo.isDirecao() || periodo.isAjuste())){
			oferta.setPeriodo(periodo);
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName(Constants.OFERTA_CADASTRAR);
			modelAndView.addObject("action", "cadastrar");
			modelAndView.addObject("cursoAtual", cursoService.buscarCursoPorCoordenadorOuVice(professor));
			modelAndView.addObject("periodoAtivo", periodoService.buscarPeriodoAtivo());
			modelAndView.addObject("disciplinas", disciplinaService.buscarDisciplinasNaoArquivadas());
			modelAndView.addObject("turmas", turmaService.buscarTodasTurmas());
		}
		try {
			ofertaService.salvarOferta(oferta);
			modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);
			redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, MSG_OFERTA_CADASTRADA);
		} catch (AlocacaoProfessoresException e) {
			modelAndView.setViewName(Constants.OFERTA_REDIRECT_CADASTRO);
			redirectAttributes.addFlashAttribute(SWAL_STATUS_ERROR, e.getMessage());
		}

		return modelAndView;
	}

	@RequestMapping(value = "/{id}/editar", method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView editarOferta(@PathVariable("id") Oferta oferta, Authentication auth, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_CADASTRAR);
		if (((Professor)auth.getPrincipal()).isDirecao() || oferta.canChange(auth.getName())) {

			modelAndView.addObject("action", "editar");
			modelAndView.addObject("oferta", oferta);
			modelAndView.addObject("cursoAtual", oferta.getTurma().getCurso());
			modelAndView.addObject("periodoAtivo", periodoService.buscarPeriodoAtivo());
			modelAndView.addObject("disciplinas", disciplinaService.buscarDisciplinasNaoArquivadas());
			modelAndView.addObject("turmas", turmaService.buscarTodasTurmas());
        } else {
			redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_ERROR, Constants.MSG_PERMISSAO_NEGADA);
			modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);
		}
        return modelAndView;
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView editarOferta(@ModelAttribute("oferta") @Valid Oferta oferta, BindingResult bindingResult,
			ModelAndView modelAndView, Authentication auth, RedirectAttributes redirectAttributes) {

		ofertaValidator.validate(oferta, bindingResult);

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName(Constants.OFERTA_EDITAR);

			Professor professor = (Professor) auth.getPrincipal();
			Curso cursoCoordenador = cursoService.buscarCursoPorCoordenadorOuVice(professor);
			List<Disciplina> disciplinas = disciplinaService.buscarDisciplinasNaoArquivadas();

			modelAndView.addObject("oferta", oferta);
			modelAndView.addObject("cursoAtual", cursoCoordenador);
			modelAndView.addObject("disciplinas", disciplinas);

			return modelAndView;
		}

		ofertaService.salvarOfertaPeriodoAtivo(oferta);

		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, MSG_OFERTA_EDITADA);

		modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);

		return modelAndView;
	}

	@RequestMapping(value = "/{id}/visualizar-oferta/", method = RequestMethod.GET)
	public ModelAndView visualizarOferta(@PathVariable("id") Integer id, @RequestParam(required = false) String erro) {
		ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_VISUALIZAR_OFERTA);

		Oferta oferta = ofertaService.buscarOferta(id);

		modelAndView.addObject("oferta", oferta);
		modelAndView.addObject("periodo", periodoService.buscarPeriodoAtivo());
		modelAndView.addObject("cursoAtual", oferta.getTurma().getCurso());
		modelAndView.addObject("disciplinas", disciplinaService.buscarDisciplinasNaoArquivadas());
		modelAndView.addObject("erro", erro);

		return modelAndView;
	}

	@RequestMapping(value = "/{id}/excluir", method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView excluirOferta(@PathVariable(name = "id", required = true) Oferta oferta, Authentication auth,
                                      RedirectAttributes redirectAttributes, ModelAndView modelAndView){
		if (((Professor)auth.getPrincipal()).isDirecao() || oferta.canChange(auth.getName())) {
	        ofertaService.excluir(oferta.getId());
		    redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_SUCCESS, Constants.MSG_OFERTA_EXCLUIR);
		} else {
            redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_ERROR, Constants.MSG_PERMISSAO_NEGADA);
        }
		modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);
		return modelAndView;
	}

	@RequestMapping(path = {"/{idOferta}/solicitar-compartilhamento"}, method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView solicitarCompartilhamento(@PathVariable("idOferta") Oferta oferta,
			@ModelAttribute("compartilhamento") Compartilhamento compartilhamento, Authentication auth){

		ModelAndView modelAndView = new ModelAndView(Constants.COMPARTILHAMENTO_CADASTRAR);
        modelAndView.addObject("action", "cadastrar");
        modelAndView.addObject("cursoAtual", cursoService.buscarCursoPorCoordenadorOuVice((Professor) auth.getPrincipal()));
		modelAndView.addObject("turmas", turmaService.buscarTodasTurmas());
		modelAndView.addObject("oferta", oferta);

		return modelAndView;
	}

	@RequestMapping(path = {"/{idOferta}/solicitar-compartilhamento"}, method = RequestMethod.POST)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView solicitarCompartilhamentoCoordenacao(@PathVariable("idOferta") Oferta oferta,
			@ModelAttribute("compartilhamento") @Valid Compartilhamento compartilhamento,
            @RequestParam(value = "curso", required = false) Curso curso,
			BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes,
			Authentication auth){
		/*Map<String, Object> mapa = new HashMap<>();
		if(curso == null){
			curso = compartilhamento.getOferta().getTurma().getCurso();
		}
		mapa.put("cursoCoordenador", curso);
		mapa.put("compartilhamento", compartilhamento);

		compartilhamentoValidator.validate(mapa, bindingResult);

		if (bindingResult.hasErrors()){
			modelAndView.setViewName(Constants.COMPARTILHAMENTO_CADASTRAR);

			Professor professor = (Professor) auth.getPrincipal();
			Curso turmas = cursoService.buscarCursoPorCoordenadorOuVice(professor);
			if(turmas!=null){
				modelAndView.addObject("turmas", turmas.getTurmas());
			}else{
                modelAndView.addObject("turmas", curso.getTurmas());
			}
			modelAndView.addObject("oferta", oferta);
            return modelAndView;

		}

		*/
		compartilhamentoService.salvar(compartilhamento);

		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, MSG_COMPARTILHAMENTO_SOLICITADO);

		modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);

		return modelAndView;
	}

    @RequestMapping(path = {"/{id}/editar-compartilhamento"}, method = RequestMethod.GET)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
    public ModelAndView editarCompartilhamentoForm(@PathVariable("id") Compartilhamento compartilhamento, Authentication auth, RedirectAttributes redirectAttributes){

        Professor professor = (Professor) auth.getPrincipal();
		ModelAndView modelAndView = new ModelAndView(Constants.COMPARTILHAMENTO_CADASTRAR);
	    if (professor.isDirecao() || compartilhamento.canChange(auth.getName())) {
            modelAndView.addObject("action", "editar");
            modelAndView.addObject("cursoAtual", compartilhamento.getTurma().getCurso());
            modelAndView.addObject("turmas", turmaService.buscarTodasTurmas());
            modelAndView.addObject("oferta", compartilhamento.getOferta());
            modelAndView.addObject("compartilhamento", compartilhamento);
        } else {
            redirectAttributes.addFlashAttribute(Constants.SWAL_STATUS_ERROR, Constants.MSG_PERMISSAO_NEGADA);
            modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);
        }
        return modelAndView;
    }

	@RequestMapping(path = {"/{idOferta}/solicitar-compartilhamento-direcao"}, method = RequestMethod.POST)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView solicitarCompartilhamentoDirecao(@PathVariable("idOferta") Integer id,
			@ModelAttribute("compartilhamento") @Valid Compartilhamento compartilhamento,
			@RequestParam("turma") @Valid Turma turma,
			BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes,
			Authentication auth){
		Map<String, Object> mapa = new HashMap<>();

		mapa.put("cursoCoordenador", turma.getCurso());

		mapa.put("compartilhamento", compartilhamento);

		compartilhamentoValidator.validate(mapa, bindingResult);

		if (bindingResult.hasErrors()){
			modelAndView.setViewName(Constants.COMPARTILHAMENTO_CADASTRAR);

			modelAndView.addObject("turma", cursoService.buscarTodosCursos());

			modelAndView.addObject("oferta", ofertaService.buscarOferta(id));

		}

		compartilhamentoService.salvar(compartilhamento);

		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, MSG_COMPARTILHAMENTO_SOLICITADO);

		modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);

		return modelAndView;
	}

	@RequestMapping(value = {"/importar", "/importar/{idPeriodo}"}, method = RequestMethod.GET)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	@PreAuthorize("hasAnyAuthority('COORDENACAO')")
	public ModelAndView importarOfertas(@PathVariable(value = "idPeriodo", required = false) Periodo periodo, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_IMPORTAR);
		Professor professor = (Professor) auth.getPrincipal();
		Curso curso = cursoService.buscarCursoPorCoordenadorOuVice(professor);
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		List<Oferta> ofertas = ofertaService.buscarOfertasNaoImportadasPeriodoAtivoPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		List<Oferta> ofertasImportadas =  ofertaService.buscarOfertasImportadasPeriodoAtivoPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		//List<Compartilhamento> ofertasCompartilhadas = compartilhamentoService.buscarCompartilhamentosNaoImportadosPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		//List<Compartilhamento> ofertasCompartilhadasImportadas = compartilhamentoService.buscarCompartilhamentosImportadosPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		List<Periodo> periodos = periodoService.buscarTodosPeriodos();
		periodos.remove(periodoAtivo);

		modelAndView.addObject("periodos", periodos);
		modelAndView.addObject("periodo", periodo);
		modelAndView.addObject("periodoAtual", periodoAtivo);
		modelAndView.addObject("ofertas", ofertas);
		modelAndView.addObject("ofertasImportadas", ofertasImportadas);
		//modelAndView.addObject("compartilhamentos", ofertasCompartilhadas);
		//modelAndView.addObject("compartilhamentosImportados", ofertasCompartilhadasImportadas);

		return modelAndView;
	}

	@RequestMapping(value = "/importar", method = RequestMethod.POST)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView importarOfertas(@RequestParam(value = "ofertas", required = false) List<Oferta> ofertas, @RequestParam("periodo") Periodo periodo, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_REDIRECT_IMPORTAR + "/" + (periodo == null ? "" : periodo.getId()));
		ofertaService.importarOfertas(ofertas);
		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, MSG_IMPORTACAO_REALIZADA);
		return modelAndView;
	}

	@RequestMapping(value = "/importar-ofertas-compartilhadas", method = RequestMethod.GET)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public @ResponseBody Map<String, Object> importarOfertasCompartilhadas(@RequestParam("compartilhamentos") List<Integer> compartilhamentos, Authentication auth) {
		Professor coordenador = (Professor) auth.getPrincipal();
		Curso curso = cursoService.buscarCursoPorCoordenadorOuVice(coordenador);
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();

		return compartilhamentoService.importarOfertasCompartilhadas(compartilhamentos, periodoAtivo, curso);
	}

	@RequestMapping(value = "/periodo/{idPeriodo}/buscar-ofertas/", method = RequestMethod.GET)
	public ModelAndView buscarOfertas(@PathVariable("idPeriodo") Integer idPeriodo, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_REDIRECT_IMPORTAR);

		Professor coordenador = (Professor) auth.getPrincipal();
		Curso curso = cursoService.buscarCursoPorCoordenadorOuVice(coordenador);
		Periodo periodo = periodoService.buscarPeriodo(idPeriodo);
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();

		List<Oferta> ofertas = ofertaService.buscarOfertasNaoImportadasPeriodoAtivoPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		List<Oferta> ofertasImportadas =  ofertaService.buscarOfertasImportadasPeriodoAtivoPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		List<Compartilhamento> ofertasCompartilhadas = compartilhamentoService.buscarCompartilhamentosNaoImportadosPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		List<Compartilhamento> ofertasCompartilhadasImportadas = compartilhamentoService.buscarCompartilhamentosImportadosPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		
		modelAndView.addObject("ofertas", ofertas);
		modelAndView.addObject("ofertasImportadas", ofertasImportadas);
		modelAndView.addObject("ofertasCompartilhadas", ofertasCompartilhadas);
		modelAndView.addObject("ofertasCompartilhadasImportadas", ofertasCompartilhadasImportadas);

		return modelAndView;
	}

	@RequestMapping(value = "/curso/{idCurso}", method = RequestMethod.GET)
	public @ResponseBody ModelMap listarOfertasPorCurso(@PathVariable("idCurso") Curso curso, Authentication auth) {
		ModelMap model = new ModelMap();

		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();

		List<Oferta> ofertas = ofertaService.buscarPorPeriodoAndCurso(periodoAtivo, curso);
		List<Compartilhamento> compartilhamentos = compartilhamentoService.buscarCompartilhamentosPorPeriodoAndCurso(periodoAtivo, curso);

		Professor pessoa = (Professor) auth.getPrincipal();
		model.addAttribute("papelDirecao", pessoa.isDirecao());
		model.addAttribute("curso", curso);
		model.addAttribute("ofertas", ofertas);
		model.addAttribute("compartilhamentos", compartilhamentos);

		return model;
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public @ResponseBody ModelMap listarOfertas(Authentication auth) {
		ModelMap model = new ModelMap();

		Professor coordenador = (Professor) auth.getPrincipal();
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		Curso cursoCoordenador = cursoService.buscarCursoPorCoordenadorOuVice(coordenador);

		if(cursoCoordenador == null) {
			cursoCoordenador = cursoService.buscarPorSigla("SI");
		} 

		List<Oferta> ofertasCurso = ofertaService.buscarPorPeriodoAndCurso(periodoAtivo, cursoCoordenador);
		List<Compartilhamento> compartilhamentos = compartilhamentoService.buscarCompartilhamentosPorPeriodoAndCurso(periodoAtivo, cursoCoordenador);

		model.addAttribute("papelDirecao", coordenador.isDirecao());
		model.addAttribute("curso", cursoCoordenador);
		model.addAttribute("ofertas", ofertasCurso);
		model.addAttribute("compartilhamentos", compartilhamentos);

		return model;
	}	


	@RequestMapping(value = "/buscar-ofertas/{periodo}", method = RequestMethod.GET)
	public @ResponseBody ModelMap buscarOfertas(@PathVariable("periodo") Periodo periodo, Authentication auth) {
		ModelMap model = new ModelMap();

		Professor coordenador = (Professor) auth.getPrincipal();
		Curso curso = cursoService.buscarCursoPorCoordenadorOuVice(coordenador);
		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();

		List<Oferta> ofertas = ofertaService.buscarOfertasNaoImportadasPeriodoAtivoPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		List<Oferta> ofertasImportadas =  ofertaService.buscarOfertasImportadasPeriodoAtivoPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		List<Compartilhamento> ofertasCompartilhadas = compartilhamentoService.buscarCompartilhamentosNaoImportadosPorPeriodoAndCurso(periodo, periodoAtivo, curso);
		List<Compartilhamento> ofertasCompartilhadasImportadas = compartilhamentoService.buscarCompartilhamentosImportadosPorPeriodoAndCurso(periodo, periodoAtivo, curso);

		model.addAttribute("ofertas", ofertas);
		model.addAttribute("ofertasImportadas", ofertasImportadas);
		model.addAttribute("ofertasCompartilhadas", ofertasCompartilhadas);
		model.addAttribute("ofertasCompartilhadasImportadas", ofertasCompartilhadasImportadas);

		return model;
	}

	@RequestMapping(value = "/substituicao-ofertas", method = RequestMethod.GET)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public @ResponseBody boolean substituirOfertas(@RequestParam("ofertas") List<Integer> ofertas) {
		ofertaService.substituirOferta(ofertas);
		return true;
	}

	@RequestMapping(value = "/{id}/relacionamentos", method = RequestMethod.GET)
	public @ResponseBody boolean hasRelacionamentos(@PathVariable("id") Oferta oferta) {
		return ofertaService.hasCompartilhamentoOuRestricaoHorario(oferta);
	}

}