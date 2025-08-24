package com.GoDo.todo_bff.service;

import org.springframework.stereotype.Service;

/**
 * TodoBffService, BFF katmanında iş mantığını ve backend servislerine çağrıları yönetir.
 */
@Service
public class TodoBffService {
    /**
     * Örnek fonksiyon: Backend servislerinden yapılacaklar listesini çeker.
     * Gerçek projede burada HTTP çağrıları veya iş mantığı olur.
     */
    public String getTodoList(String userId) {
        // Burada backend API'ye istek atılabilir
        return "Yapılacaklar listesi: " + userId;
    }
}
