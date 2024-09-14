package com.mizani.news_compose.data

import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.entities.NewsEntity
import com.mizani.news_compose.data.remote.NewsResponse

object NewsMapper {

    private val ESCAPE_CHARACTER_REGEX = "[^A-Za-z0-9]".toRegex()

    fun NewsResponse.toDto(category: String): NewsDto {
        return NewsDto(
            id = title.replace(ESCAPE_CHARACTER_REGEX, ""),
            title = title,
            thumbnail = thumbnail.orEmpty(),
            date = publishedAt.orEmpty(),
            categoryName = category,
            longDescription = content.orEmpty(),
            shortDescription = description.orEmpty(),
            url = url.orEmpty()
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

    fun NewsEntity.toDto(): NewsDto {
        return NewsDto(
            id = id,
            title = title,
            url = url,
            shortDescription = shortDescription,
            longDescription = longDescription,
            categoryName = categoryName,
            date = date,
            thumbnail = thumbnail,
        )
    }
}