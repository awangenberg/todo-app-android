package com.example.finaltest.home.goals;

import java.time.LocalDate;
import java.util.UUID;

public class Goal {

    public Goal(String title, String description, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.date = date;
        this.isDone = false;
    }

    public Goal(String id, String title, String description, LocalDate date, boolean isDone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.isDone = isDone;
    }

    //needed for serialization of firebase documents
    public Goal(){

    }

    private String id;
    private String title ;
    private String description;
    private LocalDate date;
    private boolean isDone;

    public String getId() {
        return id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
