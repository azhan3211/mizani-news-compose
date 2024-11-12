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
import com.mizani.news_compose.data.dto.NewsDataDto
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.data.dto.ResultDto
import com.mizani.news_compose.data.repository.NewsRepository
import com.mizani.news_compose.presentation.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val pageSize = 20

    private val country: String = "us"

    val uiStateMap: UIState<NewsDataDto, ErrorDto>
        get() = _uiStatesMap[_selectedCategory.value] ?: UIState.Loading
    private val _uiStatesMap =
        mutableStateMapOf<String, UIState<NewsDataDto, ErrorDto>>()

    private val _selectedCategory = mutableStateOf(CategoryConst.list[0].first)
    val selectedCategory: State<String> get() = _selectedCategory

    val categories: SnapshotStateList<Pair<String, String>> get() = _categories
    private val _categories = mutableStateListOf<Pair<String, String>>()

    private val _newsDetail = mutableStateOf(NewsDto())
    val newsDetail: State<NewsDto> = _newsDetail

    private val _showMessage = mutableStateOf("")
    val showMessage: State<String> get() = _showMessage

    private val _isLoadMore = mutableStateOf(false)
    val isLoadMore: State<Boolean> get() = _isLoadMore

    private val _newsPage = mutableStateMapOf<String, Int>()

    private var loadMoreJob: Job? = null

    fun getNewsDetail(newsDto: NewsDto) {
        viewModelScope.launch {
            _newsDetail.value = newsDto
        }
    }

    private fun getPage(category: String): Int {
        if (!_newsPage.containsKey(category)) {
            _newsPage[category] = 1
        }
        return _newsPage[category] ?: 1
    }

    private fun updatePage(category: String, newPage: Int) {
        _newsPage[category] = newPage
    }

    fun getNews() {
        val category = _selectedCategory.value
        val isCategoryHasBeenLoaded = _uiStatesMap.containsKey(category)
        val isCategoryLoadedError = _uiStatesMap[category] is UIState.Error
        if (isCategoryHasBeenLoaded.not() || isCategoryLoadedError) {
            _uiStatesMap[category] = UIState.Loading
            viewModelScope.launch {
                when (val data = repository.getNews(
                    country = country,
                    category = category,
                    page = getPage(category),
                    pageSize = pageSize
                )) {
                    is ResultDto.Success -> {
                        _uiStatesMap[category] = UIState.Success(
                            data = data.data,
                            successMessage = data.successMessage
                        )
                        _showMessage.value = data.successMessage
                    }

                    is ResultDto.Error -> {
                        _uiStatesMap[category] =
                            UIState.Error(ErrorDto(message = data.error.message))
                    }
                }
            }
        }
    }

    fun getHasMore(category: String): State<Boolean> {
        val uiState = _uiStatesMap[category]
        if ((uiState is UIState.Success).not()) return mutableStateOf(false)
        val data = (uiState as UIState.Success)
        val totalCurrent = data.data.news.size
        val totalAvailable = data.data.totalResult
        return mutableStateOf(totalCurrent < totalAvailable)
    }

    suspend fun loadMore(): Job {
        return viewModelScope.launch {
            val category = _selectedCategory.value
            if (getHasMore(category).value.not()) return@launch
            _isLoadMore.value = true
            val page = getPage(category) + 1
            delay(10000) // for testing change page while processing
            val result = repository.getNews(
                country = country,
                category = category,
                page = page,
                pageSize = pageSize
            )
            when (result) {
                is ResultDto.Success -> {
                    val data = _uiStatesMap[category]
                    val news = (data as UIState.Success).data
                    appendData(
                        newsDataDto = news,
                        newsMore = result.data.news,
                        category = category,
                        successMessage = result.successMessage
                    )
                    _isLoadMore.value = false
                    if (result.successMessage.isEmpty()) {
                        updatePage(category = category, newPage = page)
                    }
                }
                is ResultDto.Error -> {
                    _isLoadMore.value = false
                }
            }
        }
    }

    private fun appendData(
        newsDataDto: NewsDataDto,
        newsMore: List<NewsDto>,
        category: String,
        successMessage: String
    ) {
        val newData = arrayListOf<NewsDto>()
        newData.addAll(newsDataDto.news)
        newData.addAll(newsMore)
        _uiStatesMap[category] = UIState.Success(
            data = NewsDataDto(
                news = newData,
                totalResult = newsDataDto.totalResult
            ),
            successMessage = successMessage
        )
    }

    fun getCategories() {
        _categories.clear()
        _categories.addAll(CategoryConst.list)
    }

    fun setLoadMoreJob(job: Job) {
        loadMoreJob = job
    }

    fun setSelectedCategory(category: String) {
        loadMoreJob?.cancel()
        _isLoadMore.value = false
        _selectedCategory.value = category
    }

}