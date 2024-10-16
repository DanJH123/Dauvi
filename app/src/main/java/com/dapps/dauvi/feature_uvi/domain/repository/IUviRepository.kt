package com.dapps.dauvi.feature_uvi.domain.repository

import com.dapps.dauvi.feature_uvi.domain.model.Uvi
import kotlinx.coroutines.flow.Flow

interface IUviRepository {

    suspend fun loadUviDataForLocation(latitude: Double, longitude: Double)

    suspend fun clearUviData()

    val currentUviFlow: Flow<Uvi?>
}