package dev.grahamsmith.openfireandroiddemo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.grahamsmith.openfireandroiddemo.data.LoginDetails
import dev.grahamsmith.openfireandroiddemo.data.SecurePreferences
import dev.grahamsmith.openfireandroiddemo.data.XmppManager
import dev.grahamsmith.openfireandroiddemo.data.XmppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jivesoftware.smack.AbstractXMPPConnection
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val xmppManager: XmppManager,
    private val securePreferences: SecurePreferences
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    fun login(user: String, pass: String, host: String, serviceName: String, saveLoginDetails: Boolean) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val connection = xmppManager.connectAndLogin(user, pass, host, serviceName)
            if (connection != null && connection.isAuthenticated) {
                if(saveLoginDetails) {
                    securePreferences.saveLoginDetails(user, pass, host, serviceName)
                }
                _loginState.value = LoginState.Success(connection)
            } else {
                _loginState.value = LoginState.Error("Login failed")
            }
        }
    }

    fun loadLoginDetails(): LoginDetails? {
        return securePreferences.getLoginDetails()
    }

    fun clearLoginDetails() {
        securePreferences.clearLoginDetails()
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class Success(val connection: AbstractXMPPConnection) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}
