package com.tai.mark.creditrating.data

import com.tai.mark.creditrating.data.network.NetworkDataSource
import com.tai.mark.creditrating.di.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
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
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): CreditRatingRepository =
        CreditRatingRepositoryImpl(
            localDataSource = localDataSource,
            networkDataSource = networkDataSource,
            dispatcher,
        )
}