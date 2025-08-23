import React, { useState, useEffect } from 'react';
import axios from 'axios';

const TodoList = () => {
  const [todos, setTodos] = useState([]);

  useEffect(() => {
    const fetchTodos = async () => {
      try {
        const response = await axios.get('http://localhost:8081/api/todos');
        setTodos(response.data);
      } catch (error) {
        console.error('To-Do listesi alınırken hata oluştu:', error);
      }
    };
    fetchTodos();
  }, []);

  return (
    <div>
      <h1>To-Do Listesi</h1>
      <ul>
        {todos.map(todo => (
          <li key={todo.id}>
            {todo.title}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TodoList;