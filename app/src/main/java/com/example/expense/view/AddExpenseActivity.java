package com.example.expense.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.expense.R;
import com.example.expense.model.Expense;
import com.example.expense.viewmodel.ExpenseViewModel;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText etCategory, etAmount, etDate;
    private Button btnSaveExpense;
    private ExpenseViewModel expenseViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etCategory = findViewById(R.id.etCategory);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // Обработчик выбора даты
        etDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        etDate.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Обработчик для сохранения расхода
        btnSaveExpense.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String category = etCategory.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (category.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создаем объект Expense
        Expense expense = new Expense(category, amount, date, System.currentTimeMillis());
        expenseViewModel.insert(expense);
        Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
