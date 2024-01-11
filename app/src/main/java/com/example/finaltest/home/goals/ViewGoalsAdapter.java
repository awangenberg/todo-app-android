package com.example.finaltest.home.goals;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finaltest.R;
import com.example.finaltest.home.todos.AddTodoDialog;

import java.util.ArrayList;

public class ViewGoalsAdapter extends RecyclerView.Adapter<ViewGoalsAdapter.ViewHolder> {


    private final ArrayList<Goal> goals;
    private final FragmentManager fragmentManager;

    public ViewGoalsAdapter(ArrayList<Goal> goals, FragmentManager fragmentManager) {
        this.goals = goals;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public ViewGoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view =
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.goal_item,
                        parent,
                        false);

        return new ViewHolder(view, fragmentManager ,goals);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewGoalsAdapter.ViewHolder holder, int position) {
        var goal = goals.get(position);
        holder.titleTextView.setText(goal.getTitle());
        holder.descriptionTextView.setText(goal.getDescription());
        holder.dateTextView.setText(goal.getDate().toString());

        if(goal.isDone()){
            holder.addTodoButton.setVisibility(View.GONE);
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() |
                    Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView dateTextView;
        private final Button addTodoButton;

        public ViewHolder(View view,
                          FragmentManager fragmentManager,
                          ArrayList<Goal> goals) {
            super(view);
            titleTextView = itemView.findViewById(R.id.goalTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.goalDescriptionTextView);
            dateTextView = itemView.findViewById(R.id.goalDateTextView);
            addTodoButton = itemView.findViewById(R.id.addTodoButton);

            var addTodoDialog = new AddTodoDialog();

            addTodoButton.setOnClickListener(v -> {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    var clickedGoal = goals.get(position);

                    var bundle = new Bundle();
                    bundle.putString("goalId", clickedGoal.getId());

                    addTodoDialog.setArguments(bundle);
                    addTodoDialog.show(fragmentManager, "AddTodoDialog");
                }
            });
        }
    }
}
