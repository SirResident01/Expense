package com.example.expense.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Expense.class, User.class}, version = 4, exportSchema = false) // Добавляем User
public abstract class ExpenseDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();
    public abstract UserDao userDao(); // Добавляем UserDao

    private static volatile ExpenseDatabase INSTANCE;

    public static ExpenseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ExpenseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ExpenseDatabase.class, "expense_database")
                            .fallbackToDestructiveMigration() // Удаляет старую базу данных при изменении версии
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
