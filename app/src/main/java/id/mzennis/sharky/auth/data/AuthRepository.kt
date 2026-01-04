package id.mzennis.sharky.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun signInWithGoogleToken(googleIdToken: String): Result<String> {
        return try {
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val authResult = auth.signInWithCredential(firebaseCredential).await()
            val userId = authResult.user?.uid
            if (userId != null) {
                Result.success(userId)
            } else {
                Result.failure(Exception("Firebase sign-in failed: User ID is null."))
            }
        } catch (e: Exception) {
            Timber.d(e, "Firebase sign-in with Google failed")
            Result.failure(e)
        }
    }
}