package com.example.finaltest.home.goals;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finaltest.R;
import com.example.finaltest.home.todos.TodosViewModel;

import java.time.LocalDate;
import java.util.Calendar;

public class GoalIsDoneDialog extends DialogFragment {

    private Dialog dialog;
    private Button dialogDoneYes;
    private Button dialogDoneNo;
    private TextView dialogTitle;

    private Goal goal;

    private LocalDate newDate;


    public TodosViewModel todoViewModel;
    public GoalsViewModel goalsViewModel;

    private DatePickerDialog datePickerDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.todoViewModel = new ViewModelProvider(requireActivity()).get(TodosViewModel.class);
        this.goalsViewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        setupDialogUi();
        dialogTitle.setText(String.format("Times up for goal: %s", goal.getTitle()));

        dialogDoneYes.setOnClickListener(v -> {
            todoViewModel.deleteTodos(goal.getId());
            goalsViewModel.setToFinish(goal.getId());

            Toast.makeText(
                    requireActivity(),
                    "Good job, the goal is completed!",
                    Toast.LENGTH_LONG).show();

            dialog.dismiss();
        });

        dialogDoneNo.setOnClickListener(v -> {
            showDatePicker();
        });

        return dialog;
    }

    private void showDatePicker() {
        datePickerDialog.show();
        var okButton =
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE);

        var datePicker = datePickerDialog.getDatePicker();
        datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            var currentChosenDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);

            if (currentChosenDate.isBefore(LocalDate.now())) {
                okButton.setEnabled(false);
            }else {
                okButton.setEnabled(true);
                newDate = currentChosenDate;
            }
        });

        okButton.setOnClickListener(v1 -> {
            goalsViewModel.updateDate(goal.getId(), newDate);
            datePickerDialog.dismiss();
            dialog.dismiss();
        });
    }

    public void SetGoal(Goal goal) {
        this.goal = goal;
    }

    private void setupDialogUi() {
        var dialogView =
                getLayoutInflater().
                        inflate(R.layout.dialog_goal_is_done, null);
        var builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        dialog = builder.create();
        dialogDoneYes = dialogView.findViewById(R.id.dialogDoneYes);
        dialogDoneNo = dialogView.findViewById(R.id.dialogDoneNo);
        dialogTitle = dialogView.findViewById(R.id.dialogTitle);

        var newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(requireActivity(), (view, year, monthOfYear, dayOfMonth1) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth1);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

}
