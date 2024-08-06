package dev.grahamsmith.openfireandroiddemo.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var host by remember { mutableStateOf("") }
    var shouldRememberDetails by remember { mutableStateOf(false) }
    var serviceName by remember { mutableStateOf("") }
    val loginState by loginViewModel.loginState.collectAsState()

    LaunchedEffect(Unit) {
        loginViewModel.loadLoginDetails()?.let { loginDetails ->
            user = loginDetails.username
            pass = loginDetails.password
            host = loginDetails.host
            serviceName = loginDetails.serviceName
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text("Username") }
        )
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = host,
            onValueChange = { host = it },
            label = { Text("Host") }
        )
        OutlinedTextField(
            value = serviceName,
            onValueChange = { serviceName = it },
            label = { Text("Service Name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Remember me"
            )
            Checkbox(checked = shouldRememberDetails, onCheckedChange = { shouldRememberDetails = it })

        }
        Button(onClick = {
            loginViewModel.login(
                user,
                pass,
                host,
                serviceName,
                shouldRememberDetails
            )
        }) {
            Text("Login")
        }
        when (loginState) {
            is LoginViewModel.LoginState.Loading -> CircularProgressIndicator()
            is LoginViewModel.LoginState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate("chat")
                }
            }
            is LoginViewModel.LoginState.Error -> {
                Text("Error: ${(loginState as LoginViewModel.LoginState.Error).message}")
            }
            else -> {}
        }
    }
}