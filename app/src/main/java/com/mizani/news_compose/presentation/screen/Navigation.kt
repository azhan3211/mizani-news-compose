package com.mizani.news_compose.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val viewModel: NewsViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.getCategories()
        viewModel.getNews()
    }

    NavHost(navController = navController, startDestination = NavigationRoute.NewsList.route) {
        composable(route = NavigationRoute.NewsList.route) {
            NewsListScreen(
                uiState = viewModel.uiStateMap,
                categories = viewModel.categories,
                navigateToDetail = { id ->
                    viewModel.getNewsDetail(id)
                    navController.navigate(NavigationRoute.NewsDetail.route)
                },
                onCategoryChange = { category ->
                    viewModel.setSelectedCategory(category)
                    viewModel.getNews()
                }
            )
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