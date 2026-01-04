package id.mzennis.sharky.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.mzennis.sharky.auth.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _googleSignInState = MutableStateFlow<GoogleSignInState>(GoogleSignInState.Idle)
    val googleSignInState = _googleSignInState.asStateFlow()

    fun signInWithGoogleToken(googleIdToken: String) {
        viewModelScope.launch {
            _googleSignInState.value = GoogleSignInState.Loading
            authRepository.signInWithGoogleToken(googleIdToken)
                .onSuccess { userId ->
                    _googleSignInState.value = GoogleSignInState.Success(userId)
                }
                .onFailure { exception ->
                    _googleSignInState.value = GoogleSignInState.Error(
                        exception.message ?: "Firebase authentication failed."
                    )
                }
        }
    }

    fun onGoogleSignInError(errorMessage: String) {
        _googleSignInState.value = GoogleSignInState.Error(errorMessage)
    }

    fun resetGoogleSignInState() {
        _googleSignInState.value = GoogleSignInState.Idle
    }
}

sealed class GoogleSignInState {
    object Idle : GoogleSignInState()
    object Loading : GoogleSignInState()
    data class Success(val userId: String) : GoogleSignInState()
    data class Error(val message: String) : GoogleSignInState()
}