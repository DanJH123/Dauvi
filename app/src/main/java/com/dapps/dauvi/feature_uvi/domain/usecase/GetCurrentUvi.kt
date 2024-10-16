package com.dapps.dauvi.feature_uvi.domain.usecase

import com.dapps.dauvi.feature_uvi.domain.model.Uvi
import com.dapps.dauvi.feature_uvi.domain.repository.IUviRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUvi(private val repository: IUviRepository) {

    operator fun invoke(): Flow<Uvi?> {
        return repository.currentUviFlow
    }

}