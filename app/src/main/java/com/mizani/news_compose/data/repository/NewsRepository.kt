package com.mizani.news_compose.data.repository

import com.mizani.news_compose.data.dto.ErrorDto
import com.mizani.news_compose.data.dto.NewsDataDto
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.dto.ResultDto
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(
        country: String,
        category: String,
        page: Int = 1,
        pageSize: Int = 20
    ): ResultDto<NewsDataDto, ErrorDto>
    suspend fun insertToLocalData(news: List<NewsDto>)
    suspend fun deleteLocalData(categoryName: String)
    suspend fun getDetail(id: String): NewsDto

}