package com.example.finaltest.home.todos;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Todo {

    private String id;

    private String title;
    private String goalId;

    private LocalDate dateCreated;

    private boolean isDone;

    public Todo(String title, String goalId){
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.goalId = goalId;
        this.dateCreated = LocalDate.now();
        isDone = false;
    }

    //needed for serialization of firebase documents
    public Todo(){

    }

    public Todo(String todoId, String title, Boolean isDone, String goalId, LocalDate dateCreated) {
        this.id = todoId;
        this.title = title;
        this.isDone = isDone;
        this.goalId = goalId;
        this.dateCreated = dateCreated;
    }


    public String getId() {
        return id;
    }

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return isDone == todo.isDone && Objects.equals(id, todo.id) && Objects.equals(title, todo.title) && Objects.equals(goalId, todo.goalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, goalId, isDone);
    }
}
