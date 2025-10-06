package com.tai.mark.creditrating

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tai.mark.creditrating.CreditRatingScreenTestTags.LOADING_INDICATOR
import com.tai.mark.creditrating.CreditRatingScreenTestTags.RATING_SUMMARY
import com.tai.mark.creditrating.data.CreditRating
import com.tai.mark.creditrating.ui.CreditScoreLoader
import com.tai.mark.creditrating.ui.CreditScoreSummary

@Composable
fun CreditRatingScreen(
    modifier: Modifier = Modifier,
    viewModel: CreditRatingViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { ListTopBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { paddingValues ->
        val paddingModifier = Modifier.padding(paddingValues)
        when (val vm = state.value) {
            CreditRatingUiState.Loading -> LoadingContent(modifier = paddingModifier)

            is CreditRatingUiState.Loaded ->
                CreditRatingContent(
                    creditRating = vm.creditRating,
                    modifier = paddingModifier,
                )

            CreditRatingUiState.Error -> {
                val resources = LocalResources.current
                LaunchedEffect(vm) {
                    snackbarHostState.showSnackbar(
                        message = resources.getString(R.string.load_error),
                        duration = SnackbarDuration.Indefinite,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier
) {
    Box(
        contentAlignment = Center,
        modifier = modifier
            .fillMaxSize()
            .testTag(LOADING_INDICATOR)
    ) {
        CreditScoreLoader()
    }
}

@Composable
private fun CreditRatingContent(
    creditRating: CreditRating,
    modifier: Modifier,
) {
    Box(
        contentAlignment = Center,
        modifier = modifier.fillMaxSize()
    ) {
        CreditScoreSummary(
            score = creditRating.score,
            maxScore = creditRating.maxScore,
            modifier = Modifier.testTag(RATING_SUMMARY)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Use TopAppBar
@Composable
fun ListTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
    )
}

@VisibleForTesting
internal object CreditRatingScreenTestTags {
    private const val PREFIX = "credit_rating_screen_"
    const val LOADING_INDICATOR = "${PREFIX}loading"
    const val RATING_SUMMARY = "${PREFIX}summary"
}