package com.example.expense.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    // Вставка нового расхода
    @Insert
    void insertExpense(Expense expense);

    // Обновление существующего расхода
    @Update
    void updateExpense(Expense expense);

    // Удаление расхода
    @Delete
    void deleteExpense(Expense expense);

    // Получение всех расходов
    @Query("SELECT * FROM expense_table ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses();

    // Получение всех расходов для конкретного пользователя
    @Query("SELECT * FROM expense_table WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesForUser(int userId);

    // Получение расходов за определенный месяц и год
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND strftime('%Y', date) = :year AND strftime('%m', date) = :month ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesByMonth(int userId, String year, String month);

    // Получение общих расходов за месяц
    @Query("SELECT SUM(amount) FROM expense_table WHERE userId = :userId AND strftime('%Y', date) = :year AND strftime('%m', date) = :month")
    LiveData<Double> getTotalExpenseByMonth(int userId, String year, String month);

    // Получение общих расходов за год
    @Query("SELECT SUM(amount) FROM expense_table WHERE userId = :userId AND strftime('%Y', date) = :year")
    LiveData<Double> getTotalExpenseByYear(int userId, String year);

    // Получение расходов за последнюю неделю
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND timestamp >= :startTimestamp AND timestamp <= :endTimestamp ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesByWeek(int userId, long startTimestamp, long endTimestamp);
}
