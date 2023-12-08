package com.example.indexcards

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.indexcards.entities.Box
import com.example.indexcards.entities.Card
import com.example.indexcards.entities.CardTagCrossRef
import com.example.indexcards.entities.Tag

@Database(
    entities = [
        Box::class,
        Card::class,
        Tag::class,
        CardTagCrossRef::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val dao: AppDao

//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(
//            context: Context
//        ): Any {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context,
//                    AppDatabase::class.java,
//                    "app_database"
//                )
//                    .build()
//
//                INSTANCE = instance
//            }
//        }
//    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(
            context: Context
        ): AppDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}