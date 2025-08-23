// Kullanıcı API'sinin ana uygulama dosyası. Spring Boot uygulamasını başlatır.
package com.GoDo.user_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserApiApplication {
	// Uygulamanın giriş noktası
	public static void main(String[] args) {
		SpringApplication.run(UserApiApplication.class, args);
	}

}
