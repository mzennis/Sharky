package id.mzennis.sample.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.mzennis.sample.auth.PasskeyBottomSheet
import id.mzennis.sample.ui.theme.SampleAuthenticationTheme

@Composable
fun HomeScreen() {
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showBottomSheet = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Cycle",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Add more UI elements here later, like cycle predictions,
        // log buttons, etc.

        if (showBottomSheet) {
            PasskeyBottomSheet(
                onDismiss = {
                    showBottomSheet = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SampleAuthenticationTheme {
        HomeScreen()
    }
}