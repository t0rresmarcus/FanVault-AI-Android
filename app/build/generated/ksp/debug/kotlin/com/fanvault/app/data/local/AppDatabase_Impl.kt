package com.fanvault.app.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _chatDao: Lazy<ChatDao> = lazy {
    ChatDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "45cb2eb73741272cf4ea4933292e376b", "0c95107c5f1235d1f71fa24dbcf2e0e3") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `threads` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `threadId` TEXT NOT NULL, `isUser` INTEGER NOT NULL, `text` TEXT NOT NULL, `sources` TEXT NOT NULL, `isError` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`threadId`) REFERENCES `threads`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_threadId` ON `messages` (`threadId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '45cb2eb73741272cf4ea4933292e376b')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `threads`")
        connection.execSQL("DROP TABLE IF EXISTS `messages`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsThreads: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsThreads.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysThreads: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesThreads: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoThreads: TableInfo = TableInfo("threads", _columnsThreads, _foreignKeysThreads, _indicesThreads)
        val _existingThreads: TableInfo = read(connection, "threads")
        if (!_infoThreads.equals(_existingThreads)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |threads(com.fanvault.app.data.local.ThreadEntity).
              | Expected:
              |""".trimMargin() + _infoThreads + """
              |
              | Found:
              |""".trimMargin() + _existingThreads)
        }
        val _columnsMessages: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMessages.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMessages.put("threadId", TableInfo.Column("threadId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMessages.put("isUser", TableInfo.Column("isUser", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMessages.put("text", TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMessages.put("sources", TableInfo.Column("sources", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMessages.put("isError", TableInfo.Column("isError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMessages.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMessages: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysMessages.add(TableInfo.ForeignKey("threads", "CASCADE", "NO ACTION", listOf("threadId"), listOf("id")))
        val _indicesMessages: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesMessages.add(TableInfo.Index("index_messages_threadId", false, listOf("threadId"), listOf("ASC")))
        val _infoMessages: TableInfo = TableInfo("messages", _columnsMessages, _foreignKeysMessages, _indicesMessages)
        val _existingMessages: TableInfo = read(connection, "messages")
        if (!_infoMessages.equals(_existingMessages)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |messages(com.fanvault.app.data.local.MessageEntity).
              | Expected:
              |""".trimMargin() + _infoMessages + """
              |
              | Found:
              |""".trimMargin() + _existingMessages)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "threads", "messages")
  }

  public override fun clearAllTables() {
    super.performClear(true, "threads", "messages")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ChatDao::class, ChatDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun chatDao(): ChatDao = _chatDao.value
}
