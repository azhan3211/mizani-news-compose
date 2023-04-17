package com.mizani.news_compose.data.remote

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("author")
    val author: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("urlToImage")
    val thumbnail: String? = null,
    @SerializedName("publishedAt")
    val publishedAt: String? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("source")
    val source: NewsSourceResponse
)