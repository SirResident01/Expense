package com.example.expense;

import com.example.expense.viewmodel.ExpenseViewModel;
import com.example.expense.view.ExpenseAdapter;
import com.example.expense.view.ExpenseChartActivity;
import com.example.expense.model.Expense;
import com.example.expense.view.ExpenseAnalyticsActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ExpenseViewModel expenseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ExpenseAdapter adapter = new ExpenseAdapter(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Expense expense) {
                openEditExpenseDialog(expense);
            }

            @Override
            public void onDeleteClick(Expense expense) {
                // Подтверждение удаления
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Подтверждение удаления")
                        .setMessage("Вы уверены, что хотите удалить?")
                        .setPositiveButton("Да", (dialog, which) -> {
                            expenseViewModel.delete(expense);
                            Toast.makeText(MainActivity.this, "Expense deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Получение userId текущего пользователя
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("userId", -1);

        // Получение ViewModel и наблюдение за изменениями расходов только для текущего пользователя
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getExpensesForUser(currentUserId).observe(this, adapter::setExpenses);

        // Кнопка для добавления нового расхода
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> openAddExpenseDialog(currentUserId));

        // Кнопка для показа диаграммы
        FloatingActionButton fabShowChart = findViewById(R.id.fabShowChart);
        fabShowChart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExpenseChartActivity.class);
            startActivity(intent);
        });

        // Кнопка для перехода к аналитике
        FloatingActionButton fabAnalytics = findViewById(R.id.fabAnalytics);
        fabAnalytics.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExpenseAnalyticsActivity.class);
            startActivity(intent);
        });
    }

    // Метод для открытия диалога добавления расхода
    private void openAddExpenseDialog(int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_expense_dialog, null);
        builder.setView(dialogView);

        EditText etEditCategory = dialogView.findViewById(R.id.etEditCategory);
        EditText etEditAmount = dialogView.findViewById(R.id.etEditAmount);
        EditText etDate = dialogView.findViewById(R.id.etDate);

        // Установка текущей даты по умолчанию
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etDate.setText(dateFormat.format(calendar.getTime()));

        // Обработка выбора даты
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        builder.setTitle("Add Expense");
        builder.setPositiveButton("Save", (dialog, which) -> {
            String category = etEditCategory.getText().toString();
            String amountStr = etEditAmount.getText().toString();
            String date = etDate.getText().toString();

            if (category.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            Expense expense = new Expense(userId, category, amount, date, System.currentTimeMillis());
            expenseViewModel.insert(expense);
            Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    // Метод для открытия диалога редактирования расхода
    private void openEditExpenseDialog(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_expense_dialog, null);
        builder.setView(dialogView);

        EditText etEditCategory = dialogView.findViewById(R.id.etEditCategory);
        EditText etEditAmount = dialogView.findViewById(R.id.etEditAmount);
        EditText etDate = dialogView.findViewById(R.id.etDate);

        etEditCategory.setText(expense.getCategory());
        etEditAmount.setText(String.valueOf(expense.getAmount()));
        etDate.setText(expense.getDate());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Обработка выбора даты
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        builder.setTitle("Edit Expense");
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newCategory = etEditCategory.getText().toString();
            String newAmountStr = etEditAmount.getText().toString();
            String newDate = etDate.getText().toString();

            if (newCategory.isEmpty() || newAmountStr.isEmpty() || newDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double newAmount = Double.parseDouble(newAmountStr);
            expense.setCategory(newCategory);
            expense.setAmount(newAmount);
            expense.setDate(newDate);
            expenseViewModel.update(expense);
            Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
