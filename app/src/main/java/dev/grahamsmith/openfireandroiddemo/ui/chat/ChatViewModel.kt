package dev.grahamsmith.openfireandroiddemo.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.grahamsmith.openfireandroiddemo.data.XmppManager
import dev.grahamsmith.openfireandroiddemo.data.XmppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val xmppRepository: XmppRepository
) : ViewModel() {

    private val userJid = XmppManager.getConnection()?.user?.asEntityBareJidString()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    fun sendMessage(to: String, messageText: String) {
        viewModelScope.launch {
            xmppRepository.sendMessage(to, messageText)
            addMessage(to, messageText, true)
        }
    }

    fun receiveMessages() {
        viewModelScope.launch {
            xmppRepository.receiveMessages { from, message ->
                addMessage(from, message, from == userJid)
            }
        }
    }

    private fun addMessage(from: String, message: String, isMyMessage: Boolean) {
        val chatMessage = Message(message, isMyMessage)
        _messages.value += chatMessage
    }
}