package ufc.quixada.npi.ap.validation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ufc.quixada.npi.ap.model.Curso;
import ufc.quixada.npi.ap.model.Professor;
import ufc.quixada.npi.ap.repository.CursoRepository;



@Component
public class CursoValidator implements Validator {
	
	@Autowired
	public CursoRepository cursoRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Curso.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object objeto, Errors errors) {
		
		Curso curso = (Curso) objeto;
		
		validateCursoCodigo(errors, curso.getCodigo(), curso.getId(), "codigo");
		validateCursoNome(errors, curso.getNome(), curso.getId(), "nome");
		validateCursoSigla(errors, curso.getSigla(), curso.getId(), "sigla");
		validateCoordenador(errors, curso.getCoordenador(), curso.getId(), "professor");
		validateViceCoordenador(errors, curso.getViceCoordenador(), curso.getId(), "professor");
	}
	
	private void validateCursoCodigo(Errors errors, String codigo, Integer idCurso, String campo) {

		campo = "codigo";

		if (!errors.hasFieldErrors(campo)) {
			if (cursoRepository.findCursoByCodigo(codigo) != null) {
				Curso aux = cursoRepository.findCursoByCodigo(codigo);
				if(idCurso == null){
					errors.rejectValue(campo, "cursoCodigo");
				}
				else if(idCurso != aux.getId()){
					errors.rejectValue(campo, "cursoCodigo");
				}
			}   
			else if (codigo.isEmpty()) {
				errors.rejectValue(campo, "cursoCodigoNull");
			}
		}
	}
	
	private void validateCursoNome(Errors errors, String nome, Integer idCurso, String campo) {
		
		campo = "nome";
		
			if (!errors.hasFieldErrors(campo)){
				if(cursoRepository.findCursoByNome(nome) != null){
					 Curso aux = cursoRepository.findCursoByNome(nome);
					 if(idCurso == null){
						  errors.rejectValue(campo, "nomeCurso");  
					 }
					 else if(idCurso != aux.getId()){
						  errors.rejectValue(campo, "nomeCurso");  
					 }
				}
				else if(nome.isEmpty()){
					errors.rejectValue(campo, "cursoNomeNull");
				}
			}
	}

	private void validateViceCoordenador(Errors errors, Professor viceCoordenador, Integer idCurso, String campo) {
		
		campo = "viceCoordenador";
		
		if (!errors.hasFieldErrors(campo)){
			if(cursoRepository.findByViceCoordenador(viceCoordenador) != null){
				Curso aux = cursoRepository.findByViceCoordenador(viceCoordenador); 
				if(idCurso == null){
					 errors.rejectValue(campo, "viceCoordenadorCurso");
				 }
				 else if(idCurso != aux.getId()){
					 errors.rejectValue(campo, "viceCoordenadorCurso");
				 }
			}
			else if(cursoRepository.findByCoordenador(viceCoordenador) != null){
				errors.rejectValue(campo, "viceCoordenadorCoordenador");
			}
			else if(viceCoordenador == null){
				errors.rejectValue(campo, "cursoViceCoordenadorNull");
			}
		}
   }


	private void validateCoordenador(Errors errors, Professor coordenador, Integer idCurso, String campo) {
		
		campo = "coordenador";
		
		if (!errors.hasFieldErrors(campo)){
			if(cursoRepository.findByCoordenador(coordenador) != null){
			   Curso aux = cursoRepository.findByCoordenador(coordenador);
			   if(idCurso == null){
				   errors.rejectValue(campo, "coordenadorCurso");
			   }
			   else if(idCurso != aux.getId()){
				   errors.rejectValue(campo, "coordenadorCurso");
			   }  
			}
			else if(coordenador == null){
				errors.rejectValue(campo, "cursoCoordenadorNull");	
			}
			else if(cursoRepository.findByViceCoordenador(coordenador) != null){
				errors.rejectValue(campo, "coordenadorViceCoordenador");
			}
		}
	}

	private void validateCursoSigla(Errors errors, String sigla, Integer idCurso, String campo) {
		
		campo = "sigla";
		
		if (!errors.hasFieldErrors(campo)){
			if(cursoRepository.findBySigla(sigla) != null){
				  Curso aux = cursoRepository.findBySigla(sigla);
				  if(idCurso == null){
					  errors.rejectValue(campo, "siglaCurso");
				  }
				  else if(idCurso != aux.getId()){
					  errors.rejectValue(campo, "siglaCurso");
				  }
			}
			else if(sigla.isEmpty()){
				errors.rejectValue(campo, "cursoSiglaNull");
			}
		}	
	}
}
	
	
	
	


