package com.mizani.news_compose.presentation.screen

sealed class NavigationRoute(val route: String) {

    data object NewsList : NavigationRoute("NewsList")
    data object NewsWebView : NavigationRoute("NewsWebView")
    data object NewsDetail : NavigationRoute("NewsDetail")

}