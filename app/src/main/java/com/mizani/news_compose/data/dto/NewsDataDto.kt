package com.mizani.news_compose.data.dto

data class NewsDataDto(
    val news: List<NewsDto> = listOf(),
    val totalResult: Int = 0
)