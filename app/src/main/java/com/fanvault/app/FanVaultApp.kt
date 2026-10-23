package com.fanvault.app

import android.app.Application
import com.fanvault.app.data.local.AppDatabase
import com.fanvault.app.data.remote.NetworkModule
import com.fanvault.app.data.repository.ChatRepository

class FanVaultApp : Application() {
    // Simple manual DI container
    lateinit var repository: ChatRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.create(this)
        repository = ChatRepository(db.chatDao(), NetworkModule.provideChatApi())
    }
}
