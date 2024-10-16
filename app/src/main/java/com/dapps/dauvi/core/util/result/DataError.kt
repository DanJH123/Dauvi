package com.dapps.dauvi.core.util.result

import com.dapps.dauvi.R
import com.dapps.dauvi.core.util.text.UiText

sealed interface DataError: Error {

    enum class Network: DataError {
        REQUEST_TIMEOUT,
        NO_INTERNET_CONNECTION,
        SERVER_ERROR,
        API_CAP_EXCEEDED,
        UNKNOWN_ERROR
    }

}