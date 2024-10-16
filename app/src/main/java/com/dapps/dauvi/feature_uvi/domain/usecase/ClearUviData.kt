package com.dapps.dauvi.feature_uvi.domain.usecase

import com.dapps.dauvi.feature_uvi.domain.repository.IUviRepository

class ClearUviData(private val repository: IUviRepository) {

    suspend operator fun invoke() {
        repository.clearUviData()
    }

}