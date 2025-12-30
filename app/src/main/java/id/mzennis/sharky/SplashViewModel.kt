package id.mzennis.sharky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.mzennis.sharky.auth.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data class Success(val isLoggedIn: Boolean) : SplashUiState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<SplashUiState> = userPreferencesRepository.isLoggedIn
        .map { isLoggedIn -> SplashUiState.Success(isLoggedIn) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SplashUiState.Loading
        )
}