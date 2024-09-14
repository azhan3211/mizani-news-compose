package com.mizani.news_compose.data.repository

import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.remote.NewsService
import com.mizani.news_compose.data.NewsMapper
import com.mizani.news_compose.data.NewsMapper.toDto
import com.mizani.news_compose.data.dao.NewsDao
import com.mizani.news_compose.data.dto.ErrorDto
import com.mizani.news_compose.data.dto.ResultDto
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsService: NewsService,
    private val newsDao: NewsDao
): NewsRepository {

    override suspend fun getNews(country: String, category: String): ResultDto<List<NewsDto>, ErrorDto<List<NewsDto>>> {
        var localData = newsDao.getAll(categoryName = category)
        try {
            var newsResponse = newsService.getNews(country, category)
            if (newsResponse.isSuccessful) {
                val articles = newsResponse.body()?.articles ?: listOf()
                if (articles.isNotEmpty()) {
                    delete(category)
                }
                val data = articles.map { it.toDto(category) }
                insert(data)
                return ResultDto.Success(data)
            } else {
                return ResultDto.Error(
                    ErrorDto(
                        message = newsResponse.errorBody().toString(),
                        data = localData.first().map { it.toDto() }
                    )
                )
            }
        } catch (e: Exception) {
            return ResultDto.Error(
                ErrorDto(
                    message = e.message.toString(),
                    data = localData.first().map { it.toDto() }
                )
            )
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
        return newsDao.get(id).toDto()
    }


}