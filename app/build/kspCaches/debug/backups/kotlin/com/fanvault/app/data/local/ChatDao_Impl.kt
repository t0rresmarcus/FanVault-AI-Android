package com.fanvault.app.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ChatDao_Impl(
  __db: RoomDatabase,
) : ChatDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfThreadEntity: EntityInsertAdapter<ThreadEntity>

  private val __insertAdapterOfMessageEntity: EntityInsertAdapter<MessageEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfThreadEntity = object : EntityInsertAdapter<ThreadEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `threads` (`id`,`title`,`createdAt`,`updatedAt`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ThreadEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindLong(3, entity.createdAt)
        statement.bindLong(4, entity.updatedAt)
      }
    }
    this.__insertAdapterOfMessageEntity = object : EntityInsertAdapter<MessageEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `messages` (`id`,`threadId`,`isUser`,`text`,`sources`,`isError`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MessageEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.threadId)
        val _tmp: Int = if (entity.isUser) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        statement.bindText(4, entity.text)
        statement.bindText(5, entity.sources)
        val _tmp_1: Int = if (entity.isError) 1 else 0
        statement.bindLong(6, _tmp_1.toLong())
        statement.bindLong(7, entity.createdAt)
      }
    }
  }

  public override suspend fun insertThread(thread: ThreadEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfThreadEntity.insert(_connection, thread)
  }

  public override suspend fun insertMessage(message: MessageEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMessageEntity.insert(_connection, message)
  }

  public override fun observeThreads(): Flow<List<ThreadEntity>> {
    val _sql: String = "SELECT * FROM threads ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("threads")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<ThreadEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ThreadEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _item = ThreadEntity(_tmpId,_tmpTitle,_tmpCreatedAt,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeMessages(threadId: String): Flow<List<MessageEntity>> {
    val _sql: String = "SELECT * FROM messages WHERE threadId = ? ORDER BY createdAt ASC, id ASC"
    return createFlow(__db, false, arrayOf("messages")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, threadId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "threadId")
        val _columnIndexOfIsUser: Int = getColumnIndexOrThrow(_stmt, "isUser")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfSources: Int = getColumnIndexOrThrow(_stmt, "sources")
        val _columnIndexOfIsError: Int = getColumnIndexOrThrow(_stmt, "isError")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<MessageEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MessageEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpThreadId: String
          _tmpThreadId = _stmt.getText(_columnIndexOfThreadId)
          val _tmpIsUser: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsUser).toInt()
          _tmpIsUser = _tmp != 0
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpSources: String
          _tmpSources = _stmt.getText(_columnIndexOfSources)
          val _tmpIsError: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsError).toInt()
          _tmpIsError = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = MessageEntity(_tmpId,_tmpThreadId,_tmpIsUser,_tmpText,_tmpSources,_tmpIsError,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeThread(id: String): Flow<ThreadEntity?> {
    val _sql: String = "SELECT * FROM threads WHERE id = ?"
    return createFlow(__db, false, arrayOf("threads")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: ThreadEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _result = ThreadEntity(_tmpId,_tmpTitle,_tmpCreatedAt,_tmpUpdatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun messageCount(threadId: String): Int {
    val _sql: String = "SELECT COUNT(*) FROM messages WHERE threadId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, threadId)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun latestThreadId(): String? {
    val _sql: String = "SELECT id FROM threads ORDER BY updatedAt DESC LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: String?
        if (_stmt.step()) {
          if (_stmt.isNull(0)) {
            _result = null
          } else {
            _result = _stmt.getText(0)
          }
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateTitle(
    id: String,
    title: String,
    now: Long,
  ) {
    val _sql: String = "UPDATE threads SET title = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, title)
        _argIndex = 2
        _stmt.bindLong(_argIndex, now)
        _argIndex = 3
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun touch(id: String, now: Long) {
    val _sql: String = "UPDATE threads SET updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteThread(id: String) {
    val _sql: String = "DELETE FROM threads WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
