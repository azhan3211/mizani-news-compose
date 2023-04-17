package com.mizani.news_compose.presentation.screen

sealed class Screen(val route: String) {

    object NewsList : Screen("NewsList")
    object NewsWebView : Screen("NewsWebView") {
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
    object NewsDetail : Screen("NewsDetail") {
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