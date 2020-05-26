package ufc.quixada.npi.ap.validation;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ufc.quixada.npi.ap.model.Disciplina;
import ufc.quixada.npi.ap.repository.DisciplinaRepository;

@Named
public class DisciplinaValidator implements Validator {
	
	@Autowired
	public DisciplinaRepository disciplinaRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return Disciplina.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object objeto, Errors errors) {
		
		Disciplina disciplina = (Disciplina) objeto;
		
		validateCodigo(errors, disciplina.getCodigo(), disciplina.getId(), "codigo");
		
	}
		
	private void validateCodigo(Errors errors, String codigo, Integer idDisciplina, String campo) {
		
		campo = "codigo";
		
		if (!errors.hasFieldErrors(campo)) {
			if (disciplinaRepository.findDisciplinaByCodigo(codigo) != null) {
				Disciplina aux = disciplinaRepository.findDisciplinaByCodigo(codigo);
				if(idDisciplina == null){
					errors.rejectValue(campo, "disciplinaCodigo");
				}
				else if(idDisciplina != aux.getId()){
					errors.rejectValue(campo, "disciplinaCodigo");
				}
			}
			else if (codigo.isEmpty()) {
				errors.rejectValue(campo, "disciplinaCodigoNull");
			}
		}
		
	}

}