package com.mizani.news_compose.data.dto

data class ErrorDto<T>(
    val message: String,
    val data: T
)