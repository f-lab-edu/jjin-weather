package com.jin.jjinweather.feature.temperature.ui.weathercontent.success

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Dehaze
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.jin.jjinweather.R

@Composable
fun TopMenuAction(onNavigateToDistrict: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton({}) {
            Icon(
                imageVector = Icons.Outlined.Dehaze,
                contentDescription = stringResource(R.string.success_more_icon_desc),
                tint = Color.White
            )
        }
        IconButton(onNavigateToDistrict) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(R.string.success_add_location_icon_secs),
                tint = Color.White
            )
        }
    }
}
