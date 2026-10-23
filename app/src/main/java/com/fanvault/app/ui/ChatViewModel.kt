package com.fanvault.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fanvault.app.data.local.MessageEntity
import com.fanvault.app.data.local.ThreadEntity
import com.fanvault.app.data.repository.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatUiState(
    val threads: List<ThreadEntity> = emptyList(),
    val currentThreadId: String? = null,
    val currentTitle: String = ChatRepository.DEFAULT_TITLE,
    val messages: List<MessageEntity> = emptyList(),
    val isSending: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val currentThreadId = MutableStateFlow<String?>(null)
    private val isSending = MutableStateFlow(false)

    private val threads = repository.observeThreads()

    private val messages = currentThreadId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.observeMessages(id)
    }

    private val currentThread = currentThreadId.flatMapLatest { id ->
        if (id == null) flowOf(null) else repository.observeThread(id)
    }

    val uiState: StateFlow<ChatUiState> = combine(
        threads, currentThreadId, currentThread, messages, isSending
    ) { threads, id, thread, messages, sending ->
        ChatUiState(
            threads = threads,
            currentThreadId = id,
            currentTitle = thread?.title ?: ChatRepository.DEFAULT_TITLE,
            messages = messages,
            isSending = sending
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ChatUiState())

    init {
        // Restore the most recent conversation (threads are persisted locally).
        viewModelScope.launch { currentThreadId.value = repository.latestThreadId() }
    }

    fun send(text: String) {
        val question = text.trim()
        if (question.isEmpty() || isSending.value) return
        viewModelScope.launch {
            isSending.value = true
            try {
                val threadId = currentThreadId.value
                    ?: repository.createThread().also { currentThreadId.value = it }
                repository.sendQuestion(threadId, question)
            } finally {
                isSending.value = false
            }
        }
    }

    /** Starts a blank conversation; the thread row is created on the first message. */
    fun newConversation() {
        currentThreadId.value = null
    }

    fun selectThread(id: String) {
        currentThreadId.value = id
    }

    fun deleteThread(id: String) {
        viewModelScope.launch {
            repository.deleteThread(id)
            if (currentThreadId.value == id) currentThreadId.value = repository.latestThreadId()
        }
    }

    class Factory(private val repository: ChatRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = ChatViewModel(repository) as T
    }
}
