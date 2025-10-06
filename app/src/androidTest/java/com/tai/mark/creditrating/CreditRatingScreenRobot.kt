package com.tai.mark.creditrating

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.tai.mark.creditrating.data.CreditRating
import com.tai.mark.creditrating.ui.SummaryTestTags

class CreditRatingScreenRobot(
    private val interactionProvider: SemanticsNodeInteractionsProvider,
) {

    private val loadingProgress
        get() = interactionProvider.onNodeWithTag(CreditRatingScreenTestTags.LOADING_INDICATOR)

    private val summary
        get() = interactionProvider.onNodeWithTag(
            CreditRatingScreenTestTags.RATING_SUMMARY,
            useUnmergedTree = true, // Allow separate composables to be reachable in the tests
        )

    private val scoreText
        get() = summary.onChildren().filterToOne(hasTestTag(SummaryTestTags.SCORE))

    private val maxScoreText
        get() = summary.onChildren().filterToOne(hasTestTag(SummaryTestTags.MAX_SCORE))

    fun verifyLoadingShown() {
        loadingProgress.assertIsDisplayed()
    }

    fun verifyLoadingHidden() {
        loadingProgress.assertDoesNotExist()
    }

    fun verifySummaryShown(rating: CreditRating) {
        summary.assertIsDisplayed()
        scoreText.assertTextEquals(rating.score.toString())
        maxScoreText.assertTextEquals(
            InstrumentationRegistry.getInstrumentation()
                .targetContext
                .getString(R.string.credit_score_suffix, rating.maxScore)
        )
    }

    fun verifyErrorMessageShown() {
        interactionProvider.onNodeWithText(
            text = InstrumentationRegistry.getInstrumentation()
                .targetContext
                .getString(R.string.load_error)
        ).assertIsDisplayed()
    }
}