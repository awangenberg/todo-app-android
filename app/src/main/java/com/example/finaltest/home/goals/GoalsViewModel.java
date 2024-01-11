package com.example.finaltest.home.goals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finaltest.repositories.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class GoalsViewModel extends ViewModel {

    private int numberOfFinishedGoals = 0;
    private int numberOfUnFinishedGoals = 0;
    private final MutableLiveData<ArrayList<Goal>> goalsLiveData;

    private final Repository repository;

    public void addNewGoal(Goal newGoal) {
        repository.addNewGoal(newGoal);
        var updatedGoalsList = new ArrayList<>(
                Objects.requireNonNull(goalsLiveData.getValue()));
        updatedGoalsList.add(newGoal);
        goalsLiveData.setValue(updatedGoalsList);
    }

    public GoalsViewModel() {
        super();
        repository = new Repository();
        goalsLiveData = repository.getAllGoals();
    }

    public LiveData<ArrayList<Goal>> getGoalsLiveData() {
        return goalsLiveData;
    }

    public int getNumberOfFinishedGoals() {
        return numberOfFinishedGoals;
    }

    public int getNumberOfUnFinishedGoals() {
        return numberOfUnFinishedGoals;
    }

    public void setGoalsState(ArrayList<Goal> goals) {
        this.numberOfFinishedGoals = 0;
        this.numberOfUnFinishedGoals = 0;
        for (var goal : goals) {
            if (goal.isDone()) {
                this.numberOfFinishedGoals++;
            } else {
                this.numberOfUnFinishedGoals++;
            }
        }
    }

    public void setToFinish(String goalId) {
        var goals = Objects.requireNonNull(goalsLiveData.getValue());

        var foundGoal = goals.stream().filter(g -> g.getId().equals(goalId))
                .findFirst().orElse(null);

        Objects.requireNonNull(foundGoal).setDone(true);
        repository.setGoalState(goalId, true);
    }

    public void updateDate(String goalId, LocalDate newDate) {
        var goals = Objects.requireNonNull(goalsLiveData.getValue());
        var foundGoal = goals.stream().filter(g -> g.getId().equals(goalId))
                .findFirst().orElse(null);

        Objects.requireNonNull(foundGoal).setDate(newDate);
        repository.setGoalDate(goalId, newDate);
    }
}