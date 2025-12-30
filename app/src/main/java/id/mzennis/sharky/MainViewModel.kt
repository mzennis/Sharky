package id.mzennis.sharky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.mzennis.sharky.auth.data.UserPreferencesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    fun onSignInSuccess() {
        viewModelScope.launch {
            userPreferencesRepository.saveLoginState(true)
        }
    }
}