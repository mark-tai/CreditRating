package com.tai.mark.creditrating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tai.mark.creditrating.data.CreditRating
import com.tai.mark.creditrating.data.CreditRatingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CreditRatingUiState {
    data object Loading : CreditRatingUiState()
    data class Loaded(val creditRating: CreditRating) : CreditRatingUiState()
    data object Error : CreditRatingUiState()
}

@HiltViewModel
class CreditRatingViewModel @Inject constructor(
    private val repository: CreditRatingRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreditRatingUiState>(CreditRatingUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadRating()
    }

    private fun loadRating() {
        viewModelScope.launch {
            val newState: CreditRatingUiState =
                try {
                    CreditRatingUiState.Loaded(repository.loadCreditRating())
                } catch (e: Exception) {
                    if (e is CancellationException) {
                        throw e
                    }
                    CreditRatingUiState.Error
                }
            _uiState.update { newState }
        }
    }
}
