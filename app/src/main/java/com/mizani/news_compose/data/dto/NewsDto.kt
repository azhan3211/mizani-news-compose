package com.mizani.news_compose.data.dto

import java.io.Serializable

class NewsDto(
    val id: String = "",
    val title: String = "",
    val shortDescription: String = "",
    val longDescription: String = "",
    val thumbnail: String = "",
    val url: String = "",
    val categoryName: String = "",
    val date: String = ""
): Serializable