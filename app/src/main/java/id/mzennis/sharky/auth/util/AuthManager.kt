package id.mzennis.sharky.auth.util

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import id.mzennis.sharky.BuildConfig
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor() {

    suspend fun signInWithGoogle(activity: Activity): Result<String> {
        val credentialManager = CredentialManager.create(activity)
        val serverClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(activity, request)
            val credential = result.credential
            val googleIdToken: String? = when (credential) {
                is GoogleIdTokenCredential -> credential.idToken
                // Fallback for older versions
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        GoogleIdTokenCredential.createFrom(credential.data).idToken
                    } else {
                        null
                    }
                }
                else -> null
            }

            if (googleIdToken != null) {
                Result.success(googleIdToken)
            } else {
                Result.failure(Exception("Failed to retrieve Google ID Token."))
            }
        } catch (e: GetCredentialException) {
            Timber.d(e, "GetCredentialException")
            Result.failure(e)
        } catch (e: Exception) {
            Timber.d(e, "Google sign-in failed")
            Result.failure(e)
        }
    }
}
