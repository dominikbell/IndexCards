package com.example.indexcards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Box::class,
        Card::class,
        Tag::class,
        CardTagCrossRef::class,
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}