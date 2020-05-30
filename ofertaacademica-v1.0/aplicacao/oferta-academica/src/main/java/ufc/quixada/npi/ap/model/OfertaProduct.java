package ufc.quixada.npi.ap.model;


import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.ArrayList;

public class OfertaProduct {
	private List<Compartilhamento> compartilhamentos;

	public void setCompartilhamentos(List<Compartilhamento> compartilhamentos) {
		this.compartilhamentos = compartilhamentos;
	}

	public List<Compartilhamento> getCompartilhamentos() {
		if (null == this.compartilhamentos) {
			this.compartilhamentos = new ArrayList<>();
		}
		return this.compartilhamentos;
	}

	public Compartilhamento getCompartilhamentoPorCurso(String sigla) {
		for (Compartilhamento compartilhamento : this.compartilhamentos) {
			if (compartilhamento.getTurma().getCurso().getSigla().equals(sigla)) {
				return compartilhamento;
			}
		}
		return null;
	}

	public String getCompartilhamentoIndice(int indice) {
		if (getCompartilhamentos().size() == indice + 1) {
			return "" + getCompartilhamentos().get(indice).getTurma().getCurso().getSigla() + "-"
					+ getCompartilhamentos().get(indice).getTurma().getSemestre().getNumero();
		}
		return "";
	}
}