package com.shortenUrl.miniUrl;


import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shortenUrl.miniUrl.model.RegisterUser;
import com.shortenUrl.miniUrl.model.Role;
import com.shortenUrl.miniUrl.repository.UserRepository;

@SpringBootApplication
public class ShortenUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortenUrlApplication.class, args);
	}




	@Bean
	public CommandLineRunner createAdminUser(UserRepository repository, PasswordEncoder passwordEncoder) {
	  return (args) -> {
		



		if(repository.findByUserName("Admin") == null)
		{
			RegisterUser user = new RegisterUser();
			user.setUserName("Admin");
			user.setPassword("$2a$10$KlPRGpkuwMVgyHqKByQbTuCA85LqgJYP/mG6I6aRBaUlu9O.FKCOi"); // password is 1
			user.setEnabled(true);
			user.setTokenExpired(false);

			user.setRoles(List.of(new Role("ROLE_ADMIN")));
			repository.save(user);

		}
	  };
	}


}


