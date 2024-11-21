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
        _newsDetail.value = newsDto
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

    private fun initPreState(category: String, isRefresh: Boolean) {
        val uiState = _uiStatesMap[category]
        if (isRefresh && uiState is UIState.Success) {
            _uiStatesMap[category] = UIState.Success(
                data = uiState.data,
                isRefreshing = true
            )
        } else {
            _uiStatesMap[category] = UIState.Loading
        }
        if (isRefresh) {
            updatePage(category, 1)
        }
    }

    fun getNews(isRefresh: Boolean = false) {
        val category = _selectedCategory.value
        val isCategoryHasBeenLoaded = _uiStatesMap.containsKey(category)
        val isCategoryLoadedError = _uiStatesMap[category] is UIState.Error
        if (isCategoryHasBeenLoaded.not() || isCategoryLoadedError || isRefresh) {
            initPreState(category, isRefresh)
            viewModelScope.launch {
                getNews(
                    page = getPage(category),
                    category = category,
                    onSuccess = { result ->
                        onGetNewsSuccess(
                            category = category,
                            result = result
                        )
                    },
                    onError = { error ->
                        _uiStatesMap[category] =
                            UIState.Error(ErrorDto(message = error.error.message))
                    }
                )
            }
        }
    }

    private fun onGetNewsSuccess(
        category: String,
        result: ResultDto.Success<NewsDataDto>
    ) {
        _uiStatesMap[category] = UIState.Success(
            data = result.data,
            successMessage = result.successMessage,
            isRefreshing = false
        )
        _showMessage.value = result.successMessage
    }

    private suspend fun getNews(
        page: Int,
        category: String,
        onSuccess: (ResultDto.Success<NewsDataDto>) -> Unit,
        onError: (ResultDto.Error<ErrorDto>) -> Unit
    ) {
        when (val data = repository.getNews(
            country = country,
            category = category,
            page = page,
            pageSize = pageSize
        )) {
            is ResultDto.Success -> onSuccess.invoke(data)

            is ResultDto.Error -> onError.invoke(data)
        }
    }

    fun getHasMore(category: String): State<Boolean> {
        val uiState = _uiStatesMap[category]
        if ((uiState is UIState.Success).not()) return mutableStateOf(false)
        val data = (uiState as UIState.Success)
        val totalCurrent = data.data.news.size
        return mutableStateOf(getPage(category) * 20 == totalCurrent)
    }

    suspend fun loadMore(): Job {
        return viewModelScope.launch {
            val category = _selectedCategory.value
            if (getHasMore(category).value.not()) return@launch
            _isLoadMore.value = true
            val nextPage = getPage(category) + 1
            getNews(
                category = category,
                page = nextPage,
                onSuccess = { result ->
                    onLoadMoreSuccess(
                        nextPage = nextPage,
                        category = category,
                        result = result
                    )
                },
                onError = {
                    _isLoadMore.value = false
                }
            )
        }
    }

    private fun onLoadMoreSuccess(
        nextPage: Int,
        category: String,
        result: ResultDto.Success<NewsDataDto>
    ) {
        appendData(
            result = result,
            category = category
        )
        _isLoadMore.value = false
        if (result.successMessage.isEmpty()) {
            updatePage(category = category, newPage = nextPage)
        }
    }

    private fun appendData(
        result: ResultDto.Success<NewsDataDto>,
        category: String
    ) {
        val uiState = (_uiStatesMap[category] as UIState.Success).data
        val newNews = arrayListOf<NewsDto>()
        newNews.addAll(uiState.news)
        newNews.addAll(result.data.news)
        _uiStatesMap[category] = UIState.Success(
            data = NewsDataDto(
                news = newNews,
                totalResult = uiState.totalResult
            ),
            successMessage = result.successMessage
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