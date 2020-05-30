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

	private ProfessorControllerProduct professorControllerProduct = new ProfessorControllerProduct();

	@Autowired
	private HorarioBloqueadoService horarioBloqueadoService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView listarProfessores() {
		return professorControllerProduct.listarProfessores();
	}

	@RequestMapping(value = "/cadastrar-professor", method = RequestMethod.GET)
	public ModelAndView cadastrarProfessor(@ModelAttribute("professor") Professor professor) {

		return professorControllerProduct.cadastrarProfessor(professor);
	}

	@RequestMapping(value = "/cadastrar-professor", method = RequestMethod.POST)
	public ModelAndView cadastrarProfessor(@ModelAttribute("professor") @Valid Professor professor,
			BindingResult result, RedirectAttributes redirectAttributes, ModelAndView modelAndView)
			throws AlocacaoProfessorException {

		return professorControllerProduct.cadastrarProfessor(professor, result, redirectAttributes, modelAndView);

	}

	@RequestMapping(value = "/editar-professor/{id}", method = RequestMethod.GET)
	public ModelAndView editarProfessor(@PathVariable("id") Integer idProfessor) {
		return professorControllerProduct.editarProfessor(idProfessor);
	}

	@RequestMapping(value = "/editar-professor/", method = RequestMethod.POST)
	public ModelAndView editarProfessor(@ModelAttribute("professor") @Valid Professor professor, BindingResult result,
			RedirectAttributes redirectAttributes) throws AlocacaoProfessorException {

		return professorControllerProduct.editarProfessor(professor, result, redirectAttributes);
	}

	@RequestMapping(value = "/detalhes-professor/{id}", method = RequestMethod.GET)
	public ModelAndView detalhesProfessor(@PathVariable("id") Integer id) {
		return professorControllerProduct.detalhesProfessor(id);
	}

	@RequestMapping(value = "/detalhes-professor/{idProfessor}/cadastrar", method = RequestMethod.POST)
	public ModelAndView cadastrarPreferenciaProfessor(@PathVariable("idProfessor") Professor professor,
			@ModelAttribute("preferencia") Preferencia preferencia, RedirectAttributes redirectAttributes) {

		return professorControllerProduct.cadastrarPreferenciaProfessor(professor, preferencia, redirectAttributes);

	}

	@RequestMapping(value = "detalhes-professor/{id}/excluir/{idp}", method = RequestMethod.GET)
	public ModelAndView excluirPreferenciaProfessor(@PathVariable("id") Integer id, @PathVariable("idp") Integer idp,
			RedirectAttributes redirectAttribute) {
		return professorControllerProduct.excluirPreferenciaProfessor(id, idp, redirectAttribute);
	}

	@RequestMapping(value = "/cadastrar-horario-bloqueado", method = RequestMethod.POST)
	public ModelAndView cadastrarHorarioBloqueado(@ModelAttribute("horarioBloqueado") HorarioBloqueado horarioBloqueado,
			RedirectAttributes redirectAttributes) {

		ModelAndView model = new ModelAndView(
				Constants.PROFESSOR_REDIRECT_DETALHES + "/" + horarioBloqueado.getProfessor().getId());

		try {

			horarioBloqueadoService.salvar(horarioBloqueado);
			redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_HORARIO_BLOQUEADO_CADASTRADO);

		} catch (Exception exception) {

			redirectAttributes.addFlashAttribute(SWAL_STATUS_ERROR, exception.getMessage());
		}

		return model;

	}

	@RequestMapping(value = "/excluir-horario-bloqueado/{id}/{dia}/{horario}", method = RequestMethod.GET)
	public ModelAndView excluirHorarioBloqueado(@PathVariable("id") Professor professor, @PathVariable("dia") Dia dia,
			@PathVariable("horario") Horario horario, RedirectAttributes redirectAttributes) {

		ModelAndView model = new ModelAndView(Constants.PROFESSOR_REDIRECT_DETALHES + "/" + professor.getId());

		HorarioBloqueado horarioBloqueado = horarioBloqueadoService
				.buscarHorarioBloqueadoPorProfessorEDiaEHorario(professor, dia, horario);

		try {

			horarioBloqueadoService.excluir(horarioBloqueado.getId());
			redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, Constants.MSG_HORARIO_BLOQUEADO_EXCLUIDO);

		} catch (Exception exception) {

			redirectAttributes.addFlashAttribute(SWAL_STATUS_ERROR, exception.getMessage());

		}

		return model;
	}
}
