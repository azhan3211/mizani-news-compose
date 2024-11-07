package com.mizani.news_compose.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mizani.news_compose.R

@Composable
fun Loading() {
    Column {
        repeat(10) {
            NewsItemShimmer()
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun ErrorPage(errorMessage: String, action: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage.orEmpty())
        Button(
            onClick = {
                action.invoke()
            }
        ) {
            Text(text = stringResource(id = R.string.refresh))
        }
    }
}