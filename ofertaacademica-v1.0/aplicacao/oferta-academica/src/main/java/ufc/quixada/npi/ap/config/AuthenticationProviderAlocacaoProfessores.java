package ufc.quixada.npi.ap.config;

import java.util.Collection;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import br.ufc.quixada.npi.ldap.service.UsuarioService;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.repository.ProfessorRepository;
import ufc.quixada.npi.ap.util.Constants;

@Named
public class AuthenticationProviderAlocacaoProfessores implements AuthenticationProvider {
	
	@Autowired
	private ProfessorRepository professorRepository;
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	@Transactional
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String cpf = authentication.getName();
        String password = (String) authentication.getCredentials();
        
        Professor professor = professorRepository.findProfessorByCpf(cpf);
        
        if(professor == null) {
        	throw new BadCredentialsException(Constants.MSG_LOGIN_INVALIDO);
        }
        
        Collection<? extends GrantedAuthority> authorities = professor.getAuthorities();
        
        if (!usuarioService.autentica(cpf, password) || authorities == null || authorities.isEmpty()) {
            throw new BadCredentialsException(Constants.MSG_LOGIN_INVALIDO);
        }
        
        return new UsernamePasswordAuthenticationToken(professor, password, authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
