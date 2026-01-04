package id.mzennis.sharky.auth.util

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Helper ViewModel to provide AuthManager to Composables.
 */
@HiltViewModel
class AuthHelper @Inject constructor(
    val authManager: AuthManager
) : ViewModel()
