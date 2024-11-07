package com.mizani.news_compose.presentation.component

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mizani.news_compose.data.dto.NewsDto
import com.mizani.news_compose.R
import com.mizani.news_compose.presentation.theme.NewscomposeTheme
import com.mizani.news_compose.utils.DateFormatter.convertDateTo

@Composable
fun NewsItem(
    news: NewsDto,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(all = 4.dp)
            .clickable {
                onClick.invoke()
            }
    ) {
        ImageSection(news = news)
        Spacer(modifier = Modifier.width(10.dp))
        NewsInfo(news)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageSection(news: NewsDto) {

    val imageLoading = rememberSaveable { mutableStateOf(true) }
    val requestListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            imageLoading.value = false
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            imageLoading.value = false
            return false
        }
    }

    if (news.thumbnail.isNullOrEmpty()) {
        NoImageComponent()
    } else {
        Card(
            shape = RoundedCornerShape(8.dp)
        ) {
            GlideImage(
                model = news.thumbnail,
                contentDescription = "Image profile",
                modifier = Modifier
                    .clip(RectangleShape)
                    .size(100.dp),
                contentScale = ContentScale.Crop,
            ) {
                it.load(news.thumbnail)
                    .error(R.drawable.ic_no_photo)
                    .addListener(requestListener)
            }

            if (imageLoading.value) {
                Spacer(
                    modifier = Modifier
                        .size(100.dp)
                        .background(ShimmerLoading()),
                )
            }
        }
    }
}

@Composable
fun NoImageComponent() {
    Card(
        shape = RoundedCornerShape(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_no_photo),
            contentDescription = "Image profile",
            modifier = Modifier
                .clip(RectangleShape)
                .size(100.dp),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun NewsInfo(news: NewsDto) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = news.categoryName,
            fontSize = 11.sp,
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = news.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = news.shortDescription,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = news.date.convertDateTo(),
                fontSize = 11.sp
            )
        }
    }
}

@Preview
@Composable
private fun NewsItemComponentPreview() {
    NewscomposeTheme {
        Surface {
            NewsItem(
                news = NewsDto(
                    title = "Title",
                    longDescription = "",
                    shortDescription = "lorem ipsum dolor amet, lorem ipsum dolor amet, lorem ipsum dolor amet, lorem ipsum dolor amet, lorem ipsum dolor amet, lorem ipsum dolor amet, lorem ipsum dolor amet, lorem ipsum dolor amet.",
                    date = "2024-09-11T20:55:00Z"
                )
            ) {

            }
        }
    }
}