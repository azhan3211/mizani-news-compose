package com.mizani.news_compose.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsCategoriesComponent(
    newsCategories: List<Pair<String, String>>,
    action: (Int) -> Unit
) {
    var selectedIndex by rememberSaveable {
        mutableStateOf(0)
    }
    Row {
        LazyRow {
            itemsIndexed (newsCategories) { index, it ->
                Spacer(modifier = Modifier.width(16.dp))
                ChipItem(text = it.second, index, index == selectedIndex) {
                    selectedIndex = it
                    action.invoke(it)
                }
            }
        }
    }
}