package com.dapps.dauvi.feature_uvi.presentation.forecast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dapps.dauvi.core.util.secondsToDateString
import com.dapps.dauvi.feature_uvi.domain.model.UviForecastItem
import com.dapps.dauvi.feature_uvi.presentation.previewutils.hourlyForecastPreviewData
import com.dapps.dauvi.feature_uvi.presentation.toUviColor
import kotlin.math.roundToInt

@Composable
fun ForecastView(
    title: String,
    hourlyForecast: List<UviForecastItem>?,
    modifier: Modifier = Modifier,
) {
    Column (
        modifier
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
            .padding(top = 16.dp, bottom = 32.dp)
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(hourlyForecast != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp)
            ) {

                hourlyForecast!!.forEach { item ->
                    ForecastItemView(
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(color = item.uvi.toUviColor(), shape = CircleShape)
                            )
                        },
                        label = "${item.uvi.roundToInt()}",
                        time = item.epochTimeSeconds
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastItemView(
    icon: @Composable () -> Unit,
    label: String,
    time: Long,
){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        icon()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 13.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = time.secondsToDateString(pattern = "HH:mm"),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForecastViewPreview(){
    ForecastView(
        title = "Hourly UVI Forecast",
        hourlyForecast = hourlyForecastPreviewData(),
    )
}