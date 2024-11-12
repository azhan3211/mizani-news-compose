package com.mizani.news_compose.data.remote

import com.mizani.news_compose.BuildConfig
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
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<SuccessResponse<List<NewsResponse>>>

}