package com.mizani.news_compose.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mizani.news_compose.presentation.screen.Screen
import com.mizani.news_compose.presentation.screen.detail.NewsDetail
import com.mizani.news_compose.presentation.screen.list.NewsList
import com.mizani.news_compose.presentation.screen.webview.NewsWebView
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.NewsList.route) {
        composable(route = Screen.NewsList.route) {
            NewsList(navigateToDetail = { id ->
                navController.navigate(Screen.NewsDetail.withId(id))
            })
        }
        composable(
            route = Screen.NewsDetail.fullPath(),
            arguments = listOf(
                navArgument(Screen.NewsDetail.getIdArg()) {
                    type = NavType.StringType
                }
            )
        ) {
            val id = it.arguments?.getString(Screen.NewsDetail.getIdArg()).orEmpty()
            NewsDetail(navController = navController, id, navigateToWebView = { url ->
                val url = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                navController.navigate(Screen.NewsWebView.withUrl(url))
            })
        }
        composable(
            route = Screen.NewsWebView.fullPath(),
            arguments = listOf(
                navArgument(Screen.NewsWebView.getUrlArg()) {
                    type = NavType.StringType
                }
            )
        ) {
            val url = it.arguments?.getString(Screen.NewsWebView.getUrlArg()).orEmpty()
            NewsWebView(navController = navController, url = url)
        }
    }
}