package com.tai.mark.creditrating.data

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object TestDataModule {

    @Singleton
    @Provides
    fun provideCreditRatingRepository(): CreditRatingRepository {
        return StubCreditRatingRepository()
    }
}