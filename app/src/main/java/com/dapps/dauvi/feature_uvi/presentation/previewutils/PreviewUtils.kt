package com.dapps.dauvi.feature_uvi.presentation.previewutils

import com.dapps.dauvi.core.util.generateEpochTimeInSeconds
import com.dapps.dauvi.feature_uvi.domain.model.UviForecastItem

fun hourlyForecastPreviewData(): List<UviForecastItem> {
    return listOf(
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 0), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 1), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 2), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 3), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 4), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 5), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 6), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 7), uvi = 1.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 8), uvi = 2.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 9), uvi = 3.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 11), uvi = 4.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 12), uvi = 5.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 13), uvi = 5.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 14), uvi = 6.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 15), uvi = 4.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 16), uvi = 3.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 17), uvi = 2.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 18), uvi = 1.0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 19), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 20), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 21), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 22), uvi = .0),
        UviForecastItem(epochTimeSeconds = generateEpochTimeInSeconds(hour = 23), uvi = .0),
    )
}