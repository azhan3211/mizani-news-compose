package com.mizani.news_compose.presentation.screen.webview

import android.app.Activity
import android.content.pm.ActivityInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.mizani.news_compose.R
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NewsWebView(
    navController: NavController,
    url: String
) {

    val webViewState = rememberWebViewState(url = url)

    val activity = (LocalContext.current as Activity)
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    Scaffold(
        topBar =  {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
            }
        }
    ) { padding ->
        WebView(
            state = webViewState,
            modifier = Modifier.padding(padding)
        )
    }
}