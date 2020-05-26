package ufc.quixada.npi.ap.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.repository.ProfessorRepository;

@Component
public class ProfessorValidator implements Validator {
	
	@Autowired
	public ProfessorRepository professorRepository;
	private Integer cargaHorariaMinima2;
	private Integer cargaHorariaMaxima2;
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Professor.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object objeto, Errors errors) {
		
		Professor professor = (Professor) objeto;
		
		validateProfessorCPF(errors, professor.getCpf(), professor.getId(), "cpf");
		validateProfessorApelido(errors, professor.getApelido(), professor.getId(), "apelido");
		validateProfessorCargaMinima(errors, professor.getCargaHorariaMinima(), professor.getCargaHorariaMaxima(), "cargaHorariaMinima");
		validateProfessorCargaMaxima(errors, professor.getCargaHorariaMaxima(), professor.getCargaHorariaMinima(), "cargaHorariaMaxima");
	}

	private void validateProfessorApelido(Errors errors, String apelido, Integer idProfessor, String campo) {
		
		campo = "apelido";
		
		if (!errors.hasFieldErrors(campo)) {
			if(professorRepository.findProfessorByApelido(apelido) != null){
				Professor aux = professorRepository.findProfessorByApelido(apelido);
				if(idProfessor == null){
					errors.rejectValue(campo, "professorApelido");
				}
				else if(idProfessor != aux.getId()){
					errors.rejectValue(campo, "professorApelido");
				}
			}
			else if(apelido.isEmpty()){
				errors.rejectValue(campo, "professorNullApelido");
			}
			else if(apelido.length() > 20 || apelido.length() < 2){
				errors.rejectValue(campo, "professorApelidoQtd");
			}
		}
	}

	private void validateProfessorCPF(Errors errors, String cpf, Integer idProfessor, String campo) {
		
		campo = "cpf";
		
		if (!errors.hasFieldErrors(campo)) {
			if (professorRepository.findProfessorByCpf(cpf) != null){
				Professor aux = professorRepository.findProfessorByCpf(cpf);
				if(idProfessor == null){
					errors.rejectValue(campo, "professorCpf");
				}
				else if(idProfessor != aux.getId()){
					errors.rejectValue(campo, "professorCpf");
				}
			}
			else if (cpf.isEmpty()) {
				errors.rejectValue(campo, "professorNullCpf");
			}
			else if (cpf.length() != 11) {
				errors.rejectValue(campo, "professorCpfQtd");
			}
		}
		
	}
	
	
	private void validateProfessorCargaMinima(Errors errors, Integer cargaHorariaMinima, Integer cargaHorariaMaxima, String campo) {
		 
		campo = "cargaHorariaMinima";
		
		if (!errors.hasFieldErrors(campo)){
			cargaHorariaMinima2 = cargaHorariaMinima;
			if(cargaHorariaMinima2 == null){
				errors.rejectValue(campo, "professorCargaMinimaNull");
			}
			else if(cargaHorariaMinima2 > cargaHorariaMaxima){
				errors.rejectValue(campo, "professorCargaMinimaMaxima");
			}
		}
	}
	
	private void validateProfessorCargaMaxima(Errors errors, Integer cargaHorariaMaxima, Integer cargaHorariaMinima, String campo) {
		
		campo = "cargaHorariaMaxima";
		
		if (!errors.hasFieldErrors(campo)){
			cargaHorariaMaxima2 = cargaHorariaMaxima;
			if(cargaHorariaMaxima2 == null){
				errors.rejectValue(campo, "professorCargaMaximaNull");
			}
			else if(cargaHorariaMaxima2 < cargaHorariaMinima){
				errors.rejectValue(campo, "professorCargaMaximaMinima");
			}
		}
		
	}

}
