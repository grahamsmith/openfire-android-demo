package dev.grahamsmith.openfireandroiddemo.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate

class XmppRepository(private val connection: AbstractXMPPConnection?) {

    suspend fun sendMessage(to: String, messageText: String) {
        withContext(Dispatchers.IO) {
            connection?.let {
                val chatManager = ChatManager.getInstanceFor(it)
                val jid: EntityBareJid = JidCreate.entityBareFrom(to)
                val chat: Chat = chatManager.chatWith(jid)
                val message = Message(jid, Message.Type.chat)
                message.body = messageText
                chat.send(message)
            }
        }
    }

    suspend fun receiveMessages(onMessageReceived: (String, String) -> Unit) {
        withContext(Dispatchers.IO) {
            connection?.let {
                val chatManager = ChatManager.getInstanceFor(it)
                chatManager.addIncomingListener { from, message, _ ->
                    onMessageReceived(from.asEntityBareJidString(), message.body)
                }
            }
        }
    }
}