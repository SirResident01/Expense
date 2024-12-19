package com.example.expense.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "expense_table",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = CASCADE
        )
)
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId; // Новый идентификатор пользователя
    private String category;
    private double amount;
    private String date; // Дата в формате yyyy-MM-dd
    private long timestamp; // Время в миллисекундах для аналитики

    // Конструктор с полем userId
    public Expense(int userId, String category, double amount, String date, long timestamp) {
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.timestamp = timestamp;
    }

    // Конструктор с игнорируемыми параметрами (для использования в приложении)
    @Ignore
    public Expense(String category, double amount, String date, long timestamp) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.timestamp = timestamp;
    }

    @Ignore
    public Expense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.timestamp = System.currentTimeMillis();
    }

    // Геттеры и сеттеры для всех полей
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
