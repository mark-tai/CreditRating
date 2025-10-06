package com.tai.mark.creditrating.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tai.mark.creditrating.R
import com.tai.mark.creditrating.ui.SummaryTestTags.MAX_SCORE
import com.tai.mark.creditrating.ui.SummaryTestTags.SCORE
import com.tai.mark.creditrating.ui.theme.CreditRatingTheme
import com.tai.mark.creditrating.ui.theme.ScoreColours
import kotlin.math.roundToInt

@Composable
fun CreditScoreLoader(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val max = 1000f
    val scoreProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = max, infiniteRepeatable(tween(1000))
    )
    CreditScoreUi(
        score = scoreProgress.roundToInt(),
        maxScore = max.roundToInt(),
        content = {},
        modifier = modifier,
    )
}

@Composable
fun CreditScoreSummary(
    score: Int,
    maxScore: Int,
    modifier: Modifier = Modifier
) {
    val animatedScore = remember { Animatable(0f) }
    LaunchedEffect(score, maxScore) {
        animatedScore.animateTo(score.toFloat(), tween(500))
    }
    CreditScoreUi(
        score = animatedScore.value.roundToInt(),
        maxScore = maxScore,
        content = { scoreProgress ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Center)
            ) {
                Text(
                    text = stringResource(R.string.credit_score_prefix),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = score.toString(),
                    color = scoreProgress.toTextColor(),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.testTag(SCORE),
                )
                Text(
                    text = stringResource(R.string.credit_score_suffix, maxScore),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag(MAX_SCORE),
                )
            }
        },
        modifier = modifier.semantics(mergeDescendants = true) {}, // Ensure rating summary is read out as one string
    )
}

@Composable
private fun CreditScoreUi(
    score: Int,
    maxScore: Int,
    content: @Composable BoxScope.(Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scoreBrush = remember { createScoreBrush() }
    val scoreProgress = score.toFloat() / maxScore
    val outlineColor = MaterialTheme.colorScheme.outline
    Box(
        modifier = modifier
            .size(200.dp)
            .drawBehind {
                drawCircle(color = outlineColor, style = Stroke(width = 1.dp.toPx()))
            }
            .padding(5.dp)
            .drawBehind {
                drawArc(
                    brush = scoreBrush,
                    startAngle = -90f,
                    sweepAngle = scoreProgress * 360,
                    useCenter = false,
                    style = Stroke(width = 2.dp.toPx())
                )
            }) {
        content(scoreProgress)
    }
}

private fun createScoreBrush(): Brush {
    val zeroColour = lerp(ScoreColours.WorstScore, ScoreColours.MidScore, .5f)
    return Brush.sweepGradient(
        0f to zeroColour,
        0.25f to ScoreColours.MidScore,
        0.75f to ScoreColours.TopScore,
        0.75f to ScoreColours.WorstScore,
        1f to zeroColour,
    )
}

private fun Float.toTextColor(): Color =
    if (this < 0.5f) {
        lerp(ScoreColours.WorstScore, ScoreColours.MidScore, this * 2)
    } else {
        lerp(ScoreColours.MidScore, ScoreColours.TopScore, (this - .5f) * 2f)
    }

@Preview(showBackground = true)
@Composable
fun SummaryPreview() {
    CreditRatingTheme {
        val score = 450
        CreditScoreSummary(
            score = score,
            maxScore = 700,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    CreditRatingTheme {
        CreditScoreLoader(
            modifier = Modifier.padding(5.dp)
        )
    }
}

@VisibleForTesting
object SummaryTestTags {
    private const val PREFIX = "summary_"

    const val SCORE = "${PREFIX}score"
    const val MAX_SCORE = "${PREFIX}max_score"
}