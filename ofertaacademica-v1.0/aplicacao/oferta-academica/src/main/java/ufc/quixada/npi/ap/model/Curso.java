package ufc.quixada.npi.ap.model;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Curso {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nome;
	
	private String codigo;

	
	@OneToOne
	private Professor coordenador;
	
	@Enumerated(EnumType.STRING)
	private Turno turno;
	
	private Integer semestres;
	
	@OneToOne
	private Professor viceCoordenador;
	
	private String sigla;
	
	
	public enum Turno{
		MANHA("Manhã"), TARDE("Tarde"), NOITE("Noite");
		
		private String descricao;
		
		Turno(String descricao){
			this.descricao = descricao;
		}
		
		public String getDescricao(){
			return descricao;
		}
		
	}
	
	@OneToMany(mappedBy = "curso")
	@JsonIgnore
	private List<Turma> turmas;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Professor getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(Professor coordenador) {
		this.coordenador = coordenador;
	}

	public Professor getViceCoordenador() {
		return viceCoordenador;
	}

	public void setViceCoordenador(Professor viceCoordenadorVice) {
		this.viceCoordenador = viceCoordenadorVice;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public Integer getSemestres() {
		return semestres;
	}

	public void setSemestres(Integer semestres) {
		this.semestres = semestres;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Curso)) {
            return false;
        }
        final Curso other = (Curso) obj;
         return this.getCodigo().equals(other.getCodigo());
    }

	public boolean contains(Curso curso) {
		return false;
	}
			
}
