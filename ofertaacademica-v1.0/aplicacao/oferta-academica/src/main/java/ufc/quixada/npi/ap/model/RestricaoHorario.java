package ufc.quixada.npi.ap.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;

@Entity
public class RestricaoHorario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private Oferta primeiraOferta;
	
	@ManyToOne
	private Oferta segundaOferta;
	
	private boolean habilitada;
	
	@ManyToOne
	@JoinColumn(name = "periodo_id")
	private Periodo periodo;
	
	public enum Tipo{
		EMPILHAMENTO("Empilhamento"),DISTINTO("Distinto");
		
		private String descricao;
		
		Tipo(String descricao){
			this.descricao = descricao;
		}
		
		public String getDescricao(){
			return descricao;
		}
	}
	
	@Enumerated(EnumType.STRING)
	private Tipo tipo;
	

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Oferta getPrimeiraOferta() {
		return primeiraOferta;
	}

	public void setPrimeiraOferta(Oferta primeiraOferta) {
		this.primeiraOferta = primeiraOferta;
	}

	public Oferta getSegundaOferta() {
		return segundaOferta;
	}

	public void setSegundaOferta(Oferta segundaOferta) {
		this.segundaOferta = segundaOferta;
	}

	public boolean isHabilitada() {
		return habilitada;
	}

	public void setHabilitada(boolean habilitada) {
		this.habilitada = habilitada;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public boolean canChange(String cpf) {
		return this.primeiraOferta.getTurma().getCurso().getCoordenador().getCpf().equals(cpf) || this.primeiraOferta.getTurma().getCurso().getViceCoordenador().getCpf().equals(cpf);
	}

}
