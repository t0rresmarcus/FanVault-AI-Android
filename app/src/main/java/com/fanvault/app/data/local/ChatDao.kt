package com.fanvault.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM threads ORDER BY updatedAt DESC")
    fun observeThreads(): Flow<List<ThreadEntity>>

    @Query("SELECT * FROM messages WHERE threadId = :threadId ORDER BY createdAt ASC, id ASC")
    fun observeMessages(threadId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM threads WHERE id = :id")
    fun observeThread(id: String): Flow<ThreadEntity?>

    @Insert
    suspend fun insertThread(thread: ThreadEntity)

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("UPDATE threads SET title = :title, updatedAt = :now WHERE id = :id")
    suspend fun updateTitle(id: String, title: String, now: Long)

    @Query("UPDATE threads SET updatedAt = :now WHERE id = :id")
    suspend fun touch(id: String, now: Long)

    @Query("SELECT COUNT(*) FROM messages WHERE threadId = :threadId")
    suspend fun messageCount(threadId: String): Int

    @Query("DELETE FROM threads WHERE id = :id")
    suspend fun deleteThread(id: String)

    @Query("SELECT id FROM threads ORDER BY updatedAt DESC LIMIT 1")
    suspend fun latestThreadId(): String?
}
