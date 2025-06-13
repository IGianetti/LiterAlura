package com.challenge.literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner; // Importar CommandLineRunner
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.challenge.literalura.principal.MenuPrincipal;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner { // Implementar CommandLineRunner

	@Autowired
	private MenuPrincipal menuPrincipal; // Inyectar MenuPrincipal

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		menuPrincipal.muestraElMenu(); // Llamar al metodo muestraElMenu()
	}
}