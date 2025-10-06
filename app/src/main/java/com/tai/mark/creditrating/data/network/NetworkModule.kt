package com.tai.mark.creditrating.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideKtorClient(): HttpClient =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true    // Only parse the required data for now
                    },
                )
            }
        }

    @Provides
    @Singleton
    fun provideNetworkDataSource(
        client: HttpClient,
    ): NetworkDataSource =
        NetworkDataSourceImpl(client = client)
}