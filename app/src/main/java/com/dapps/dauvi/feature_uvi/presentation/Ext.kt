package com.dapps.dauvi.feature_uvi.presentation

import androidx.compose.ui.graphics.Color
import com.dapps.dauvi.R
import com.dapps.dauvi.core.util.result.DataError
import com.dapps.dauvi.core.util.text.UiText
import kotlin.math.roundToInt


fun DataError.Network.asUiText(): UiText {
    return when(this){
        DataError.Network.REQUEST_TIMEOUT ->
            UiText.StringResource(R.string.request_timeout)
        DataError.Network.NO_INTERNET_CONNECTION ->
            UiText.StringResource(R.string.no_internet_connection)
        DataError.Network.SERVER_ERROR ->
            UiText.StringResource(R.string.server_error)
        DataError.Network.API_CAP_EXCEEDED ->
            UiText.StringResource(R.string.api_cap_exceeded)
        DataError.Network.UNKNOWN_ERROR ->
            UiText.StringResource(R.string.unknown_error)
    }
}

fun Double.toUviColor(): Color {
    val uvi = this.roundToInt()
    return when {
        uvi == 0 -> LightGrey
        uvi < 3 -> LightBlue
        uvi <= 5 -> Yellow
        uvi <= 7 -> Orange
        uvi <= 10 -> Red
        else -> DeepRed
    }
}