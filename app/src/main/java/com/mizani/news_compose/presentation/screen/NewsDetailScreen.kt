package com.mizani.news_compose.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mizani.news_compose.utils.DateFormatter.convertDateTo
import com.mizani.news_compose.R
import com.mizani.news_compose.const.CategoryConst
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.presentation.theme.NewscomposeTheme

@Composable
fun NewsDetailScreen(
    newsDto: NewsDto,
    onBackClicked: () -> Unit = {},
    navigateToWebView: () -> Unit = {},
) {

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
                ImageSection(newsDto = newsDto)
                DetailContentSection(
                    newsDto = newsDto,
                    navigateToWebView = navigateToWebView
                )
            }
            BackButton(
                onBackClicked = onBackClicked
            )
        }
    }
}

@Composable
private fun DetailContentSection(
    newsDto: NewsDto,
    navigateToWebView: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier
            .height(20.dp)
            .fillMaxWidth())
        CategoryDateSection(newsDto = newsDto)
        Spacer(modifier = Modifier
            .height(20.dp)
            .fillMaxWidth())
        Text(
            text = newsDto.title,
            fontWeight = FontWeight.W700,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier
            .height(10.dp)
            .fillMaxWidth())
        Text(
            text = newsDto.longDescription,
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
                navigateToWebView.invoke()
            }
        )
    }
}

@Composable
private fun BackButton(
    onBackClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(color = Color.White)
                .clickable {
                    onBackClicked.invoke()
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

@Composable
private fun CategoryDateSection(newsDto: NewsDto) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = newsDto.categoryName,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            text = newsDto.date.convertDateTo(),
            fontWeight = FontWeight.W400,
            fontSize = 14.sp
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ImageSection(newsDto: NewsDto) {
    if (newsDto.thumbnail.isEmpty()) {
        Card(
            shape = RoundedCornerShape(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_photo),
                contentDescription = "Image profile",
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .clip(RectangleShape)
                    .size(100.dp),
                contentScale = ContentScale.Crop,
            )
        }
    } else {
        GlideImage(
            model = newsDto.thumbnail,
            contentDescription = null,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
private fun NewsDetailScreenPreview() {
    NewscomposeTheme {
        Surface {
            NewsDetailScreen(
                newsDto = NewsDto(
                    title = "Title",
                    shortDescription = "This is short description",
                    longDescription = "Lorem ipsum dolor amet",
                    date = "2024-09-11T20:55:00Z",
                    thumbnail = "",
                    categoryName = CategoryConst.list[0].second
                )
            )
        }
    }
}