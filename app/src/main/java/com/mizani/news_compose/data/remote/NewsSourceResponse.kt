package com.mizani.news_compose.data.remote

import com.google.gson.annotations.SerializedName

data class NewsSourceResponse(
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("name")
    val name: String? = null
)