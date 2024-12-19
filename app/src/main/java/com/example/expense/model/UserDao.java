package com.example.expense.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    // Вставка нового пользователя
    @Insert
    void insertUser(User user);

    // Получение пользователя по имени и паролю
    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    User getUser(String username, String password);
}
