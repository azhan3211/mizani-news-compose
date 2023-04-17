package com.mizani.news_compose.data.repository

import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.remote.NewsService
import com.mizani.news_compose.data.NewsMapper
import com.mizani.news_compose.data.dao.NewsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsService: NewsService,
    private val newsDao: NewsDao
): NewsRepository {

    override fun getNews(country: String, category: String): Flow<List<NewsDto>> = flow {
        var newsResponse = newsService.getNews(country, category).articles
        if (newsResponse.isNotEmpty()) {
            delete(category)
        }
        insert(newsResponse.map { NewsMapper.mapResponseToDto(it, category) })

        newsDao.getAll(categoryName = category).collect {
            emit(it.map { data -> NewsMapper.mapEntityToDto(data) })
        }
    }

    override suspend fun insert(news: List<NewsDto>) {
        val newsEntities = news.map {
            NewsMapper.mapDtoToEntity(it)
        }
        newsDao.insert(newsEntities)
    }

    override suspend fun delete(categoryName: String) {
        newsDao.delete(categoryName)
    }

    override suspend fun getDetail(id: String): NewsDto {
        return NewsMapper.mapEntityToDto(newsDao.get(id))
    }


}