package com.mizani.news_compose.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String = "",
    @ColumnInfo("title")
    val title: String = "",
    @ColumnInfo("short_description")
    val shortDescription: String = "",
    @ColumnInfo("long_description")
    val longDescription: String = "",
    @ColumnInfo("thumbnail")
    val thumbnail: String = "",
    @ColumnInfo("date")
    val date: String = "",
    @ColumnInfo("category_name")
    val categoryName: String = "",
    @ColumnInfo("url")
    val url: String = ""
)