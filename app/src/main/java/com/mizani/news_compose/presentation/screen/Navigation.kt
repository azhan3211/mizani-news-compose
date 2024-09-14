package com.mizani.news_compose.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                    navController.navigate(NavigationRoute.NewsDetail.withId(id))
                },
                onCategoryChange = { category ->
                    viewModel.setSelectedCategory(category)
                    viewModel.getNews()
                }
            )
        }
        composable(
            route = NavigationRoute.NewsDetail.fullPath(),
            arguments = listOf(
                navArgument(NavigationRoute.NewsDetail.getIdArg()) {
                    type = NavType.StringType
                }
            )
        ) {
            NewsDetailScreen(
                newsDto = viewModel.newsDetail.value,
                navigateToWebView = { url ->
                    val source = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                    navController.navigate(NavigationRoute.NewsWebView.withUrl(source))
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = NavigationRoute.NewsWebView.fullPath(),
            arguments = listOf(
                navArgument(NavigationRoute.NewsWebView.getUrlArg()) {
                    type = NavType.StringType
                }
            )
        ) {
            val url = it.arguments?.getString(NavigationRoute.NewsWebView.getUrlArg()).orEmpty()
            NewsWebViewScreen(
                onBackClicked = { navController.popBackStack() },
                url = url
            )
        }
    }
}