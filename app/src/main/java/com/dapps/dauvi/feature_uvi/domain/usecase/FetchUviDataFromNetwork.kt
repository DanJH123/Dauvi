package com.dapps.dauvi.feature_uvi.domain.usecase

import com.dapps.dauvi.core.util.result.DataError
import com.dapps.dauvi.core.util.result.Result
import com.dapps.dauvi.feature_uvi.domain.repository.IUviRepository
import retrofit2.HttpException

class FetchUviDataFromNetwork(private val repository: IUviRepository) {

    suspend operator fun invoke(latitude: Double, longitude: Double): Result<Unit, DataError.Network> {
        return try {
            Result.Success(repository.loadUviDataForLocation(latitude, longitude))
        } catch (h: HttpException){
            when (h.code()) {
                403 -> Result.Error(DataError.Network.API_CAP_EXCEEDED)
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                500 -> Result.Error(DataError.Network.SERVER_ERROR)
                else -> Result.Error(DataError.Network.UNKNOWN_ERROR)
            }
        }
        catch (e: Exception){
            e.printStackTrace()
            Result.Error(DataError.Network.UNKNOWN_ERROR)
        }
    }

}

