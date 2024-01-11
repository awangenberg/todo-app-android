package com.example.finaltest.home.todos;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finaltest.repositories.Repository;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class TodosViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Todo>> todosLiveData;

    private final ArrayList<Todo> changedStateTodos;

    private final Repository repository;

    public MutableLiveData<ArrayList<Todo>> getTodosLiveData() {
        return todosLiveData;
    }

    public void setTodosLiveData(MutableLiveData<ArrayList<Todo>> todosLiveData) {
        this.todosLiveData = todosLiveData;
    }

    public TodosViewModel(){
        super();
        this.repository = new Repository();
        this.todosLiveData = repository.getAllTodos();
        this.changedStateTodos = new ArrayList<>();
    }

    public void addNewTodo(Todo newTodo){
        repository.addNewTodo(newTodo);

        var updatedTodoList = new ArrayList<>(
                Objects.requireNonNull(todosLiveData.getValue()));
            updatedTodoList.add(newTodo);
            todosLiveData.setValue(updatedTodoList);
    }

    public void setIsDone(Todo todo, boolean newState) {

       var foundTodo = todosLiveData.getValue().stream()
                .filter(t -> t.equals(todo))
                .findFirst().orElse(null);

        Objects.requireNonNull(foundTodo).setDone(newState);
        changedStateTodos.removeIf(t -> t.getId().equals(todo.getId()));
        changedStateTodos.add(foundTodo);
    }

    public ArrayList<Todo> getChangedStateTodos() {
        return changedStateTodos;
    }

    public void updateTodoState() {

        for (var itemToUpdate: changedStateTodos) {
            repository.setTodoState(itemToUpdate.getId(), itemToUpdate.isDone());
        }
    }

    public void deleteTodos(String goalId) {
        var todos = Objects.requireNonNull(todosLiveData.getValue());

        var todosToDelete = Objects.requireNonNull(todosLiveData.getValue())
                .stream().filter(t -> t.getGoalId().equals(goalId)).collect(Collectors.toList());

        for (var todo: todosToDelete) {
            todos.remove(todo);
            repository.deleteTodo(todo.getId());
        }
    }
}