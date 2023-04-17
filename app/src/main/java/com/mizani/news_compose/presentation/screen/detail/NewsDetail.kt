package com.mizani.news_compose.presentation.screen.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mizani.news_compose.utils.DateFormatter.convertDateTo
import com.mizani.news_compose.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsDetail(
    navController: NavController,
    id: String,
    navigateToWebView: (String) -> Unit,
    viewModel: NewsDetailViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = 1) {
        viewModel.getNewsDetail(id)
    }

    val scrollState = rememberScrollState()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                GlideImage(
                    model = viewModel.newsDetail.value.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = viewModel.newsDetail.value.categoryName,
                            fontWeight = FontWeight.W400,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.weight(1.0f))
                        Text(
                            text = viewModel.newsDetail.value.date.convertDateTo(),
                            fontWeight = FontWeight.W400,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth())
                    Text(
                        text = viewModel.newsDetail.value.title,
                        fontWeight = FontWeight.W700,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth())
                    Text(
                        text = viewModel.newsDetail.value.longDescription,
                        fontWeight = FontWeight.W400,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth())
                    Text(
                        text = "Visit here to see more",
                        fontWeight = FontWeight.W700,
                        fontSize = 14.sp,
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            navigateToWebView.invoke(viewModel.newsDetail.value.url)
                        }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier.size(45.dp)
                        .clip(CircleShape)
                        .background(color = Color.White)
                        .clickable {
                            navController.popBackStack()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}