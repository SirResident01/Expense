package com.example.expense.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expense.R;
import com.example.expense.model.ExpenseDatabase;
import com.example.expense.model.User;
import com.example.expense.model.UserDao;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterUsername, etRegisterPassword;
    private Button btnRegister;
    private TextView tvAlreadyHaveAccount;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Инициализация элементов интерфейса
        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvAlreadyHaveAccount = findViewById(R.id.tvAlreadyHaveAccount);

        // Инициализация DAO
        userDao = ExpenseDatabase.getDatabase(this).userDao();

        // Обработчик кнопки "Зарегистрироваться"
        btnRegister.setOnClickListener(v -> {
            String username = etRegisterUsername.getText().toString().trim();
            String password = etRegisterPassword.getText().toString().trim();

            // Проверка на заполнение всех полей
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            // Регистрация пользователя
            User newUser = new User(username, password);
            new Thread(() -> {
                // Проверяем, существует ли пользователь
                User existingUser = userDao.getUser(username, password);
                if (existingUser != null) {
                    runOnUiThread(() ->
                            Toast.makeText(RegisterActivity.this, "Пользователь с таким логином уже существует", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    // Добавляем нового пользователя
                    userDao.insertUser(newUser);
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                        // Переход к LoginActivity после успешной регистрации
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    });
                }
            }).start();
        });

        // Обработчик для перехода на экран входа
        tvAlreadyHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}
