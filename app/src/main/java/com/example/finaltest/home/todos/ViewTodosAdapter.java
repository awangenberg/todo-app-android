package com.example.finaltest.home.todos;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finaltest.R;
import com.example.finaltest.home.goals.Goal;

import java.util.ArrayList;
import java.util.Objects;

public class ViewTodosAdapter extends RecyclerView.Adapter<ViewTodosAdapter.ViewHolder>{

    private final ArrayList<Todo> todos;
    private final ArrayList<Goal> goals;
    private final TodosViewModel todosViewModel;


    public ViewTodosAdapter(ArrayList<Todo> todos, ArrayList<Goal> goals, TodosViewModel todosViewModel){
        this.todos = todos;
        this.goals = goals;
        this.todosViewModel = todosViewModel;
    }

    @NonNull
    @Override
    public ViewTodosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view =
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.todo_item,
                        parent,
                        false);

        return new ViewHolder(view, todos, todosViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTodosAdapter.ViewHolder holder, int position) {
        var todo = todos.get(position);

        Goal goal = null;
        for (var currentGoal: goals) {
                if (Objects.equals(currentGoal.getId(), todo.getGoalId())) {
                    goal = currentGoal;
                    break;
                }
        }

        holder.todoTitle.setText(todo.getTitle());
        holder.goalTitle.setText(
                String.format("Goal: %s", Objects.requireNonNull(goal).getTitle()));
        holder.createdDate.setText(
                String.format("Created: %s", Objects.requireNonNull(todo.getDateCreated())));

        if(goal.isDone()){
            holder.goalTitle.setPaintFlags(holder.goalTitle.getPaintFlags() |
                    Paint.STRIKE_THRU_TEXT_FLAG);
            holder.todoTitle.setPaintFlags(holder.todoTitle.getPaintFlags() |
                    Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.checkbox.setChecked(todo.isDone());
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView todoTitle;
        private final TextView goalTitle;
        private final TextView createdDate;

        private final CheckBox checkbox;
        private final ArrayList<Todo> todos;

        public ViewHolder(View view,
                          ArrayList<Todo> todos, TodosViewModel viewModel) {
            super(view);
            this.todos = todos;
            todoTitle = itemView.findViewById(R.id.todoTitle);
            goalTitle = itemView.findViewById(R.id.goalTitle);
            checkbox = itemView.findViewById(R.id.checkbox);
            createdDate = itemView.findViewById(R.id.createdDate);

            checkbox.setOnClickListener(v -> {
                var position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    var clickedTodo = todos.get(position);

                    viewModel.setIsDone(clickedTodo, checkbox.isChecked());
                }
            });
        }
    }
}
