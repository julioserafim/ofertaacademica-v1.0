package ufc.quixada.npi.ap.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.repository.ProfessorRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ProfessorRepository professorRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Professor professor = professorRepository.findProfessorByCpf(username);

		if (professor == null) {
			throw new UsernameNotFoundException("Usuário e/ou senha inválidos");
		} else {
			return professor;
		}
	}

}
