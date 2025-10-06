package com.tai.mark.creditrating.data

import com.tai.mark.creditrating.data.network.NetworkDataSource
import com.tai.mark.creditrating.di.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(): LocalDataSource =
        LocalDataSource()

    @Provides
    @Singleton
    fun provideRepository(
        localDataSource: LocalDataSource,
        networkDataSource: NetworkDataSource,
        @ApplicationScope coroutineScope: CoroutineScope,
    ): CreditRatingRepository =
        CreditRatingRepositoryImpl(
            localDataSource = localDataSource,
            networkDataSource = networkDataSource,
            coroutineScope,
        )
}