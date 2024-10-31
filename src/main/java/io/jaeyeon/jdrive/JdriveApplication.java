package io.jaeyeon.jdrive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JdriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdriveApplication.class, args);
	}

}
