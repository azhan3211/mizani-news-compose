package com.mizani.news_compose.data.dto

sealed class ResultDto<out Success, out Error> {
    data class Success<out Success>(val data: Success): ResultDto<Success, Nothing>()
    data class Error<out Error>(val error: Error): ResultDto<Nothing, Error>()
}