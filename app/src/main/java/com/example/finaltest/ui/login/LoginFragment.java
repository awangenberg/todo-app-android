package com.example.finaltest.ui.login;

import android.content.Intent;
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
import com.example.finaltest.databinding.FragmentLoginBinding;
import com.example.finaltest.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;

    private EditText emailTextField;
    private EditText passwordEditText;

    private Button loginButton;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setActionBarTitle();
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        this.loginButton = binding.loginButton;
        var signUpTextView = binding.signUpTextView;

        mAuth = FirebaseAuth.getInstance();

        setupInputFields();
        signUpTextView.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_signUpFragment));

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> loginUser());

        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(
                        emailTextField.getText().toString(),
                        passwordEditText.getText().toString())
                .addOnSuccessListener(authResult -> {
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Login Failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show());
    }

    private void setActionBarTitle() {
        var supportActionBar = ((LoginActivity) requireActivity()).getSupportActionBar();

        if(supportActionBar != null){
            supportActionBar.setTitle(R.string.actionbar_title_login);
        }
    }

    private void setupInputFields() {
        this.emailTextField = binding.emailTextField;
        this.passwordEditText = binding.passwordTextField;
        emailTextField.addTextChangedListener(textHaveValueWatcher);
        passwordEditText.addTextChangedListener(textHaveValueWatcher);
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
            loginButton.setEnabled(!TextUtils.isEmpty(emailTextField.getText()) &&
                    !TextUtils.isEmpty(passwordEditText.getText()));
        }
    };
}