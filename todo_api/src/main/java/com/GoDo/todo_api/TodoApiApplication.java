// ToDo API ana uygulama dosyası. Spring Boot uygulamasını başlatır.
package com.GoDo.todo_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApiApplication {
	// Uygulamanın giriş noktası
	public static void main(String[] args) {
		SpringApplication.run(TodoApiApplication.class, args);
	}

}
