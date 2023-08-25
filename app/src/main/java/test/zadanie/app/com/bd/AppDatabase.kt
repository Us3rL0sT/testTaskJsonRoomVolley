package test.zadanie.app.com.bd

import androidx.room.Database
import androidx.room.RoomDatabase
import test.zadanie.app.com.dao.UserDao
import test.zadanie.app.com.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
