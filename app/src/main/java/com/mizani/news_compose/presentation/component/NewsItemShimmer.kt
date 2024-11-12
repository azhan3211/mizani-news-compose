package com.mizani.news_compose.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NewsItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(all = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(100.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .size(100.dp)
                    .background(ShimmerLoading()),
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(ShimmerLoading())
            )
            Spacer(modifier = Modifier.height(4.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(ShimmerLoading())
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Spacer(
                    modifier = Modifier
                        .width(100.dp)
                        .height(10.dp)
                        .background(ShimmerLoading())
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsItemShimmerPreview() {
    NewsItemShimmer()
}