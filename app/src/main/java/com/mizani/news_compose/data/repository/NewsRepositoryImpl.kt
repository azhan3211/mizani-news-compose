package com.mizani.news_compose.data.repository

import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.remote.NewsService
import com.mizani.news_compose.data.NewsMapper
import com.mizani.news_compose.data.NewsMapper.toDto
import com.mizani.news_compose.data.dao.NewsDao
import com.mizani.news_compose.data.dto.ErrorDto
import com.mizani.news_compose.data.dto.NewsDataDto
import com.mizani.news_compose.data.dto.ResultDto
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NewsRepositoryImpl  @Inject constructor(
    private val newsService: NewsService,
    private val newsDao: NewsDao
): NewsRepository {

    override suspend fun getNews(
        country: String,
        category: String,
        page: Int,
        pageSize: Int
    ): ResultDto<NewsDataDto, ErrorDto> {
        try {
            val newsResponse = newsService.getNews(
                country = country,
                category = category,
                page = page
            )
            if (newsResponse.isSuccessful) {
                val totalResult = newsResponse.body()?.totalResults ?: 0
                val articles = newsResponse.body()?.articles ?: listOf()
                if (articles.isNotEmpty()) deleteLocalData(category)
                val data = articles.map { it.toDto(category) }
                if (page == 1) insertToLocalData(data)
                return ResultDto.Success(
                    NewsDataDto(
                        news = data,
                        totalResult = totalResult
                    )
                )
            } else {
                return checkLocalData(
                    page = page,
                    errorMessage = newsResponse.errorBody().toString(),
                    category = category
                )
            }
        } catch (e: Exception) {
            return checkLocalData(
                page = page,
                errorMessage = e.message.toString(),
                category = category
            )
        }
    }

    private suspend fun checkLocalData(
        page: Int,
        errorMessage: String,
        category: String
    ): ResultDto<NewsDataDto, ErrorDto>{
        val localData = newsDao.getAll(categoryName = category).first().map { it.toDto() }
        return if (localData.isNotEmpty() && page == 1) {
            return ResultDto.Success(
                NewsDataDto(
                    news = localData,
                    totalResult = localData.size
                ),
                errorMessage
            )
        } else {
            ResultDto.Error(
                ErrorDto(
                    message = errorMessage
                )
            )
        }
    }

    override suspend fun insertToLocalData(news: List<NewsDto>) {
        val newsEntities = news.map {
            NewsMapper.mapDtoToEntity(it)
        }
        newsDao.insert(newsEntities)
    }

    override suspend fun deleteLocalData(categoryName: String) {
        newsDao.delete(categoryName)
    }

    override suspend fun getDetail(id: String): NewsDto {
        return newsDao.get(id).toDto()
    }


}