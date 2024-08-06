package dev.grahamsmith.openfireandroiddemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.grahamsmith.openfireandroiddemo.data.XmppManager
import dev.grahamsmith.openfireandroiddemo.ui.chat.ChatScreen
import dev.grahamsmith.openfireandroiddemo.ui.chat.ChatViewModel
import dev.grahamsmith.openfireandroiddemo.ui.login.LoginScreen
import dev.grahamsmith.openfireandroiddemo.ui.login.LoginViewModel
import dev.grahamsmith.openfireandroiddemo.ui.theme.OpenFireAndroidDemoTheme
import org.jivesoftware.smack.android.AndroidSmackInitializer

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSmackInitializer.initialize(applicationContext)
        setContent {
            OpenFireAndroidDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
        composable("chat") {
            val chatViewModel: ChatViewModel = hiltViewModel()
            ChatScreen(chatViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppNav() {
    AppNavigation()
}