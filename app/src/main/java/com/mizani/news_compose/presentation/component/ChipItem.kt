package com.mizani.news_compose.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChipItem(text: String, index: Int, isSelected: Boolean = false, onClick: (Int) -> Unit = {}) {
    OutlinedButton(
        modifier = Modifier
            .clip(RectangleShape)
            .height(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) Color.Black else Color.White
        ),
        onClick = {
              onClick.invoke(index)
        },
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.W400,
            color = if (isSelected) Color.White else Color.Black,
            modifier = Modifier.padding(horizontal = 10.dp),
        )
    }
}