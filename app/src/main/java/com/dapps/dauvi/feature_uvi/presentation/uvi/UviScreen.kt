package com.dapps.dauvi.feature_uvi.presentation.uvi

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dapps.dauvi.R
import com.dapps.dauvi.core.util.currentTimeSeconds
import com.dapps.dauvi.core.util.generateEpochTimeInSeconds
import com.dapps.dauvi.core.util.secondsToDateString
import com.dapps.dauvi.feature_uvi.domain.model.UviForecastItem
import com.dapps.dauvi.feature_uvi.presentation.LightGrey
import com.dapps.dauvi.feature_uvi.presentation.Orange
import com.dapps.dauvi.feature_uvi.presentation.Yellow
import com.dapps.dauvi.feature_uvi.presentation.forecast.ForecastView
import com.dapps.dauvi.feature_uvi.presentation.previewutils.hourlyForecastPreviewData
import com.dapps.dauvi.feature_uvi.presentation.toUviColor
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt

@Composable
fun UviScreen(
    navController: NavController,
    viewModel: UviViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(UviUiState.Initial)
    val weather by viewModel.uvi.collectAsStateWithLifecycle(null)
    val hourlyForecast by viewModel.hourlyForecast.collectAsStateWithLifecycle(null)
    val currentUvi by viewModel.currentUvi.collectAsStateWithLifecycle(null)
    val sunrise by viewModel.sunrise.collectAsStateWithLifecycle(0)
    val sunset by viewModel.sunset.collectAsStateWithLifecycle(0)

    val currentUviText by viewModel.currentUviText
        .map { it?.asString(context) ?: "" }
        .collectAsStateWithLifecycle("")

    val highUviTimeRange by viewModel.highUviTimeRange
        .map { it.asString(context) }
        .collectAsStateWithLifecycle("")

    val locationDateTime by viewModel.locationDateString
        .map { it.asString(context) }
        .collectAsStateWithLifecycle("")

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        val state = uiState
        when {
            (state is UviUiState.Initial || state is UviUiState.FetchingWeather)
                    && weather == null -> {
                CircularProgressIndicator()
                Text(
                    text = stringResource(id = R.string.uvi_fetching_weather_data),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            state is UviUiState.WeatherLoadFail && weather == null -> {
                Text(
                    text = state.message.asString(context),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = { viewModel.loadWeatherData() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.uvi_retry_button),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            else -> {
                PageBody(
                    currentUvi = currentUvi,
                    sunrise = sunrise,
                    defaultSunrise = viewModel.defaultSunrise,
                    sunset = sunset,
                    defaultSunset = viewModel.defaultSunset,
                    currentUviText = currentUviText,
                    highUviTimeRange = highUviTimeRange,
                    locationDateTime = locationDateTime,
                    hourlyForecast = hourlyForecast,
                )
            }
        }
    }
}

@Composable
fun PageBody(
    currentUvi: Double?,
    sunrise: Long?,
    defaultSunrise: Long,
    sunset: Long?,
    defaultSunset: Long,
    currentUviText: String,
    highUviTimeRange: String,
    locationDateTime: String,
    hourlyForecast: List<UviForecastItem>?,
    modifier: Modifier = Modifier,
) {
    Column (
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            UviCircle(currentUvi ?: 0.0)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentUviText,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )
                UviGauge(
                    sunriseTimeSeconds = sunrise ?: defaultSunrise,
                    sunsetTimeSeconds = sunset ?: defaultSunset,
                    currentTimeSeconds = currentTimeSeconds(),
                    currentTimeColour = Yellow,
                    showSunTimes = sunset != null && sunrise != null,
                    forecastItems = hourlyForecast ?: emptyList(),
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = highUviTimeRange,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = locationDateTime,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.End),
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.onBackground,
            )
            ForecastView(
                title = stringResource(id = R.string.uvi_hourly_forecast_title),
                hourlyForecast
            )
        }
    }
}

@Composable
fun UviCircle(uvi: Double, modifier: Modifier = Modifier) {
    val baseColor = uvi.toUviColor()
    val size = 50
    val pulseSize = 500f
    val speedMillis = 10000
    val secondarySpeed = (speedMillis * 0.75f).roundToInt()

    val infiniteTransition = rememberInfiniteTransition(
        label = "uvi_circle_it",
    )

    val animatedRadius1 by infiniteTransition.animateFloat(
        label = "uvi_circle_radius1",
        initialValue = 0f,
        targetValue = pulseSize,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = speedMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedRadius2 by infiniteTransition.animateFloat(
        label = "uvi_circle_radius2",
        initialValue = 0f,
        targetValue = pulseSize,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = secondarySpeed, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedAlpha1 by infiniteTransition.animateFloat(
        label = "uvi_circle_alpha1",
        initialValue = 0.5f,
        targetValue = 0f, // Fade out to invisible
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = speedMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedAlpha2 by infiniteTransition.animateFloat(
        label = "uvi_circle_alpha2",
        initialValue = 0.5f,
        targetValue = 0f, // Fade out to invisible
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = secondarySpeed, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.size(size.dp)) {

        drawCircle(
            color = baseColor.copy(alpha = animatedAlpha1),
            radius = animatedRadius1,
            style = Fill
        )
        drawCircle(
            color = baseColor.copy(alpha = animatedAlpha2),
            radius = animatedRadius2,
            style = Fill
        )

        drawCircle(
            color = baseColor,
            radius = size.dp.toPx() / 2,
            style = Fill
        )
    }
}

@Composable
fun GradientTestBox() {
    Box(
        modifier = Modifier
            .size(200.dp, 50.dp)
            .drawBehind {
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Red, Color.Blue)
                    ),
                    size = size,
                    cornerRadius = CornerRadius(25f, 25f)
                )
            }
    )
}

