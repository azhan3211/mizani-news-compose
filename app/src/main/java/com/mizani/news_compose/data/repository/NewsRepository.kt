package com.mizani.news_compose.data.repository

import com.mizani.news_compose.data.dto.NewsDto
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(country: String, category: String): Flow<List<NewsDto>>
    suspend fun insert(news: List<NewsDto>)
    suspend fun delete(categoryName: String)
    suspend fun getDetail(id: String): NewsDto

}