package com.jin.jjinweather.feature.outfitrecommend.ui.recommendcontent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jin.jjinweather.R
import com.jin.jjinweather.ui.theme.ButtonColor

@Composable
fun OutfitError() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 20.dp),
            painter = painterResource(R.drawable.img_outfit_error),
            contentDescription = stringResource(R.string.outfit_error_img_desc)
        )
        Text(
            text = stringResource(R.string.outfit_error),
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 20.dp)
        )
        //FIXME : 재요청?
        Box(
            modifier = Modifier
                .clickable { }
                .clip(RoundedCornerShape(8.dp))
                .background(ButtonColor)
                .padding(horizontal = 4.dp),
        ) {
            Text(
                text = stringResource(R.string.outfit_error_button_retry),
                fontSize = 10.sp,
                color = Color.White
            )
        }
    }
}
