package com.example.pengolahancitra.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pengolahancitra.data.database.entity.Edit
import com.example.pengolahancitra.data.database.entity.Result

@Database(entities = [Edit::class, Result::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun processingDao() : Dao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? =  null

        @JvmStatic
        fun getDatabase(context: Context) : AppDatabase {
            if (INSTANCE == null){
                synchronized(AppDatabase::class.java){
                    if (INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java,
                            "processing_database")
                            .build()
                    }
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}