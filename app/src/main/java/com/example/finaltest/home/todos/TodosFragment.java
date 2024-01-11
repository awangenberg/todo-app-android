package com.example.finaltest.home.todos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finaltest.databinding.FragmentTodosBinding;
import com.example.finaltest.home.goals.GoalsViewModel;

import java.util.Comparator;

public class TodosFragment extends Fragment {

    public static final String TAG = "TodosFragment";
    private FragmentTodosBinding binding;

    private RecyclerView recyclerView;

    private TodosViewModel todosViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTodosBinding.inflate(inflater, container, false);

        todosViewModel =
                new ViewModelProvider(requireActivity()).get(TodosViewModel.class);

        var goalsViewModel =
                new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);

        recyclerView = binding.recyclerView;

        viewTodos(goalsViewModel);

        return binding.getRoot();
    }

    private void viewTodos(GoalsViewModel goalsViewModel) {
        todosViewModel.getTodosLiveData().observe(getViewLifecycleOwner(), result -> {
            result.sort(Comparator.comparing(Todo::getDateCreated));

            var adapter = new ViewTodosAdapter(
                    result,
                    goalsViewModel.getGoalsLiveData().getValue(),
                    todosViewModel);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(
                    recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL));

            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        todosViewModel.updateTodoState();
    }
}