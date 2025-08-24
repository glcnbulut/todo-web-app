package com.GoDo.todo_bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * TodoBffConfig, BFF katmanında kullanılacak genel bean ve ayarları içerir.
 */
@Configuration
public class TodoBffConfig {
    /**
     * RestTemplate bean'i, BFF'nin diğer backend servislerine HTTP çağrısı yapabilmesi için kullanılır.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
