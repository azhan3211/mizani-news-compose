package com.mizani.news_compose.presentation.screen

sealed class NavigationRoute(val route: String) {

    data object NewsList : NavigationRoute("NewsList")
    data object NewsWebView : NavigationRoute("NewsWebView") {
        fun withUrl(url: String) : String {
            return route.plus("/$url")
        }
        fun getUrlArg() : String {
            return "url"
        }
        fun fullPath()  : String{
            return "$route/{${getUrlArg()}}"
        }
    }
    data object NewsDetail : NavigationRoute("NewsDetail") {
        fun withId(id: String) : String {
            return route.plus("/$id")
        }
        fun getIdArg() : String {
            return "news_id"
        }
        fun fullPath()  : String{
            return "$route/{${getIdArg()}}"
        }
    }

}