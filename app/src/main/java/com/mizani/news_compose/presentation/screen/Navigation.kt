package com.mizani.news_compose.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val viewModel: NewsViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.getCategories()
        viewModel.getNews()
    }

    val coroutine = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.NewsList.route
    ) {
        composable(route = NavigationRoute.NewsList.route) {
            with (viewModel) {
                NewsListScreen(
                    uiState = uiStateMap,
                    categories = categories,
                    selectedCategory = selectedCategory.value,
                    successMessage = showMessage.value,
                    isLoadMore = isLoadMore.value,
                    hasMore = getHasMore(selectedCategory.value).value,
                    navigateToDetail = { news ->
                        getNewsDetail(news)
                        navController.navigate(NavigationRoute.NewsDetail.route)
                    },
                    onCategoryChange = { category ->
                        setSelectedCategory(category)
                        getNews()
                    },
                    onRefresh = {
                        getNews(true)
                    },
                    loadMore = {
                        coroutine.launch {
                            setLoadMoreJob(loadMore())
                        }
                    }
                )
            }
        }
        composable(
            route = NavigationRoute.NewsDetail.route
        ) {
            NewsDetailScreen(
                newsDto = viewModel.newsDetail.value,
                navigateToWebView = {
                    navController.navigate(NavigationRoute.NewsWebView.route)
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = NavigationRoute.NewsWebView.route
        ) {
            NewsWebViewScreen(
                onBackClicked = { navController.popBackStack() },
                url = viewModel.newsDetail.value.url
            )
        }
    }
}