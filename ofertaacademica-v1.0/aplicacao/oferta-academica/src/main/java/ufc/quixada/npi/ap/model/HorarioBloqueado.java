package ufc.quixada.npi.ap.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class HorarioBloqueado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "professor_id")
	private Professor professor;
	
	@Enumerated(EnumType.STRING)
	private Dia dia;
	
	@Enumerated(EnumType.STRING)
	private Horario horario;
	
	public enum Dia{
		SEGUNDA("Segunda"), 
		TERCA("Terça"), 
		QUARTA("Quarta"), 
		QUINTA("Quinta"), 
		SEXTA("Sexta");
	
		private String descricao;
		
		private Dia(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao() {
			return this.descricao;
		}
	}
	
	public enum Horario{
		MANHAAB("Manhã AB"), 
		MANHACD("Manhã CD"), 
		TARDEAB("Tarde AB"), 
		TARDECD("Tarde CD"),
		NOITEAB("Noite AB"),
		NOITECD("Noite CD");
	
		private String descricao;
		
		private Horario(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao() {
			return this.descricao;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Dia getDia() {
		return dia;
	}

	public void setDia(Dia dia) {
		this.dia = dia;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}
	
	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime * result + ((horario == null) ? 0 : horario.hashCode());
		result = prime * result + ((professor == null) ? 0 : professor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HorarioBloqueado other = (HorarioBloqueado) obj;
		if (dia != other.dia)
			return false;
		if (horario != other.horario)
			return false;
		if (professor == null) {
			if (other.professor != null)
				return false;
		} else if (!professor.equals(other.professor))
			return false;
		return true;
	}
	
	
}
