package com.mizani.news_compose.presentation.screen.list

import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable

enum class TabNews {
}

@Composable
fun TabNewsHost(selectedIndex: Int, onSelectedChange: (Int) -> Unit) {
    TabRow(selectedTabIndex = selectedIndex) {

    }
}