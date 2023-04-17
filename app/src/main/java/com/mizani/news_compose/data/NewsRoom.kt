package com.mizani.news_compose.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mizani.news_compose.data.dao.NewsDao
import com.mizani.news_compose.data.entities.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsRoom : RoomDatabase() {

    abstract fun getNews(): NewsDao

}