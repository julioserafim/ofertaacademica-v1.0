package ufc.quixada.npi.ap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ufc.quixada.npi.ap.model.Configuracao;

@Repository
@Transactional
public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Integer>{

	@Query("FROM Configuracao as c WHERE c.id = 1")
	public Configuracao buscarConfiguracao();

}
