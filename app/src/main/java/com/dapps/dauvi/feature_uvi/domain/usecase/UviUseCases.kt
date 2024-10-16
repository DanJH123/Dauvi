package com.dapps.dauvi.feature_uvi.domain.usecase

data class UviUseCases (
    val clearUviData: ClearUviData,
    val fetchUviDataFromNetwork: FetchUviDataFromNetwork,
    val getCurrentUvi: GetCurrentUvi,
)
