package com.GoDo.todo_api.repository;

import com.GoDo.todo_api.model.Todo;
import com.GoDo.todo_api.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TodoRepositoryTests {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setName("Test");
        testUser.setSurname("User");
        testUser.setEmail("testuser@todo.com");
        testUser.setPassword("password");
        testUser.setRole("ROLE_USER");
        testUser = userRepository.save(testUser);
    }

    @Test
    void testCreateTodo() {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        todo.setUser(testUser);
        Todo saved = todoRepository.save(todo);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Todo");
    }

    @Test
    void testReadTodo() {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        todo.setUser(testUser);
        Todo saved = todoRepository.save(todo);

        Optional<Todo> todoOpt = todoRepository.findById(saved.getId());
        assertThat(todoOpt).isPresent();
        assertThat(todoOpt.get().getTitle()).isEqualTo("Test Todo");
    }

    @Test
    void testUpdateTodo() {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        todo.setUser(testUser);
        Todo saved = todoRepository.save(todo);

        saved.setDescription("Updated Description");
        Todo updated = todoRepository.save(saved);
        assertThat(updated.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void testDeleteTodo() {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        todo.setUser(testUser);
        Todo saved = todoRepository.save(todo);

        todoRepository.deleteById(saved.getId());
        Optional<Todo> deleted = todoRepository.findById(saved.getId());
        assertThat(deleted).isNotPresent();
    }
}
