package ufc.quixada.npi.ap.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Configuracao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private int laboratorios;	
	private int ofertasProfessor;
	private int permanencia;
	private int pesoPreferencias;
	private int pesoEquilibrio;
	private int pesoPermanencia;
	
	
	public Configuracao(){
		
	}
	
	public Configuracao(int laboratorios, int ofertasProfessor, int permanencia, int pesoPreferencias, int pesoEquilibrio, int pesoPermanencia){
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getLaboratorios() {
		return laboratorios;
	}

	public void setLaboratorios(int laboratorios) {
		this.laboratorios = laboratorios;
	}

	public int getOfertasProfessor() {
		return ofertasProfessor;
	}

	public void setOfertasProfessor(int ofertasProfessor) {
		this.ofertasProfessor = ofertasProfessor;
	}

	public int getPermanencia() {
		return permanencia;
	}

	public void setPermanencia(int permanencia) {
		this.permanencia = permanencia;
	}

	public int getPesoPreferencias() {
		return pesoPreferencias;
	}

	public void setPesoPreferencias(int pesoPreferencias) {
		this.pesoPreferencias = pesoPreferencias;
	}

	public int getPesoEquilibrio() {
		return pesoEquilibrio;
	}

	public void setPesoEquilibrio(int pesoEquilibrio) {
		this.pesoEquilibrio = pesoEquilibrio;
	}

	public int getPesoPermanencia() {
		return pesoPermanencia;
	}

	public void setPesoPermanencia(int pesoPermanencia) {
		this.pesoPermanencia = pesoPermanencia;
	}

	
	
}
