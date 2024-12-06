package com.unaskividicomprayventadeautos.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.unaskividicomprayventadeautos.MainActivity;
import com.unaskividicomprayventadeautos.R;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        // Agregar usuario por defecto (kevin/1234)
        SharedPreferences prefs = getSharedPreferences("Users", MODE_PRIVATE);
        if (!prefs.contains("kevin")) {
            prefs.edit().putString("kevin", "1234").apply();
        }

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            
            if (validateLogin(username, password)) {
                // Guardar estado de login
                getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("currentUser", username)
                    .apply();

                // Ir al MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", 
                    Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> mostrarDialogoRegistro());
    }

    private boolean validateLogin(String username, String password) {
        SharedPreferences prefs = getSharedPreferences("Users", MODE_PRIVATE);
        String storedPassword = prefs.getString(username, null);
        return storedPassword != null && storedPassword.equals(password);
    }

    private void mostrarDialogoRegistro() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_register, null);
        EditText newUsername = dialogView.findViewById(R.id.newUsername);
        EditText newPassword = dialogView.findViewById(R.id.newPassword);

        new AlertDialog.Builder(this)
            .setTitle("Registro de nuevo usuario")
            .setView(dialogView)
            .setPositiveButton("Registrar", (dialog, which) -> {
                String username = newUsername.getText().toString();
                String password = newPassword.getText().toString();
                
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Por favor complete todos los campos", 
                        Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("Users", MODE_PRIVATE);
                if (prefs.contains(username)) {
                    Toast.makeText(this, "El usuario ya existe", 
                        Toast.LENGTH_SHORT).show();
                    return;
                }

                // Guardar nuevo usuario
                prefs.edit().putString(username, password).apply();
                Toast.makeText(this, "Usuario registrado exitosamente", 
                    Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}