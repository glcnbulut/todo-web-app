package com.GoDo.todo_bff.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TodoBffController, frontend'den gelen istekleri karşılar ve ilgili backend servislerine yönlendirir.
 */
@RestController
public class TodoBffController {
    /**
     * Sağlık kontrolü için basit bir endpoint.
     * Frontend, BFF'nin çalışıp çalışmadığını bu endpoint ile kontrol edebilir.
     */
    @GetMapping("/bff/health")
    public String healthCheck() {
        return "Todo BFF çalışıyor";
    }
}
