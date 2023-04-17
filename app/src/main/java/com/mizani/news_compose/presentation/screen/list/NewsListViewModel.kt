package com.mizani.news_compose.presentation.screen.list

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.repository.NewsRepository
import com.mizani.news_compose.presentation.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    private val _categories = mutableStateMapOf<String, UIState<List<NewsDto>, Throwable>>()
    val categories: SnapshotStateMap<String, UIState<List<NewsDto>, Throwable>> = _categories

    fun getNews(country: String, category: String) {
        if (_categories.containsKey(category).not()) {
            _categories[category] = UIState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.getNews(country, category).collect {
                        launch(Dispatchers.Main) {
                            _categories[category] = UIState.Success(it)
                        }
                    }
                } catch (e: Exception) {
                    _categories[category] = UIState.Error(e)
                }
            }
        }
    }

}