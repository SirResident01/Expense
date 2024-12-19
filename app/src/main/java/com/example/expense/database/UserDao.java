package com.example.expense.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
    private final SQLiteDatabase database;

    public UserDao(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Регистрация нового пользователя
    public boolean registerUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        long result = database.insert(DatabaseHelper.TABLE_USERS, null, values);
        return result != -1;
    }

    // Метод для проверки логина (authenticateUser)
    public boolean authenticateUser(String username, String password) {
        Cursor cursor = null;
        try {
            cursor = database.query(
                    DatabaseHelper.TABLE_USERS,
                    null,
                    DatabaseHelper.COLUMN_USERNAME + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?",
                    new String[]{username, password},
                    null,
                    null,
                    null
            );
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Закрытие базы данных
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
