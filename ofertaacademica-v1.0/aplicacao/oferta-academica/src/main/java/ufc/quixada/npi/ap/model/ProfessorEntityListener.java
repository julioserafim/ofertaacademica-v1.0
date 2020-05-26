package ufc.quixada.npi.ap.model;

import javax.persistence.PostLoad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import br.ufc.quixada.npi.ldap.model.Usuario;
import br.ufc.quixada.npi.ldap.service.UsuarioService;

@Component
public class ProfessorEntityListener implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostLoad
	public void loadPessoa(Professor professor) {
		context.getAutowireCapableBeanFactory().autowireBean(this);
		Usuario usuario = usuarioService.getByCpf(professor.getCpf());
		professor.setNome(usuario.getNome());
		professor.setEmail(usuario.getEmail());
		professor.setSiape(usuario.getSiape());
	}
	
	public ApplicationContext getApplicationContext() {
        return context;
    }
 
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }

}