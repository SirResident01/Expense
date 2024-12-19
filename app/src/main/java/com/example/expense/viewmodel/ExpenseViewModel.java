package com.example.expense.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExpenseViewModel extends AndroidViewModel {
    private final ExpenseRepository repository;
    private final LiveData<List<Expense>> allExpenses;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public void update(Expense expense) {
        repository.update(expense);
    }

    public void delete(Expense expense) {
        repository.delete(expense);
    }

    // Метод для получения расходов текущего пользователя
    public LiveData<List<Expense>> getExpensesForUser(int userId) {
        return repository.getExpensesForUser(userId);
    }

    // Метод для анализа расходов по месяцам
    public LiveData<Map<String, Double>> getMonthlyExpenseAnalytics() {
        return Transformations.map(allExpenses, expenses -> {
            Map<String, Double> monthlyAnalytics = new HashMap<>();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

            for (Expense expense : expenses) {
                try {
                    Date date = inputFormat.parse(expense.getDate());
                    if (date != null) {
                        String month = monthFormat.format(date);
                        double amount = expense.getAmount();
                        monthlyAnalytics.put(month, monthlyAnalytics.getOrDefault(month, 0.0) + amount);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            return monthlyAnalytics;
        });
    }

    // Метод для анализа расходов по дням
    public LiveData<Map<String, Double>> getDailyExpenseAnalytics() {
        return Transformations.map(allExpenses, expenses -> {
            Map<String, Double> dailyAnalytics = new HashMap<>();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

            for (Expense expense : expenses) {
                try {
                    Date date = inputFormat.parse(expense.getDate());
                    if (date != null) {
                        String day = dayFormat.format(date);
                        double amount = expense.getAmount();
                        dailyAnalytics.put(day, dailyAnalytics.getOrDefault(day, 0.0) + amount);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            return dailyAnalytics;
        });
    }
}
