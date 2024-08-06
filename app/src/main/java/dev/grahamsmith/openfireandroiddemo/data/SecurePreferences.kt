package dev.grahamsmith.openfireandroiddemo.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecurePreferences(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_USERNAME = "key_username"
        private const val KEY_PASSWORD = "key_password"
        private const val KEY_HOST = "key_host"
        private const val KEY_SERVICE_NAME = "key_service_name"
    }

    fun saveLoginDetails(username: String, password: String, host: String, serviceName: String) {
        sharedPreferences.edit().apply {
            putString(KEY_USERNAME, username)
            putString(KEY_PASSWORD, password)
            putString(KEY_HOST, host)
            putString(KEY_SERVICE_NAME, serviceName)
            apply()
        }
    }

    fun getLoginDetails(): LoginDetails? {
        val username = sharedPreferences.getString(KEY_USERNAME, null)
        val password = sharedPreferences.getString(KEY_PASSWORD, null)
        val host = sharedPreferences.getString(KEY_HOST, null)
        val serviceName = sharedPreferences.getString(KEY_SERVICE_NAME, null)
        return if (username != null && password != null && host != null && serviceName != null) {
            LoginDetails(username, password, host, serviceName)
        } else {
            null
        }
    }

    fun clearLoginDetails() {
        sharedPreferences.edit().apply {
            remove(KEY_USERNAME)
            remove(KEY_PASSWORD)
            remove(KEY_HOST)
            remove(KEY_SERVICE_NAME)
            apply()
        }
    }
}

data class LoginDetails(
    val username: String,
    val password: String,
    val host: String,
    val serviceName: String
)
