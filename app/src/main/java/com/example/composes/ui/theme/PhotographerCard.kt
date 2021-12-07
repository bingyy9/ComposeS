package com.example.composes.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composes.Message
import com.example.composes.R

@Composable
fun PhotographerCard (modifier: Modifier = Modifier){
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = MaterialTheme.colors.surface)//圆角
            .clickable(onClick = {})
            .padding(16.dp)
    ) {
        //圆形
        Surface(modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            ) {
            Image(
                painter = painterResource(id = R.drawable.ic_document_filled),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = "Alfred Sisley", fontWeight = FontWeight.Bold)
            //LocalContentAlpha 由 ContentAlpha.medium提供，然后隐式传参通过CompositionLocal，里面Text的alpha就是 media alpha
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = "3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview
@Composable
fun PreviewPhotographerCard(){
    PhotographerCard()
}