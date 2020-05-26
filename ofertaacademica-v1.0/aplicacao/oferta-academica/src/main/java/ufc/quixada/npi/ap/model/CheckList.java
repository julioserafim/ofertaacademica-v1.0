package ufc.quixada.npi.ap.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class CheckList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="id_item")	
	private ItemCheckList itemCheckList;
	
	@ManyToOne
	@JoinColumn(name="id_curso")
	private Curso curso;

	private boolean resposta;
	
	public CheckList(){}
	
	public CheckList(ItemCheckList itemCheckList){
		this.itemCheckList = itemCheckList;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ItemCheckList getItemCheckList() {
		return itemCheckList;
	}

	public void setItemCheckList(ItemCheckList itemCheckList) {
		this.itemCheckList = itemCheckList;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public boolean isResposta() {
		return resposta;
	}

	public void setResposta(boolean resposta) {
		this.resposta = resposta;
	}
	
	

}
