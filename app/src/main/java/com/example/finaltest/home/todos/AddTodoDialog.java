package com.example.finaltest.home.todos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finaltest.R;

public class AddTodoDialog extends DialogFragment {

    private Dialog dialog;
    private Button dialogAddButton;
    private Button dialogCancelButton;
    private EditText todoTitle;
    private TodosViewModel todoViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoViewModel = new ViewModelProvider(requireActivity()).get(TodosViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        setupDialogUi();
        dialogAddButton.setOnClickListener(v -> {

            if (TextUtils.isEmpty(todoTitle.getText()) == false) {

                createNewTodo();
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "Please provide a todo title!", Toast.LENGTH_LONG).show();
            }

        });

        dialogCancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        return dialog;
    }

    private void createNewTodo() {

        var bundle = getArguments();
        if (bundle != null) {
            var goalId = bundle.getString("goalId", "");
            var newTodo = new Todo(todoTitle.getText().toString(), goalId);

            todoViewModel.addNewTodo(newTodo);
            Toast.makeText(dialog.getOwnerActivity(), "New todo is added!", Toast.LENGTH_LONG).show();
        }
    }

    private void setupDialogUi() {
        var dialogView =
                getLayoutInflater().
                        inflate(R.layout.dialog_add_new_todo, null);
        var builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        dialog = builder.create();
        dialogAddButton = dialogView.findViewById(R.id.dialogAddButton1);
        dialogCancelButton = dialogView.findViewById(R.id.dialogCancelButton);
        todoTitle = dialogView.findViewById(R.id.addTodoText);
    }
}
