package com.tai.mark.creditrating

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tai.mark.creditrating.data.DEFAULT_CREDIT_RATING
import com.tai.mark.creditrating.data.StubCreditRatingRepository
import com.tai.mark.creditrating.ui.theme.CreditRatingTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("TestFunctionName") // Allow GIVEN WHEN THEN naming without warnings
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@ExperimentalCoroutinesApi
class CreditRatingScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    val repository = StubCreditRatingRepository()

    private val screenRobot = CreditRatingScreenRobot(composeTestRule)

    fun setup() {
        val vm = CreditRatingViewModel(repository = repository)
        composeTestRule.setContent {
            CreditRatingTheme {
                CreditRatingScreen(
                    viewModel = vm,
                )
            }
        }
    }

    @Test
    fun WHEN_started_THEN_show_loading() {
        setup()

        screenRobot.verifyLoadingShown()
    }

    @Test
    fun GIVEN_credit_data_THEN_loading_not_shown() = runTest {
        setup()
        repository.emitCreditRating(DEFAULT_CREDIT_RATING)

        screenRobot.verifyLoadingHidden()
    }

    @Test
    fun GIVEN_credit_data_THEN_show_list() = runTest {
        setup()
        repository.emitCreditRating(DEFAULT_CREDIT_RATING)

        screenRobot.verifySummaryShown(DEFAULT_CREDIT_RATING)
    }

    @Test
    fun WHEN_error_loading_data_THEN_show_error_message() = runTest {
        repository.shouldError = true
        setup()

        screenRobot.verifyErrorMessageShown()
    }
}