package com.jin.jjinweather.feature.district.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.jin.jjinweather.R
import com.jin.jjinweather.ui.theme.PointColor
import com.jin.jjinweather.ui.theme.SearchBoxBackgroundColor
import kotlinx.coroutines.launch

@Composable
fun DistrictSearchScreen(onNavigateToTemperature: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            // header
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onNavigateToTemperature) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.tutorial_back_arrow_icon_desc),
                        tint = Color.Black
                    )
                }
                Text(
                    text = stringResource(R.string.district_title),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
            DistrictSearchBottomSheet()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistrictSearchBottomSheet() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var keyword by remember { mutableStateOf("") }

    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        when (scaffoldState.bottomSheetState.currentValue) {
            SheetValue.Expanded -> focusRequester.requestFocus()
            SheetValue.PartiallyExpanded, SheetValue.Hidden -> focusManager.clearFocus()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 120.dp,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetDragHandle = null, // 커스텀 핸들 사용
        sheetShadowElevation = 30.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // 핸들
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .background(Color.LightGray, RoundedCornerShape(2.dp))
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 검색 박스
                // FIXME : 위치 검색 API 연동 필요
                SearchDistrictBox(
                    query = keyword,
                    focusRequester = focusRequester,
                    onQueryChange = { keyword = it },
                    onFocusChanged = { isFocused ->
                        coroutineScope.launch {
                            if (isFocused) {
                                scaffoldState.bottomSheetState.expand()
                            } else {
                                scaffoldState.bottomSheetState.partialExpand()
                                focusManager.clearFocus()
                            }
                        }
                    }
                )
            }
        }
    ) {
        // Weather 영역 리스트
        // FIXME : DB 데이터로 그려줄 예정(임시 작성)
        Column {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PointColor)
                    .padding(
                        start = 12.dp, end = 12.dp,
                        top = 8.dp, bottom = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.NearMe,
                        contentDescription = stringResource(R.string.success_current_location_icon_desc),
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column(modifier = Modifier.padding(start = 4.dp)) {
                    Text(
                        text = stringResource(R.string.district_current_location),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 1.5.em
                    )
                    Text(text = "서울시 서초구", color = Color.White, fontSize = 12.sp, lineHeight = 1.5.em)
                }
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.ic_main_clear_sky_day),
                    contentDescription = stringResource(R.string.success_current_temperature_icon_desc),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "27",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Text(
                text = stringResource(R.string.district_added_location),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 20.dp)
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PointColor)
                    .padding(
                        start = 12.dp, end = 12.dp,
                        top = 12.dp, bottom = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.padding(start = 4.dp)) {
                    Text(
                        text = "도쿄 도",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 1.5.em
                    )
                }
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.ic_main_clear_sky_day),
                    contentDescription = stringResource(R.string.success_current_temperature_icon_desc),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "27",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SearchDistrictBox(
    query: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(SearchBoxBackgroundColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.district_search_icon_desc),
            tint = Color.Gray,
            modifier = Modifier.padding(end = 8.dp)
        )

        // 입력 필드
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged(it.isFocused) },
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = stringResource(R.string.district_search_hint),
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )
    }
}
