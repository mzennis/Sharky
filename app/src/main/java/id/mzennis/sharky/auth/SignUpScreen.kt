package id.mzennis.sharky.auth

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import id.mzennis.sharky.R
import id.mzennis.sharky.auth.util.AuthHelper
import id.mzennis.sharky.ui.theme.SampleAuthenticationTheme
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onSignUpClick: (String, String, String) -> Unit,
    onSignUpSuccess: () -> Unit,
) {
    val authHelper: AuthHelper = hiltViewModel()
    val authViewModel: SignUpViewModel = hiltViewModel()
    val googleSignInState by authViewModel.googleSignInState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(googleSignInState) {
        when (val state = googleSignInState) {
            is GoogleSignInState.Success -> {
                Toast.makeText(context, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                onSignUpSuccess()
                authViewModel.resetGoogleSignInState()
            }
            is GoogleSignInState.Error -> {
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                authViewModel.resetGoogleSignInState()
            }
            else -> {
                // Idle or Loading, do nothing here
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (googleSignInState is GoogleSignInState.Loading) {
                CircularProgressIndicator()
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onSignUpClick(fullName, email, password) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign Up")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("or")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (activity == null) return@Button // unhandled null, it isn't supposed to happen.
                            coroutineScope.launch {
                                // Step 1: Call AuthManager to get the token
                                authHelper.authManager.signInWithGoogle(activity)
                                    .onSuccess { googleIdToken ->
                                        // Step 2: Pass token to ViewModel for Firebase auth
                                        authViewModel.signInWithGoogleToken(googleIdToken)
                                    }
                                    .onFailure { exception ->
                                        authViewModel.onGoogleSignInError(
                                            exception.message ?: "Google sign-in failed."
                                        )
                                    }
                            }
                        },
                        enabled = googleSignInState !is GoogleSignInState.Loading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google_logo),
                                contentDescription = "Google sign-up",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sign up with Google")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SampleAuthenticationTheme {
        SignUpScreen(onSignUpClick = { _, _, _ -> }, onSignUpSuccess = {})
    }
}