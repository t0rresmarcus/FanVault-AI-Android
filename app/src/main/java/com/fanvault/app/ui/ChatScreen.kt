package com.fanvault.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fanvault.app.data.local.MessageEntity
import com.fanvault.app.data.local.ThreadEntity
import com.fanvault.app.ui.theme.GreenOnline
import com.fanvault.app.ui.theme.Navy
import com.fanvault.app.ui.theme.Purple
import com.fanvault.app.ui.theme.SkyBottom
import com.fanvault.app.ui.theme.SkyTop
import com.fanvault.app.ui.theme.UserBubble
import kotlinx.coroutines.launch

private const val GREETING = "Hey! I\u2019m FanVault \u2014 your matchday companion. What are we talking about today?"

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ThreadDrawer(
                threads = state.threads,
                selectedId = state.currentThreadId,
                onClose = { scope.launch { drawerState.close() } },
                onNew = {
                    viewModel.newConversation()
                    scope.launch { drawerState.close() }
                },
                onSelect = {
                    viewModel.selectThread(it)
                    scope.launch { drawerState.close() }
                },
                onDelete = viewModel::deleteThread
            )
        }
    ) {
        ChatContent(
            state = state,
            onMenu = { scope.launch { drawerState.open() } },
            onSend = viewModel::send
        )
    }
}

@Composable
private fun ChatContent(state: ChatUiState, onMenu: () -> Unit, onSend: (String) -> Unit) {
    var input by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val showGreeting = state.messages.isEmpty()
    val totalItems = state.messages.size + (if (showGreeting) 1 else 0) + (if (state.isSending) 1 else 0)

    // Auto-scroll to newest item
    LaunchedEffect(totalItems, state.currentThreadId) {
        if (totalItems > 0) listState.animateScrollToItem(totalItems - 1)
    }
    val showScrollDown by remember {
        derivedStateOf { listState.canScrollForward }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SkyTop, SkyBottom)))
    ) {
        Column(Modifier.fillMaxSize()) {
            // Header
            Row(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenu) { Icon(Icons.Default.Menu, contentDescription = "Conversations", tint = Navy) }
                Surface(shape = RoundedCornerShape(12.dp), color = Color.White.copy(alpha = 0.85f)) {
                    Column(Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text(
                            state.currentTitle,
                            color = Navy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            modifier = Modifier.widthIn(max = 220.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(6.dp).clip(CircleShape).background(GreenOnline))
                            Text("  Ready for matchday", fontSize = 11.sp, color = Color(0xFF4B5563))
                        }
                    }
                }
            }

            // Messages
            Box(Modifier.weight(1f)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (showGreeting) item { AssistantBubble(GREETING, emptyList(), false) }
                    items(state.messages, key = { it.id }) { msg ->
                        if (msg.isUser) UserBubble(msg.text)
                        else AssistantBubble(
                            text = msg.text,
                            sources = msg.sources.split("\n").filter { it.isNotBlank() },
                            isError = msg.isError
                        )
                    }
                    if (state.isSending) item { AssistantBubble("FanVault is thinking\u2026", emptyList(), false) }
                }
                if (showScrollDown) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                            .size(40.dp)
                            .clickable { scope.launch { listState.animateScrollToItem((totalItems - 1).coerceAtLeast(0)) } }
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, "Scroll to bottom", Modifier.padding(8.dp), tint = Navy)
                    }
                }
            }

            // Input
            Column(Modifier.navigationBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .border(1.dp, Purple.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                ) {
                    androidx.compose.foundation.text.BasicTextField(
                        value = input,
                        onValueChange = { input = it },
                        modifier = Modifier.fillMaxSize().padding(start = 16.dp, top = 14.dp, end = 64.dp, bottom = 10.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color(0xFF1F2937)),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            onSend(input); input = ""
                        }),
                        decorationBox = { inner ->
                            if (input.isEmpty()) {
                                Text("Ask about a game, team, player, or rule\u2026", color = Color(0xFF6B7280), fontSize = 16.sp)
                            }
                            inner()
                        }
                    )
                    IconButton(
                        onClick = { onSend(input); input = "" },
                        enabled = input.isNotBlank() && !state.isSending,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (input.isNotBlank() && !state.isSending) Purple else Purple.copy(alpha = 0.4f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
                Text(
                    "FanVault can make mistakes. Check live scores with an official source.",
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun UserBubble(text: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(shape = RoundedCornerShape(14.dp), color = UserBubble) {
            Text(text, Modifier.padding(horizontal = 16.dp, vertical = 12.dp).widthIn(max = 280.dp), fontSize = 16.sp)
        }
    }
}

@Composable
private fun AssistantBubble(text: String, sources: List<String>, isError: Boolean) {
    Column {
        Text("FanVault", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151), modifier = Modifier.padding(bottom = 6.dp))
        Surface(
            shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp, bottomEnd = 14.dp, bottomStart = 4.dp),
            color = if (isError) Color(0xFFFEE2E2) else Color.White,
            shadowElevation = 1.dp
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(text, fontSize = 16.sp, lineHeight = 24.sp, color = if (isError) Color(0xFF991B1B) else Color(0xFF111827))
                if (sources.isNotEmpty()) {
                    Spacer(Modifier.height(10.dp))
                    Text("Sources", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280))
                    sources.forEach { s ->
                        Text(
                            "\u2022 " + s.substringAfterLast("/").removeSuffix(".pdf"),
                            fontSize = 12.sp,
                            color = Purple,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThreadDrawer(
    threads: List<ThreadEntity>,
    selectedId: String?,
    onClose: () -> Unit,
    onNew: () -> Unit,
    onSelect: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    ModalDrawerSheet(drawerContainerColor = SkyTop) {
        Column(Modifier.padding(16.dp).statusBarsPadding()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Purple), contentAlignment = Alignment.Center) {
                    Text("\uD83C\uDFC6", fontSize = 20.sp)
                }
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text("FanVault", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Navy)
                    Text("Sports companion", fontSize = 12.sp, color = Color(0xFF4B5563))
                }
                IconButton(onClick = onClose) { Icon(Icons.Default.Close, "Close", tint = Navy) }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onNew,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Icon(Icons.Default.Add, null)
                Text("  New conversation")
            }
            Spacer(Modifier.height(20.dp))
            Text("CONVERSATIONS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4B5563))
            Spacer(Modifier.height(8.dp))
            LazyColumn(Modifier.weight(1f, fill = false)) {
                items(threads, key = { it.id }) { t ->
                    val selected = t.id == selectedId
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) Color.White else Color.Transparent)
                            .clickable { onSelect(t.id) }
                            .padding(start = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            t.title,
                            Modifier.weight(1f).padding(vertical = 14.dp),
                            maxLines = 1,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                        )
                        IconButton(onClick = { onDelete(t.id) }) {
                            Icon(Icons.Default.Delete, "Delete", tint = Color(0xFF6B7280), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(GreenOnline))
                Text("  Saved on this device", fontSize = 12.sp, color = Color(0xFF4B5563))
            }
        }
    }
}
