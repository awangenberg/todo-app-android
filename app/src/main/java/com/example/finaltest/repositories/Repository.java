package com.example.finaltest.repositories;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.finaltest.home.goals.Goal;
import com.example.finaltest.home.todos.Todo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Repository implements GoalRepository, TodoRepository {

    final private FirebaseFirestore dataBase;

    private String userId;

    final String TAG = "FirebaseSaver";

    private final MutableLiveData<ArrayList<Goal>> allGoalsLiveData = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<Todo>> allTodosLiveData = new MutableLiveData<>();

    public Repository() {
        this.dataBase = FirebaseFirestore.getInstance();
        var firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.userId = firebaseAuth.getCurrentUser().getUid();
        }
    }

    @Override
    public void addNewGoal(Goal goal) {

        var newGoal = new HashMap<>();
        newGoal.put("goalId", goal.getId());
        newGoal.put("title", goal.getTitle());
        newGoal.put("description", goal.getDescription());
        newGoal.put("date", goal.getDate().toString());
        newGoal.put("isDone", goal.isDone());

        dataBase.collection("users").document(userId)
                .collection("goals").document(goal.getId().toString())
                .set(newGoal)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot goal added with ID: " + userId);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    @Override
    public MutableLiveData<ArrayList<Goal>> getAllGoals() {

        var docRef = dataBase.collection("users").
                document(userId).collection("goals");

        var allGoals = new ArrayList<Goal>();
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                var documents = task.getResult().getDocuments();

                for (var doc : documents) {
                    Log.d(TAG, "fetched goal document with ID: " + doc.getId());

                    var data = doc.getData();
                    if (data != null) {
                        var goal = getGoal(data);

                        allGoals.add(goal);
                    }
                }

                allGoalsLiveData.postValue(allGoals);

            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        return allGoalsLiveData;
    }

    @Override
    public void setGoalState(String goalId, boolean isDone) {
        var docRef = dataBase.collection("users").
                document(userId).collection("goals").document(goalId);

        docRef.update("isDone", isDone).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d(TAG, String.format("setting goal with Id: %s to state: %s", goalId, isDone));
            }
            else {
                Log.d(TAG, "update todo failed with ", task.getException());
            }
        });
    }

    @Override
    public void setGoalDate(String goalId, LocalDate newDate) {
        var docRef = dataBase.collection("users").
                document(userId).collection("goals").document(goalId);

        docRef.update("date", newDate.toString()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d(TAG, String.format("setting goal with Id: %s to date: %s", goalId, newDate.toString()));
            }
            else {
                Log.d(TAG, "update todo failed with ", task.getException());
            }
        });
    }

    @Override
    public void addNewTodo(Todo todo) {
        var newTodo = new HashMap<>();
        newTodo.put("todoId", todo.getId());
        newTodo.put("title", todo.getTitle());
        newTodo.put("isDone", todo.isDone());
        newTodo.put("goalId", todo.getGoalId());
        newTodo.put("dateCreated", todo.getDateCreated().toString());

        dataBase.collection("users").document(userId)
                .collection("todos").document(todo.getId().toString())
                .set(newTodo)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot todo added with ID: " + userId);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    @Override
    public MutableLiveData<ArrayList<Todo>> getAllTodos() {

        var docRef = dataBase.collection("users").
                document(userId).collection("todos");

        var allTodos = new ArrayList<Todo>();
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                var documents = task.getResult().getDocuments();

                for (var doc : documents) {
                    Log.d(TAG, "fetched todo document with ID: " + doc.getId());

                    var data = doc.getData();
                    if (data != null) {
                        var todo = getTodo(data);

                        allTodos.add(todo);
                    }
                }

                allTodosLiveData.postValue(allTodos);

            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        return allTodosLiveData;
    }

    @Override
    public void deleteTodo(String todoId) {
        var docRef = dataBase.collection("users").
                document(userId).collection("todos").document(todoId);

        docRef.delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d(TAG, String.format("Removed TODO with Id: %s", todoId));
            }
            else {
                Log.d(TAG, "update todo failed with ", task.getException());
            }
        });
    }

    @Override
    public void setTodoState(String id, boolean state) {
        var docRef = dataBase.collection("users").
                document(userId).collection("todos").document(id);

        docRef.update("isDone", state).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d(TAG, String.format("setting TODO with Id: %s to state: %s", id, state));
            }
            else {
                Log.d(TAG, "update todo failed with ", task.getException());

            }
        });
    }

    @NonNull
    private static Goal getGoal(Map<String, Object> data) {
        var goalId = Objects.requireNonNull(data.get("goalId")).toString();
        var title = Objects.requireNonNull(data.get("title")).toString();
        var description = Objects.requireNonNull(data.get("description")).toString();
        var date = LocalDate.parse(Objects.requireNonNull(data.get("date")).toString(), DateTimeFormatter.ISO_LOCAL_DATE);
        var isDone = (Boolean) Objects.requireNonNull(data.get("isDone"));

        return new Goal(goalId, title, description, date, isDone);
    }

    @NonNull
    private static Todo getTodo(Map<String, Object> data) {
        var todoId = Objects.requireNonNull(data.get("todoId")).toString();
        var title = Objects.requireNonNull(data.get("title")).toString();
        var isDone = (Boolean) Objects.requireNonNull(data.get("isDone"));
        var goalId = Objects.requireNonNull(data.get("goalId")).toString();
        var dateCreated = Objects.requireNonNull(data.get("dateCreated")).toString();

        return new Todo(todoId, title, isDone, goalId, LocalDate.parse(dateCreated));
    }
}
