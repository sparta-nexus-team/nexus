package com.sparta.nexusteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NexusTeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexusTeamApplication.class, args);
	}

}
