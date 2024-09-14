package com.mizani.news_compose.presentation.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mizani.news_compose.const.CategoryConst
import com.mizani.news_compose.data.dto.ErrorDto
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.dto.ResultDto
import com.mizani.news_compose.data.repository.NewsRepository
import com.mizani.news_compose.presentation.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val uiStateMap: UIState<List<NewsDto>, ErrorDto<List<NewsDto>>>
        get() = _uiStatesMap[_selectedCategory.value] ?: UIState.Loading
    private val _uiStatesMap =
        mutableStateMapOf<String, UIState<List<NewsDto>, ErrorDto<List<NewsDto>>>>()

    private var _selectedCategory = mutableStateOf(CategoryConst.list[0].first)

    val categories: SnapshotStateList<Pair<String, String>> get() = _categories
    private val _categories = mutableStateListOf<Pair<String, String>>()

    private val _newsDetail = mutableStateOf(NewsDto())
    val newsDetail: State<NewsDto> = _newsDetail

    fun getNewsDetail(id: String) {
        viewModelScope.launch {
            _newsDetail.value = repository.getDetail(id)
        }
    }

    fun getNews(country: String = "us") {
        val isCategoryHasBeenLoaded = _uiStatesMap.containsKey(_selectedCategory.value)
        val isCategoryLoadedError = _uiStatesMap[_selectedCategory.value] is UIState.Error
        if (isCategoryHasBeenLoaded.not() || isCategoryLoadedError) {
            _uiStatesMap[_selectedCategory.value] = UIState.Loading
            viewModelScope.launch {
                when (val data = repository.getNews(country, _selectedCategory.value)) {
                    is ResultDto.Success -> {
                        _uiStatesMap[_selectedCategory.value] = UIState.Success(data.data)
                    }

                    is ResultDto.Error -> {
                        _uiStatesMap[_selectedCategory.value] =
                            UIState.Error(ErrorDto(message = data.error.message, data.error.data))
                    }
                }
            }
        }
    }

    fun getCategories() {
        _categories.clear()
        _categories.addAll(CategoryConst.list)
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

}