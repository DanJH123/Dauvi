package com.dapps.dauvi.di

import android.app.Application
import android.content.Context
import android.location.Geocoder
import androidx.room.Room
import com.dapps.dauvi.feature_settings.data.SettingsRepositoryImpl
import com.dapps.dauvi.feature_settings.data.datastore.DataStoreManager
import com.dapps.dauvi.feature_settings.data.datastore.IDataStoreManager
import com.dapps.dauvi.feature_settings.domain.GetCurrentLocationUseCase
import com.dapps.dauvi.feature_settings.domain.GetDailyUviAlertOnFlowUseCase
import com.dapps.dauvi.feature_settings.domain.GetDailyUviAlertTimeValueFlowUseCase
import com.dapps.dauvi.feature_settings.domain.GetHighUviAlertOnFlowUseCase
import com.dapps.dauvi.feature_settings.domain.GetHighUviAlertValueFlowUseCase
import com.dapps.dauvi.feature_settings.domain.GetHighUviValueFlowUseCase
import com.dapps.dauvi.feature_settings.domain.GetLocationFlowUseCase
import com.dapps.dauvi.feature_settings.domain.SetDailyUviAlertOnUseCase
import com.dapps.dauvi.feature_settings.domain.SetDailyUviAlertTimeValueUseCase
import com.dapps.dauvi.feature_settings.domain.SetHighUviAlertOnUseCase
import com.dapps.dauvi.feature_settings.domain.SetHighUviAlertValueUseCase
import com.dapps.dauvi.feature_settings.domain.SetHighUviValueUseCase
import com.dapps.dauvi.feature_settings.domain.SettingsRepository
import com.dapps.dauvi.feature_settings.domain.SettingsUseCases
import com.dapps.dauvi.feature_settings.domain.UpdateLocationUseCase
import com.dapps.dauvi.feature_uvi.data.db.WeatherDatabase
import com.dapps.dauvi.feature_uvi.data.network.IWeatherDataSource
import com.dapps.dauvi.feature_uvi.data.network.WeatherApiDataSource
import com.dapps.dauvi.feature_uvi.domain.repository.IUviRepository
import com.dapps.dauvi.feature_uvi.domain.repository.UviRepository
import com.dapps.dauvi.feature_uvi.domain.usecase.ClearUviData
import com.dapps.dauvi.feature_uvi.domain.usecase.FetchUviDataFromNetwork
import com.dapps.dauvi.feature_uvi.domain.usecase.GetCurrentUvi
import com.dapps.dauvi.feature_uvi.domain.usecase.UviUseCases
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideWeatherApiDataSource(): IWeatherDataSource {
        return WeatherApiDataSource()
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(app: Application): WeatherDatabase {
        return Room.databaseBuilder(
            app,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(dataSource: IWeatherDataSource, db: WeatherDatabase): IUviRepository {
        return UviRepository(dataSource, db.weatherDao)
    }

    @Provides
    @Singleton
    fun provideWeatherUseCases(repository: IUviRepository): UviUseCases {
        return UviUseCases(
            clearUviData = ClearUviData(repository),
            fetchUviDataFromNetwork = FetchUviDataFromNetwork(repository),
            getCurrentUvi = GetCurrentUvi(repository),
        )
    }

    @Provides
    @Singleton
    fun provideDatastoreManager(@ApplicationContext context: Context): IDataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(dataStoreManager: IDataStoreManager): SettingsRepository {
        return SettingsRepositoryImpl(dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSettingsUseCases(
        repository: SettingsRepository,
        @ApplicationContext context: Context,
    ): SettingsUseCases {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val geocoder = Geocoder(context, Locale.getDefault())

        return SettingsUseCases(
            getCurrentLocationUseCase = GetCurrentLocationUseCase(fusedLocationClient, geocoder),
            updateLocation = UpdateLocationUseCase(repository),
            setHighUviValue = SetHighUviValueUseCase(repository),
            setDailyUviAlertOn = SetDailyUviAlertOnUseCase(repository),
            setDailyUviAlertTimeValue = SetDailyUviAlertTimeValueUseCase(repository),
            setHighUviAlertOn = SetHighUviAlertOnUseCase(repository),
            setHighUviAlertValue = SetHighUviAlertValueUseCase(repository),
            getLocationFlow = GetLocationFlowUseCase(repository),
            getHighUviValueFlow = GetHighUviValueFlowUseCase(repository),
            getDailyUviAlertOnFlow = GetDailyUviAlertOnFlowUseCase(repository),
            getDailyUviAlertTimeValueFlow = GetDailyUviAlertTimeValueFlowUseCase(repository),
            getHighUviAlertOnFlow = GetHighUviAlertOnFlowUseCase(repository),
            getHighUviAlertValueFlow = GetHighUviAlertValueFlowUseCase(repository)
        )
    }


}