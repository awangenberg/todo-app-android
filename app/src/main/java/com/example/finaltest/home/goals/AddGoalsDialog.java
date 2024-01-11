package com.example.finaltest.home.goals;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finaltest.R;

import java.time.LocalDate;
import java.util.Calendar;

public class AddGoalsDialog {

    private final Activity activity;
    private EditText addGoalTitle;
    private EditText addGoalDescription;
    private Button datePickerButton;
    private Button dialogAddButton;
    private Button dialogCancelButton;
    private DatePickerDialog datePickerDialog;

    private final Dialog dialog;
    private final GoalsViewModel viewModel;


    public AddGoalsDialog(Activity activity, GoalsViewModel viewModel) {
        this.activity = activity;
        this.dialog = new Dialog(activity);
        this.viewModel = viewModel;

        setupUiComponents(dialog);
    }

    public void ShowDialog() {
        dialog.setTitle("Add new goal");

        datePickerButton.setOnClickListener(v -> {
            datePickerDialog.show();
        });

        dialogAddButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(addGoalTitle.getText()) ||
                    TextUtils.isEmpty(addGoalDescription.getText())) {
                Toast.makeText(activity, "Please provide title and description!", Toast.LENGTH_LONG).show();
                return;
            }
            var datePicker = datePickerDialog.getDatePicker();
            var addGoalDate = LocalDate.of(datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth());

            if(addGoalDate.isEqual(LocalDate.now()) || addGoalDate.isBefore(LocalDate.now())){
                Toast.makeText(activity, "Date needs to be in the future!", Toast.LENGTH_LONG).show();
                return;
            }

            var newGoal = new Goal(
                    addGoalTitle.getText().toString(),
                    addGoalDescription.getText().toString(),
                    addGoalDate);

            viewModel.addNewGoal(newGoal);

            dialog.dismiss();
        });

        dialogCancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setupUiComponents(Dialog dialog) {
        dialog.setContentView(R.layout.dialog_add_new_goal);

        addGoalTitle = (EditText) dialog.findViewById(R.id.addGoalTitle);
        addGoalDescription = (EditText) dialog.findViewById(R.id.addGoalDescription);
        datePickerButton = (Button) dialog.findViewById(R.id.datePickerButton);

        dialogAddButton = dialog.findViewById(R.id.dialogAddButton);
        dialogCancelButton = dialog.findViewById(R.id.dialogCancelButton);

        var newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(activity, (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth1) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth1);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


}
