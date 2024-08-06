package dev.grahamsmith.openfireandroiddemo.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.impl.JidCreate

object XmppManager {
    private var connection: AbstractXMPPConnection? = null

    suspend fun connectAndLogin(user: String, pass: String, host: String, serviceName: String): AbstractXMPPConnection? {
        return withContext(Dispatchers.IO) {
            try {
                if (connection?.isConnected == false) {
                    val config = XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword(user, pass)
                        .setHost(host)
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                        .setServiceName(JidCreate.domainBareFrom(serviceName))
                        .setPort(5222)
                        .build()
                    connection = XMPPTCPConnection(config).connect()
                    connection?.login()
                }
                connection
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun getConnection(): AbstractXMPPConnection? {
        return connection
    }
}
