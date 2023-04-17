package com.mizani.news_compose.presentation.screen.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    val repository: NewsRepository
) : ViewModel() {

    private val _newsDetail = mutableStateOf(NewsDto())
    val newsDetail: State<NewsDto> = _newsDetail

    fun getNewsDetail(id: String) {
        viewModelScope.launch {
            _newsDetail.value = repository.getDetail(id)
        }
    }

}