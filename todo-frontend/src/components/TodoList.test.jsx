import React from 'react';
import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import TodoList from './TodoList';

// API fonksiyonlarını mock'la
let todos = [
  { id: 1, title: 'Test Görev', completed: false },
  { id: 2, title: 'Başka Görev', completed: true }
];

vi.mock('../services/api', () => ({
  default: {
    get: () => Promise.resolve({ data: todos }),
    post: (url, body) => {
      const newTodo = { id: todos.length + 1, ...body };
      todos.push(newTodo);
      return Promise.resolve({ data: newTodo });
    },
    delete: (url) => {
      const id = Number(url.split('/').pop());
      todos = todos.filter(t => t.id !== id);
      return Promise.resolve();
    },
    put: (url, body) => {
      const id = Number(url.split('/').pop());
      todos = todos.map(t => t.id === id ? { ...t, ...body } : t);
      return Promise.resolve();
    },
  }
}));

describe('TodoList Bileşeni', () => {
  it('Yapılacaklar listesi render edilir', async () => {
    render(<TodoList />);
    expect(await screen.findByText('Test Görev')).toBeInTheDocument();
    expect(await screen.findByText('Başka Görev')).toBeInTheDocument();
  });

  it('Yeni görev ekleme çalışır', async () => {
  render(<TodoList />);
  const input = screen.getAllByPlaceholderText('Yeni görev ekle...')[0];
  fireEvent.change(input, { target: { value: 'Yeni Görev' } });
  fireEvent.click(screen.getAllByText('Ekle')[0]);
  expect(await screen.findByText('Yeni Görev')).toBeInTheDocument();
  });
});
