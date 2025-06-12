package com.challenge.literalura;

import com.challenge.literalura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	// Ya no necesitas inyectar LibroRepository y AutorRepository aquí para pasarlos a Principal.
	// Principal ahora los inyecta directamente.

	@Autowired // Inyecta la instancia de Principal que Spring ha creado!
	private Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Llamas al metodo en la instancia de Principal que Spring te proporciono!
		principal.muestraElMenu();
	}
}