package ufc.quixada.npi.ap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.model.Configuracao;
import ufc.quixada.npi.ap.repository.ConfiguracaoRepository;
import ufc.quixada.npi.ap.service.ConfiguracaoService;

@Service
public class ConfiguracaoServiceImpl implements ConfiguracaoService{

	@Autowired
	private ConfiguracaoRepository configuracaoRepository;
	
	public void salvar(Configuracao configuracao){
		configuracaoRepository.save(configuracao);
	}
	
	public void editar(Configuracao configuracao) {
		configuracao.setId(1);
		configuracaoRepository.save(configuracao);
	}
	
	public Configuracao buscarConfiguracao(){
		return configuracaoRepository.buscarConfiguracao();
	}
	
	
}
