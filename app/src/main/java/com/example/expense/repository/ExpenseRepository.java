package com.example.expense.repository;

import com.example.expense.model.ExpenseDao;
import com.example.expense.model.Expense;
import com.example.expense.model.ExpenseDatabase;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ExpenseRepository {
    private final ExpenseDao expenseDao;
    private final LiveData<List<Expense>> allExpenses;

    public ExpenseRepository(Application application) {
        ExpenseDatabase db = ExpenseDatabase.getDatabase(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getExpensesForUser(int userId) {
        return expenseDao.getExpensesForUser(userId);
    }

    public void insert(Expense expense) {
        new Thread(() -> expenseDao.insertExpense(expense)).start();
    }

    public void update(Expense expense) {
        new Thread(() -> expenseDao.updateExpense(expense)).start();
    }

    public void delete(Expense expense) {
        new Thread(() -> expenseDao.deleteExpense(expense)).start();
    }
}
