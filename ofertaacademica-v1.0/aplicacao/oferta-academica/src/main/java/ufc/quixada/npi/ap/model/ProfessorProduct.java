package ufc.quixada.npi.ap.model;

import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.util.List;

public class ProfessorProduct {
	private List<Papel> papeis;

	public List<Papel> getPapeis() {
		return papeis;
	}

	public void setPapeis(List<Papel> papeis) {
		this.papeis = papeis;
	}

	public boolean isDirecao() {
		for (Papel p : papeis) {
			if (p.getNome() == Papel.Tipo.DIRECAO) {
				return true;
			}
		}
		return false;
	}

	public boolean isCoordenacao() {
		for (Papel p : papeis) {
			if (p.getNome() == Papel.Tipo.COORDENACAO) {
				return true;
			}
		}
		return false;
	}
}