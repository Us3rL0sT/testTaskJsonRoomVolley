package test.zadanie.app.com.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import test.zadanie.app.com.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun getUser(username: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): UserEntity?
}