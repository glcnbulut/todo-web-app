import React, { useEffect, useState } from 'react';
import api from '../services/api';

const TodoList = () => {
  const [todos, setTodos] = useState([]);
  const [newTodoTitle, setNewTodoTitle] = useState('');
  const [editingTodo, setEditingTodo] = useState(null);
  const [updatedTitle, setUpdatedTitle] = useState('');
  const [error, setError] = useState('');

  const fetchTodos = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await api.get('/api/todos', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setTodos(response.data);
    } catch (error) {
      console.error("Görevler getirilirken hata:", error);
      if (error.response && error.response.status === 401) {
        setError('Oturum süreniz doldu, lütfen tekrar giriş yapın.');
      } else {
        setError('Görevler yüklenirken bir hata oluştu.');
      }
    }
  };

  useEffect(() => {
    fetchTodos();
  }, []);

  const handleAddTodo = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      await api.post(
        '/api/todos',
        {
          title: newTodoTitle,
          description: '',
          completed: false,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setNewTodoTitle('');
      fetchTodos();
    } catch (error) {
      console.error("Görev eklenirken hata:", error);
      setError('Görev eklenirken bir hata oluştu.');
    }
  };

  const handleDeleteTodo = async (id) => {
    try {
      const token = localStorage.getItem('token');
      await api.delete(`/api/todos/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      fetchTodos();
    } catch (error) {
      console.error("Görev silinirken hata:", error);
      setError('Görev silinirken bir hata oluştu.');
    }
  };

  const handleEditClick = (todo) => {
    setEditingTodo(todo.id);
    setUpdatedTitle(todo.title);
  };

  const handleUpdateTodo = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const todoToUpdate = todos.find(t => t.id === editingTodo);
      if (todoToUpdate) {
        await api.put(`/api/todos/${editingTodo}`, {
          ...todoToUpdate,
          title: updatedTitle
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setEditingTodo(null);
        setUpdatedTitle('');
        fetchTodos();
      }
    } catch (error) {
      console.error("Görev güncellenirken hata:", error);
      setError('Görev güncellenirken bir hata oluştu.');
    }
  };
  
  const handleToggleComplete = async (todo) => {
    try {
        const token = localStorage.getItem('token');
        await api.put(`/api/todos/${todo.id}`, {
            ...todo,
            completed: !todo.completed
        }, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        fetchTodos();
    } catch (error) {
        console.error("Görev durumu güncellenirken hata:", error);
        setError('Görev durumu güncellenirken bir hata oluştu.');
    }
  };


return (
    <div>
      <h2>Yapılacaklar Listesi</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleAddTodo}>
        <input
          type="text"
          value={newTodoTitle}
          onChange={(e) => setNewTodoTitle(e.target.value)}
          placeholder="Yeni görev ekle..."
          required
        />
        <button type="submit">Ekle</button>
      </form>
      <ul>
        {todos.map((todo) => (
          <li key={todo.id}>
            {editingTodo === todo.id ? (
              <form onSubmit={handleUpdateTodo}>
                <input
                  type="text"
                  value={updatedTitle}
                  onChange={(e) => setUpdatedTitle(e.target.value)}
                  required
                />
                <button type="submit">Kaydet</button>
                <button onClick={() => setEditingTodo(null)}>İptal</button>
              </form>
            ) : (
              <>
                <span 
                    style={{ textDecoration: todo.completed ? 'line-through' : 'none', cursor: 'pointer' }}
                    onClick={() => handleToggleComplete(todo)}
                >
                    {todo.title}
                </span>
                <button onClick={() => handleEditClick(todo)}>Düzenle</button>
                <button onClick={() => handleDeleteTodo(todo.id)}>Sil</button>
              </>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TodoList;