@Composable
fun UviGauge(
    sunriseTimeSeconds: Long,
    sunsetTimeSeconds: Long,
    currentTimeSeconds: Long,
    currentTimeColour: Color,
    showSunTimes: Boolean,
    forecastItems: List<UviForecastItem>,
    modifier: Modifier = Modifier,
) {

    fun Long.toPositionFraction(rangeStart: Long, rangeEnd: Long): Float {
        val range: Long = rangeEnd - rangeStart
        val offset: Long = this - rangeStart
        return offset.toFloat() / range.toFloat()
    }

    val currentTimeFraction: Float? =
        if (currentTimeSeconds in (sunriseTimeSeconds + 1) until sunsetTimeSeconds) {
            currentTimeSeconds.toPositionFraction(
                rangeStart = sunriseTimeSeconds,
                rangeEnd = sunsetTimeSeconds
            )
        } else null

    val trackHeight = 12.dp
    val markerRadius = 12.dp
    val sunIconSize = 32.dp

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.sun_horizon),
                contentDescription = stringResource(id = R.string.uvi_sunrise_description),
                tint = Orange,
                modifier = Modifier
                    .size(sunIconSize)
                    .padding(bottom = 8.dp),
            )
            if (showSunTimes) Text(
                text = sunriseTimeSeconds.secondsToDateString(pattern = "HH:mm"),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        val totalTime = sunsetTimeSeconds - sunriseTimeSeconds

        // Step 1: Prepare the initial color stops from forecast items
        val colorStops = mutableListOf(0f to LightGrey)
        for (item in forecastItems) {
            val timeFraction = ((item.epochTimeSeconds - sunriseTimeSeconds).toFloat() / totalTime)
                .coerceIn(0f, 1f)
            val color = item.uvi.toUviColor()
            colorStops.add(timeFraction to color)
        }

        val gaugeBrush =
            if (colorStops.size <= 1) SolidColor(colorStops[0].second)
            else Brush.horizontalGradient(colorStops = colorStops.toTypedArray())

        Box(modifier = Modifier
            .weight(1f)
            .padding(16.dp)
            .height(trackHeight)
            .drawBehind {
                val midHeight = size.height / 2

                val trackHeightToPx = trackHeight.toPx()
                drawRoundRect(
                    brush = gaugeBrush,
                    topLeft = Offset(0f, midHeight - trackHeightToPx / 2),
                    size = Size(size.width, trackHeightToPx),
                    cornerRadius = CornerRadius(trackHeightToPx / 2, trackHeightToPx / 2)
                )

                if (currentTimeFraction != null)
                    drawCircle(
                        color = currentTimeColour,
                        radius = markerRadius.toPx(),
                        center = Offset(x = size.width * currentTimeFraction, y = midHeight)
                    )
            }
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.sun_horizon),
                contentDescription = stringResource(id = R.string.uvi_sunset_description),
                tint = Orange,
                modifier = Modifier
                    .size(sunIconSize)
                    .padding(bottom = 8.dp),
            )
            if (showSunTimes) Text(
                text = sunsetTimeSeconds.secondsToDateString(pattern = "HH:mm"),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PageBodyPreview() {
    PageBody(
        currentUvi = 3.0,
        sunrise = generateEpochTimeInSeconds(hour = 6),
        defaultSunrise = generateEpochTimeInSeconds(hour = 10),
        sunset = generateEpochTimeInSeconds(hour = 20),
        defaultSunset = generateEpochTimeInSeconds(hour = 17),
        currentUviText = "3 UVI",
        highUviTimeRange = "High between 11am - 2pm",
        locationDateTime = "Melbourne, 8:45AM 27 Aug",
        hourlyForecast = hourlyForecastPreviewData(),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUviCircle() {
    UviCircle(uvi = 3.0)
}

@Preview(showBackground = true)
@Composable
fun DayTimeGaugePreview() {
    UviGauge(
        sunriseTimeSeconds = generateEpochTimeInSeconds(hour = 6),
        sunsetTimeSeconds = generateEpochTimeInSeconds(hour = 20),
        currentTimeSeconds = currentTimeSeconds(),
        currentTimeColour = Yellow,
        forecastItems = hourlyForecastPreviewData(),
        showSunTimes = true,
    )
}
