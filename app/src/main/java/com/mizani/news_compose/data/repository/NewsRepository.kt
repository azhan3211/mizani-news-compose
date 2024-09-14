package com.mizani.news_compose.data.repository

import com.mizani.news_compose.data.dto.ErrorDto
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.dto.ResultDto
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(country: String, category: String): ResultDto<List<NewsDto>, ErrorDto<List<NewsDto>>>
    suspend fun insert(news: List<NewsDto>)
    suspend fun delete(categoryName: String)
    suspend fun getDetail(id: String): NewsDto

}