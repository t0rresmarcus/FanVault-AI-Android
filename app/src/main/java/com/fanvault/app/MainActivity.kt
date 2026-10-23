package com.fanvault.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.fanvault.app.ui.ChatScreen
import com.fanvault.app.ui.ChatViewModel
import com.fanvault.app.ui.theme.FanVaultTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels {
        ChatViewModel.Factory((application as FanVaultApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FanVaultTheme { ChatScreen(viewModel) }
        }
    }
}
