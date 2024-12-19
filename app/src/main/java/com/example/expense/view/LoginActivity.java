package com.example.expense.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expense.MainActivity;
import com.example.expense.R;
import com.example.expense.model.ExpenseDatabase;
import com.example.expense.model.User;
import com.example.expense.model.UserDao;

public class LoginActivity extends AppCompatActivity {
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Инициализация DAO
        userDao = ExpenseDatabase.getDatabase(this).userDao();

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        // Обработчик кнопки входа
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите логин и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                // Проверяем пользователя в базе данных
                User user = userDao.getUser(username, password);
                if (user != null) {
                    // Сохраняем userId текущего пользователя
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userId", user.getUserId());
                    editor.apply();

                    // Переход к MainActivity
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    });
                } else {
                    // Неверный логин или пароль
                    runOnUiThread(() -> Toast.makeText(this, "Неверные логин или пароль", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });

        // Обработчик перехода к экрану регистрации
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
