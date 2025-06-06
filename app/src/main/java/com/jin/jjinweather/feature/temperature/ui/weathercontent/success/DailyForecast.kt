package com.jin.jjinweather.feature.temperature.ui.weathercontent.success

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.jin.jjinweather.R
import com.jin.jjinweather.feature.temperature.ui.FormattedDate
import com.jin.jjinweather.feature.weather.domain.model.DailyForecast
import com.jin.jjinweather.ui.theme.DefaultTemperatureColor
import com.jin.jjinweather.ui.theme.TemperatureGraphBackgroundColor
import com.jin.jjinweather.ui.theme.TemperatureColors
import java.util.Calendar
import java.util.Locale

@Composable
fun DailyForecast(modifier: Modifier, backgroundColor: Color, dailyWeatherList: List<DailyForecast>) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
    ) {
        Column {
            DailyHeader()
            HorizontalDivider(thickness = 1.dp)
            val absoluteMinTemperature = dailyWeatherList
                .map { it.temperatureRange.min }
                .minOfOrNull { it.toInt() } ?: 0
            val absoluteMaxTemperature = dailyWeatherList
                .map { it.temperatureRange.max }
                .maxOfOrNull { it.toInt() } ?: 30
            for (i in dailyWeatherList.indices) {
                DailyItem(dailyWeatherList[i], absoluteMinTemperature, absoluteMaxTemperature)
                if (i != dailyWeatherList.lastIndex) {
                    HorizontalDivider(thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
private fun DailyHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.CalendarToday,
            contentDescription = stringResource(R.string.success_daily_forecast_icon_desc),
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = stringResource(R.string.success_daily_forecast_title),
            fontSize = 14.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
private fun DailyItem(forecast: DailyForecast, absoluteMinTemp: Int, absoluteMaxTemp: Int) {
    val formattedDate = formatDateOrToday(forecast.date)
    val dailyMinTemp = forecast.temperatureRange.min.toInt()
    val dailyMaxTemp = forecast.temperatureRange.max.toInt()
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                DayOfWeekText(formattedDate)
            }
            Image(
                painter = painterResource(forecast.icon.drawableRes),
                contentDescription = stringResource(R.string.success_daily_forecast_temperature_icon_desc)
            )
            TemperatureGraph(dailyMinTemp, dailyMaxTemp, absoluteMinTemp, absoluteMaxTemp)
        }
    }
}

@Composable
private fun DayOfWeekText(formattedDate: FormattedDate) {
    when (formattedDate) {
        is FormattedDate.Today -> Text(
            text = stringResource(formattedDate.labelRes),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        is FormattedDate.Date -> {
            Text(
                text = formattedDate.dayOfWeek,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 1.5.em
            )
            Text(
                text = stringResource(
                    R.string.success_daily_forecast_month_day,
                    formattedDate.month,
                    formattedDate.day
                ),
                color = Color.LightGray,
                fontSize = 10.sp,
                lineHeight = 1.5.em
            )
        }
    }
}

@Composable
private fun TemperatureGraph(dailyMinTemp: Int, dailyMaxTemp: Int, absoluteMinTemp: Int, absoluteMaxTemp: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = stringResource(R.string.success_temperature, dailyMinTemp),
            color = Color.LightGray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier
                .width(36.dp)
                .padding(end = 4.dp)
        )
        BoxWithConstraints(
            modifier = Modifier
                .width(130.dp)
                .height(10.dp)
                .background(
                    color = TemperatureGraphBackgroundColor,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            // 가로 길이를 확인.
            val boxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }

            val tempRange = (absoluteMaxTemp - absoluteMinTemp).takeIf { it != 0 } ?: 1

            // 온도 그래프의 시작점과 끝점을 구하기 위하여 요일의 최저/최고 온도와 모든 요일의 최저/최고 온도로 padding 값을 구한다.
            val startRatio = (dailyMinTemp - absoluteMinTemp).toFloat() / tempRange
            val endRatio = (absoluteMaxTemp - dailyMaxTemp).toFloat() / tempRange

            val startPx = boxWidthPx * startRatio
            val endPx = boxWidthPx * endRatio

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(
                        start = with(LocalDensity.current) { startPx.toDp() },
                        end = with(LocalDensity.current) { endPx.toDp() }
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                TemperatureColors[dailyMinTemp] ?: DefaultTemperatureColor,
                                TemperatureColors[dailyMaxTemp] ?: DefaultTemperatureColor
                            )
                        ),
                        shape = RoundedCornerShape(30.dp)
                    )
            )
        }
        Text(
            text = stringResource(R.string.success_temperature, dailyMaxTemp),
            color = Color.LightGray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 4.dp)
                .width(32.dp)
        )
    }
}

private fun formatDateOrToday(date: Calendar): FormattedDate {
    val today = Calendar.getInstance()
    val isToday = today.get(Calendar.YEAR) == date.get(Calendar.YEAR)
            && today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
    return if (isToday) {
        FormattedDate.Today(R.string.success_daily_forecast_today)
    } else {
        val month = date.get(Calendar.MONTH) + 1
        val day = date.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()) ?: ""
        FormattedDate.Date(dayOfWeek, month, day)
    }
}
