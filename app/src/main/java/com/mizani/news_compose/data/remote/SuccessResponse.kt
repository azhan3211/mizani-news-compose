package com.mizani.news_compose.data.remote

import com.google.gson.annotations.SerializedName

class SuccessResponse<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("articles")
    val articles: T
)