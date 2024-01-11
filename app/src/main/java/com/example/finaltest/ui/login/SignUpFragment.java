package com.example.finaltest.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finaltest.R;
import com.example.finaltest.databinding.FragmentSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference users;

    private Button registerButton;

    private EditText nicknameEditText;
    private EditText emailEditText;
    private EditText passwordEditText1;
    private EditText passwordEditText2;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setActionBarTitle();
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        var fireBaseDatabase = FirebaseDatabase.getInstance();
        users = fireBaseDatabase.getReference("users");


        setupInputFields();


        registerButton.setOnClickListener(v -> {
            if (passwordEditText1.getText().toString().equals(passwordEditText2.getText().toString())) {

                registerUser(
                        nicknameEditText.getText().toString().trim(),
                        emailEditText.getText().toString().trim(),
                        passwordEditText1.getText().toString().trim());
            } else {
                Toast.makeText(getActivity(), "Registration Failed: Password must match!", Toast.LENGTH_LONG).show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void setActionBarTitle() {
        var supportActionBar = ((LoginActivity) requireActivity()).getSupportActionBar();

        if(supportActionBar != null){
            supportActionBar.setTitle(R.string.actionbar_title_signup);
        }
    }



    private void registerUser(String name, String emailAddress, String password) {
        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnSuccessListener(authResult -> {

                    var user = new User(name, emailAddress);

                    users.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                            .setValue(user)
                            .addOnFailureListener(e -> Toast.makeText(getActivity(),
                                    "Registration Failed", Toast.LENGTH_LONG).show());

                    Toast.makeText(getActivity(), "Registration Successful",
                            Toast.LENGTH_LONG).show();

                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_signUpFragment_to_loginFragment);

                }).addOnFailureListener(e ->
                        Toast.makeText(getActivity(), "Registration Failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }

    private final TextWatcher textHaveValueWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(emailEditText.getText()) ||
                    TextUtils.isEmpty(nicknameEditText.getText()) ||
                    TextUtils.isEmpty(passwordEditText1.getText()) ||
                    TextUtils.isEmpty(passwordEditText2.getText())) {
                registerButton.setEnabled(false);
            } else {
                registerButton.setEnabled(true);
            }
        }
    };

    private void setupInputFields() {
        this.emailEditText = binding.emailSignupTextField;
        this.passwordEditText1 = binding.passwordSignupTextField1;
        this.passwordEditText2 = binding.passwordSignupTextField2;
        this.registerButton = binding.registerButton;
        this.nicknameEditText = binding.nickNameSignupTextField;

        emailEditText.addTextChangedListener(textHaveValueWatcher);
        passwordEditText1.addTextChangedListener(textHaveValueWatcher);
        passwordEditText2.addTextChangedListener(textHaveValueWatcher);
        nicknameEditText.addTextChangedListener(textHaveValueWatcher);
    }
}