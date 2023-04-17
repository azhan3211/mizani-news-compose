package com.mizani.news_compose.data.remote

import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = "d9ed37cef7f84f26841e0d7fb6079ebb"
    ): SuccessResponse<List<NewsResponse>>

}