package com.dapps.dauvi.feature_uvi.presentation.uvi

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapps.dauvi.R
import com.dapps.dauvi.core.util.HOUR_IN_SECONDS
import com.dapps.dauvi.core.util.LocationDs
import com.dapps.dauvi.core.util.generateEpochTimeInSeconds
import com.dapps.dauvi.core.util.result.DataError
import com.dapps.dauvi.core.util.result.Result
import com.dapps.dauvi.core.util.roundDownToHour
import com.dapps.dauvi.core.util.roundUpToHour
import com.dapps.dauvi.core.util.secondsToDateString
import com.dapps.dauvi.core.util.text.UiText
import com.dapps.dauvi.feature_settings.domain.SettingsRepository
import com.dapps.dauvi.feature_uvi.domain.model.UviForecastItem
import com.dapps.dauvi.feature_uvi.domain.model.Uvi
import com.dapps.dauvi.feature_uvi.domain.usecase.UviUseCases
import com.dapps.dauvi.feature_uvi.presentation.Constants
import com.dapps.dauvi.feature_uvi.presentation.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class UviViewModel @Inject constructor(
    private val uviUseCases: UviUseCases,
    private val settingsRepository: SettingsRepository
): ViewModel() {

    private var _uiState: MutableStateFlow<UviUiState> = MutableStateFlow(UviUiState.Initial)
    val uiState: Flow<UviUiState> = _uiState.asStateFlow()

    val uvi: Flow<Uvi?> = uviUseCases.getCurrentUvi()
    val hourlyForecast: Flow<List<UviForecastItem>?> = uvi.map {
        getDaytimeHourlyForecast(it)
    }

    val sunrise: Flow<Long?> = uvi.map{ it?.sunrise }
    val defaultSunrise = generateEpochTimeInSeconds(Constants.DEFAULT_SUNRISE_HOUR)
    val sunset: Flow<Long?> = uvi.map { it?.sunset }
    val defaultSunset = generateEpochTimeInSeconds(Constants.DEFAULT_SUNSET_HOUR)

    val currentUvi: Flow<Double?> = uvi.map { it?.uvi }
    val currentUviText: Flow<UiText?> = uvi.map {
        if(it != null) UiText.StringResource(R.string.uvi_value, arrayOf(it.uvi.roundToInt()))
        else null
    }

    val highUviTimeRange: Flow<UiText> =
        combine(uvi, settingsRepository.highUviValueFlow){ uvi, highUviValue ->
            return@combine highUviTimeRange(
                highUviValue ?: Constants.DEFAULT_HIGH_UVI, uvi?.hourlyForecast
            )
        }

    val locationDateString: Flow<UiText> =
        combine(uvi, settingsRepository.locationFlow) { weather, locationDs ->
            return@combine cityLocationText(weather, locationDs)
        }

    init {
        loadWeatherData()
    }

    fun loadWeatherData() = fetchWeatherData()

    private fun getDaytimeHourlyForecast(uvi: Uvi?): List<UviForecastItem>? {
        val dayStart = uvi?.sunrise?.roundDownToHour()
        val dayEnd = uvi?.sunset?.roundUpToHour()
        return uvi?.hourlyForecast?.filter {
            it.epochTimeSeconds in (dayStart ?: 0).. (dayEnd ?: 0)
        }
    }

    private fun cityLocationText(uvi: Uvi?, locationDs: LocationDs?): UiText {
        val city = locationDs?.city
        val lastUpdated = uvi?.lastUpdated?.secondsToDateString()

        return when {
            city.isNullOrEmpty() && lastUpdated == null ->
                UiText.Empty
            !city.isNullOrEmpty() && lastUpdated == null -> UiText.StringResource(
                R.string.city_text, arrayOf(city)
            )
            city.isNullOrEmpty() && lastUpdated != null -> UiText.StringResource(
                R.string.last_updated_text, arrayOf(lastUpdated)
            )
            else -> UiText.StringResource(
                R.string.city_location_text, arrayOf(lastUpdated!!, city!!)
            )
        }
    }

    private fun highUviTimeRange(highUviValue: Int, forecast: List<UviForecastItem>?): UiText {
        if (forecast.isNullOrEmpty()) return UiText.Empty

        val startTimeString = getHighUviStart(highUviValue, forecast)?.secondsToDateString(pattern = "ha")?.toLowerCase(Locale.current)
        val endTimeString = getHighUviEnd(highUviValue, forecast)?.secondsToDateString(pattern = "ha")?.toLowerCase(Locale.current)

        return when {
            startTimeString == null && endTimeString == null -> UiText.StringResource(R.string.no_high_range_today)
            startTimeString == null -> UiText.StringResource(R.string.uvi_high_until, arrayOf(endTimeString!!))
            endTimeString == null -> UiText.StringResource(R.string.uvi_high_from, arrayOf(startTimeString))
            else -> UiText.StringResource(
                R.string.uvi_high_range, arrayOf(startTimeString, endTimeString)
            )
        }
    }

    private fun getHighUviStart(highUviValue: Int, forecast: List<UviForecastItem>): Long? {
        val index = forecast.indexOfFirst { it.uvi.roundToInt() >= highUviValue }
        return if (index == -1) null
        else forecast[index].epochTimeSeconds
    }

    private fun getHighUviEnd(highUviValue: Int, forecast: List<UviForecastItem>): Long? {
        val index = forecast.indexOfLast { it.uvi.roundToInt() >= highUviValue }

        // adding one hour to the end time includes the final hour of high UVI
        return if (index == -1) null
        else forecast[index].epochTimeSeconds + HOUR_IN_SECONDS
    }

    private fun fetchWeatherData(){
        viewModelScope.launch {
            _uiState.emit(UviUiState.FetchingWeather)
            val locationDs: LocationDs? = settingsRepository.locationFlow.firstOrNull()
            if (locationDs != null) {
                handleFetchWeatherResult(uviUseCases.fetchUviDataFromNetwork(
                    latitude = locationDs.latitude,
                    longitude = locationDs.longitude
                ))
                println()
            }
            else {
                _uiState.emit(
                    UviUiState.WeatherLoadFail(UiText.StringResource(R.string.location_not_set))
                )
            }
        }
    }

    private suspend fun handleFetchWeatherResult(result: Result<Unit, DataError.Network>) {
        _uiState.emit(
            when (result) {
                is Result.Error -> UviUiState.WeatherLoadFail(result.error.asUiText())
                is Result.Success -> UviUiState.WeatherLoaded
            }
        )
    }
}

