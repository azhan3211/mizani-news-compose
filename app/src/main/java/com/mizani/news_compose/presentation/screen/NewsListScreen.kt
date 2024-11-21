package com.mizani.news_compose.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mizani.news_compose.R
import com.mizani.news_compose.const.CategoryConst
import com.mizani.news_compose.data.dto.ErrorDto
import com.mizani.news_compose.data.dto.NewsDataDto
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.presentation.UIState
import com.mizani.news_compose.presentation.component.ErrorPage
import com.mizani.news_compose.presentation.component.Loading
import com.mizani.news_compose.presentation.component.NewsCategoriesComponent
import com.mizani.news_compose.presentation.component.NewsItem
import com.mizani.news_compose.presentation.component.NewsItemShimmer

@Composable
fun NewsListScreen(
    uiState: UIState<NewsDataDto, ErrorDto> = UIState.Loading,
    categories: List<Pair<String, String>> = listOf(),
    selectedCategory: String,
    successMessage: String = "",
    isLoadMore: Boolean = false,
    hasMore: Boolean = false,
    navigateToDetail: (NewsDto) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onRefresh: () -> Unit,
    loadMore: () -> Unit
) {

    val context = LocalContext.current

    var selectedIndex by rememberSaveable {
        mutableStateOf(0)
    }

    LaunchedEffect(successMessage) {
        if (successMessage.isNotEmpty()) {
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
        }
    }


    Column {
        NewsCategoriesComponent(
            newsCategories = categories
        ) { index ->
            onCategoryChange.invoke(categories[index].first)
            selectedIndex = index
        }
        NewsContent(
            uiState = uiState,
            selectedCategory = selectedCategory,
            navigateToDetail = navigateToDetail,
            isLoadMore = isLoadMore,
            hasMore = hasMore,
            onRefresh = onRefresh,
            loadMore = loadMore
        )
    }

}

@Composable
private fun NewsContent(
    uiState: UIState<NewsDataDto, ErrorDto> = UIState.Success(NewsDataDto()),
    selectedCategory: String,
    navigateToDetail: (NewsDto) -> Unit = {},
    isLoadMore: Boolean = false,
    hasMore: Boolean = false,
    onRefresh: () -> Unit = {},
    loadMore: () -> Unit
) {

    val scrollStates = remember { mutableStateMapOf<String, LazyListState>() }
    val listState = scrollStates.getOrPut(selectedCategory) { LazyListState() }

    when (uiState) {
        is UIState.Success ->
            NewsListSection(
                data = uiState.data.news,
                listState = listState,
                isLoadMore = isLoadMore,
                navigateToDetail = navigateToDetail,
                hasMore = hasMore,
                loadMore = loadMore,
                onRefresh = onRefresh,
                isRefreshing = uiState.isRefreshing
            )
        is UIState.Error -> {
            ErrorPage(
                errorMessage = uiState.error.message,
                action = {
                    onRefresh.invoke()
                }
            )
        }
        else -> Loading()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsListSection(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    data: List<NewsDto>,
    navigateToDetail: (NewsDto) -> Unit = {},
    isLoadMore: Boolean = false,
    hasMore: Boolean = false,
    loadMore: () -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(data) { index, news ->
                if (index >= data.lastIndex && isLoadMore.not()) {
                    loadMore.invoke()
                }
                NewsItem(
                    news = news,
                    onClick = {
                        navigateToDetail.invoke(news)
                    }
                )
            }

            if (hasMore.not()) {
                item {
                    NoMoreNews()
                }
            }

            if (isLoadMore) {
                item {
                    NewsItemShimmer()
                }
            }
        }
    }
}

@Composable
private fun NoMoreNews() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = R.string.no_more_news))
    }
}

@Preview
@Composable
private fun NewsListScreenPreview() {
    val dummyData = NewsDto(
        categoryName = "Business",
        title = "Title",
        longDescription = "Lorem Isum dolor amet",
        date = "2024-09-11T20:55:00Z"
    )
    MaterialTheme {
        NewsListScreen(
            uiState = UIState.Success(
                data = NewsDataDto(
                    news = arrayListOf(dummyData),
                    totalResult = 20
                )
            ),
            selectedCategory = "business",
            categories = CategoryConst.list,
            loadMore = {},
            onRefresh = {}
        )
    }
}