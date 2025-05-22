package com.jin.jjinweather.feature.outfitrecommend.ui.recommendcontent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jin.jjinweather.R

@Composable
fun CityNameAndWeatherSummary(cityName: String, summary: String) {
    Column(
        modifier = Modifier.padding(
            horizontal = 20.dp,
            vertical = 10.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.NearMe,
                contentDescription = stringResource(R.string.success_current_temperature_icon_desc),
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
            Text(cityName)
        }
        Text(
            text = summary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
