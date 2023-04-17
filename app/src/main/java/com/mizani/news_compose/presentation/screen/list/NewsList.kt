package com.mizani.news_compose.presentation.screen.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mizani.news_compose.const.CategoryConst
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.presentation.UIState
import com.mizani.news_compose.presentation.component.NewsCategoriesComponent
import com.mizani.news_compose.presentation.component.NewsItem
import com.mizani.news_compose.presentation.screen.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsList(
    navController: NavController,
    viewModel: NewsListViewModel = hiltViewModel()
) {
    val pagerSelect = rememberPagerState()
    val scope = rememberCoroutineScope()
    val categories = CategoryConst.list

    LaunchedEffect(key1 = 1) {
        viewModel.getNews("us", categories[0].first)
    }

    Column {
        Row(
            modifier = Modifier.padding(vertical = 15.dp)
        ) {
            NewsCategoriesComponent(
                newsCategories = categories
            ) {
                scope.launch {
                    viewModel.getNews("us", categories[it].first)
                    pagerSelect.animateScrollToPage(it)
                }
            }
        }
        HorizontalPager(pageCount = categories.size, state = pagerSelect, userScrollEnabled = false) {
            NewsContent(navController = navController, viewModel.categories[categories[it].first])
        }
    }

}

@Composable
private fun NewsContent(
    navController: NavController,
    uiState: UIState<List<NewsDto>, Throwable>?
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            when (uiState) {
                is UIState.Success ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(uiState.data) { data ->
                            NewsItem(
                                news = data,
                                onClick = {
                                    navController.navigate(Screen.NewsDetail.setId(data.id))
                                }
                            )
                        }
                    }
                is UIState.Error -> {
                    ErrorPage(
                        error = uiState.error,
                        action = {

                        }
                    )
                }
                else -> Loading()
            }
        }
    )
}

@Composable
private fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorPage(error: Throwable, action: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       Text(text = error.message.orEmpty())
        Button(
            onClick = {
                action.invoke()
            }
        ) {
            Text(text = "Refresh")
        }
    }
}