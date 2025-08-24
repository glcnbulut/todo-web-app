package com.GoDo.todo_bff.dto;

/**
 * TodoDto, frontend ile BFF arasında yapılacaklar bilgisini taşımak için kullanılır.
 */
public class TodoDto {
    /** Yapılacak işin benzersiz ID'si */
    private String todoId;
    /** Yapılacak işin başlığı */
    private String title;
    /** Yapılacak işin açıklaması */
    private String description;

    // Getter ve setter metotları
    public String getTodoId() { return todoId; }
    public void setTodoId(String todoId) { this.todoId = todoId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
