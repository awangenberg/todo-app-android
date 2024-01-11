package com.example.finaltest.home.goals;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finaltest.R;
import com.example.finaltest.databinding.FragmentGoalsBinding;
import com.example.finaltest.home.HomeActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GoalsFragment extends Fragment {

    private GoalsViewModel viewModel;

    public static final String TAG = "GoalsFragment";

    private RecyclerView recyclerView;

    private FragmentGoalsBinding binding;

    private GoalIsDoneDialog goalsIsDoneDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentGoalsBinding.inflate(inflater, container, false);

        recyclerView = binding.recyclerView;
        viewGoals();

        var addGoalsButton = binding.addGoalButton;

        var addGoalsDialog = new AddGoalsDialog(requireActivity(), viewModel);
        addGoalsButton.setOnClickListener(v -> {
            addGoalsDialog.ShowDialog();
        });

        return binding.getRoot();
    }

    private void viewGoals() {
        var numberOfGoalsText = binding.numberOfGoalsText;

        viewModel.getGoalsLiveData().observe(getViewLifecycleOwner(), result -> {

            checkIfGoalIsDone(result);

            result.sort(
                    Comparator.comparing((Goal goal) -> goal.isDone() == false)
                            .thenComparing(Goal::getDate)
            );
            Collections.reverse(result);
            var adapter = new ViewGoalsAdapter(result, getChildFragmentManager());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(
                    recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL));

            recyclerView.setAdapter(adapter);
            viewModel.setGoalsState(result);

            var nrOfGoalsText = getString(R.string.nr_of_Goals,
                    viewModel.getNumberOfFinishedGoals(), viewModel.getNumberOfUnFinishedGoals());

            numberOfGoalsText.setText(nrOfGoalsText);
        });
    }

    private void checkIfGoalIsDone(ArrayList<Goal> goals) {
        for (var goal : goals) {
            if (goal.isDone() == false &&
                    goal.getDate().isBefore(LocalDate.now())) {

                goalsIsDoneDialog = new GoalIsDoneDialog();
                goalsIsDoneDialog.SetGoal(goal);
                goalsIsDoneDialog.show(getChildFragmentManager(),
                        "GoalIsDoneDialog");
            }
        }

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}