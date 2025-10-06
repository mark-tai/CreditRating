package com.tai.mark.creditrating

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.tai.mark.creditrating.ui.CreditScoreSummary
import com.tai.mark.creditrating.ui.theme.CreditRatingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreditRatingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CreditScoreSummary(
                        100,
                        700,
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

