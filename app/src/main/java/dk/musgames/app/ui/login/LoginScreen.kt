// ui/login/LoginScreen.kt
package dk.musgames.app.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dk.musgames.app.auth.LoginViewModel
import dk.musgames.app.R
@Composable
fun LoginScreen(
    vm: LoginViewModel = viewModel(),
    onLoggedIn: () -> Unit,
    onForgotPassword: () -> Unit
) {
    val ui by vm.ui.collectAsState()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (ui.isLoading) {
                CircularProgressIndicator()
            } else {
                Surface(
                    color = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.musgamessquare_no_bg),
                            contentDescription = "MusGames",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(280.dp)
                        )

                        Spacer(Modifier.height(24.dp))

                        Text("Log dig ind", style = MaterialTheme.typography.titleLarge)

                        Spacer(Modifier.height(24.dp))

                        OutlinedTextField(
                            value = ui.email,
                            onValueChange = vm::onEmailChange,
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = ui.password,
                            onValueChange = vm::onPasswordChange,
                            label = { Text("Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = { vm.login(onLoggedIn) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Log ind")
                        }

                        TextButton(
                            onClick = onForgotPassword,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Forgot password?")
                        }

                        Spacer(Modifier.height(24.dp))
                        ui.error?.let {
                            Spacer(Modifier.height(8.dp))
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}