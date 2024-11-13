package com.mizani.news_compose.presentation

sealed class UIState<out T, out E> {

    data object Loading: UIState<Nothing, Nothing>()
    data class Success<T>(val data: T, val successMessage: String = "", val isRefreshing: Boolean = false): UIState<T, Nothing>()
    data class Error<E>(val error: E): UIState<Nothing, E>()

}