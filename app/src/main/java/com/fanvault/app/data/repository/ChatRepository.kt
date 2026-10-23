package com.fanvault.app.data.repository

import com.fanvault.app.data.local.ChatDao
import com.fanvault.app.data.local.MessageEntity
import com.fanvault.app.data.local.ThreadEntity
import com.fanvault.app.data.remote.ChatApi
import com.fanvault.app.data.remote.ChatRequest
import java.util.UUID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val dao: ChatDao,
    private val api: ChatApi
) {
    fun observeThreads(): Flow<List<ThreadEntity>> = dao.observeThreads()
    fun observeThread(id: String): Flow<ThreadEntity?> = dao.observeThread(id)
    fun observeMessages(threadId: String): Flow<List<MessageEntity>> = dao.observeMessages(threadId)

    suspend fun latestThreadId(): String? = dao.latestThreadId()

    suspend fun createThread(): String {
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        dao.insertThread(ThreadEntity(id, DEFAULT_TITLE, now, now))
        return id
    }

    suspend fun deleteThread(id: String) = dao.deleteThread(id)

    /** Saves the question, calls the RAG API, saves the answer (or an error bubble). */
    suspend fun sendQuestion(threadId: String, question: String) {
        val now = System.currentTimeMillis()
        if (dao.messageCount(threadId) == 0) {
            dao.updateTitle(threadId, question.take(40), now)
        } else {
            dao.touch(threadId, now)
        }
        dao.insertMessage(MessageEntity(threadId = threadId, isUser = true, text = question, createdAt = now))

        try {
            val response = api.chat(ChatRequest(question))
            dao.insertMessage(
                MessageEntity(
                    threadId = threadId,
                    isUser = false,
                    text = response.answer.ifBlank { "I could not find an answer to that." },
                    sources = response.sources.joinToString("\n"),
                    createdAt = System.currentTimeMillis()
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            dao.insertMessage(
                MessageEntity(
                    threadId = threadId,
                    isUser = false,
                    text = "Could not reach the FanVault server. Check your connection and try again.\n(${e.javaClass.simpleName}: ${e.message})",
                    isError = true,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
        dao.touch(threadId, System.currentTimeMillis())
    }

    companion object {
        const val DEFAULT_TITLE = "New conversation"
    }
}
