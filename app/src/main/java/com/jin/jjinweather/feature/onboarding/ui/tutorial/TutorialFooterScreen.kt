package com.jin.jjinweather.feature.onboarding.ui.tutorial


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jin.jjinweather.R
import com.jin.jjinweather.ui.theme.ButtonColor
import com.jin.jjinweather.ui.theme.FeatureBackgroundColor
import com.jin.jjinweather.ui.theme.HighlightFeatureBackgroundColor
import com.jin.jjinweather.ui.theme.JJinWeatherTheme
import com.jin.jjinweather.ui.theme.PointColor
import com.jin.jjinweather.ui.theme.WelcomeBackgroundColor

@Composable
fun TutorialFooterScreen(currentPage: Int, onRequestPermission: () -> Unit) {
    val backgroundColor = when (currentPage) {
        0 -> WelcomeBackgroundColor
        1 -> FeatureBackgroundColor
        else -> HighlightFeatureBackgroundColor
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(
                buildAnnotatedString {
                    append(stringResource(R.string.tutorial_content_before))
                    withStyle(style = SpanStyle(PointColor)) {
                        append(stringResource(R.string.tutorial_content_highlight))
                    }
                    append(stringResource(R.string.tutorial_content_after))
                },
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 10.dp),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
            Button(
                onClick = onRequestPermission,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = ButtonColor,
                )
            ) {
                Text(stringResource(R.string.tutorial_button_start), modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}


@Composable
@Preview
fun PermissionScreenPreview() {
    JJinWeatherTheme {
        TutorialFooterScreen(0, {})
    }
}
