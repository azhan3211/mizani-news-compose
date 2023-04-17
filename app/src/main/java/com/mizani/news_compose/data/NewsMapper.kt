package com.mizani.news_compose.data

import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.entities.NewsEntity
import com.mizani.news_compose.data.remote.NewsResponse

object NewsMapper {

    fun mapResponseToDto(newsResponse: NewsResponse, category: String): NewsDto {
        return NewsDto(
            id = newsResponse.title,
            title = newsResponse.title,
            thumbnail = newsResponse.thumbnail.orEmpty(),
            date = newsResponse.publishedAt.orEmpty(),
            categoryName = category,
            longDescription = newsResponse.content.orEmpty(),
            shortDescription = newsResponse.description.orEmpty(),
            url = newsResponse.url.orEmpty()
        )
    }

    fun mapDtoToEntity(news: NewsDto): NewsEntity {
        return NewsEntity(
            id = news.id,
            title = news.title,
            url = news.url,
            shortDescription = news.shortDescription,
            longDescription = news.longDescription,
            categoryName = news.categoryName,
            date = news.date,
            thumbnail = news.thumbnail
        )
    }

    fun mapEntityToDto(news: NewsEntity): NewsDto {
        return NewsDto(
            id = news.id,
            title = news.title,
            url = news.url,
            shortDescription = news.shortDescription,
            longDescription = news.longDescription,
            categoryName = news.categoryName,
            date = news.date,
            thumbnail = news.thumbnail,
        )
    }
}