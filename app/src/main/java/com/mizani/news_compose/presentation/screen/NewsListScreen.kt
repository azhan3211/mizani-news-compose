package com.mizani.news_compose.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mizani.news_compose.R
import com.mizani.news_compose.const.CategoryConst
import com.mizani.news_compose.data.dto.ErrorDto
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.presentation.UIState
import com.mizani.news_compose.presentation.component.ErrorPage
import com.mizani.news_compose.presentation.component.Loading
import com.mizani.news_compose.presentation.component.NewsCategoriesComponent
import com.mizani.news_compose.presentation.component.NewsItem
import kotlinx.coroutines.launch

@Composable
fun NewsListScreen(
    uiState: UIState<List<NewsDto>, ErrorDto<List<NewsDto>>> = UIState.Loading,
    categories: List<Pair<String, String>> = listOf(),
    navigateToDetail: (String) -> Unit = {},
    onCategoryChange: (String) -> Unit = {}
) {

    val pagerSelect = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    Column {
        Row(
            modifier = Modifier.padding(vertical = 15.dp)
        ) {
            NewsCategoriesComponent(
                newsCategories = categories
            ) { selectedIndex ->
                scope.launch {
                    onCategoryChange.invoke(categories[selectedIndex].first)
                    pagerSelect.animateScrollToPage(selectedIndex)
                }
            }
        }
        HorizontalPager(state = pagerSelect, userScrollEnabled = false) {
            NewsContent(
                uiState,
                navigateToDetail = navigateToDetail,
                onRefresh = {
                    onCategoryChange.invoke(categories[it].first)
                }
            )
        }
    }

}

@Composable
private fun NewsContent(
    uiState: UIState<List<NewsDto>, ErrorDto<List<NewsDto>>> = UIState.Success(arrayListOf()),
    navigateToDetail: (String) -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            when (uiState) {
                is UIState.Success ->
                    NewsListSection(
                        modifier = Modifier.padding(paddingValues),
                        data = uiState.data,
                        navigateToDetail = navigateToDetail
                    )
                is UIState.Error -> {
                    if (uiState.error.data.isEmpty()) {
                        ErrorPage(
                            errorMessage = uiState.error.message,
                            action = {
                                onRefresh.invoke()
                            }
                        )
                    } else {
                        NewsListSection(
                            isLocalData = true,
                            modifier = Modifier.padding(paddingValues),
                            data = uiState.error.data,
                            navigateToDetail = navigateToDetail
                        )
                    }
                }
                else -> Loading()
            }
        }
    )
}

@Composable
private fun NewsListSection(
    isLocalData: Boolean = false,
    modifier: Modifier = Modifier,
    data: List<NewsDto>,
    navigateToDetail: (String) -> Unit = {}
) {
    Column {
        if (isLocalData) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.you_are_using_local_data),
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(data) { data ->
                NewsItem(
                    news = data,
                    onClick = {
                        navigateToDetail.invoke(data.id)
                    }
                )
            }
        }
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
            uiState = UIState.Error(ErrorDto(message = "error", data = listOf(dummyData))),
            categories = CategoryConst.list
        )
    }
}