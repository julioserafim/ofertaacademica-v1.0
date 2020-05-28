package ufc.quixada.npi.ap.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Email;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ufc.quixada.npi.ap.model.HorarioBloqueado.Dia;
import ufc.quixada.npi.ap.model.HorarioBloqueado.Horario;

@Entity
// @EntityListeners(ProfessorEntityListener.class)
public class Professor implements UserDetails {

	private ProfessorProduct professorProduct = new ProfessorProduct();

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;

	private String apelido;

	@Email(message = "O formato do E-mail não está apropriado")
	private String email;

	private String cpf;

	private String siape;

	private String password;

	@Min(0)
	@Max(20)
	private Integer cargaHorariaMinima;

	@Min(0)
	@Max(20)
	private Integer cargaHorariaMaxima;

	private boolean ativo;

	@JsonIgnore
	@OneToMany(mappedBy = "professor", cascade = CascadeType.MERGE)
	private List<HorarioBloqueado> horariosBloqueados;

	@JsonIgnore
	@OneToOne
	private Professor relacionamento;

	@Transient
	private Integer cargaHorariaAtual;

	@JsonIgnore
	@OneToMany(mappedBy = "professor", cascade = CascadeType.MERGE)
	private List<Preferencia> preferencias;

	public Professor() {
	}

	public Professor(String nome, String email, String cpf, List<Papel> papeis) {
		super();
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
		professorProduct.setPapeis(papeis);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome == null ? nome : nome.toUpperCase();
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getApelido() {
		return apelido == null ? apelido : apelido.toUpperCase();
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

	public Integer getCargaHorariaMinima() {
		return cargaHorariaMinima;
	}

	public void setCargaHorariaMinima(Integer cargaHorariaMinima) {
		this.cargaHorariaMinima = cargaHorariaMinima;
	}

	public Integer getCargaHorariaMaxima() {
		return cargaHorariaMaxima;
	}

	public void setCargaHorariaMaxima(Integer cargaHorariaMaxima) {
		this.cargaHorariaMaxima = cargaHorariaMaxima;
	}

	public Integer getCargaHorariaAtual() {
		return cargaHorariaAtual;
	}

	public void setCargaHorariaAtual(Integer cargaHorariaAtual) {
		this.cargaHorariaAtual = cargaHorariaAtual;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<HorarioBloqueado> getHorariosBloqueados() {
		return horariosBloqueados;
	}

	public void setHorariosBloqueados(List<HorarioBloqueado> horariosBloqueados) {
		this.horariosBloqueados = horariosBloqueados;
	}

	public Professor getRelacionamento() {
		return relacionamento;
	}

	public void setRelacionamento(Professor relacionamento) {
		this.relacionamento = relacionamento;
	}

	public List<Preferencia> getPreferencias() {
		return preferencias;
	}

	public void setPreferencias(List<Preferencia> preferencias) {
		this.preferencias = preferencias;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Professor other = (Professor) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean contemHorarioBloqueado(String dia, String horario) {
		HorarioBloqueado horarioBloqueado = new HorarioBloqueado();
		horarioBloqueado.setDia(Dia.valueOf(dia));
		horarioBloqueado.setHorario(Horario.valueOf(horario));
		horarioBloqueado.setProfessor(this);
		return horariosBloqueados.contains(horarioBloqueado);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.professorProduct.getPapeis();
	}

	@Override
	public String getUsername() {
		return this.cpf;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return ativo;
	}

	public boolean isDirecao() {
		return professorProduct.isDirecao();
	}

	public boolean isCoordenacao() {
		return professorProduct.isCoordenacao();
	}

	public List<Papel> getPapeis() {
		return professorProduct.getPapeis();
	}

	public void setPapeis(List<Papel> papeis) {
		professorProduct.setPapeis(papeis);
	}
}
