package br.com.sartori.sgrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SgrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgrmApplication.class, args);
	}

}
