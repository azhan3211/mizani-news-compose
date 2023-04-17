package com.mizani.news_compose.data.dao

import androidx.room.*
import com.mizani.news_compose.data.entities.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Upsert
    suspend fun insert(news: List<NewsEntity>)

    @Query("SELECT * FROM news_table WHERE category_name = :categoryName")
    fun getAll(categoryName: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_table WHERE id = :id")
    suspend fun get(id: String): NewsEntity

    @Query("DELETE FROM news_table WHERE category_name = :categoryName")
    suspend fun delete(categoryName: String)

}