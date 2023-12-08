package com.example.indexcards

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.indexcards.entities.Card
import com.example.indexcards.entities.CrossRef
import com.example.indexcards.entities.Tag

@Database(
    entities = [
        Card::class,
        Tag::class,
        CrossRef::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val dao: AppDao
}