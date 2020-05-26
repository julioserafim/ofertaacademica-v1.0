package ufc.quixada.npi.ap.controller;

import static ufc.quixada.npi.ap.util.Constants.MSG_CONFIGURACOES_ATUALIZADAS;
import static ufc.quixada.npi.ap.util.Constants.SWAL_STATUS_SUCCESS;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.ap.model.Configuracao;
import ufc.quixada.npi.ap.service.ConfiguracaoService;
import ufc.quixada.npi.ap.util.Constants;

@Controller
@RequestMapping(path = "/configuracao")
public class ConfiguracaoController {
	
	
	@Autowired
	private ConfiguracaoService configuracaoService;
	
	
	@RequestMapping(path = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView editarConfiguracao(	@ModelAttribute("configuracao") Configuracao configuracao){
		
		ModelAndView modelAndView = new ModelAndView(Constants.CONFIGURACAO_EDITAR);
		
		configuracao = configuracaoService.buscarConfiguracao();
		
		modelAndView.addObject("configuracao", configuracao);
	
		return modelAndView;
	}
	
	@RequestMapping(path = {"/editar"}, method = RequestMethod.POST)
	public ModelAndView editarConfiguracao(@ModelAttribute("configuracao") @Valid Configuracao configuracao,RedirectAttributes redirectAttributes, BindingResult result,
			ModelAndView modelAndView){

			configuracaoService.editar(configuracao);					
			
			redirectAttributes.addFlashAttribute(SWAL_STATUS_SUCCESS, MSG_CONFIGURACOES_ATUALIZADAS);
			modelAndView.setViewName(Constants.CONFIGURACAO_REDIRECT_EDITAR);	
			
			return modelAndView;
	}
	

}
