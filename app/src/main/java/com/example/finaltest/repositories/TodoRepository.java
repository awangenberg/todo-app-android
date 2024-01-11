package com.example.finaltest.repositories;

import androidx.lifecycle.LiveData;

import com.example.finaltest.home.todos.Todo;

import java.util.ArrayList;

public interface TodoRepository {
    void addNewTodo(Todo todo);

    LiveData<ArrayList<Todo>> getAllTodos();

    void setTodoState(String id, boolean state);

    void deleteTodo(String todoId);
}
