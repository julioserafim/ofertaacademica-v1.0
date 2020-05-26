package ufc.quixada.npi.ap.service;

import ufc.quixada.npi.ap.model.Configuracao;

public interface ConfiguracaoService {

	public Configuracao buscarConfiguracao();
	
	public void salvar(Configuracao configuracao);
	
	public void editar(Configuracao configuracao);
	
}
