package ufc.quixada.npi.ap.controller;

import static ufc.quixada.npi.ap.util.Constants.EXPORTAR;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.ufc.quixada.npi.ldap.model.Usuario;
import br.ufc.quixada.npi.ldap.service.UsuarioService;
import ufc.quixada.npi.ap.annotation.RestricaoDePeriodo;



import ufc.quixada.npi.ap.exception.AlocacaoProfessorException;

import ufc.quixada.npi.ap.model.Compartilhamento;
import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Oferta;
import ufc.quixada.npi.ap.model.Periodo;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.model.Turma;
import ufc.quixada.npi.ap.service.CompartilhamentoService;
import ufc.quixada.npi.ap.service.CursoService;
import ufc.quixada.npi.ap.service.DisciplinaService;
import ufc.quixada.npi.ap.service.OfertaService;
import ufc.quixada.npi.ap.service.PeriodoService;
import ufc.quixada.npi.ap.service.ProfessorService;
import ufc.quixada.npi.ap.service.RestricaoHorarioService;
import ufc.quixada.npi.ap.service.TurmaService;
import ufc.quixada.npi.ap.util.Constants;
import ufc.quixada.npi.ap.validation.OfertaValidator;

@Controller
public class DirecaoController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProfessorService professorService;

	@Autowired
	private PeriodoService periodoService;

	@Autowired
	private OfertaService ofertaService;

	@Autowired
	private DisciplinaService disciplinaService;

	@Autowired
	private RestricaoHorarioService restricaoHorarioService;

	@Autowired
	private CursoService cursoService;

	@Autowired
	private OfertaValidator ofertaValidator;

	private CompartilhamentoService compartilhamentoService;

	@Autowired
	private TurmaService turmaService;

	@ModelAttribute("cursos")
	public List<Curso> todosCursos() {
		return cursoService.buscarTodosCursos();
	}

	@ModelAttribute("professores")
	public List<Professor> todosProfessores() {
		return professorService.buscarTodosProfessores();
	}
	
	@RequestMapping(path = { "/exportacao" }, method = RequestMethod.GET)
	public ModelAndView exportar() {
		ModelAndView modelAndView = new ModelAndView(EXPORTAR);

		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		modelAndView.addObject("empilhamentos", restricaoHorarioService.buscarTodasRestricoesHorario());
		modelAndView.addObject("ofertas", ofertaService.buscarPorPeriodo(periodoAtivo));
		//System.err.println(ofertaService.buscarOfertaPorPeriodo(periodoAtivo).size());
		return modelAndView;
	}

	@RequestMapping(path = { "/exportacao-csv" }, method = RequestMethod.GET)
	public String exportarCSV() {
		ModelAndView modelAndView = new ModelAndView(EXPORTAR);

		Periodo periodoAtivo = periodoService.buscarPeriodoAtivo();
		modelAndView.addObject("empilhamentos", restricaoHorarioService.buscarTodasRestricoesHorario());
		modelAndView.addObject("ofertas", ofertaService.buscarPorPeriodo(periodoAtivo));
		System.err.println(ofertaService.buscarOfertaPorPeriodo(periodoAtivo).size());
		return EXPORTAR;
	}

	@RequestMapping(path = { "/oferta-campus" }, method = RequestMethod.GET)
	public ModelAndView listarCompartilhamentos(Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(Constants.COMPARTILHAMENTO_LISTAR);
	
		
		modelAndView.addObject("periodo", periodoService.buscarPeriodoAtivo());
		modelAndView.addObject("ofertas", ofertaService.buscarOfertasPeriodoAtivo());

		return modelAndView;

	}

	@RequestMapping(value = "/professores", method = RequestMethod.GET)
	public ModelAndView listarProfessores() {
		ModelAndView modelAndView = new ModelAndView(Constants.PROFESSOR_LISTAR);

		List<Professor> professores = professorService.buscarTodosProfessores();

		modelAndView.addObject("professores", professores);
		modelAndView.addObject("cargaHorariaAtual", professores);

		return modelAndView;
	}
	
	@RequestMapping(value = "/relatorio-carga-horaria-professor", method = RequestMethod.GET)
	public ModelAndView relatorioCargaHorariaProfessores() {
		ModelAndView modelAndView = new ModelAndView(Constants.PROFESSOR_RELATORIO_CARGA_HORARIA);
		
		modelAndView.addObject("relatorioCargaHoraria", professorService.gerarRelatorioCargaHorariaProfessores());

		return modelAndView;
	}

	@RequestMapping(value = "/atualizar-professores", method = RequestMethod.GET)
	public ModelAndView atualizarProfessores() throws AlocacaoProfessorException {
		ModelAndView modelAndView = new ModelAndView(Constants.PROFESSOR_REDIRECT_LISTAR);

		List<Usuario> usuarios = usuarioService.getByAffiliation(Constants.AFFILIATION_DOCENTE);

		for (Usuario usuario : usuarios) {
			Professor professor = professorService.buscarProfessorCpf(usuario.getCpf());

			if (professor == null) {
				Professor professor1 = new Professor();
			
				professor1.setCpf(usuario.getCpf());
				professor1.setEmail(usuario.getEmail());
				professor1.setNome(usuario.getNome());
				professor1.setApelido(usuario.getNome());
				
				professorService.salvar(professor1);
			}

		}

		return modelAndView;
	}	
	
	@RequestMapping(value = "/editar-oferta/{id}", method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_CAMPUS_REDIRECT_LISTAR)
	public ModelAndView editarOferta(@PathVariable("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_EDITAR);

		Oferta oferta = ofertaService.buscarOferta(id);

		modelAndView.addObject("oferta", oferta);
		if(oferta.getTurma().getCurso()!=null){
			modelAndView.addObject("cursoAtual", oferta.getTurma().getCurso());
		}
		modelAndView.addObject("disciplinas", disciplinaService.buscarDisciplinasNaoArquivadas());

		return modelAndView;
	}

	private boolean validarLista(List<?> lista) {
		return lista != null && !lista.isEmpty() && !lista.contains(null);
	}

	@RequestMapping(value = "/direcao/ofertas/{id}/editar", method = RequestMethod.GET)
	public ModelAndView editarOferta(@PathVariable("id") Integer id, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_EDITAR);

		Professor professor = (Professor) auth.getPrincipal();
		Curso cursoAtual = cursoService.buscarCursoPorCoordenadorOuVice(professor);

		modelAndView.addObject("cursoAtual", cursoAtual);

		modelAndView.addObject("oferta", ofertaService.buscarOferta(id));
		modelAndView.addObject("disciplinas", disciplinaService.buscarDisciplinasNaoArquivadas());

		return modelAndView;
	}

	@RequestMapping(value = "/direcao/ofertas/{id}/editar", method = RequestMethod.POST)
	public ModelAndView editarOferta(@PathVariable(name = "id", required = true) Integer id,
			@ModelAttribute("oferta") @Valid Oferta oferta, BindingResult bindingResult, ModelAndView modelAndView,
			Authentication auth) {

		ofertaValidator.validate(oferta, bindingResult);

		if (bindingResult.hasErrors()) {
			Professor professor = (Professor) auth.getPrincipal();
			modelAndView.addObject("cursoAtual", cursoService.buscarCursoPorCoordenadorOuVice(professor));
			modelAndView.setViewName(Constants.OFERTA_EDITAR);
			modelAndView.addObject("disciplinas", disciplinaService.buscarDisciplinasNaoArquivadas());

			return modelAndView;
		}

		ofertaService.salvarOfertaPeriodoAtivo(oferta);

		modelAndView.setViewName(Constants.OFERTA_REDIRECT_LISTAR);

		return modelAndView;
	}

	@RequestMapping(value = "/editar-compartilhamentos-oferta/", method = RequestMethod.GET)
	public @ResponseBody boolean editarOferta(@RequestParam("idsCompartilhamentos") List<Integer> idsCompartilhamentos,
			@RequestParam("idsTurmas") List<Integer> idsTurmas,
			@RequestParam("vagas") List<Integer> vagasCompartilhamentos) {

		if (validarLista(idsCompartilhamentos) && validarLista(idsTurmas) && validarLista(vagasCompartilhamentos)
				&& idsCompartilhamentos.size() == idsTurmas.size()
				&& idsTurmas.size() == vagasCompartilhamentos.size()) {

			for (int i = 0; i < idsCompartilhamentos.size(); i++) {
				int idCompartilhamento = idsCompartilhamentos.get(i);
				int idTurma = idsTurmas.get(i);
				int vagas = vagasCompartilhamentos.get(i);

				Turma turma = turmaService.buscarTurma(idTurma);
				Compartilhamento compartilhamento = compartilhamentoService.buscarCompartilhamento(idCompartilhamento);

				if (compartilhamento != null && turma != null && vagas > 0) {
					compartilhamento.setVagas(vagas);
					compartilhamento.setTurma(turma);

					compartilhamentoService.salvar(compartilhamento);
				}
			}

			return true;
		}

		return false;
	}
	
	
}
