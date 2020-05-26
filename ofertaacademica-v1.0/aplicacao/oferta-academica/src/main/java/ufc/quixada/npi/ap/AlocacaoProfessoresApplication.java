package ufc.quixada.npi.ap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "br.ufc.quixada.npi.ldap", "ufc.quixada.npi.ap" })
public class AlocacaoProfessoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlocacaoProfessoresApplication.class, args);
	}
	
}
