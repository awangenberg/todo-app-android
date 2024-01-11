package com.example.finaltest.repositories;

import androidx.lifecycle.LiveData;
import com.example.finaltest.home.goals.Goal;

import java.time.LocalDate;
import java.util.ArrayList;

public interface GoalRepository {
    void addNewGoal(Goal goal);

    LiveData<ArrayList<Goal>> getAllGoals();

    void setGoalState(String goalId, boolean isDone);

    void setGoalDate(String goalId, LocalDate newDate);
}
