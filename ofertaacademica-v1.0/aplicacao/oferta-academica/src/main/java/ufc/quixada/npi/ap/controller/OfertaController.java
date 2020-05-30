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

	private OfertaControllerProduct ofertaControllerProduct = new OfertaControllerProduct();

	@Autowired
	CursoRepository crepo; 

	@Autowired
	private ProfessorService professorService;

	@ModelAttribute("professores")
	public List<Professor> todosProfessores() {
		return professorService.buscarTodosProfessores();
	}

	@ModelAttribute("cursos")
	public List<Curso> todosCursos() {
		return ofertaControllerProduct.getCursoService().buscarTodosCursos();
	}

	@RequestMapping(value = {"", "/", "/{id}"}, method = RequestMethod.GET)
	public ModelAndView listarOfertasIndex(Authentication auth, @PathVariable(name="id", required=false) Curso cursoAtual) {


		return ofertaControllerProduct.listarOfertasIndex(auth, cursoAtual, this);
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

		for (Turma turma : turmasDoCurso) {
			List<Compartilhamento> compartilhamentosPorSemestre = new ArrayList<>();
			for (Compartilhamento compartilhamento : compartilhamentosDoCurso) {
				if (turma.getSemestre().equals(compartilhamento.getTurma().getSemestre())) {
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
		return ofertaControllerProduct.cadastrarOferta(oferta, auth);
	}


    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView cadastrarOferta(@ModelAttribute("oferta") @Valid Oferta oferta,BindingResult bindingResult, 
			ModelAndView modelAndView,RedirectAttributes redirectAttributes, Authentication auth) {


		return ofertaControllerProduct.cadastrarOferta(oferta, bindingResult, modelAndView, redirectAttributes, auth);
	}

	@RequestMapping(value = "/{id}/editar", method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView editarOferta(@PathVariable("id") Oferta oferta, Authentication auth, RedirectAttributes redirectAttributes) {
        return ofertaControllerProduct.editarOferta(oferta, auth, redirectAttributes);

	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView editarOferta(@ModelAttribute("oferta") @Valid Oferta oferta, BindingResult bindingResult,
			ModelAndView modelAndView, Authentication auth, RedirectAttributes redirectAttributes) {

		return ofertaControllerProduct.editarOferta(oferta, bindingResult, modelAndView, auth, redirectAttributes);
	}

	@RequestMapping(value = "/{id}/visualizar-oferta/", method = RequestMethod.GET)
	public ModelAndView visualizarOferta(@PathVariable("id") Integer id, @RequestParam(required = false) String erro) {
		return ofertaControllerProduct.visualizarOferta(id, erro);
	}

	@RequestMapping(value = "/{id}/excluir", method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView excluirOferta(@PathVariable(name = "id", required = true) Oferta oferta, Authentication auth,
                                      RedirectAttributes redirectAttributes, ModelAndView modelAndView){

		return ofertaControllerProduct.excluirOferta(oferta, auth, redirectAttributes, modelAndView);
	}

	@RequestMapping(path = { "/{idOferta}/solicitar-compartilhamento" }, method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView solicitarCompartilhamento(@PathVariable("idOferta") Oferta oferta,
			@ModelAttribute("compartilhamento") Compartilhamento compartilhamento, Authentication auth) {

		return ofertaControllerProduct.solicitarCompartilhamento(oferta, compartilhamento, auth);
	}

	@RequestMapping(path = { "/{idOferta}/solicitar-compartilhamento" }, method = RequestMethod.POST)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView solicitarCompartilhamentoCoordenacao(@PathVariable("idOferta") Oferta oferta,
			@ModelAttribute("compartilhamento") @Valid Compartilhamento compartilhamento,

                                                           @RequestParam(value = "curso", required = false) Curso curso,
			BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes,
			Authentication auth){
		return ofertaControllerProduct.solicitarCompartilhamentoCoordenacao(oferta, compartilhamento, curso,
				bindingResult, modelAndView, redirectAttributes, auth);
	}

    @RequestMapping(path = {"/{id}/editar-compartilhamento"}, method = RequestMethod.GET)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
    public ModelAndView editarCompartilhamentoForm(@PathVariable("id") Compartilhamento compartilhamento, Authentication auth, RedirectAttributes redirectAttributes){

        return ofertaControllerProduct.editarCompartilhamentoForm(compartilhamento, auth, redirectAttributes);
    }

	@RequestMapping(path = {"/{idOferta}/solicitar-compartilhamento-direcao"}, method = RequestMethod.POST)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView solicitarCompartilhamentoDirecao(@PathVariable("idOferta") Integer id,
			@ModelAttribute("compartilhamento") @Valid Compartilhamento compartilhamento,
			@RequestParam("turma") @Valid Turma turma,
			BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes,
			Authentication auth){

		return ofertaControllerProduct.solicitarCompartilhamentoDirecao(id, compartilhamento, turma, bindingResult,
				modelAndView, redirectAttributes, auth);
	}

	@RequestMapping(value = { "/importar", "/importar/{idPeriodo}" }, method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	@PreAuthorize("hasAnyAuthority('COORDENACAO')")
	public ModelAndView importarOfertas(@PathVariable(value = "idPeriodo", required = false) Periodo periodo, Authentication auth) {

		return ofertaControllerProduct.importarOfertas(periodo, auth);
	}

	@RequestMapping(value = "/importar", method = RequestMethod.POST)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView importarOfertas(@RequestParam(value = "ofertas", required = false) List<Oferta> ofertas, @RequestParam("periodo") Periodo periodo, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView(Constants.OFERTA_REDIRECT_IMPORTAR + "/" + (periodo == null ? "" : periodo.getId()));
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public ModelAndView importarOfertas(@RequestParam(value = "ofertas", required = false) List<Oferta> ofertas,
			@RequestParam("periodo") Periodo periodo, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView(
				Constants.OFERTA_REDIRECT_IMPORTAR + "/" + (periodo == null ? "" : periodo.getId()));

		ofertaControllerProduct.getOfertaService().importarOfertas(ofertas);
		redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, MSG_IMPORTACAO_REALIZADA);
		return modelAndView;
	}

	@RequestMapping(value = "/importar-ofertas-compartilhadas", method = RequestMethod.GET)
    @RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public @ResponseBody Map<String, Object> importarOfertasCompartilhadas(@RequestParam("compartilhamentos") List<Integer> compartilhamentos, Authentication auth) {
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public @ResponseBody Map<String, Object> importarOfertasCompartilhadas(
			@RequestParam("compartilhamentos") List<Integer> compartilhamentos, Authentication auth) {
		return ofertaControllerProduct.importarOfertasCompartilhadas(compartilhamentos, auth);
	}

	@RequestMapping(value = "/periodo/{idPeriodo}/buscar-ofertas/", method = RequestMethod.GET)
	public ModelAndView buscarOfertas(@PathVariable("idPeriodo") Integer idPeriodo, Authentication auth) {
		return ofertaControllerProduct.buscarOfertas(idPeriodo, auth);
	}

	@RequestMapping(value = "/curso/{idCurso}", method = RequestMethod.GET)
	public @ResponseBody ModelMap listarOfertasPorCurso(@PathVariable("idCurso") Curso curso, Authentication auth) {
		return ofertaControllerProduct.listarOfertasPorCurso(curso, auth);
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public @ResponseBody ModelMap listarOfertas(Authentication auth) {
		return ofertaControllerProduct.listarOfertas(auth);
	}	



	@RequestMapping(value = "/buscar-ofertas/{periodo}", method = RequestMethod.GET)
	public @ResponseBody ModelMap buscarOfertas(@PathVariable("periodo") Periodo periodo, Authentication auth) {
		return ofertaControllerProduct.buscarOfertas(periodo, auth);
	}

	@RequestMapping(value = "/substituicao-ofertas", method = RequestMethod.GET)
	@RestricaoDePeriodo(Constants.OFERTA_REDIRECT_LISTAR)
	public @ResponseBody boolean substituirOfertas(@RequestParam("ofertas") List<Integer> ofertas) {
		ofertaControllerProduct.getOfertaService().substituirOferta(ofertas);
		return true;
	}

	@RequestMapping(value = "/{id}/relacionamentos", method = RequestMethod.GET)
	public @ResponseBody boolean hasRelacionamentos(@PathVariable("id") Oferta oferta) {
		return ofertaControllerProduct.getOfertaService().hasCompartilhamentoOuRestricaoHorario(oferta);
	}

}