package com.GoDo.todo_bff.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TodoBffService için birim testler. Servisin temel işlevlerini doğrular.
 */
class TodoBffServiceTest {
    /**
     * Servisin örnek bir fonksiyonu test edilir.
     * Gerçek projede burada iş mantığı testleri yer alır.
     */
    @Test
    void testServiceInstance() {
        TodoBffService service = new TodoBffService();
        assertNotNull(service, "Service instance oluşturulmalı");
    }
}
