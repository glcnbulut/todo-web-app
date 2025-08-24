package com.GoDo.todo_bff.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.GoDo.todo_bff.service.TodoBffService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoBffController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class TodoBffControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoBffService todoBffService;

    @Test
    void getAllTodos_returnsOk() throws Exception {
        mockMvc.perform(get("/bff/todos"))
                .andExpect(status().isOk());
    }

    @Test
    void getTodoById_returnsOk() throws Exception {
        mockMvc.perform(get("/bff/todos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createTodo_returnsOk() throws Exception {
        String todoJson = "{\"title\":\"Test\",\"description\":\"Açıklama\"}";
        mockMvc.perform(post("/bff/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoJson))
                .andExpect(status().isOk());
    }

    @Test
    void updateTodo_returnsOk() throws Exception {
        String todoJson = "{\"title\":\"Güncel\",\"description\":\"Açıklama\"}";
        mockMvc.perform(put("/bff/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTodo_returnsOk() throws Exception {
        mockMvc.perform(delete("/bff/todos/1"))
                .andExpect(status().isOk());
    }
}
